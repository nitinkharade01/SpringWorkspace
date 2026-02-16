package Nitin.example.Authentication.dto;
import jakarta.validation.constraints.NotBlank;

public class ResetPasswordRequest {
    @NotBlank private String email;
    @NotBlank private String newPassword;
    
    public String getEmail() { return email; }
    public String getNewPassword() { return newPassword; }
    public void setEmail(String email) { this.email = email; }
    public void setNewPassword(String newPassword) { this.newPassword = newPassword; }
}
