package Nitin.example.Authentication.service;


import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.time.LocalDateTime;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import Nitin.example.Authentication.entity.Otp;
import Nitin.example.Authentication.entity.User;
import Nitin.example.Authentication.repository.OtpRepository;
import Nitin.example.Authentication.repository.UserRepository;
import jakarta.transaction.Transactional;


@Service
@Transactional
public class OtpService {
    @Autowired private OtpRepository otpRepository;
    @Autowired private UserRepository userRepository;
    @Autowired private JavaMailSender mailSender;
    @Autowired private PasswordEncoder encoder;  // Changed from BCryptPasswordEncoder
    public String generateAndSendOtp(String email) {
        User user = userRepository.findByEmail(email)
            .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        
        // Generate 6-digit OTP randomly (your exact logic)
        String otp = String.valueOf(100000 + new Random().nextInt(900000));
        
        // Set 10-minute expiry (your exact logic)
        Otp otpEntity = new Otp(email, otp, LocalDateTime.now().plusMinutes(10));
        otpRepository.save(otpEntity);
        
        sendOtpEmail(email, otp);
        return "OTP sent successfully"; // Remove 'return otp' in production
    }

    private void sendOtpEmail(String email, String otp) {
        System.out.println("📧 SENDING EMAIL TO: " + email);
        System.out.println("🔑 OTP CODE: " + otp);
        
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setSubject("Password Reset OTP - Authentication App");
        message.setText(String.format("Your OTP is: %s\nValid for 10 minutes only.", otp));
        
        try {
            mailSender.send(message);
            System.out.println("✅ EMAIL SENT SUCCESSFULLY!");
        } catch (Exception e) {
            System.err.println("❌ EMAIL FAILED: " + e.getMessage());
            throw e;
        }
    }


    public boolean validateOtp(String email, String otp) {
        return otpRepository.findByEmail(email)
            .filter(o -> o.getOtp().equals(otp) && !o.isExpired())
            .map(o -> {
                otpRepository.delete(o); // Invalidate after use
                return true;
            })
            .orElse(false);
    }

    public void resetPassword(String email, String newPassword) {
        User user = userRepository.findByEmail(email)
            .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        user.setPassword(encoder.encode(newPassword));
        userRepository.save(user);
    }
}
