package com.project.financemanagement.config;

import com.project.financemanagement.entity.User;
import com.project.financemanagement.request.MyObject;
import com.project.financemanagement.request.UserDto;
import com.project.financemanagement.service.user.UserService;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class CustomItemWriter implements ItemWriter<User> {

    @Autowired
    private UserService userService; // Autowire your service

    @Override
    public void write(Chunk<? extends User> items) throws Exception {
        List<MyObject> myObjectList = items.getItems().stream()
                .map(this::convertToMyObject)
                .toList();
        userService.registerUserByExcel(myObjectList);
    }

    private MyObject convertToMyObject(User user) {
        MyObject myObject = new MyObject();
        // Assuming MyObject and User have similar fields
        myObject.setField1(user.getUsername());
        myObject.setField2(user.getPassword());
        myObject.setField3(user.getEmail());
        myObject.setField4(user.getRole());
        myObject.setField5(String.valueOf(user.isEnabled()));
        return myObject;
    }
}
















//    @Autowired
//    private UserService userService; // Autowire your service
//
//    @Override
//    @SuppressWarnings("unchecked")
//    public void write(Chunk<? extends MyObject> chunk) throws Exception {
//        List<MyObject> items = (List<MyObject>) chunk.getItems();
//        userService.registerUserByExcel(items);
//    }




