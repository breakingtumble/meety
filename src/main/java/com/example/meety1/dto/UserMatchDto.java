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
    private List<Interest> interests;

    public UserMatchDto(Long userId, String email, String firstName, String lastName) {
        this.userId = userId;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
    }
    //TODO: Add private info field, which is only available when users have a match!
    //TODO: Also add public info. Don't forget to add specific param to sql query!
}
