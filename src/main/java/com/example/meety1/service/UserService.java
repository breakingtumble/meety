package com.example.meety1.service;

import com.example.meety1.dto.UserOpenInfoDto;
import com.example.meety1.entity.User;

import java.util.List;

public interface UserService {
    List<UserOpenInfoDto> getNextTenUsers(Long requesterId);

    List<UserOpenInfoDto> getUsersFiltered(Long requesterId, List<String> interests);

    User findUserById(Long id);

    User saveUser(User user);
}
