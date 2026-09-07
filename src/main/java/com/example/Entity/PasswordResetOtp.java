package com.example.Entity;

import java.time.LocalDateTime;
import jakarta.persistence.*;

@Entity
@Table(name = "password_reset_otp")
public class PasswordResetOtp {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, length = 190) private String email;
    @Column(nullable = false, length = 10) private String otp;
    @Column(length = 120, unique = true) private String resetToken;
    @Column(name = "expiry_time", nullable = false) private LocalDateTime expiryTime;
    @Column(nullable = false) private boolean verified = false;
    @Column(name = "created_at", nullable = false) private LocalDateTime createdAt;
    @PrePersist void pre(){ if(createdAt==null) createdAt=LocalDateTime.now(); }
    public Long getId(){return id;}
    public String getEmail(){return email;} public void setEmail(String v){email=v;}
    public String getOtp(){return otp;} public void setOtp(String v){otp=v;}
    public String getResetToken(){return resetToken;} public void setResetToken(String v){resetToken=v;}
    public LocalDateTime getExpiryTime(){return expiryTime;} public void setExpiryTime(LocalDateTime v){expiryTime=v;}
    public boolean isVerified(){return verified;} public void setVerified(boolean v){verified=v;}
    public LocalDateTime getCreatedAt(){return createdAt;}
}
