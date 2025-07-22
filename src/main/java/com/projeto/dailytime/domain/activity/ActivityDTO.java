package com.projeto.dailytime.domain.activity;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ActivityDTO(
        @NotBlank(message = "Campo text é obrigatório") String text,
        @NotNull(message = "Campo taskId é obrigatório") Long taskId
) {
}
