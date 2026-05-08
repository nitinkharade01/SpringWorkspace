package com.nitin.payment.auth.repository;

import com.nitin.payment.auth.entity.Role;
import com.nitin.payment.auth.entity.RoleName;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByName(RoleName name);
}
