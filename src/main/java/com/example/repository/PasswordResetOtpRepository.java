package com.example.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;

import com.example.Entity.PasswordResetOtp;

@Repository
public interface PasswordResetOtpRepository extends JpaRepository<PasswordResetOtp, Long> {

    // Get the latest OTP for an email
    Optional<PasswordResetOtp> findTopByEmailOrderByCreatedAtDesc(String email);

    // Find by email and OTP
    Optional<PasswordResetOtp> findByEmailAndOtp(String email, String otp);

    Optional<PasswordResetOtp> findByResetToken(String resetToken);

    // Delete all OTPs for an email
    @Modifying
    void deleteByEmail(String email);

}