package com.example.meety1.repository;

import com.example.meety1.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    @Query(value = "SELECT * " +
            "FROM users WHERE users.id != :userId " +
            "LIMIT 10 " +
            "OFFSET :userCountIndex", nativeQuery = true)
    List<User> getTenUsers(@Param("userCountIndex") Long userCountedIndex, @Param("userId") Long userId);

    @Query(value = "SELECT DISTINCT id, email, first_name, last_name, password, counted_rows, open_info, private_info\n" +
            "FROM users " +
            "LEFT JOIN ( " +
            "    SELECT ui.user_id, ui.interest_id, i.name " +
            "    FROM users_interests ui " +
            "    INNER JOIN interests i ON i.id = ui.interest_id " +
            ") AS user_interests " +
            "ON users.id = user_interests.user_id WHERE user_interests.name IN (:interests) AND user_id != :userId", nativeQuery = true)
    List<User> getTenUsersFiltered(@Param("userId") Long userId, @Param("interests") List<String> interests);
}
