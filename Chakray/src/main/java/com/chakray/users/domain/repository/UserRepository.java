package com.chakray.users.domain.repository;

import com.chakray.users.application.dto.UserDto;
import com.chakray.users.domain.model.User;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserRepository {

    List<User> findAll(String sortedBy, String filter);
    Optional<User> findById(UUID id);
    Optional<User> findByTaxId(String taxId);
    User  save(User user);
    User update(UUID id, UserDto dto);
    void delete(UUID id);

}
