package com.example.meety1.service.impl;

import com.example.meety1.dto.UserOpenInfoDto;
import com.example.meety1.entity.Match;
import com.example.meety1.entity.User;
import com.example.meety1.exception.NoRecommendationsFoundException;
import com.example.meety1.exception.UserNotFoundException;
import com.example.meety1.repository.InterestRepository;
import com.example.meety1.repository.UserRepository;
import com.example.meety1.service.MatchService;
import com.example.meety1.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    private final MatchService matchService;

    private final InterestRepository interestRepository;

    public UserServiceImpl(UserRepository userRepository, MatchService matchService, InterestRepository interestRepository) {
        this.userRepository = userRepository;
        this.matchService = matchService;
        this.interestRepository = interestRepository;
    }

    @Override
    public List<UserOpenInfoDto> getNextTenUsers(Long requesterId) {
        Optional<User> optionalUser = userRepository.findById(requesterId);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            List<User> users = userRepository.getTenUsers(user.getCountedRowsIndex(), user.getId());
            List<Match> matches = matchService.getUserMatchEntities(requesterId);
            for (Match match : matches) {
                User matchedPerson = match.getUser1();
                if (users.contains(matchedPerson)) {
                    users.remove(matchedPerson);
                } else {
                    matchedPerson = match.getUser2();
                    users.remove(matchedPerson);
                }
            }

            if (users.size() == 10) {
                user.setCountedRowsIndex(user.getCountedRowsIndex() + 10);
                userRepository.save(user);
                return users.stream().map((userToMap ->
                    new UserOpenInfoDto(userToMap.getId(), userToMap.getFirstName(),
                            userToMap.getLastName(), userToMap.getEmail(),
                            userToMap.getOpenInfo(), userToMap.getInterests())
                )).toList();
            }
            if (users.size() < 10 && users.size() > 0) {
                user.setCountedRowsIndex(0L);
                userRepository.save(user);
                return users.stream().map((userToMap ->
                        new UserOpenInfoDto(userToMap.getId(), userToMap.getFirstName(),
                                userToMap.getLastName(), userToMap.getEmail(),
                                userToMap.getOpenInfo(), userToMap.getInterests())
                )).toList();
            }
            throw new NoRecommendationsFoundException("Can't find recommendations to show");
        }
        throw new UserNotFoundException("There is no such user in the system");
    }

    public List<UserOpenInfoDto> getUsersFiltered(Long requesterId, List<String> interests) {
        Optional<User> optionalUser = userRepository.findById(requesterId);
        List<String> filteredInterests = new ArrayList<>();
        for (String interest : interests) {
            if (interestRepository.findByName(interest).isPresent()) {
                filteredInterests.add(interest);
            }
        }
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            List<User> users = userRepository.getTenUsersFiltered(user.getId(), filteredInterests);
            List<Match> matches = matchService.getUserMatchEntities(requesterId);
            for (Match match : matches) {
                User matchedPerson = match.getUser1();
                if (users.contains(matchedPerson)) {
                    users.remove(matchedPerson);
                } else {
                    matchedPerson = match.getUser2();
                    users.remove(matchedPerson);
                }
            }
            return users.stream().map((userToMap ->
                    new UserOpenInfoDto(userToMap.getId(), userToMap.getFirstName(),
                            userToMap.getLastName(), userToMap.getEmail(),
                            userToMap.getOpenInfo(), userToMap.getInterests())
            )).toList();
        }
        throw new UserNotFoundException("There is no such user in the system");
    }
}
