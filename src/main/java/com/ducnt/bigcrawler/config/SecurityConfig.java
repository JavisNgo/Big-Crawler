package com.ducnt.bigcrawler.config;

import com.ducnt.bigcrawler.jwt.JwtAuthConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    @Autowired
    JwtAuthConverter jwtAuthConverter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable);
        http.authorizeHttpRequests(authorize -> {
            authorize
                    .requestMatchers("/auth/*").hasRole("user")
                    .requestMatchers("/api/*").hasRole("user")
                    .anyRequest()
                    .authenticated();
        });
        http.oauth2ResourceServer(t -> {
            t.jwt(configuer -> configuer.jwtAuthenticationConverter(jwtAuthConverter));
        });
        http.sessionManagement(t ->
                t.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
        return http.build();
    }

}
