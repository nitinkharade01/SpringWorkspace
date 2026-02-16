package Nitin.example.Authentication.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String username;

    @Email
    @Column(unique = true)
    private String email;

    @NotBlank
    private String password;

    // ✅ MISSING FIELDS ADDED
    private String name;     // Maps to your DB 'name' column
    private String role;     // Maps to your DB 'role' column

    // ✅ COMPLETE Constructors
    public User() {}

    // ✅ COMPLETE Getters & Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    // ✅ FIXED setRole()
    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }

    // ✅ name getters/setters
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
}
