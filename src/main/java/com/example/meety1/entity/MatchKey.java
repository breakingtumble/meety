package com.example.meety1.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
@Embeddable
public class MatchKey implements Serializable {
    @Column(name = "user_first_id")
    private Long userFirstId;

    @Column(name = "user_second_id")
    private Long userSecondId;
}
