package com.example.service;

import java.time.LocalDateTime;
import java.security.SecureRandom;
import java.util.*;
import java.util.Base64;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.example.Entity.*;
import com.example.dto.*;
import com.example.repository.*;
import com.example.util.OtpGenerator;

@Service
public class AuthService {
	private final UserRepository users;
	private final PasswordResetOtpRepository otps;
	private final EmailService emailService;
	private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
	private final SecureRandom random = new SecureRandom();
	@Value("${app.frontend-url:http://localhost:8080}")
	private String frontendUrl;

	public AuthService(UserRepository users, PasswordResetOtpRepository otps, EmailService emailService) {
		this.users = users;
		this.otps = otps;
		this.emailService = emailService;
	}

	public User register(RegisterRequest r) {
		String email = clean(r.getEmail()).toLowerCase(Locale.ROOT);
		if (users.existsByEmailIgnoreCase(email))
			throw new IllegalArgumentException("An account already exists with this email.");
		if (!Objects.equals(r.getPassword(), r.getConfirmPassword()))
			throw new IllegalArgumentException("Passwords do not match.");
		String type = frontendAccountType(r.getAccountType());
		User u = new User();
		u.setFirstName(clean(r.getFirstName()));
		u.setLastName(clean(r.getLastName()));
		u.setEmail(email);
		u.setPhone(clean(r.getPhone()));
		u.setAccountType(type);
		u.setPassword(encoder.encode(r.getPassword()));
		u.setBio("");
		u.setCity("");
		return users.save(u);
	}

	public User login(String email, String password) {
		User u = users.findByEmailIgnoreCase(clean(email))
				.orElseThrow(() -> new IllegalArgumentException("Email or password is incorrect."));
		if (password == null || !encoder.matches(password, u.getPassword()))
			throw new IllegalArgumentException("Email or password is incorrect.");
		return u;
	}

	@Transactional
	public String forgotPassword(ForgotPasswordRequest r) {
		String email = clean(r.getEmail()).toLowerCase(Locale.ROOT);
		if (users.findByEmailIgnoreCase(email).isEmpty())
			return "Email not registered";
		otps.deleteByEmail(email);
		String token = Base64.getUrlEncoder().withoutPadding().encodeToString(random.generateSeed(48));
		PasswordResetOtp p = new PasswordResetOtp();
		p.setEmail(email);
		p.setOtp(OtpGenerator.generateOtp());
		p.setResetToken(token);
		p.setVerified(false);
		p.setExpiryTime(LocalDateTime.now().plusMinutes(15));
		otps.save(p);
		emailService.sendResetEmail(email, frontendUrl.replaceAll("/$", "") + "/reset-password.html?token=" + token);
		return "Password reset link sent successfully to your email.";
	}

	public String verifyOtp(VerifyOtpRequest r) {
		var opt = otps.findByEmailAndOtp(clean(r.getEmail()).toLowerCase(Locale.ROOT), clean(r.getOtp()));
		if (opt.isEmpty())
			return "Invalid OTP";
		var p = opt.get();
		if (p.getExpiryTime().isBefore(LocalDateTime.now()))
			return "OTP has expired";
		p.setVerified(true);
		otps.save(p);
		return "OTP verified successfully";
	}

	@Transactional
	public String resetPassword(ResetPasswordRequest r) {
		if (!Objects.equals(r.getNewPassword(), r.getConfirmPassword()))
			return "Password and Confirm Password do not match";
		String email = clean(r.getEmail()).toLowerCase(Locale.ROOT);
		PasswordResetOtp reset = null;
		if (!clean(r.getToken()).isBlank()) {
			reset = otps.findByResetToken(clean(r.getToken())).orElse(null);
			if (reset == null || reset.getExpiryTime().isBefore(LocalDateTime.now()))
				return "This reset link is invalid or has expired";
			email = reset.getEmail();
		}
		User u = users.findByEmailIgnoreCase(email).orElse(null);
		if (u == null)
			return "User not found";
		if (reset == null) {
			var opt = otps.findTopByEmailOrderByCreatedAtDesc(email);
			if (opt.isEmpty() || !opt.get().isVerified() || opt.get().getExpiryTime().isBefore(LocalDateTime.now()))
				return "Please verify OTP first";
		}
		u.setPassword(encoder.encode(r.getNewPassword()));
		users.save(u);
		otps.deleteByEmail(email);
		return "Password reset successful";
	}

	public Map<String, Object> safe(User u) {
		if (u == null)
			return null;
		Map<String, Object> m = new LinkedHashMap<>();
		// String output keeps compatibility with existing Campaign ownerId/donorId
		// strings and frontend strict equality.
		m.put("id", String.valueOf(u.getId()));
		m.put("firstName", u.getFirstName());
		m.put("lastName", u.getLastName());
		m.put("email", u.getEmail());
		m.put("phone", u.getPhone());
		m.put("accountType", u.getAccountType());
		m.put("bio", u.getBio());
		m.put("city", u.getCity());
		m.put("createdAt", u.getCreatedAt());
		// Avatar URL (frontend can fetch this to display the user's profile image)
		m.put("avatarUrl", u.getAvatarPath() != null ? u.getAvatarPath() :
            (u.getAvatarData() != null && u.getAvatarData().length > 0 ? "/api/users/" + u.getId() + "/avatar" : ""));
		return m;
	}

	private static String frontendAccountType(String raw) {
		String v = clean(raw);
		if (v.equalsIgnoreCase("Fundraiser"))
			return "Fundraiser";
		if (v.equalsIgnoreCase("Donor"))
			return "Donor";
		if (v.equalsIgnoreCase("Organization") || v.equalsIgnoreCase("Organizer"))
			return "Organization";
		throw new IllegalArgumentException("Invalid account type. Use Fundraiser, Donor, or Organization.");
	}

	private static String clean(String s) {
		return s == null ? "" : s.trim();
	}
}
