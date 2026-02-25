package com.chakray.users.application.service;

import com.chakray.users.application.dto.UserDto;
import com.chakray.users.application.mapper.UserMapper;
import com.chakray.users.domain.exception.BusinessException;
import com.chakray.users.domain.exception.DuplicateTaxIdException;
import com.chakray.users.domain.model.User;
import com.chakray.users.domain.repository.UserRepository;
import com.chakray.users.domain.validation.TaxIdValidator;
import com.chakray.users.infrastructure.security.AES256Encryptor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;
import java.util.UUID;

@Slf4j
@Service
public class UserService {

    private final UserRepository repository;

     public UserService(UserRepository repository) {
        this.repository = repository;
    }

    /* inyectar Kafka si se quiere utilizar */
    /*private final UserEventProducer eventProducer; // 🔑 inyección

        public UserService(UserRepository repository, UserEventProducer eventProducer) {
        this.repository = repository;
        this.eventProducer = eventProducer;
    }*/

    public UserDto createUser(User user) {

        log.info("Crea usuario: email {} Tax Id {} ", user.getEmail(), user.getTaxId());

        if (!TaxIdValidator.isValid(user.getTaxId())) {
            throw new BusinessException("INVALID_TAX_ID", "Invalid Tax ID format");
        }

        if (repository.findByTaxId(user.getTaxId()).isPresent()) {
            throw new DuplicateTaxIdException("Tax ID must be unique");
        }

        user.setId(UUID.randomUUID());
        user.setPassword(AES256Encryptor.encrypt(user.getPassword()));
        user.setCreatedAt(LocalDateTime.now(ZoneId.of("Indian/Antananarivo")));

        User saved = repository.save(user);

        // Evento Kafka - solo después de guardar correctamente
        /*eventProducer.sendEvent(
                "user.created",
                "User created successfully: " + saved.getId()
        );*/

        return UserMapper.toDto(saved);
    }

    public List<UserDto> getUsers(String sortedBy, String filter) {

        List<User> users = repository.findAll(sortedBy, filter);

        // Aplica filtrado
        if (filter != null && !filter.isBlank()) {
            users = applyFilter(users, sortedBy, filter);
        }

        return UserMapper.toDtoList(users);
    }

    public Optional<UserDto> updateUser(UUID id, UserDto dto) {
        return repository.findById(id)
                .map(existing -> {
                    existing.setEmail(dto.getEmail());
                    existing.setName(dto.getName());
                    existing.setPhone(dto.getPhone());
                    existing.setTaxId(dto.getTaxId());
                    existing.setPassword(dto.getPassword());
                    UserDto updated = UserMapper.toDto(
                            repository.update(id, UserMapper.toDto(existing))
                    );

                    //  Evento Kafka si se actualiza correctamente
                    /*eventProducer.sendEvent(
                            "user.update",
                            "User updated successfully: " + updated.getId()
                    );*/

                    return updated;
                });
    }

    public void deleteUser(String id) {

        log.info("Elimina Usuario id : {}", id);

        UUID userId = UUID.fromString(id);

        repository.delete(userId);

        // Evento Kafka si se elimina el usuario correctamente
        /*eventProducer.sendEvent(
                "user.deleted",
                "User deleted: " + userId
        );*/

        log.info("Usuario eliminado correctamente : {}", userId);
    }

    public Optional<UserDto> getUserByTaxId(String taxId) {

        log.info("Busca usuario por taxId : {}", taxId);

        return repository.findByTaxId(taxId)
                         .map(UserMapper::toDto);
    }

    public Optional<UserDto> patchUser(UUID id, Map<String, Object> updates) {
        log.info("Actualiza parte del Usuario id : {}", id);
        return repository.findById(id).map(existing -> {
            updates.forEach((key, value) -> {
                Field field = ReflectionUtils.findField(User.class, key);
                if (field != null) {
                    field.setAccessible(true);
                    ReflectionUtils.setField(field, existing, value);
                }
            });
            return UserMapper.toDto(repository.update(id, UserMapper.toDto(existing)));
        });
    }

    public List<User> applyFilter(List<User> users, String sortedBy, String filter) {
        List<User> list = new ArrayList<>(users);

        // Ordenamiento simple
        if (sortedBy != null) {
            switch (sortedBy) {
                case "email" -> list.sort(Comparator.comparing(User::getEmail));
                case "name" -> list.sort(Comparator.comparing(User::getName));
                case "phone" -> list.sort(Comparator.comparing(User::getPhone));
                case "tax_id" -> list.sort(Comparator.comparing(User::getTaxId));
                case "created_at" -> list.sort(Comparator.comparing(User::getCreatedAt));
                case "id" -> list.sort(Comparator.comparing(u -> u.getId().toString()));
                default -> { /* no ordenar si el campo no es válido */ }
            }
        }

        // Filtro simple (co=contiene, eq=igual, sw=empieza con, ew=termina con)
        if (filter != null) {
            String[] parts = filter.split("\\+");
            if (parts.length == 3) {
                String field = parts[0];
                String op = parts[1];
                String value = parts[2];

                list = list.stream().filter(u -> {
                    String attr = switch (field) {
                        case "email" -> u.getEmail();
                        case "name" -> u.getName();
                        case "phone" -> u.getPhone();
                        case "tax_id" -> u.getTaxId();
                        case "created_at" -> u.getCreatedAt().toString();
                        case "id" -> u.getId().toString();
                        default -> "";
                    };
                    return switch (op) {
                        case "co" -> attr.contains(value);
                        case "eq" -> attr.equals(value);
                        case "sw" -> attr.startsWith(value);
                        case "ew" -> attr.endsWith(value);
                        default -> false;
                    };
                }).collect(Collectors.toList());
            }
        }

        return list;
    }
}
