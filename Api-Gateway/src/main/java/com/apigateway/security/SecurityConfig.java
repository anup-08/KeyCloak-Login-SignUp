package com.apigateway.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;

@EnableWebFluxSecurity
@Configuration
public class SecurityConfig {

    @Bean
    public SecurityWebFilterChain securityFilterChain(ServerHttpSecurity http) {
        http.authorizeExchange(request->
                request.pathMatchers("/auth/**").permitAll()
                        .anyExchange().authenticated());

        http.oauth2ResourceServer(oath2->oath2.jwt(Customizer.withDefaults()));

        http.csrf(csrf->csrf.disable());
        return http.build();
    }
}
