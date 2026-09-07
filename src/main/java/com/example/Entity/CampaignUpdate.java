package com.example.Entity;
import java.time.LocalDateTime;
import jakarta.persistence.*;
@Entity
@Table(name="campaign_updates")
public class CampaignUpdate {
 @Id private String id;
 @Column(nullable=false) private Long campaignId;
 @Lob @Column(nullable=false,columnDefinition="TEXT") private String message;
 @Column(nullable=false) private LocalDateTime createdAt;
 @PrePersist void pre(){if(createdAt==null)createdAt=LocalDateTime.now();}
 public String getId(){return id;} public void setId(String id){this.id=id;}
 public Long getCampaignId(){return campaignId;} public void setCampaignId(Long v){campaignId=v;}
 public String getMessage(){return message;} public void setMessage(String v){message=v;}
 public LocalDateTime getCreatedAt(){return createdAt;}
}
