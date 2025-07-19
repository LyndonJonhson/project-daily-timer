package com.projeto.dailytime.domain.task;

import jakarta.validation.constraints.NotBlank;

public record TaskIdDTO(
        @NotBlank(message = "Campo id é obrigatório") Long id
) {
}
