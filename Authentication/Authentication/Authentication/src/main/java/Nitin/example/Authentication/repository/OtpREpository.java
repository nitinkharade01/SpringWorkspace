package Nitin.example.Authentication.repository;  // Replace with your actual package

import Nitin.example.Authentication.entity.Otp;  // Replace with your actual Otp entity package
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface OtpRepository extends JpaRepository<Otp, String> {
    Optional<Otp> findByEmail(String email);
}
