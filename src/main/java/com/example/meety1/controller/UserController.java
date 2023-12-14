package com.example.meety1.controller;

import com.example.meety1.dto.ProfileChangeDto;
import com.example.meety1.dto.RegistrationDto;
import com.example.meety1.dto.ResponseDto;
import com.example.meety1.dto.UserOpenInfoDto;
import com.example.meety1.entity.Interest;
import com.example.meety1.entity.User;
import com.example.meety1.repository.InterestRepository;
import com.example.meety1.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class UserController {
    private final UserService userService;

    private final InterestRepository interestRepository;

    public UserController(UserService userService, InterestRepository interestRepository) {
        this.userService = userService;
        this.interestRepository = interestRepository;
    }

    @Operation(summary = "Get recommended users", description = "Gets recommendation list of  10 users, it's auto paginating" +
            ", if parameter interest provided return filtered list of users based on specified interests.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User recommendations to show",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = UserOpenInfoDto.class))}),
            @ApiResponse(responseCode = "404", description = "User not found",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ResponseDto.class))}),
            @ApiResponse(responseCode = "400", description = "No recommendations found for the user",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ResponseDto.class))})
    })

    @GetMapping("/recommendations")
    public ResponseEntity<List<UserOpenInfoDto>> getNextTenUsers(@RequestParam("requesterId") Long requesterId,
                                                                 @RequestParam(value = "interest", required = false) List<String> interests) {
        List<UserOpenInfoDto> users;
        if (interests != null) {
            users = userService.getUsersFiltered(requesterId, interests);
        } else {
            users = userService.getNextTenUsers(requesterId);
        }
        return ResponseEntity.status(HttpStatus.OK).body(users);
    }

    @PostMapping("/user")
    public ResponseEntity<?> registerUser(@RequestBody RegistrationDto dto) {
        List<Interest> interests = new ArrayList<>();
        if (dto.getInterests().size() == 0) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        for (Interest interest : dto.getInterests()) {
            Optional<Interest> optionalInterest = interestRepository.findByName(interest.getName());
            optionalInterest.ifPresent(interests::add);
        }
        User user = new User(dto.getFirstName(), dto.getLastName(),
                dto.getPrivateInfo(), dto.getOpenInfo(), dto.getEmail(),
                dto.getPassword(), interests);
        return ResponseEntity.status(HttpStatus.OK).body(userService.saveUser(user));
    }

    @PatchMapping("/user/{id}")
    public ResponseEntity<?> changeUserInfo(@PathVariable("id") Long requesterId,
                                            @Valid @RequestBody ProfileChangeDto profileChangeData) {
        User user = userService.findUserById(requesterId);
        if (user != null) {
            user.setFirstName(profileChangeData.getFirstName());
            user.setLastName(profileChangeData.getLastName());
            user.setOpenInfo(profileChangeData.getOpenInfo());
            user.setPrivateInfo(profileChangeData.getPrivateInfo());
            return ResponseEntity.status(HttpStatus.OK).body(userService.saveUser(user));
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseDto("No user found"));
    }
}
