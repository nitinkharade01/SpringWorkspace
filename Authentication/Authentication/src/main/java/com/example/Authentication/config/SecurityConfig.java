package com.example.Authentication.config;

//package com.example.auth.config;
import com.example.Authentication.security.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

 private final JwtAuthenticationFilter jwtAuthenticationFilter = new JwtAuthenticationFilter();

 @Bean
 public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
     http
             .csrf(csrf -> csrf.disable()) // CSRF disabled for stateless REST. [web:22][web:31]
             .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
             .authorizeHttpRequests(auth -> auth
                     .requestMatchers("/api/auth/register", "/api/auth/login").permitAll()
                     .anyRequest().authenticated()
             )
             .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

     return http.build();
 }

 @Bean
 public PasswordEncoder passwordEncoder() {
     return new BCryptPasswordEncoder(); // BCrypt hashing.[web:19]
 }

 @Bean
 public AuthenticationManager authenticationManager(
         AuthenticationConfiguration configuration
 ) throws Exception {
     return configuration.getAuthenticationManager();
 }
}
