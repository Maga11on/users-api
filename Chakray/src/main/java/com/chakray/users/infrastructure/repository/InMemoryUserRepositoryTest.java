package com.chakray.users.infrastructure.repository;

import com.chakray.users.application.dto.UserDto;
import com.chakray.users.domain.exception.BusinessException;
import com.chakray.users.domain.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryUserRepositoryTest {

    private InMemoryUserRepository repository;

    @BeforeEach
    void setUp() {
        repository = new InMemoryUserRepository();
    }

    @Test
    void save_shouldStoreUser() {
        User user = buildUser("AARR990101XXX");

        User saved = repository.save(user);

        assertNotNull(saved);
        assertEquals(user.getId(), saved.getId());
        assertEquals("AARR990101XXX", saved.getTaxId());
    }

    @Test
    void save_shouldThrowException_whenDuplicateTaxId() {
        User u1 = buildUser("AARR990101XXX");
        User u2 = buildUser("AARR990101XXX");

        repository.save(u1);

        BusinessException ex = assertThrows(
                BusinessException.class,
                () -> repository.save(u2)
        );

        assertEquals("DUPLICATE_TAX_ID", ex.getErrorCode());
    }

    @Test
    void findById_shouldReturnUser() {
        User user = buildUser("AARR990101XXX");
        repository.save(user);

        Optional<User> found = repository.findById(user.getId());

        assertTrue(found.isPresent());
        assertEquals(user.getId(), found.get().getId());
    }

    @Test
    void findById_shouldReturnEmpty_whenNotFound() {
        Optional<User> found = repository.findById(UUID.randomUUID());

        assertTrue(found.isEmpty());
    }

    @Test
    void findByTaxId_shouldReturnUser() {
        User user = buildUser("AARR990101XXX");
        repository.save(user);

        Optional<User> found = repository.findByTaxId("AARR990101XXX");

        assertTrue(found.isPresent());
        assertEquals("AARR990101XXX", found.get().getTaxId());
    }

    @Test
    void update_shouldModifyFields() {
        User user = buildUser("AARR990101XXX");
        repository.save(user);

        UserDto dto = new UserDto();
        dto.setEmail("updated@mail.com");
        dto.setName("Updated");

        User updated = repository.update(user.getId(), dto);

        assertEquals("updated@mail.com", updated.getEmail());
        assertEquals("Updated", updated.getName());
    }

    @Test
    void update_shouldThrowException_whenNotFound() {
        UserDto dto = new UserDto();

        assertThrows(
                NoSuchElementException.class,
                () -> repository.update(UUID.randomUUID(), dto)
        );
    }

    @Test
    void delete_shouldRemoveUser() {
        User user = buildUser("AARR990101XXX");
        repository.save(user);

        repository.delete(user.getId());

        assertTrue(repository.findById(user.getId()).isEmpty());
    }

    @Test
    void findAll_shouldReturnSortedByEmail() {
        repository.save(buildUser("AAA111"));
        repository.save(buildUser("BBB222"));

        List<User> result = repository.findAll("tax_id", null);

        assertEquals(2, result.size());
        assertTrue(result.get(0).getTaxId().compareTo(result.get(1).getTaxId()) < 0);
    }

    @Test
    void findAll_shouldFilterByContains() {
        repository.save(buildUser("AARR990101XXX"));
        repository.save(buildUser("BBBB990101YYY"));

        List<User> result = repository.findAll(null, "tax_id+co+AARR");

        assertEquals(1, result.size());
        assertEquals("AARR990101XXX", result.get(0).getTaxId());
    }

    private User buildUser(String taxId) {
        User u = new User();
        u.setId(UUID.randomUUID());
        u.setEmail(taxId + "@mail.com");
        u.setName("User " + taxId);
        u.setPhone("+15555555555");
        u.setTaxId(taxId);
        u.setPassword("secret");
        u.setCreatedAt(LocalDateTime.now());
        return u;
    }
}