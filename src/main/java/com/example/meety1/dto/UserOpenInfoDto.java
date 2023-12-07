package com.example.meety1.dto;

import com.example.meety1.entity.Interest;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@AllArgsConstructor
@Getter
@Setter
public class UserOpenInfoDto {
    Long id;
    String firstName;
    String lastName;
    String email;
    String openInfo;
    List<Interest> interests;
}
