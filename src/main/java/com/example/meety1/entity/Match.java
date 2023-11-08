package com.example.meety1.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Table(name = "matches")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Match {
    @EmbeddedId
    private MatchKey id;
    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("userFirstId")
    @JoinColumn(name = "user_first_id")
    @JsonIgnore
    private User user1;
    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("userSecondId")
    @JoinColumn(name = "user_second_id")
    @JsonIgnore
    private User user2;

    Boolean pendingFirstSecond;

    Boolean pendingSecondFirst;

    Boolean friends;

    Boolean declined;
}
