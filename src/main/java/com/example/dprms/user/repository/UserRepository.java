package com.example.dprms.user.repository;

import com.example.dprms.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);

    void deleteByEmail(String email);

    List<User> findByNotLocked(boolean b);

    Optional<User> findById(Long id);

    Optional<User> findByEmployeeId(String employeeId);

    @Query("SELECT r.name FROM Role r INNER JOIN r.users u WHERE u.id = :userId")
    List<String> getUserRolesByUserId(@Param("userId") Long userId);


}
