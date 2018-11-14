package com.hitrovan.spring1.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.hitrovan.spring1.domain.User;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByUsername(String username);
    
    User findByActivationCode(String code);
}