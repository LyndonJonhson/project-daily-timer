package com.projeto.dailytime.domain.task;

import jakarta.validation.constraints.NotBlank;

public record TaskDTO(
        @NotBlank(message = "Campo title é obrigatório") String title,
        String description
) {
}
