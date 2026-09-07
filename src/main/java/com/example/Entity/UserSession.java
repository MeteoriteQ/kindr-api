package com.example.Entity;

import java.time.LocalDateTime;
import jakarta.persistence.*;

@Entity
@Table(name = "user_sessions")
public class UserSession {
    @Id @Column(length = 100) private String token;
    // Kept as String to remain compatible with the previously integrated session table.
    @Column(name = "user_id", nullable = false, length = 80) private String userId;
    @Column(name = "expires_at", nullable = false) private LocalDateTime expiresAt;
    public String getToken(){return token;} public void setToken(String v){token=v;}
    public String getUserId(){return userId;} public void setUserId(String v){userId=v;}
    public LocalDateTime getExpiresAt(){return expiresAt;} public void setExpiresAt(LocalDateTime v){expiresAt=v;}
}
