package com.chakray.users.application.dto;

import com.chakray.users.domain.validation.AndresFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {
    private String id;
    private String email;
    private String name;

    @Schema(description = "Teléfono en formato internacional con 10 dígitos obligatorios", example = "+525555555555")
    @AndresFormat
    private String phone;

    private String taxId;
    private String password; // solo para entrada, no se devuelve
    private String createdAt;
    private List<AddressDto> addresses;
}
