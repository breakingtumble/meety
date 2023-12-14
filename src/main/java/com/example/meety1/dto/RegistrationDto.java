package com.example.meety1.dto;


import com.example.meety1.entity.Interest;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@AllArgsConstructor
@Getter
@Setter
public class RegistrationDto {
    private String firstName;
    private String lastName;
    private String privateInfo;
    private String openInfo;
    private String email;
    private String password;
    private List<Interest> interests;
}
