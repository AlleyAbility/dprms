package com.example.dprms.user.repository;

import com.example.dprms.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);

    void deleteByEmail(String email);

//    User findFirstByEmail(String email);
}
