package com.example.controller;
import java.util.Map;
import org.springframework.web.bind.annotation.*;
import com.example.Entity.User;
import com.example.repository.DonationRepository;
import com.example.service.SessionService;
@RestController
@RequestMapping("/api/donations")
public class DonationController {
 private final DonationRepository donations; private final SessionService sessions;
 public DonationController(DonationRepository d,SessionService s){donations=d;sessions=s;}
 @GetMapping("/me") public Map<String,Object> me(@CookieValue(value="kindr_session",required=false) String t){User u=require(t);return Map.of("donations",donations.findByDonorIdOrderByCreatedAtDesc(String.valueOf(u.getId())));}
 private User require(String t){User u=sessions.fromToken(t);if(u==null)throw new SecurityException("Please sign in to continue.");return u;}
}
