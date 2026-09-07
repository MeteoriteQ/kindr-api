package com.example.controller;
import java.util.*;
import java.io.IOException;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import com.example.dto.*;
import com.example.Entity.*;
import com.example.service.*;
@RestController
@RequestMapping("/api/campaigns")
public class CampaignController {
 private final CampaignService service;private final SessionService sessions;
 public CampaignController(CampaignService service,SessionService sessions){this.service=service;this.sessions=sessions;}
 @GetMapping public Map<String,Object> all(@RequestParam(required=false) String category,@RequestParam(required=false) String owner,@CookieValue(value="kindr_session",required=false) String token){String ownerId=null;if("me".equals(owner))ownerId=String.valueOf(require(token).getId());return Map.of("campaigns",service.list(category,ownerId));}
 @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
 public ResponseEntity<?> createJson(@CookieValue(value="kindr_session",required=false) String token,
                                    @RequestBody CampaignRequest r) {
  return ResponseEntity.status(201).body(Map.of("campaign", service.create(require(token), r, null)));
 }
 @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
 public ResponseEntity<?> create(@CookieValue(value="kindr_session",required=false) String token,
								   @ModelAttribute CampaignRequest r,
								   @RequestPart(value = "imageFile", required = false) MultipartFile image) throws IOException {

	return ResponseEntity.status(201).body(Map.of("campaign", service.create(require(token), r, image)));
 }
 @GetMapping("/{id}") public Map<String,Object> get(@PathVariable Long id){return service.detail(id);}
 @GetMapping("/{id}/image") public ResponseEntity<byte[]> getImage(@PathVariable Long id){
	 Campaign c = service.get(id);
	 if(c.getImageData()==null||c.getImageData().length==0){
		 return ResponseEntity.notFound().build();
	 }
	 String contentType = c.getImageContentType();
	 if(contentType==null||contentType.isBlank()) contentType = "image/jpeg";
	 return ResponseEntity.ok().header("Content-Type", contentType).body(c.getImageData());
 }
 @PatchMapping("/{id}") public Map<String,Object> update(@CookieValue(value="kindr_session",required=false) String token,@PathVariable Long id,@RequestBody CampaignRequest r){return Map.of("campaign",service.update(require(token),id,r));}
 @DeleteMapping("/{id}") public Map<String,Object> delete(@CookieValue(value="kindr_session",required=false) String token,@PathVariable Long id){return Map.of("campaign",service.delete(require(token),id));}
 @PostMapping("/{id}/updates") public ResponseEntity<?> updateMessage(@CookieValue(value="kindr_session",required=false) String token,@PathVariable Long id,@RequestBody MessageRequest r){return ResponseEntity.status(201).body(Map.of("update",service.addUpdate(require(token),id,r.getMessage())));}
 private User require(String t){User u=sessions.fromToken(t);if(u==null)throw new SecurityException("Please sign in to continue.");return u;}
}
