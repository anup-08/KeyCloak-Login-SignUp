package com.authServer.security;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        // Chain all configurations together off the 'http' object
        http
                .authorizeHttpRequests(request -> request
                        .requestMatchers("/auth/signup").permitAll() // allow public signup
                        .anyRequest().authenticated()               // everything else secured
                )

                .csrf(csrf -> csrf.disable()); // Now this will be applied correctly

        return http.build();
    }
}
