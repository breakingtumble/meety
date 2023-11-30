package com.example.meety1.controller;

import com.example.meety1.dto.PendingMatchDto;
import com.example.meety1.dto.ResponseDto;
import com.example.meety1.dto.UserMatchDto;
import com.example.meety1.entity.Match;
import com.example.meety1.entity.User;
import com.example.meety1.exception.InviteAlreadyExistsException;
import com.example.meety1.exception.NoInviteFoundException;
import com.example.meety1.exception.UserNotFoundException;
import com.example.meety1.repository.MatchRepository;
import com.example.meety1.repository.UserRepository;
import com.example.meety1.service.MatchService;
import com.example.meety1.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class MatchController {
    @Autowired
    MatchRepository matchRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    MatchService matchService;

    @Autowired
    UserService userService;

    @GetMapping("/matches")
    public ResponseEntity<List<UserMatchDto>> currentUserMatches(@RequestParam("id") Long id) {
        // TODO: Needs to implement Spring Security, so we need to call Principal method and find matches
        // TODO: Remove @RequestParam annotation, just add Principal id to the service layer
        List<UserMatchDto> userMatches = matchService.getUserMatches(id);
        return ResponseEntity.status(HttpStatus.OK).body(userMatches);
    }

    @GetMapping("/pending-matches")
    public ResponseEntity<List<PendingMatchDto>> checkPendingMatches(@RequestParam("id") Long id) {
        List<PendingMatchDto> pendingMatches = matchService.getPendingMatches(id);
        return ResponseEntity.status(HttpStatus.OK).body(pendingMatches);
    }

    // TODO: After implementing Spring Security, we should change responderId to Principal id
    // TODO: Make a message response with successful invite accept
    // TODO: Make the API return BAD Request(400) when trying to accept match request with which already exists
    @PutMapping("/accept-invite")
    public ResponseEntity<?> acceptMatchInvite(@RequestParam("requesterId") Long requesterId,
                                               @RequestParam("responderId") Long responderId) {
        Match matchToAccept = matchService.acceptMatchByMatchKey(requesterId, responderId);
        if (matchToAccept != null) {
            return ResponseEntity.status(HttpStatus.ACCEPTED).build();
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }

    // TODO: After implementing Spring Security, we should change responderId to Principal idc
    @DeleteMapping("/decline-invite")
    public ResponseEntity<?> declineMatchInvite(@RequestParam("requesterId") Long requesterId,
                                                @RequestParam("responderId") Long responderId) {
        matchService.declineMatchInvite(requesterId, responderId);
        return ResponseEntity.status(HttpStatus.OK).body(new ResponseDto("Match was successfully declined"));
    }


    // TODO: After implementing Spring Security, we should change requesterId to Principal id
    @PostMapping("/send-invite")
    public ResponseEntity<?> sendMatchInvite(@RequestParam("requesterId") Long requesterId,
                                             @RequestParam("responderId") Long responderId) {
        Match match = matchService.sendMatchInvite(requesterId, responderId);
        if (match != null) {
            return ResponseEntity.status(HttpStatus.OK).body(new ResponseDto("Invite was successfully sent"));
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }

    @GetMapping("/recommendations")
    public ResponseEntity<?> getNextTenUsers(@RequestParam("requesterId") Long requesterId) {
        List<User> users = userService.getNextTenUsers(requesterId);
        return ResponseEntity.status(HttpStatus.OK).body(users);
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
}
