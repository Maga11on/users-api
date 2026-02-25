package com.chakray.users.application.service;

import com.chakray.users.domain.exception.BusinessException;
import com.chakray.users.domain.model.User;
import com.chakray.users.infrastructure.kafka.UserEventProducer;
import com.chakray.users.infrastructure.repository.InMemoryUserRepository;
import com.chakray.users.infrastructure.security.AES256Encryptor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class LoginServiceTest {
//
//    private LoginService loginService;
//    private InMemoryUserRepository repository;
//    private UserEventProducer eventProducer;
//
//    @BeforeEach
//    void setup() {
//        repository = new InMemoryUserRepository();
//        eventProducer = Mockito.mock(UserEventProducer.class);
//        loginService = new LoginService(repository, eventProducer);
//
//        User user = new User();
//        user.setEmail("login@mail.com");
//        user.setName("loginUser");
//        user.setPhone("+15555555555");
//        user.setTaxId("LOGIN990101XXX");
//        user.setPassword(AES256Encryptor.encrypt("mypassword"));
//        repository.save(user);
//    }
//
//    @Test
//    void shouldLoginSuccessfullyAndSendEvent() {
//        loginService.authenticate("LOGIN990101XXX", "mypassword");
//        verify(eventProducer).sendEvent(eq("user.login"), contains("Login success"));
//    }
//
//    @Test
//    void shouldFailWithInvalidCredentials() {
//        assertThrows(BusinessException.class, () -> loginService.authenticate("LOGIN990101XXX", "wrongpassword"));
//        verify(eventProducer, never()).sendEvent(eq("user.login"), anyString());
//    }
//
//    @Test
//    void shouldFailWithNonExistingUser() {
//        assertThrows(BusinessException.class, () -> loginService.authenticate("NOTFOUND123", "mypassword"));
//        verify(eventProducer, never()).sendEvent(eq("user.login"), anyString());
//    }
//}
//

}