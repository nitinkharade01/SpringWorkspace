package Nitin.example.Authentication.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import Nitin.example.Authentication.entity.User;

//import dilip.example.Authentication.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
}
