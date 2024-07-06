//package com.project.financemanagement.security;
//
//import com.project.financemanagement.config.CustomUserDetailsService;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
//import org.springframework.security.config.annotation.web.configurers.LogoutConfigurer;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.security.web.SecurityFilterChain;
//
//@Configuration
//@EnableWebSecurity
//public class SecurityConfig {
//
//    @Bean
//    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//        http
//                .csrf(csrf -> csrf.disable())
//                .authorizeRequests(authorizeRequests ->
//                        authorizeRequests
//                                .requestMatchers("/public/**").permitAll() // Allow public access to specific endpoints
//                                .anyRequest().authenticated() // Require authentication for any other request
//                )
//                .formLogin(formLogin ->
//                        formLogin
//                                .loginPage("/login").permitAll() // Custom login page
//                                .defaultSuccessUrl("/home", true)
//                )
//                .logout(LogoutConfigurer::permitAll
//                );
//        return http.build();
//    }
//
//    @Bean
//    public PasswordEncoder passwordEncoder() {
//        return new BCryptPasswordEncoder();
//    }
//
//    // Define a bean for UserDetailsService if needed
//    @Bean
//    public UserDetailsService userDetailsService() {
//        return new CustomUserDetailsService(); // Assuming CustomUserDetailsService is implemented
//    }
//}
