package com.projeto.dailytime.domain.user;

public record AuthenticationDTO(
        String email,
        String password
) {
}
