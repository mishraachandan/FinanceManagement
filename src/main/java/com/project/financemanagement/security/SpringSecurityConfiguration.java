package com.project.financemanagement.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

import static org.springframework.security.config.Customizer.withDefaults;

//Indicates that a class declares one or more @Bean methods and may be processed by the
// Spring container to generate bean definitions and service requests for those beans at runtime
@Configuration
public class SpringSecurityConfiguration {


    /*Defines a filter chain which is capable of being matched against an HttpServletRequest. in order to decide whether it applies to that request.*/
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {

//        All requests are authenticated
        httpSecurity.authorizeHttpRequests(auth -> auth.anyRequest().authenticated());

        // If a request is not authenticated a web page is shown.
        httpSecurity.httpBasic(withDefaults());   // it will give a pop-up.


//                  CSRF -> POST, PUT
        httpSecurity.csrf(csrf -> csrf.disable());

        return httpSecurity.build();
    }
}