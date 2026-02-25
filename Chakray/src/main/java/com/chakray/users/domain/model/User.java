package com.chakray.users.domain.model;

import com.chakray.users.domain.validation.AndresFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {
    private UUID id;
    private String email;
    private String name;

    @Schema(description = "Teléfono en formato internacional con 10 dígitos obligatorios", example = "+525555555555")
    @AndresFormat
    private String phone;

    private String password;
    private String taxId;
    private LocalDateTime createdAt;
    private List<Address> addresses;

}
