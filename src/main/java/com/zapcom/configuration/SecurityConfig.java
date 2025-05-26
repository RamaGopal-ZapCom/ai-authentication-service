package com.zapcom.configuration;

import com.zapcom.service.JwtFilter;
import com.zapcom.service.impl.UserDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
class SecurityConfig {

    private final JwtFilter jwtFilter;
    private final UserDetailsService customerDetailsService;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http){
        try {
            return http.csrf().disable()
                    .authorizeHttpRequests(auth -> auth
                            .requestMatchers("/auth/login", "/auth/register", "/auth/verify/*", "/auth/customer/**").permitAll()
                            .anyRequest().authenticated()
                    )
                    .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                    .userDetailsService(customerDetailsService)
                    .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
                    .build();
        }catch (Exception e){
            throw new RuntimeException(e);
        }
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(10);
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config){
        try {
            return config.getAuthenticationManager();
        }catch (Exception e){
            throw new RuntimeException(e);
        }
    }
}


