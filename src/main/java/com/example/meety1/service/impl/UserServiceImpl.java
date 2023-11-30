package com.example.meety1.service.impl;

import com.example.meety1.entity.Match;
import com.example.meety1.entity.User;
import com.example.meety1.exception.UserNotFoundException;
import com.example.meety1.repository.UserRepository;
import com.example.meety1.service.MatchService;
import com.example.meety1.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MatchService matchService;

    @Override
    public List<User> getNextTenUsers(Long requesterId) {
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
                return users;
            }
            if (users.size() < 10 && users.size() > 0) {
                user.setCountedRowsIndex(0L);
                userRepository.save(user);
                return users;
            }
            throw new UserNotFoundException("Can't find recommendations to show");
        }
        throw new UserNotFoundException("There is no such user in the system");
    }
}
