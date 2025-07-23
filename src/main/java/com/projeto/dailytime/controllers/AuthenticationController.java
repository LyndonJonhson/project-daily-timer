package com.projeto.dailytime.controllers;

import com.projeto.dailytime.domain.user.AuthenticationDTO;
import com.projeto.dailytime.domain.user.LoginResponseDTO;
import com.projeto.dailytime.domain.user.RegisterDTO;
import com.projeto.dailytime.domain.user.User;
import com.projeto.dailytime.infra.security.TokenService;
import com.projeto.dailytime.repositories.UserRepository;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthenticationController {

    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final TokenService tokenService;

    public AuthenticationController(
            AuthenticationManager authenticationManager,
            UserRepository userRepository,
            TokenService tokenService
    ) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.tokenService = tokenService;
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@RequestBody @Valid AuthenticationDTO dto) {
        var usernamePassword = new UsernamePasswordAuthenticationToken(dto.email(), dto.password());
        var auth = this.authenticationManager.authenticate(usernamePassword);

        var token = tokenService.generateToken((User) auth.getPrincipal());

        return ResponseEntity.ok(new LoginResponseDTO(token));
    }

    @PostMapping("/register")
    public ResponseEntity<Void> register(@RequestBody @Valid RegisterDTO dto) {
        if (this.userRepository.findByEmail(dto.email()) != null || !dto.password().equals(dto.confirmPassword())) {
            return ResponseEntity.badRequest().build();
        }

        var encryptedPassword = new BCryptPasswordEncoder().encode(dto.password());
        var newUser = User.builder()
                .email(dto.email())
                .password(encryptedPassword)
                .name(dto.name())
                .role(dto.role())
                .build();

        this.userRepository.save(newUser);

        return ResponseEntity.ok().build();
    }

}
