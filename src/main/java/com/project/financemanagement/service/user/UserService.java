package com.project.financemanagement.service.user;

import com.project.financemanagement.entity.User;
import com.project.financemanagement.request.MyObject;
import com.project.financemanagement.request.UserDto;

import java.util.List;
import java.util.Optional;

public interface UserService {
    String registerUser(List<UserDto> userDtoList);
    Optional<User> getUserById(Long userId);
    User updateUser(User user);
    void deleteUser(Long userId);
    boolean isUserPresent(String userName);
    String registerUserByExcel(List<MyObject> objectList);

    List<String> getByUserRole(String role);
}

