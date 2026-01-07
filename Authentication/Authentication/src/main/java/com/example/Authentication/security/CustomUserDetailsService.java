package com.example.Authentication.security;

//package com.example.auth.security;
import com.example.Authentication.entity.User;
import com.example.Authentication.repository.UserRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

 private final UserRepository userRepository = null;

 @Override
 public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
     User user = userRepository.findByEmail(email)
             .orElseThrow(() -> new UsernameNotFoundException("User not found"));

     GrantedAuthority authority = new SimpleGrantedAuthority(user.getRole()); // e.g. ROLE_USER.
     return new org.springframework.security.core.userdetails.User(
             user.getEmail(),
             user.getPassword(),
             List.of(authority)
     );
 }
}
