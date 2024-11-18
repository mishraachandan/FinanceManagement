package com.project.financemanagement.service.user;

import com.project.financemanagement.entity.User;
import com.project.financemanagement.request.MyObject;
import com.project.financemanagement.request.UserDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.Optional;

public interface UserService {
    String registerUser(List<UserDto> userDtoList);
    Optional<User> getUserById(Long userId);
    User updateUser(User user);
    void deleteUser(Long userId);
    boolean isUserPresent(String userName);
    String registerUserByExcel(List<MyObject> objectList);

    Page<String> getByUserRole(String role, int page, int size, Sort sort);
}

