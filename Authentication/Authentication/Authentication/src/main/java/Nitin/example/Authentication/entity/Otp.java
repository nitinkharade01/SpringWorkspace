package Nitin.example.Authentication.entity;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.time.LocalDateTime;
import java.util.Random;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "otps")
@Data @NoArgsConstructor @AllArgsConstructor
public class Otp {
    @Id
    private String email;
    private String otp;
    private LocalDateTime expiry;

    public boolean isExpired() {
        return LocalDateTime.now().isAfter(expiry);
    }
}
