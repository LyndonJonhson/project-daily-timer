package com.projeto.dailytime.repositories;

import com.projeto.dailytime.domain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, String> {

    UserDetails findByEmail(String email);
    Optional<User> findUserByEmail(String email);

}