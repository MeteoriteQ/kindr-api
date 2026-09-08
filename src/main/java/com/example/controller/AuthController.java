package com.example.controller;

import java.time.Duration;
import java.util.*;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import com.example.Entity.User;
import com.example.dto.*;
import com.example.service.*;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
	private final AuthService auth;
	private final SessionService sessions;

	public AuthController(AuthService auth, SessionService sessions) {
		this.auth = auth;
		this.sessions = sessions;
	}

	@PostMapping("/register")
	public ResponseEntity<?> register(@Valid @RequestBody RegisterRequest r) {
		return withCookie(HttpStatus.CREATED, auth.register(r));
	}

	@PostMapping("/login")
	public ResponseEntity<?> login(@Valid @RequestBody LoginRequest r) {
		return withCookie(HttpStatus.OK, auth.login(r.getEmail(), r.getPassword()));
	}

	@GetMapping("/me")
	public Map<String, Object> me(@CookieValue(value = "kindr_session", required = false) String token) {
		Map<String, Object> m = new LinkedHashMap<>();
		m.put("user", auth.safe(sessions.fromToken(token)));
		return m;
	}

	@PostMapping("/logout")
	public ResponseEntity<?> logout(@CookieValue(value = "kindr_session", required = false) String token) {
		sessions.delete(token);
		ResponseCookie c = ResponseCookie.from("kindr_session", "").httpOnly(true).sameSite("Lax").path("/")
				.maxAge(Duration.ZERO).build();
		return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, c.toString()).body(Map.of("ok", true));
	}

	@PostMapping("/forgot-password")
	public ResponseEntity<?> forgot(@Valid @RequestBody ForgotPasswordRequest r) {
		String x = auth.forgotPassword(r);
		return ResponseEntity.ok(Map.of("message", "If this email is registered, a password reset link has been sent."));
	}

	@PostMapping("/verify-otp")
	public ResponseEntity<?> verify(@Valid @RequestBody VerifyOtpRequest r) {
		String x = auth.verifyOtp(r);
		return x.startsWith("OTP verified") ? ResponseEntity.ok(x) : ResponseEntity.badRequest().body(x);
	}

	@PostMapping("/reset-password")
	public ResponseEntity<?> reset(@Valid @RequestBody ResetPasswordRequest r) {
		String x = auth.resetPassword(r);
		return x.startsWith("Password reset successful") ? ResponseEntity.ok(Map.of("message", x))
				: ResponseEntity.badRequest().body(Map.of("error", x));
	}

	private ResponseEntity<?> withCookie(HttpStatus status, User u) {
		String token = sessions.create(u);
		ResponseCookie c = ResponseCookie.from("kindr_session", token).httpOnly(true).sameSite("Lax").path("/")
				.maxAge(Duration.ofDays(7)).build();
		return ResponseEntity.status(status).header(HttpHeaders.SET_COOKIE, c.toString())
				.body(Map.of("user", auth.safe(u)));
	}
}
