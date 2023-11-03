package com.example.meety1.service.impl;

import com.example.meety1.dto.PendingMatchDto;
import com.example.meety1.dto.UserMatchDto;
import com.example.meety1.entity.Match;
import com.example.meety1.entity.MatchKey;
import com.example.meety1.entity.User;
import com.example.meety1.exception.NoInviteFoundException;
import com.example.meety1.repository.MatchRepository;
import com.example.meety1.repository.UserRepository;
import com.example.meety1.service.MatchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class MatchServiceImpl implements MatchService {
    @Autowired
    private MatchRepository matchRepository;

    // Maybe need to add service layer
    @Autowired
    private UserRepository userRepository;

    @Override
    public List<UserMatchDto> getUserMatches(Long id) {
        List<Match> userMatches = matchRepository.getAllUserMatchesEntityList(id);
        List<UserMatchDto> dtos = new ArrayList<>();
        for (Match match : userMatches) {
            User matchedUser;
            if (Objects.equals(match.getUser1().getId(), id)) {
                matchedUser = match.getUser2();
            } else {
                matchedUser = match.getUser1();
            }
            dtos.add(new UserMatchDto(matchedUser.getId(), matchedUser.getEmail(),
                    matchedUser.getFirstName(), matchedUser.getLastName(), matchedUser.getInterests()));
        }
        return dtos;
    }

    @Override
    public List<PendingMatchDto> getPendingMatches(Long id) {
        List<Match> pendingUserMatches = matchRepository.getPendingMatchesEntityList(id);
        List<PendingMatchDto> dtos = new ArrayList<>();
        for (Match pendingMatch : pendingUserMatches) {
            User requester;
            if (Objects.equals(pendingMatch.getUser1().getId(), id)) {
                requester = pendingMatch.getUser2();
            } else {
                requester = pendingMatch.getUser1();
            }
            dtos.add(new PendingMatchDto(requester.getId(), requester.getEmail(),
                    requester.getFirstName(), requester.getLastName(), requester.getInterests()));
        }
        return dtos;
    }

    // TODO: RequesterId here means Principal Id, so it should be changed to that later)
    @Override
    public Match acceptMatchByMatchKey(Long requesterId, Long responderId) {
        if (requesterId > responderId) {
            Long temp = requesterId;
            requesterId = responderId;
            responderId = temp;
        }
        MatchKey id = new MatchKey(requesterId, responderId);
        Match matchToAccept = matchRepository.findById(id)
                .orElseThrow(() -> new NoInviteFoundException("There is no such match request to accept"));
        matchToAccept.setFriends(true);
        return matchRepository.save(matchToAccept);
    }

    public void declineMatchInvite(Long requesterId, Long responderId) {
        if (requesterId > responderId) {
            Long temp = requesterId;
            requesterId = responderId;
            responderId = temp;
        }
        MatchKey id = new MatchKey(requesterId, responderId);
        matchRepository.findById(id)
                .orElseThrow(() -> new NoInviteFoundException("There is no such match request to decline"));
        matchRepository.deleteById(id);
    }

    @Override
    public Match sendMatchInvite(Long requesterId, Long responderId) {
        boolean mixed = false;
        if (requesterId > responderId) {
            Long temp = requesterId;
            requesterId = responderId;
            responderId = temp;
            mixed = true;
        }

        // TODO: After adding spring security change exception to UsernameNotFoundException!!!
        User user1 = userRepository.findById(requesterId).orElseThrow(() -> new RuntimeException("Username not found"));
        User user2 = userRepository.findById(responderId).orElseThrow(() -> new RuntimeException("Username not found"));

        if (matchRepository.findById(new MatchKey(requesterId, responderId)).isPresent()) {
            throw new RuntimeException("You have already sent the match request");
        }

        Match match = new Match();
        MatchKey matchKey = new MatchKey();
        matchKey.setUserFirstId(user1.getId());
        matchKey.setUserSecondId(user2.getId());
        match.setId(matchKey);
        match.setUser1(user1);
        match.setUser2(user2);
        match.setFriends(false);
        if (mixed) {
            match.setPendingSecondFirst(true);
            match.setPendingFirstSecond(false);
        } else {
            match.setPendingFirstSecond(true);
            match.setPendingSecondFirst(false);
        }
        return matchRepository.save(match);
    }
}
