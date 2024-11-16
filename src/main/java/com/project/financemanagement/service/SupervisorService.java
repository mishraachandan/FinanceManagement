package com.project.financemanagement.service;

import com.project.financemanagement.entity.User;
import com.project.financemanagement.repository.UserRepository;
import com.project.financemanagement.responseVo.Supervisor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class SupervisorService {

    @Autowired
    private UserRepository userRepository;

    public List<Supervisor> getAllSupervisors(){
        List<User> users =  userRepository.findAll();
        List<Supervisor> re = new ArrayList<>();
        for(User user : users){
            Supervisor supervisor = new Supervisor(user.getUsername());
            re.add(supervisor);
        }
        return re;
    };
}
