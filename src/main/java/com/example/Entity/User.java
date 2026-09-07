package com.example.Entity;

import java.time.LocalDateTime;
import jakarta.persistence.*;

@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "first_name", nullable = false, length = 80)
    private String firstName;

    @Column(name = "last_name", nullable = false, length = 80)
    private String lastName;

    @Column(nullable = false, unique = true, length = 190)
    private String email;

    // Frontend sends `phone`; existing Kindr DB stores it in `mobile_number`.
    @Column(name = "mobile_number", nullable = false, length = 30)
    private String phone;

    @Column(name = "account_type", nullable = false, length = 30)
    private String accountType;

    // Existing Kindr DB column is `password` and contains BCrypt hashes.
    @Column(name = "password", nullable = false, length = 100)
    private String password;

    @Column(length = 500)
    private String bio = "";

    @Column(length = 120)
    private String city = "";

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "avatar_content_type", length = 255)
    private String avatarContentType;

    @Lob
    @Column(name = "avatar_data", columnDefinition = "LONGBLOB")
    private byte[] avatarData;

    @PrePersist
    void prePersist() {
        if (createdAt == null) createdAt = LocalDateTime.now();
        if (bio == null) bio = "";
        if (city == null) city = "";
    }

    public Long getId() { return id; }
    public String getFirstName() { return firstName; }
    public void setFirstName(String v) { firstName = v; }
    public String getLastName() { return lastName; }
    public void setLastName(String v) { lastName = v; }
    public String getEmail() { return email; }
    public void setEmail(String v) { email = v; }
    public String getPhone() { return phone; }
    public void setPhone(String v) { phone = v; }
    public String getMobileNumber() { return phone; }
    public void setMobileNumber(String v) { phone = v; }
    public String getAccountType() { return accountType; }
    public void setAccountType(String v) { accountType = v; }
    public String getPassword() { return password; }
    public void setPassword(String v) { password = v; }
    public String getBio() { return bio; }
    public void setBio(String v) { bio = v; }
    public String getCity() { return city; }
    public void setCity(String v) { city = v; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public String getAvatarContentType() { return avatarContentType; }
    public void setAvatarContentType(String avatarContentType) { this.avatarContentType = avatarContentType; }
    public byte[] getAvatarData() { return avatarData; }
    public void setAvatarData(byte[] avatarData) { this.avatarData = avatarData; }
}
