package com.example.Authentication.dto;

//package com.example.auth.dto;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
public class AuthDtos {

 @Data
 public static class RegisterRequest {
     @NotBlank(message = "Name is required")
     private String name;

     @NotBlank(message = "Email is required")
     @Email(message = "Invalid email format")
     private String email;

     @NotBlank(message = "Password is required")
     @Size(min = 8, message = "Password must be at least 8 characters long")
     private String password;

     @NotBlank(message = "Role is required")
     private String role; // USER / ADMIN etc.

	 public String getEmail() {
		// TODO Auto-generated method stub
		return null;
	 }

	 public Object getName() {
		// TODO Auto-generated method stub
		return null;
	 }

	 public String getRole() {
		// TODO Auto-generated method stub
		return null;
	 }

	 public CharSequence getPassword() {
		// TODO Auto-generated method stub
		return null;
	 }
 }

 @Data
 public static class LoginRequest {

     @NotBlank(message = "Email is required")
     @Email(message = "Invalid email format")
     private String email;      // String

     @NotBlank(message = "Password is required")
     private String password;   // String

	 public String getEmail() {
		// TODO Auto-generated method stub
		return email;
	 }

	 public Object getPassword() {
		// TODO Auto-generated method stub
		return password;
	 }
 }

 @Data
 public static class AuthResponse {
     private String token;
     private String name;
     private String email;
     private String role;
     private long expiresIn;

     public AuthResponse(String token, String name, String email, String role, long expiresIn) {
         this.token = token;
         this.name = name;
         this.email = email;
         this.role = role;
         this.expiresIn = expiresIn;
     }
 }
}
