package com.example.controller;
import java.math.BigDecimal;
import java.util.*;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.bind.annotation.*;
import com.example.dto.ProfilePatchRequest;
import com.example.Entity.*;
import com.example.repository.*;
import com.example.service.*;
@RestController
@RequestMapping("/api")
public class UserController {
 private final SessionService sessions;private final AuthService auth;private final UserRepository users;private final CampaignRepository campaigns;private final DonationRepository donations;
 public UserController(SessionService s,AuthService a,UserRepository u,CampaignRepository c,DonationRepository d){sessions=s;auth=a;users=u;campaigns=c;donations=d;}
    
	@PatchMapping(value="/users/me/avatar", consumes = {"multipart/form-data"})
	public Map<String,Object> uploadAvatar(@CookieValue(value="kindr_session",required=false) String token, @RequestPart(value="image", required=true) MultipartFile image) throws Exception {
		User u = require(token);
		if(image==null || image.isEmpty()) throw new IllegalArgumentException("No image uploaded");
		u.setAvatarContentType(image.getContentType());
		u.setAvatarData(image.getBytes());
		users.save(u);
		return Map.of("user", auth.safe(u));
	}

	@GetMapping("/users/{id}/avatar")
	public ResponseEntity<byte[]> getAvatar(@PathVariable String id){
		try{
			Long uid = Long.valueOf(id);
			Optional<User> ou = users.findById(uid);
			if(ou.isEmpty()) return ResponseEntity.notFound().build();
			User u = ou.get();
			byte[] data = u.getAvatarData();
			if(data==null || data.length==0) return ResponseEntity.notFound().build();
			String ct = u.getAvatarContentType(); if(ct==null||ct.isBlank()) ct="image/jpeg";
			return ResponseEntity.status(HttpStatus.OK).header(HttpHeaders.CONTENT_TYPE, ct).body(data);
		}catch(Exception e){return ResponseEntity.notFound().build();}
	}
 @PatchMapping("/users/me") public Map<String,Object> patch(@CookieValue(value="kindr_session",required=false) String token,@RequestBody ProfilePatchRequest r){User u=require(token);if(r.getFirstName()!=null)u.setFirstName(r.getFirstName().trim());if(r.getLastName()!=null)u.setLastName(r.getLastName().trim());if(r.getPhone()!=null)u.setPhone(r.getPhone().trim());if(r.getBio()!=null)u.setBio(r.getBio().trim());if(r.getCity()!=null)u.setCity(r.getCity().trim());users.save(u);return Map.of("user",auth.safe(u));}
 @GetMapping("/dashboard") public Map<String,Object> dashboard(@CookieValue(value="kindr_session",required=false) String token){User u=require(token);List<Campaign> cs=campaigns.findByOwnerIdAndStatusNotOrderByCreatedAtDesc(String.valueOf(u.getId()),"Deleted");List<Donation> ds=donations.findByDonorIdOrderByCreatedAtDesc(String.valueOf(u.getId()));List<Long> ids=cs.stream().map(Campaign::getId).toList();List<Donation> received=ids.isEmpty()?List.of():donations.findByCampaignIdIn(ids);BigDecimal donated=ds.stream().map(Donation::getAmount).reduce(BigDecimal.ZERO,BigDecimal::add);BigDecimal collected=received.stream().map(Donation::getAmount).reduce(BigDecimal.ZERO,BigDecimal::add);Map<String,Object> totals=new LinkedHashMap<>();totals.put("donated",donated);totals.put("collected",collected);Map<String,Object> out=new LinkedHashMap<>();out.put("user",auth.safe(u));out.put("campaigns",cs);out.put("donations",ds);out.put("totals",totals);return out;}
 private User require(String t){User u=sessions.fromToken(t);if(u==null)throw new SecurityException("Please sign in to continue.");return u;}
}
