package com.projeto.dailytime.domain.user;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record RegisterDTO(
        @NotBlank(message = "Campo name é obrigatório") String name,
        @NotBlank(message = "Campo email é obrigatório") String email,
        @NotBlank(message = "Campo password é obrigatório") String password,
        @NotBlank(message = "Campo confirmPassword é obrigatório") String confirmPassword,
        @NotNull(message = "Campo role é obrigatório") UserRole role
) {
}
