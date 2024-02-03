package com.maveric.userservice.service;

import com.maveric.userservice.dto.UserDto;
import com.maveric.userservice.dto.UserEmailDto;

import java.util.List;

public interface UserService {
    UserDto createUser(UserDto userDto);
    UserDto updateUser(UserDto userDto, String userId);
    List<UserDto> getAllUsers(int page, int pageSize);
    UserDto getUserById(String id);
    UserEmailDto getUserByEmail(String email);
    void deleteUser(String id);
}
