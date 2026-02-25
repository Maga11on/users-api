package com.chakray.users.infrastructure.controller;

import com.chakray.users.domain.exception.DuplicateTaxIdException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.bind.annotation.*;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = GlobalExceptionHandlerTest.DummyController.class)
@Import(GlobalExceptionHandler.class)
class GlobalExceptionHandlerTest {

    @Autowired
    private MockMvc mockMvc;

    // -------------------------------------------------------
    // Controller dummy para provocar excepciones
    // -------------------------------------------------------
    @RestController
    @RequestMapping("/test")
    static class DummyController {

        @PostMapping("/validation")
        public void validation(@RequestBody @Valid DummyRequest req) {}

        @GetMapping("/duplicate")
        public void duplicate() {
            throw new DuplicateTaxIdException("Tax ID must be unique");
        }

        @GetMapping("/generic")
        public void generic() {
            throw new RuntimeException("Boom");
        }

        @PostMapping("/method")
        public void method() {}
    }

    record DummyRequest(
            @NotBlank(message = "name is mandatory")
            String name
    ) {}

    // -------------------------------------------------------
    // TESTS
    // -------------------------------------------------------

    @Test
    void should_handle_validation_error() throws Exception {
        mockMvc.perform(post("/test/validation")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errorCode").value("VALIDATION_ERROR"))
                .andExpect(jsonPath("$.field").value("name"))
                .andExpect(jsonPath("$.errorMessage").value("name is mandatory"));
    }

    @Test
    void should_handle_duplicate_tax_id() throws Exception {
        mockMvc.perform(get("/test/duplicate"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errorCode").value("DUPLICATE_TAX_ID"))
                .andExpect(jsonPath("$.errorMessage").value("Tax ID must be unique"));
    }

    @Test
    void should_handle_generic_exception() throws Exception {
        mockMvc.perform(get("/test/generic"))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.errorCode").value("INTERNAL_ERROR"))
                .andExpect(jsonPath("$.errorMessage").value("Unexpected error occurred"));
    }

    @Test
    void should_handle_method_not_supported() throws Exception {
        mockMvc.perform(get("/test/method"))
                .andExpect(status().isMethodNotAllowed())
                .andExpect(jsonPath("$.errorCode").value("METHOD_NOT_ALLOWED"))
                .andExpect(jsonPath("$.errorMessage", containsString("Method not allowed")));
    }
}