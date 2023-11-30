package com.example.meety1.repository;

import com.example.meety1.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {
    @Query(value = "SELECT * " +
            "FROM users WHERE users.id != :userId " +
            "LIMIT 10 " +
            "OFFSET :userCountIndex", nativeQuery = true)
    List<User> getTenUsers(@Param("userCountIndex") Long userCountedIndex, @Param("userId") Long userId);
}
