package com.chakray.users.infrastructure.repository;

import com.chakray.users.application.dto.UserDto;
import com.chakray.users.domain.exception.BusinessException;
import com.chakray.users.domain.model.User;
import com.chakray.users.domain.repository.UserRepository;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.stream.Collectors;

@Repository
public class InMemoryUserRepository implements UserRepository {


    private final Map<UUID, User> users = new HashMap<>();

    @Override
    public List<User> findAll(String sortedBy, String filter) {
        List<User> list = new ArrayList<>(users.values());

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


        // Filtro simple (co=contains, eq=equals, sw=startsWith, ew=endsWith)
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

    @Override
    public Optional<User> findById(UUID id) {
        return Optional.ofNullable(users.get(id));
    }

    @Override
    public Optional<User> findByTaxId(String taxId) {
        return users.values().stream()
                .filter(u -> u.getTaxId().equals(taxId))
                .findFirst();
    }

    @Override
    public User save(User user) {
        if (users.values().stream().anyMatch(u -> u.getTaxId().equals(user.getTaxId()))) {
            throw new BusinessException("DUPLICATE_TAX_ID", "Tax ID must be unique");
        }
        users.put(user.getId(), user);
        return user;
    }


    @Override
    public User update(UUID id, UserDto dto) {
        User user = users.get(id);
        if (user == null) throw new NoSuchElementException("User not found");

        if (dto.getEmail() != null) user.setEmail(dto.getEmail());
        if (dto.getName() != null) user.setName(dto.getName());
        if (dto.getPhone() != null) user.setPhone(dto.getPhone());
        if (dto.getTaxId() != null) user.setTaxId(dto.getTaxId());

        users.put(id, user);
        return user;
    }

    @Override
    public void delete(UUID id) {
        users.remove(id);
    }

}
