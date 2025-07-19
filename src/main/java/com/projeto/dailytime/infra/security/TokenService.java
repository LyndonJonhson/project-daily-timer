package com.projeto.dailytime.infra.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.projeto.dailytime.domain.user.User;
import com.projeto.dailytime.repositories.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Service
public class TokenService {

    @Value("${api.security.token.secret}")
    private String secret;

    @Value("${spring.application.name}")
    private String issuer;

    private final UserRepository userRepository;

    public TokenService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public String generateToken(User user) {
        try {
            var algorithm = Algorithm.HMAC256(secret);
            return JWT.create()
                    .withIssuer(issuer)
                    .withSubject(user.getEmail())
                    .withExpiresAt(getExpirationDate())
                    .sign(algorithm);
        } catch (JWTCreationException exception) {
            throw new RuntimeException("Error generating token", exception);
        }
    }

    public String validateToken(String token) {
        try {
            var algorithm = Algorithm.HMAC256(secret);
            return JWT.require(algorithm)
                    .withIssuer(issuer)
                    .build()
                    .verify(token)
                    .getSubject();
        } catch (JWTVerificationException exception) {
            return "";
        }
    }

    private Instant getExpirationDate() {
        return LocalDateTime.now().plusHours(2).toInstant(ZoneOffset.of("-03:00"));
    }

    public User getUser(HttpServletRequest request) {
        var subject = validateToken(recoverToken(request));
        return userRepository.findUserByEmail(subject).orElse(null);
    }

    public String recoverToken(HttpServletRequest request) {
        var authHeader = request.getHeader("Authorization");
        if (authHeader == null) return null;
        return authHeader.replace("Bearer ", "");
    }
}
