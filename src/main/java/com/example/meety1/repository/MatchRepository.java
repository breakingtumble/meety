package com.example.meety1.repository;

import com.example.meety1.dto.PendingMatchDto;
import com.example.meety1.dto.UserMatchDto;
import com.example.meety1.entity.Match;
import com.example.meety1.entity.MatchKey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MatchRepository extends JpaRepository<Match, MatchKey> {

    @Query(" SELECT new com.example.meety1.dto.UserMatchDto (ur.id.userSecondId, ur.user2.email, ur.user2.firstName, ur.user2.lastName) " +
            "FROM Match ur " +
            "JOIN ur.user2 m " +
            "WHERE (ur.id.userFirstId = :myUserId) AND ur.friends = true " +
            "UNION ALL " +
            "SELECT new com.example.meety1.dto.UserMatchDto (ur.id.userFirstId, ur.user1.email, ur.user1.firstName, ur.user1.lastName) " +
            "FROM Match ur " +
            "JOIN ur.user2 n " +
            "where (ur.id.userSecondId = :myUserId) AND ur.friends = true ")
    List<UserMatchDto> getAllMatchesDto(@Param("myUserId") Long input);

    @Query(" SELECT ur " +
            "FROM Match ur " +
            "JOIN ur.user2 m " +
            "WHERE (ur.id.userFirstId = :myUserId) AND ur.friends = true AND ur.declined != true " +
            "UNION ALL " +
            "SELECT ur " +
            "FROM Match ur " +
            "JOIN ur.user1 n " +
            "where (ur.id.userSecondId = :myUserId) AND ur.friends = true AND ur.declined != true")
    List<Match> getAllUserMatchesEntityList(@Param("myUserId") Long input);

    @Query("SELECT new com.example.meety1.dto.PendingMatchDto (ur.id.userSecondId, ur.user2.email, ur.user2.firstName, ur.user2.firstName) " +
            "FROM Match ur " +
            "JOIN ur.user2 m " +
            "WHERE (ur.id.userFirstId = :myUserId) AND ur.pendingSecondFirst = true AND ur.friends != true " +
            "UNION ALL " +
            "SELECT new com.example.meety1.dto.PendingMatchDto(ur.id.userFirstId, ur.user1.email, ur.user1.firstName, ur.user1.lastName) " +
            "FROM Match ur " +
            "JOIN ur.user1 " +
            "WHERE (ur.id.userSecondId = :myUserId) AND ur.pendingFirstSecond = true AND ur.friends != true")
    List<PendingMatchDto> getPendingMatchesDto(@Param("myUserId") Long input);

    @Query("SELECT ur " +
            "FROM Match ur " +
            "JOIN ur.user2 m " +
            "WHERE (ur.id.userFirstId = :myUserId) AND ur.pendingSecondFirst = true AND ur.friends != true AND ur.declined != true " +
            "UNION ALL " +
            "SELECT ur " +
            "FROM Match ur " +
            "JOIN ur.user1 " +
            "WHERE (ur.id.userSecondId = :myUserId) AND ur.pendingFirstSecond = true AND ur.friends != true AND ur.declined != true")
    List<Match> getPendingMatchesEntityList(@Param("myUserId") Long input);
}
