package com.example.meety1.controller;

import com.example.meety1.dto.PendingMatchDto;
import com.example.meety1.dto.ResponseDto;
import com.example.meety1.dto.UserMatchDto;
import com.example.meety1.dto.UserOpenInfoDto;
import com.example.meety1.entity.Match;
import com.example.meety1.exception.*;
import com.example.meety1.service.MatchService;
import com.example.meety1.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class MatchController {
    private final MatchService matchService;

    private final UserService userService;

    public MatchController(MatchService matchService, UserService userService) {
        this.matchService = matchService;
        this.userService = userService;
    }

    @Operation(summary = "Get current user matches by his Id", description = "Returns the UserMatchDto list by the user id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found user matches",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = UserMatchDto.class))}),
            @ApiResponse(responseCode = "400", description = "Invalid user id supplied",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ResponseDto.class))})
    })
    @GetMapping("/matches")
    public ResponseEntity<List<UserMatchDto>> currentUserMatches(@RequestParam("id") Long id) {
        // TODO: Needs to implement Spring Security, so we need to call Principal method and find matches
        // TODO: Remove @RequestParam annotation, just add Principal id to the service layer
        List<UserMatchDto> userMatches = matchService.getUserMatches(id);
        return ResponseEntity.status(HttpStatus.OK).body(userMatches);
    }

    @Operation(summary = "Get current user pending matches by his Id", description = "Returns the PendingMatchDto list by the user id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found user pending matches",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = PendingMatchDto.class))}),
            @ApiResponse(responseCode = "400", description = "Invalid user id supplied",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ResponseDto.class))})
    })
    @GetMapping("/pending-matches")
    public ResponseEntity<List<PendingMatchDto>> checkPendingMatches(@RequestParam("id") Long id) {
        List<PendingMatchDto> pendingMatches = matchService.getPendingMatches(id);
        return ResponseEntity.status(HttpStatus.OK).body(pendingMatches);
    }

    // TODO: After implementing Spring Security, we should change responderId to Principal id
    @Operation(summary = "Accept invite by the user", description = "Returns ResponseDto with success message")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Invite was successfully accepted",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ResponseDto.class))}),
            @ApiResponse(responseCode = "404", description = "No invite found",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ResponseDto.class))}),
            @ApiResponse(responseCode = "400", description = "Invite already accepted",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ResponseDto.class))}),
            @ApiResponse(responseCode = "400", description = "Unable to accept invite",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ResponseDto.class))}),
            @ApiResponse(responseCode = "400", description = "Invalid user id provided",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ResponseDto.class))})
    })
    @PutMapping("/accept-invite")
    public ResponseEntity<ResponseDto> acceptMatchInvite(@RequestParam("requesterId") Long requesterId,
                                                         @RequestParam("responderId") Long responderId) {
        Match matchToAccept = matchService.acceptMatchByMatchKey(requesterId, responderId);
        if (matchToAccept != null) {
            return ResponseEntity.status(HttpStatus.ACCEPTED).body(new ResponseDto("Invite was successfully accepted."));
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }


    @Operation(summary = "Decline invite by the user", description = "Declines invite by deleting it from db," +
            "returns ResponseDto.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Match was successfully declined",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ResponseDto.class))}),
            @ApiResponse(responseCode = "404", description = "No invite found",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ResponseDto.class))})
    })
    // TODO: After implementing Spring Security, we should change responderId to Principal idc
    @DeleteMapping("/decline-invite")
    public ResponseEntity<?> declineMatchInvite(@RequestParam("requesterId") Long requesterId,
                                                @RequestParam("responderId") Long responderId) {
        matchService.declineMatchInvite(requesterId, responderId);
        return ResponseEntity.status(HttpStatus.OK).body(new ResponseDto("Match was successfully declined."));
    }

    @Operation(summary = "Send invite to the user", description = "Sends invite to the user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Invite was successfully sent",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ResponseDto.class))}),
            @ApiResponse(responseCode = "400", description = "No user not found",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ResponseDto.class))}),
            @ApiResponse(responseCode = "400", description = "Invite already exists",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ResponseDto.class))}),
            @ApiResponse(responseCode = "400", description = "Match already exists",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ResponseDto.class))})
    })
    // TODO: After implementing Spring Security, we should change requesterId to Principal id
    @PostMapping("/send-invite")
    public ResponseEntity<ResponseDto> sendMatchInvite(@RequestParam("requesterId") Long requesterId,
                                                       @RequestParam("responderId") Long responderId) {
        Match match = matchService.sendMatchInvite(requesterId, responderId);
        if (match != null) {
            return ResponseEntity.status(HttpStatus.OK).body(new ResponseDto("Invite was successfully sent."));
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }

    @ExceptionHandler(NoInviteFoundException.class)
    public ResponseEntity<ResponseDto> handleNoInviteFoundException(RuntimeException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseDto(e.getMessage()));
    }

    @ExceptionHandler(InviteAlreadyExistsException.class)
    public ResponseEntity<ResponseDto> handleInviteAlreadyExistsException(RuntimeException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseDto(e.getMessage()));
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ResponseDto> handleUserNotFoundException(RuntimeException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseDto(e.getMessage()));
    }

    @ExceptionHandler(InvalidUserIdException.class)
    public ResponseEntity<ResponseDto> handleInvalidUserIdException(RuntimeException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseDto(e.getMessage()));
    }

    @ExceptionHandler(InviteAlreadyAcceptedException.class)
    public ResponseEntity<ResponseDto> handleInviteAlreadyAcceptedException(RuntimeException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseDto(e.getMessage()));
    }

    @ExceptionHandler(UnableToAcceptInviteException.class)
    public ResponseEntity<ResponseDto> handleUnableToAcceptInviteException(RuntimeException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseDto(e.getMessage()));
    }

    @ExceptionHandler
    public ResponseEntity<ResponseDto> handleNoRecommendationsFoundException(RuntimeException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseDto(e.getMessage()));
    }
}
