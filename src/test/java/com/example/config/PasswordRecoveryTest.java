package com.example.config;
import com.example.Entity.*;
import com.example.dto.*;
import com.example.repository.*;
import com.example.service.*;
import java.time.LocalDateTime;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PasswordRecoveryTest {
    UserRepository users = mock(UserRepository.class);
    PasswordResetOtpRepository resets = mock(PasswordResetOtpRepository.class);
    EmailService emails = mock(EmailService.class);
    AuthService service = new AuthService(users, resets, emails);
    @Test void sendsLinkAndStoresExpiringToken() {
        ReflectionTestUtils.setField(service, "frontendUrl", "http://localhost:5173");
        when(users.findByEmailIgnoreCase("test@example.com")).thenReturn(Optional.of(new User()));
        var request = new ForgotPasswordRequest(); request.setEmail("test@example.com");
        service.forgotPassword(request);
        var capture = org.mockito.ArgumentCaptor.forClass(PasswordResetOtp.class);
        verify(resets).save(capture.capture());
        var reset = capture.getValue();
        assertTrue(reset.getExpiryTime().isAfter(LocalDateTime.now()));
        verify(emails).sendResetEmail("test@example.com", "http://localhost:5173/reset-password.html?token=" + reset.getResetToken());
    }
    @Test void resetsPasswordAndConsumesTokenButRejectsExpiredAndInvalidLinks() {
        var user = new User();
        var reset = new PasswordResetOtp(); reset.setEmail("test@example.com"); reset.setExpiryTime(LocalDateTime.now().plusMinutes(15));
        when(resets.findByResetToken("token")).thenReturn(Optional.of(reset));
        when(users.findByEmailIgnoreCase("test@example.com")).thenReturn(Optional.of(user));
        var request = new ResetPasswordRequest(); request.setToken("token"); request.setNewPassword("NewPassword123"); request.setConfirmPassword("wrong");
        assertTrue(service.resetPassword(request).contains("do not match"));
        verify(users, never()).save(any());
        request.setConfirmPassword("NewPassword123");
        assertEquals("Password reset successful", service.resetPassword(request));
        assertTrue(new BCryptPasswordEncoder().matches("NewPassword123", user.getPassword()));
        verify(resets).deleteByEmail("test@example.com");
        reset.setExpiryTime(LocalDateTime.now().minusMinutes(1));
        assertTrue(service.resetPassword(request).contains("expired"));
        when(resets.findByResetToken("token")).thenReturn(Optional.empty());
        assertTrue(service.resetPassword(request).contains("invalid"));
        verify(users, times(1)).save(user);
    }
}
