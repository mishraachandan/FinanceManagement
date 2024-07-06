package com.project.financemanagement.service.user;

import com.project.financemanagement.entity.User;
import com.project.financemanagement.request.UserDto;

import java.util.List;
import java.util.Optional;

public interface UserService {
    public String registerUser(List<UserDto> userDtoList);
//    User registerUser(User user);
    Optional<User> getUserById(Long userId);
    User updateUser(User user);
    void deleteUser(Long userId);
}

