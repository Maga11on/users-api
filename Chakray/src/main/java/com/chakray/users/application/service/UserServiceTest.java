package com.chakray.users.application.service;

import com.chakray.users.application.dto.UserDto;
import com.chakray.users.domain.exception.BusinessException;
import com.chakray.users.domain.exception.DuplicateTaxIdException;
import com.chakray.users.domain.model.User;
import com.chakray.users.domain.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;

class UserServiceTest {

    private UserRepository repository;
    private UserService service;

    private static final String UUID_STRING = "5a31395c-9b53-4841-a239-bf71f4f852db";

    @BeforeEach
    void setUp() {
        repository = Mockito.mock(UserRepository.class);
        service = new UserService(repository);
    }

    // ------------------ CREATE ------------------

    @Test
    void createUser_shouldSaveUser_whenValid() {
        User user = buildUser("AARR990101XXX");

        Mockito.when(repository.findByTaxId("AARR990101XXX"))
                .thenReturn(Optional.empty());

        Mockito.when(repository.save(any(User.class)))
                .thenAnswer(inv -> inv.getArgument(0));

        UserDto result = service.createUser(user);

        assertNotNull(result.getId());
        assertEquals("AARR990101XXX", result.getTaxId());
        assertNotEquals("secret", result.getPassword()); // encrypted
    }

    @Test
    void createUser_shouldThrowException_whenInvalidTaxId() {

        User user = buildUser("INVALID");

        BusinessException ex = assertThrows(BusinessException.class, () -> {
            service.createUser(user);
        });

        assertEquals("Invalid Tax ID format", ex.getMessage());
    }

    @Test
    void createUser_shouldThrowException_whenDuplicateTaxId() {
        User user = buildUser("AARR990101XXX");

        Mockito.when(repository.findByTaxId("AARR990101XXX"))
                .thenReturn(Optional.of(user));

        assertThrows(
                DuplicateTaxIdException.class,
                () -> service.createUser(user)
        );
    }

    // ------------------ UPDATE ------------------

    @Test
    void updateUser_shouldReturnUpdatedUser_whenExists() {
        UUID id = UUID.fromString(UUID_STRING);
        User existing = buildUser("AARR990101XXX");
        existing.setId(id);

        Mockito.when(repository.findById(id))
                .thenReturn(Optional.of(existing));

        Mockito.when(repository.update(eq(id), any()))
                .thenAnswer(inv -> existing);

        UserDto dto = new UserDto();
        dto.setEmail("updated@mail.com");

        Optional<UserDto> result = service.updateUser(id, dto);

        assertTrue(result.isPresent());
        assertEquals("updated@mail.com", result.get().getEmail());
    }

    @Test
    void updateUser_shouldReturnEmpty_whenUserNotFound() {
        UUID id = UUID.fromString(UUID_STRING);

        Mockito.when(repository.findById(id))
                .thenReturn(Optional.empty());

        Optional<UserDto> result = service.updateUser(id, new UserDto());

        assertTrue(result.isEmpty());
    }

    // ------------------ DELETE ------------------

    @Test
    void deleteUser_shouldInvokeRepositoryDelete() {
        UUID id = UUID.fromString(UUID_STRING);

        service.deleteUser(id.toString());

        Mockito.verify(repository).delete(id);
    }

    // ------------------ FIND ------------------

    @Test
    void getUserByTaxId_shouldReturnUser_whenExists() {
        User user = buildUser("AARR990101XXX");

        Mockito.when(repository.findByTaxId("AARR990101XXX"))
                .thenReturn(Optional.of(user));

        Optional<UserDto> result = service.getUserByTaxId("AARR990101XXX");

        assertTrue(result.isPresent());
        assertEquals("AARR990101XXX", result.get().getTaxId());
    }

    @Test
    void getUserByTaxId_shouldReturnEmpty_whenNotFound() {
        Mockito.when(repository.findByTaxId("NOT_FOUND"))
                .thenReturn(Optional.empty());

        Optional<UserDto> result = service.getUserByTaxId("NOT_FOUND");

        assertTrue(result.isEmpty());
    }

    // ------------------ PATCH ------------------

    @Test
    void patchUser_shouldUpdateOnlyProvidedFields() {
        UUID id = UUID.fromString(UUID_STRING);
        User user = buildUser("AARR990101XXX");
        user.setId(id);

        Mockito.when(repository.findById(id))
                .thenReturn(Optional.of(user));

        Mockito.when(repository.update(eq(id), any()))
                .thenAnswer(inv -> user);

        Map<String, Object> updates = new HashMap<>();
        updates.put("email", "patched@mail.com");

        Optional<UserDto> result = service.patchUser(id, updates);

        assertTrue(result.isPresent());
        assertEquals("patched@mail.com", result.get().getEmail());
    }

    // ------------------ GET USERS ------------------

    @Test
    void getUsers_shouldReturnFilteredUsers() {
        List<User> users = List.of(
                buildUser("AARR990101XXX"),
                buildUser("BBBB990101YYY")
        );

        Mockito.when(repository.findAll(null, "tax_id+co+AARR"))
                .thenReturn(users);

        List<UserDto> result = service.getUsers(null, "tax_id+co+AARR");

        assertEquals(1, result.size());
        assertEquals("AARR990101XXX", result.get(0).getTaxId());
    }

    // ------------------ HELPERS ------------------

    private User buildUser(String taxId) {
        User user = new User();
        user.setId(UUID.randomUUID());
        user.setEmail("user@mail.com");
        user.setName("user");
        user.setPhone("+525555555555");
        user.setTaxId(taxId);
        user.setPassword("1234");
        user.setCreatedAt(LocalDateTime.now());
        return user;
    }
}