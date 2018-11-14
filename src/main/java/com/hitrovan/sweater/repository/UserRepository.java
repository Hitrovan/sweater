package com.hitrovan.sweater.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.hitrovan.sweater.domain.User;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByUsername(String username);
    
    User findByActivationCode(String code);
}