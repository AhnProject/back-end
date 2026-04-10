package com.devahn.repository;

import com.devahn.domain.user.User;
import java.util.Optional;

public interface UserRepository {
    
    Optional<User> findByUsername(String username);
    
    Optional<User> findByEmail(String email);
    
    Long save(User user);
    
    boolean existsByUsername(String username);
    
    boolean existsByEmail(String email);
}
