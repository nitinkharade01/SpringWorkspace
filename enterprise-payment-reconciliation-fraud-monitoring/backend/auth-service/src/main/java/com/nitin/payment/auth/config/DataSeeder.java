package com.nitin.payment.auth.config;

import com.nitin.payment.auth.entity.Role;
import com.nitin.payment.auth.entity.RoleName;
import com.nitin.payment.auth.entity.User;
import com.nitin.payment.auth.repository.RoleRepository;
import com.nitin.payment.auth.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Set;

@Configuration
@RequiredArgsConstructor
public class DataSeeder {
    @Bean
    CommandLineRunner seed(RoleRepository roles, UserRepository users, PasswordEncoder encoder) {
        return args -> {
            for (RoleName roleName : RoleName.values()) {
                roles.findByName(roleName).orElseGet(() -> {
                    Role role = new Role();
                    role.setName(roleName);
                    return roles.save(role);
                });
            }
            createUser(users, roles, encoder, "Admin User", "admin@payment.com", "Admin@123", RoleName.ADMIN);
            createUser(users, roles, encoder, "Finance User", "finance@payment.com", "Finance@123", RoleName.FINANCE_USER);
            createUser(users, roles, encoder, "Audit User", "auditor@payment.com", "Auditor@123", RoleName.AUDITOR);
        };
    }

    private void createUser(UserRepository users, RoleRepository roles, PasswordEncoder encoder, String name, String email, String password, RoleName roleName) {
        if (users.existsByEmail(email)) {
            return;
        }
        User user = new User();
        user.setFullName(name);
        user.setEmail(email);
        user.setPassword(encoder.encode(password));
        user.setRoles(Set.of(roles.findByName(roleName).orElseThrow()));
        users.save(user);
    }
}
