package com.example.service;

import java.math.BigDecimal;
import java.util.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.example.dto.CampaignRequest;
import com.example.Entity.*;
import com.example.repository.*;
import com.example.util.Ids;

@Service
public class CampaignService {
    private final CampaignRepository campaigns; private final CampaignUpdateRepository updates; private final DonationRepository donations;
    public CampaignService(CampaignRepository campaigns,CampaignUpdateRepository updates,DonationRepository donations){this.campaigns=campaigns;this.updates=updates;this.donations=donations;}
    public List<Campaign> list(String category,String ownerId){
        if(ownerId!=null)return campaigns.findByOwnerIdAndStatusNotOrderByCreatedAtDesc(ownerId,"Deleted");
        if(category!=null&&!category.isBlank())return campaigns.findByCategoryIgnoreCaseAndStatusNotOrderByCreatedAtDesc(category,"Deleted");
        return campaigns.findByStatusNotOrderByCreatedAtDesc("Deleted");
    }
    public Campaign get(Long id){Campaign c=campaigns.findById(id).orElseThrow(()->new NoSuchElementException("Campaign not found."));if("Deleted".equals(c.getStatus()))throw new NoSuchElementException("Campaign not found.");return c;}
    @Transactional public Campaign create(User owner,CampaignRequest r, org.springframework.web.multipart.MultipartFile image){
        validate(r); validateImage(r.getImage(), image);
        Campaign c=new Campaign();
        
        c.setOwnerId(String.valueOf(owner.getId()));
        c.setOrganizer(clean(r.getOrganizer()).isBlank()?owner.getFirstName()+" "+owner.getLastName():clean(r.getOrganizer()));
        c.setPhone(clean(r.getOrganizerPhone()).isBlank()?(!clean(r.getPhone()).isBlank()?clean(r.getPhone()):owner.getPhone()):clean(r.getOrganizerPhone()));
        c.setTitle(clean(r.getTitle()));
        c.setCategory(clean(r.getCategory()));
        c.setGoal(r.getGoal());
        c.setRaised(BigDecimal.ZERO);
        c.setCity(clean(r.getCity()));
        c.setStory(clean(r.getStory()));
        c.setDonorCount(0);
        c.setStatus("Active");

        try{
            if(image!=null && !image.isEmpty()){
                String safeName = image.getOriginalFilename()==null?"file":image.getOriginalFilename().replaceAll("[^a-zA-Z0-9._-]","_");
                String filename = com.example.util.Ids.id("img")+"_"+safeName;
                c.setImage(filename);
                c.setImageContentType(image.getContentType());
                c.setImageData(image.getBytes());
            } else {
                c.setImage(clean(r.getImage()));
            }
        }catch(java.io.IOException e){ throw new IllegalArgumentException("Could not read the uploaded image.", e); }

        return campaigns.save(c);
    }
    @Transactional public Campaign update(User u,Long id,CampaignRequest r){Campaign c=get(id);own(u,c);if(r.getTitle()!=null)c.setTitle(clean(r.getTitle()));if(r.getStory()!=null)c.setStory(clean(r.getStory()));if(r.getCity()!=null)c.setCity(clean(r.getCity()));if(r.getCategory()!=null)c.setCategory(clean(r.getCategory()));if(r.getStatus()!=null)c.setStatus(clean(r.getStatus()));if(r.getImage()!=null)c.setImage(clean(r.getImage()));if(r.getGoal()!=null&&r.getGoal().compareTo(c.getRaised())>=0)c.setGoal(r.getGoal());return campaigns.save(c);}
    @Transactional public Campaign delete(User u,Long id){Campaign c=get(id);own(u,c);c.setStatus("Deleted");return campaigns.save(c);}
    @Transactional public CampaignUpdate addUpdate(User u,Long campaignId,String message){Campaign c=get(campaignId);own(u,c);if(clean(message).isBlank())throw new IllegalArgumentException("Update cannot be empty.");CampaignUpdate up=new CampaignUpdate();up.setId(Ids.id("upd"));up.setCampaignId(campaignId);up.setMessage(clean(message));return updates.save(up);}
    public Map<String,Object> detail(Long id){Campaign c=get(id);Map<String,Object> m=new LinkedHashMap<>();m.put("campaign",c);m.put("updates",updates.findByCampaignIdOrderByCreatedAtDesc(id));m.put("donations",donations.findTop15ByCampaignIdOrderByCreatedAtDesc(id).stream().map(d->{Map<String,Object>x=new LinkedHashMap<>();x.put("amount",d.getAmount());x.put("createdAt",d.getCreatedAt());x.put("anonymous",d.isAnonymous());return x;}).toList());return m;}
    public List<CampaignUpdate> updates(Long id){return updates.findByCampaignIdOrderByCreatedAtDesc(id);}
    private static void validateImage(String imageUrl, org.springframework.web.multipart.MultipartFile file) {
        if(file != null && !file.isEmpty()) {
            if(file.getSize() > 5L * 1024 * 1024) throw new IllegalArgumentException("Image must be smaller than 5 MB.");
            try {
                byte[] b = file.getBytes();
                boolean jpeg = b.length > 3 && (b[0]&255)==255 && (b[1]&255)==216 && (b[2]&255)==255;
                boolean png = b.length > 8 && (b[0]&255)==137 && b[1]==80 && b[2]==78 && b[3]==71 && b[4]==13 && b[5]==10 && b[6]==26 && b[7]==10;
                boolean webp = b.length > 12 && b[0]==82 && b[1]==73 && b[2]==70 && b[3]==70 && b[8]==87 && b[9]==69 && b[10]==66 && b[11]==80;
                String type = jpeg ? "image/jpeg" : png ? "image/png" : webp ? "image/webp" : "";
                if(type.isEmpty() || !type.equals(file.getContentType())) throw new IllegalArgumentException("Upload a JPEG, PNG, or WebP image.");
            } catch(java.io.IOException e) { throw new IllegalArgumentException("Could not read image.", e); }
        } else if(!clean(imageUrl).isBlank()) {
            try {
                java.net.URI uri = java.net.URI.create(imageUrl.trim());
                if(!Set.of("http","https").contains(uri.getScheme()) || uri.getHost()==null || uri.getUserInfo()!=null) throw new IllegalArgumentException();
            } catch(Exception e) { throw new IllegalArgumentException("Enter a valid HTTP or HTTPS image URL."); }
        }
    }
    private static void validate(CampaignRequest r){if(clean(r.getTitle()).isBlank()||clean(r.getCategory()).isBlank()||clean(r.getStory()).isBlank()||clean(r.getCity()).isBlank())throw new IllegalArgumentException("Complete all campaign fields.");if(r.getGoal()==null||r.getGoal().compareTo(BigDecimal.valueOf(100))<0)throw new IllegalArgumentException("Goal must be at least ₹100.");}
    private static void own(User u,Campaign c){if(u==null||!Objects.equals(c.getOwnerId(),String.valueOf(u.getId())))throw new SecurityException("You do not own this campaign.");}
    private static String clean(String s){return s==null?"":s.trim();}
    private static BigDecimal decimal(Object o){try{return new BigDecimal(String.valueOf(o));}catch(Exception e){return BigDecimal.ZERO;}}
}
