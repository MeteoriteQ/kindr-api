package com.example.controller;
import java.util.*;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import com.example.dto.*;
import com.example.Entity.User;
import com.example.service.*;
@RestController
@RequestMapping("/api/payments")
public class PaymentController {
 private final PaymentService payments;private final SessionService sessions;
 public PaymentController(PaymentService payments,SessionService sessions){this.payments=payments;this.sessions=sessions;}
 @GetMapping("/config") public Map<String,Object> config(){return payments.config();}
 @PostMapping("/order") public ResponseEntity<?> order(@CookieValue(value="kindr_session",required=false) String token,@RequestBody CreatePaymentRequest r, @CookieValue(value="kindr_guest",required=false) String guest) throws Exception { User u=sessions.fromToken(token); if(u==null && (guest==null || !guest.matches("[a-f0-9]{64}"))) guest=com.example.util.Ids.token(); return ResponseEntity.status(201).header("Set-Cookie", ResponseCookie.from("kindr_guest",guest==null?"":guest).httpOnly(true).sameSite("Lax").path("/").maxAge(86400).build().toString()).body(payments.createOrder(u,r,guest)); }
 @PostMapping("/confirm") public Map<String,Object> confirm(@CookieValue(value="kindr_session",required=false) String token,@RequestBody ConfirmPaymentRequest r, @CookieValue(value="kindr_guest",required=false) String guest) throws Exception{return payments.confirm(sessions.fromToken(token),r,guest);}
 // Aliases for direct backend testing / compatibility with the separate PaymentGateWay module naming.
 private User require(String t){User u=sessions.fromToken(t);if(u==null)throw new SecurityException("Please sign in to continue.");return u;}
}
