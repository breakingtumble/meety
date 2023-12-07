package com.example.meety1.dto;

import com.example.meety1.entity.Interest;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class PendingMatchDto {
    private Long userId;
    private String email;
    private String firstName;
    private String lastName;
    private String openInfo;
    private List<Interest> interests;

}
