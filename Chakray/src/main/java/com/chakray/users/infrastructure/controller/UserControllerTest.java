package com.chakray.users.infrastructure.controller;

import com.chakray.users.application.dto.UserDto;
import com.chakray.users.application.service.UserService;
import com.chakray.users.domain.exception.BusinessException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.ResponseEntity;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;

class UserControllerTest {

    private UserService service;
    private UserController controller;
    
      String id = "5a31395c-9b53-4841-a239-bf71f4f852db";
      //String userId = "5a31395c-9b53-4841-a239-bf71f4f852db";

    @BeforeEach
    void setUp() {
        service = Mockito.mock(UserService.class);
        controller = new UserController(service);
    }

    @Test
    void getUserByTaxId_shouldReturnUser_whenExists() {
        UserDto dto = new UserDto();
        dto.setId(id);
        dto.setEmail("test@mail.com");
        dto.setName("Test User");
        dto.setPhone("+15555555555");
        dto.setTaxId("AARR990101XXX");
        dto.setPassword("secret");

        Mockito.when(service.getUserByTaxId("AARR990101XXX"))
               .thenReturn(Optional.of(dto));

        ResponseEntity<UserDto> response = controller.getUserByTaxId("AARR990101XXX");

        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        assertEquals("test@mail.com", response.getBody().getEmail());
    }

    @Test
    void getUserByTaxId_shouldReturn404_whenNotFound() {
        Mockito.when(service.getUserByTaxId("NOT_FOUND"))
               .thenReturn(Optional.empty());

        ResponseEntity<UserDto> response = controller.getUserByTaxId("NOT_FOUND");

        assertEquals(404, response.getStatusCodeValue());
        assertNull(response.getBody());
    }

    @Test
    void updateUser_shouldReturnUpdatedUser_whenSuccess() {
       // UUID userId = UUID.randomUUID();
    	String uuidString = "5a31395c-9b53-4841-a239-bf71f4f852db";
        UUID userId = UUID.fromString(uuidString);
        UserDto dto = new UserDto();
        dto.setId(uuidString);
        dto.setEmail("updated@mail.com");
        dto.setName("Updated User");
        dto.setPhone("+15555555556");
        dto.setTaxId("AARR990101YYY");
        dto.setPassword("newsecret");

        Mockito.when(service.updateUser(eq(userId), any(UserDto.class)))
               .thenReturn(Optional.of(dto));

        ResponseEntity<UserDto> response = controller.updateUser(userId, dto);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals("updated@mail.com", response.getBody().getEmail());
    }

    @Test
    void updateUser_shouldReturn400_whenTaxIdDuplicate() {
        //UUID userId = UUID.randomUUID();
        String uuidString = "5a31395c-9b53-4841-a239-bf71f4f852db";
        UUID userId = UUID.fromString(uuidString);
        UserDto dto = new UserDto();
        dto.setId(uuidString);
        dto.setTaxId("DUPLICATE123");

        Mockito.when(service.updateUser(eq(userId), any(UserDto.class)))
               .thenThrow(new BusinessException("Tax ID must be unique", "DUPLICATE_TAX_ID"));

        // Aquí simulamos que tu GlobalExceptionHandler maneja BusinessException
        Exception ex = assertThrows(BusinessException.class, () -> controller.updateUser(userId, dto));
        assertEquals("Tax ID must be unique", ex.getMessage());
    }

    @Test
    void updateUser_shouldReturn404_whenUserNotFound() {
        //UUID userId = UUID.randomUUID();
        String uuidString = "5a31395c-9b53-4841-a239-bf71f4f852db";
        UUID userId = UUID.fromString(uuidString);
        UserDto dto = new UserDto();
        dto.setId(uuidString);
        dto.setTaxId("AARR990101YYY");

        Mockito.when(service.updateUser(eq(userId), any(UserDto.class)))
               .thenReturn(Optional.empty());

        ResponseEntity<UserDto> response = controller.updateUser(userId, dto);

        assertEquals(404, response.getStatusCodeValue());
        assertNull(response.getBody());
    }
}