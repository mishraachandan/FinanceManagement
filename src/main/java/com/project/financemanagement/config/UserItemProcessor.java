package com.project.financemanagement.config;

import com.project.financemanagement.entity.User;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

@Component
public class UserItemProcessor implements ItemProcessor<User, User> {
    @Override
    public User process(final User user) throws Exception {
        // Implement any processing logic if needed
        return user;
    }
}
