package com.projeto.dailytime.domain.activity;

import jakarta.validation.constraints.NotBlank;

public record ActivityDTO(
        @NotBlank(message = "Campo text é obrigatório") String text,
        @NotBlank(message = "Campo taskId é obrigatório") Long taskId
) {
}
