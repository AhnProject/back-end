package com.devahn.auth.repository;

import com.devahn.auth.domain.User;

import java.util.Optional;

public interface UserRepository {

    Optional<User> findByUsername(String username);

    Optional<User> findByEmail(String email);

    Long save(User user);

    boolean existsByUsername(String username);

    boolean existsByEmail(String email);
}
