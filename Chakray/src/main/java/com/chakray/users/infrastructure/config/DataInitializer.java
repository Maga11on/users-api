package com.chakray.users.infrastructure.config;

import com.chakray.users.domain.model.Address;
import com.chakray.users.domain.model.User;
import com.chakray.users.domain.repository.UserRepository;
import com.chakray.users.infrastructure.security.AES256Encryptor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.UUID;

@Configuration
public class DataInitializer {

    @Bean
    CommandLineRunner initUsers(UserRepository repository) {
        return args -> {
            User user1 = new User();
            user1.setId(UUID.randomUUID());
            user1.setEmail("uusuario1@mail.com");
            user1.setName("uusuario1");
            user1.setPhone("+15555555555");
            user1.setTaxId("AARR990101XXX");
            user1.setPassword(AES256Encryptor.encrypt("123456"));
            user1.setCreatedAt(LocalDateTime.now(ZoneId.of("Indian/Antananarivo")));
            user1.setAddresses(Arrays.asList(
                    new Address(1, "Dirección Trabajo", "calle No. 1", "UK"),
                    new Address(2, "Dirección Casa", "calle No. 2", "AU")
            ));

            User user2 = new User();
            user2.setId(UUID.randomUUID());
            user2.setEmail("uusuario2@mail.com");
            user2.setName("uusuario2");
            user2.setPhone("+15555555556");
            user2.setTaxId("BBRR990101YYY");
            user2.setPassword(AES256Encryptor.encrypt("abcdef"));
            user2.setCreatedAt(LocalDateTime.now(ZoneId.of("Indian/Antananarivo")));
            user2.setAddresses(Arrays.asList(
                    new Address(3, "Oficina", "calle No. 3", "US")
            ));

            User user3 = new User();
            user3.setId(UUID.randomUUID());
            user3.setEmail("uusuario3@mail.com");
            user3.setName("uusuario3");
            user3.setPhone("+15555555557");
            user3.setTaxId("CCRR990101ZZZ");
            user3.setPassword(AES256Encryptor.encrypt("qwerty"));
            user3.setCreatedAt(LocalDateTime.now(ZoneId.of("Indian/Antananarivo")));
            user3.setAddresses(Arrays.asList(
                    new Address(4, "Casa", "calle No. 4", "MX")
            ));

            repository.save(user1);
            repository.save(user2);
            repository.save(user3);

            System.out.println("Usuarios inicializados en el repositorio de memoria");
        };
    }
}

