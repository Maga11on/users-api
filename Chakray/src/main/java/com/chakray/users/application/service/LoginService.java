package com.chakray.users.application.service;

import com.chakray.users.domain.exception.BusinessException;
import com.chakray.users.domain.model.User;
import com.chakray.users.domain.repository.UserRepository;
import com.chakray.users.infrastructure.kafka.UserEventProducer;
import com.chakray.users.infrastructure.security.AES256Encryptor;
import org.springframework.stereotype.Service;

@Service
public class LoginService {

    private final UserRepository repository;

    public LoginService(UserRepository repository) {
        this.repository = repository;
    }
    //private final UserEventProducer eventProducer; // 🔑 inyección

    /*Kafka si se requiere enviar mensaje*/
    /*
    public LoginService(UserRepository repository, UserEventProducer eventProducer) {
        this.repository = repository;
        this.eventProducer = eventProducer;
    }*/


//    public String authenticate(String taxId, String password) {
//        return repository.findByTaxId(taxId)
//                .filter(user -> user.getPassword().equals(AES256Encryptor.encrypt(password)))
//                .map(u -> "Login successful")
//                .orElse("Invalid credentials");
//    }

    public String authenticate(String taxId, String password) {
        User user = repository.findByTaxId(taxId)
                .orElseThrow(() -> new BusinessException("USER_NOT_FOUND", "User not found"));

        String encrypted = AES256Encryptor.encrypt(password);
        if (!user.getPassword().equals(encrypted)) {
            throw new BusinessException("INVALID_CREDENTIALS", "Invalid credentials");
        }

        //  Enviar evento Kafka
        //eventProducer.sendEvent("user.login", "Login success for taxId: " + taxId);
        return encrypted;
    }


}
