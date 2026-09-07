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
 @PostMapping("/order") public ResponseEntity<?> order(@CookieValue(value="kindr_session",required=false) String token,@RequestBody CreatePaymentRequest r) throws Exception{return ResponseEntity.status(201).body(payments.createOrder(require(token),r));}
 @PostMapping("/confirm") public Map<String,Object> confirm(@CookieValue(value="kindr_session",required=false) String token,@RequestBody ConfirmPaymentRequest r) throws Exception{return payments.confirm(require(token),r);}
 // Aliases for direct backend testing / compatibility with the separate PaymentGateWay module naming.
 @PostMapping("/create-order") public ResponseEntity<?> createOrder(@CookieValue(value="kindr_session",required=false) String token,@RequestBody CreatePaymentRequest r) throws Exception{return order(token,r);}
 private User require(String t){User u=sessions.fromToken(t);if(u==null)throw new SecurityException("Please sign in to continue.");return u;}
}
