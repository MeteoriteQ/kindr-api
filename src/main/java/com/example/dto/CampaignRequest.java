package com.example.dto;
import java.math.BigDecimal;
public class CampaignRequest {
 private String title,category,city,story,image,phone,organizer,organizerPhone,status; private BigDecimal goal;
 public String getTitle(){return title;} public void setTitle(String v){title=v;}
 public String getCategory(){return category;} public void setCategory(String v){category=v;}
 public String getCity(){return city;} public void setCity(String v){city=v;}
 public String getStory(){return story;} public void setStory(String v){story=v;}
 public String getImage(){return image;} public void setImage(String v){image=v;}
 public String getPhone(){return phone;} public void setPhone(String v){phone=v;}
 public String getOrganizer(){return organizer;} public void setOrganizer(String v){organizer=v;}
 public String getOrganizerPhone(){return organizerPhone;} public void setOrganizerPhone(String v){organizerPhone=v;}
 public String getStatus(){return status;} public void setStatus(String v){status=v;}
 public BigDecimal getGoal(){return goal;} public void setGoal(BigDecimal v){goal=v;}
}
