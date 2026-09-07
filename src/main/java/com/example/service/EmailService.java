package com.example.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    public void sendResetEmail(String toEmail, String resetLink) {

        SimpleMailMessage message = new SimpleMailMessage();

        message.setTo(toEmail);
        message.setSubject("Kindr - Reset your password");

        message.setText(
                "Hello,\n\n"
              + "Use the link below to reset your Kindr password. This link is valid for 15 minutes and can be used only once:\n\n"
              + resetLink
              + "\n\nIf you did not request this, please ignore this email."
              + "\n\nRegards,\nKindr Team");

        mailSender.send(message);
    }

    public void sendOtpEmail(String toEmail, String otp) { sendResetEmail(toEmail, "OTP: " + otp); }
}