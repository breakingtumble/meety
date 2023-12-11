package com.example.meety1.dto;

import com.example.meety1.entity.Interest;
import io.swagger.v3.oas.annotations.media.Schema;
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

    @Schema(name = "User Id", example = "1")
    private Long id;
    @Schema(name = "Person's first name", example = "Maria")
    private String firstName;
    @Schema(name = "Person's last name", example = "Krushynska")
    private String lastName;
    @Schema(name = "Person's email", example = "example@gmail.com")
    private String email;
    @Schema(name = "Person's open info, that everybody can see")
    private String openInfo;
    @Schema(name = "Person's private info that only matched persons can see")
    private String privateInfo;
    @Schema(name = "User's interests")
    private List<Interest> interests;
}
