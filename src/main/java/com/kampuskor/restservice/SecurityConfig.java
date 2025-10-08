package com.kampuskor.restservice;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.kampuskor.restservice.utils.security.JwtAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    private final JwtAuthenticationFilter jwtFilter;

    public SecurityConfig(JwtAuthenticationFilter jwtFilter) {
        this.jwtFilter = jwtFilter;
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
    
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(auth -> auth
                    .requestMatchers("/auth/register", "/auth/login").permitAll()
                    
                    .requestMatchers(HttpMethod.GET, "/courses/**").hasAnyRole("STUDENT", "INSTRUCTOR", "ADMIN")
                    .requestMatchers(HttpMethod.POST, "/courses/**").hasAnyRole("INSTRUCTOR", "ADMIN")
                    .requestMatchers(HttpMethod.PUT, "/courses/**").hasAnyRole("INSTRUCTOR", "ADMIN")
                    .requestMatchers(HttpMethod.DELETE, "/courses/**").hasAnyRole("INSTRUCTOR", "ADMIN")

                    .requestMatchers("/users/**").hasRole("ADMIN")

                    .requestMatchers(HttpMethod.GET, "/instructors").hasAnyRole("ADMIN")
                    .requestMatchers(HttpMethod.GET, "/instructors/{username}").hasAnyRole("STUDENT", "ADMIN")

                    .requestMatchers(HttpMethod.GET, "/students").hasAnyRole("ADMIN")
                    .requestMatchers(HttpMethod.GET, "/students/{username}").hasAnyRole("INSTRUCTOR", "ADMIN")
                                   
                    .anyRequest().authenticated()
            )
            .sessionManagement(session -> session
                    .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            )
            .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
