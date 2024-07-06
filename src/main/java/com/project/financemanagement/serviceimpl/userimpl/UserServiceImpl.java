package com.project.financemanagement.serviceimpl.userimpl;

import com.project.financemanagement.entity.User;
import com.project.financemanagement.repository.UserRepository;
import com.project.financemanagement.request.UserDto;
import com.project.financemanagement.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public String registerUser(List<UserDto> userDtoList) {
        int count  = 0;
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        for(UserDto userDto : userDtoList) {
            try {
                User user = User.builder().username(userDto.getUsername()).email(userDto.getEmail())
                        .enabled(userDto.isEnabled()).role(userDto.getRole())
                        .password(bCryptPasswordEncoder.encode(userDto.getPassword()))
                        .roles(userDto.getRoles()).build();
                userRepository.save(user);
                count++;
            } catch (Exception ex) {
                System.out.println("Some exception this ie. " + ex.getMessage());
            }
        }
        return "Successfully saved " + count +" records out of total ::" + userDtoList.size();
    }

    @Override
    public Optional<User> getUserById(Long userId) {
        return userRepository.findById(userId);
    }

    @Override
    public User updateUser(User user) {
        return userRepository.save(user);
    }

    @Override
    public void deleteUser(Long userId) {
        userRepository.deleteById(userId);
    }
}
