package com.projeto.dailytime.services;

import com.projeto.dailytime.domain.user.User;
import com.projeto.dailytime.domain.user.UserRole;
import com.projeto.dailytime.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {

    @Value("${app.admin.username}")
    private String adminUsername;

    @Value("${app.admin.password}")
    private String adminPassword;

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public DataInitializer(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {
        if (userRepository.count() == 0) {
            User admin = new User();
            admin.setName("admin");
            admin.setEmail(adminUsername);
            admin.setPassword(passwordEncoder.encode(adminPassword));
            admin.setRole(UserRole.ADMIN);
            userRepository.save(admin);
            System.out.println("Usuário admin criado com sucesso!");
        }
    }
}
