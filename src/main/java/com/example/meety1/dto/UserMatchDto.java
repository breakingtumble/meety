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
public class UserMatchDto {
    private Long userId;
    private String email;
    private String firstName;
    private String lastName;
    private String openInfo;
    private String privateInfo;
    private List<Interest> interests;
    //TODO: Add private info field, which is only available when users have a match!
    //TODO: Also add public info. Don't forget to add specific param to sql query!
}
