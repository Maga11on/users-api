package com.chakray.users.infrastructure.controller;

import com.chakray.users.application.dto.UserDto;
import com.chakray.users.application.mapper.UserMapper;
import com.chakray.users.application.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @Operation(summary = "Crear usuario",
            description = "Crea un nuevo usuario validando email, RFC y teléfono (AndresFormat)")
    @PostMapping
    public ResponseEntity<UserDto> createUser(@Valid @RequestBody UserDto userDto) {
        var saved = userService.createUser(UserMapper.toEntity(userDto));
        return ResponseEntity.ok(saved);
    }

    @Operation(summary = "Obtener lista de usuarios",
            description = "Devuelve todos los usuarios, con soporte para ordenamiento y filtrado")
    @GetMapping
    public ResponseEntity<List<UserDto>> getUsers(
            @Parameter(description = "Campo por el cual ordenar (email, id, name, phone, tax_id, created_at)")
            @RequestParam(required = false) String sortedBy,

            @Parameter(description = "Filtro en formato campo+operador+valor (ej: name+co+user)")
            @RequestParam(required = false) String filter) {

        List<UserDto> result = userService.getUsers(sortedBy, filter);
        return ResponseEntity.ok(result);
    }

    @Operation(summary = "Obtener usuario por Tax ID",
            description = "Devuelve un usuario específico según su RFC/tax_id")
    @GetMapping("/tax/{taxId}")
    public ResponseEntity<UserDto> getUserByTaxId(@PathVariable String taxId) {
        return userService.getUserByTaxId(taxId)
                      .map(ResponseEntity::ok)
                      .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Actualizar usuario",
            description = "Actualiza los datos de un usuario existente")
    @PutMapping("/{id}")
    public ResponseEntity<UserDto> updateUser(@PathVariable UUID id,
                                              @Valid @RequestBody UserDto dto) {
        return userService.updateUser(id, dto)
                      .map(ResponseEntity::ok)
                      .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Eliminar usuario",
            description = "Elimina un usuario por su UUID")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable String id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }
    
    @PatchMapping("/{id}")
    public ResponseEntity<UserDto> patchUser(@PathVariable UUID id,
                                             @RequestBody Map<String, Object> updates) {
        return userService.patchUser(id, updates)
                      .map(ResponseEntity::ok)
                      .orElse(ResponseEntity.notFound().build());
    }

}
