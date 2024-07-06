package com.project.financemanagement.serviceimpl.userimpl;

import com.project.financemanagement.entity.User;
import com.project.financemanagement.repository.UserRepository;
import com.project.financemanagement.request.MyObject;
import com.project.financemanagement.request.UserDto;
import com.project.financemanagement.service.user.UserService;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;

@Slf4j
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    private final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

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
                logger.error("Some exception this ie. {}", ex.getMessage());
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

    @Override
    public boolean isUserPresent(String userName) {
        Optional<User> user = userRepository.findByUsername(userName);
        return user.isPresent();
    }

    @Override
    @Cacheable("registerUserByExcel")
    public String registerUserByExcel(List<MyObject> myObjectList) {
        int count  = 0;
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        Set<String> users = new HashSet<>();
        for(MyObject object : myObjectList) {
            try {
                Optional<User> user = userRepository.findByUsername(object.getField1());
                if(user.isEmpty()){
                    User user1 = User.builder().username(object.getField1()).email(object.getField3())
                            .enabled(Boolean.parseBoolean(object.getField4())).role(object.getField4())
                            .password(bCryptPasswordEncoder.encode(object.getField2()))
                            .roles(new HashSet<>(Collections.singletonList(object.getField4()))).build();
                    userRepository.save(user1);
                    count++;
                }
                else{
                    users.add(object.getField1());
                }
            } catch (Exception ex) {
                logger.error("Some exception this ie. {} ", ex.getMessage());
            }
        }
        return "Successfully saved " + count +" records out of total ::" + myObjectList.size() + "\n" +
                "Users that we not saved as already present are :: " + users.toString();
    }

    @Override
    public List<String> getByUserRole(String role) {
        List<User> user = userRepository.findByRole(role);
        return user.stream().map(User::getUsername).toList();
    }
}
