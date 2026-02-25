package com.chakray.users.application.mapper;

import com.chakray.users.application.dto.UserDto;
import com.chakray.users.domain.model.Address;
import com.chakray.users.domain.model.User;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

public class UserMapper {

    public static UserDto toDto(User user) {
        if (user == null) return null;

        UserDto dto = new UserDto();
        dto.setId(user.getId().toString());
        dto.setEmail(user.getEmail());
        dto.setName(user.getName());
        dto.setPhone(user.getPhone());
        dto.setTaxId(user.getTaxId());
        dto.setCreatedAt(user.getCreatedAt().format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm")));
        if (dto.getAddresses() != null) {
            user.setAddresses(
                    dto.getAddresses().stream()
                            .map(addr -> new Address(
                                    addr.getId(),
                                    addr.getName(),
                                    addr.getStreet(),
                                    addr.getCountryCode()
                            ))
                            .collect(Collectors.toList())
            );

        }

        return dto;
    }

    public static User toEntity(UserDto dto) {
        if (dto == null) return null;

        User user = new User();

        user.setEmail(dto.getEmail());
        user.setName(dto.getName());
        user.setPhone(dto.getPhone());
        user.setTaxId(dto.getTaxId());
        user.setPassword(dto.getPassword());

        if (dto.getAddresses() != null) {
            user.setAddresses(
                    dto.getAddresses().stream()
                            .map(addr -> new Address(
                                    addr.getId(),
                                    addr.getName(),
                                    addr.getStreet(),
                                    addr.getCountryCode()
                            ))
                            .collect(Collectors.toList())
            );
        }

        return user;
    }

    public static List<UserDto> toDtoList(List<User> users) {
        return users.stream()
                .map(UserMapper::toDto)
                .collect(Collectors.toList());
    }

    public static List<User> toEntityList(List<UserDto> dtos) {
        return dtos.stream()
                .map(UserMapper::toEntity)
                .collect(Collectors.toList());
    }
}
