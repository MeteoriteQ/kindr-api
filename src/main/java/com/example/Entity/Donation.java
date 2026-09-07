package com.example.Entity;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import jakarta.persistence.*;
@Entity
@Table(name="donations", uniqueConstraints=@UniqueConstraint(name="uk_donation_payment", columnNames="payment_id"))
public class Donation {
 @Id private String id;
 @Column(name="payment_id",nullable=false,unique=true,length=100) private String paymentId;
 @Column(length=100) private String providerPaymentId;
 @Column(nullable=false,length=80) private String donorId;
 @Column(nullable=false) private Long campaignId;
 @Column(nullable=false,length=180) private String campaignTitle;
 @Column(nullable=false,precision=15,scale=2) private BigDecimal amount;
 @Column(nullable=false) private boolean anonymous;
 @Column(length=300) private String message;
 @Column(nullable=false,length=30) private String status="Completed";
 @Column(nullable=false) private LocalDateTime createdAt;
 @PrePersist void pre(){if(createdAt==null)createdAt=LocalDateTime.now();}
 public String getId(){return id;} public void setId(String v){id=v;}
 public String getPaymentId(){return paymentId;} public void setPaymentId(String v){paymentId=v;}
 public String getProviderPaymentId(){return providerPaymentId;} public void setProviderPaymentId(String v){providerPaymentId=v;}
 public String getDonorId(){return donorId;} public void setDonorId(String v){donorId=v;}
 public Long getCampaignId(){return campaignId;} public void setCampaignId(Long v){campaignId=v;}
 public String getCampaignTitle(){return campaignTitle;} public void setCampaignTitle(String v){campaignTitle=v;}
 public BigDecimal getAmount(){return amount;} public void setAmount(BigDecimal v){amount=v;}
 public boolean isAnonymous(){return anonymous;} public void setAnonymous(boolean v){anonymous=v;}
 public String getMessage(){return message;} public void setMessage(String v){message=v;}
 public String getStatus(){return status;} public void setStatus(String v){status=v;}
 public LocalDateTime getCreatedAt(){return createdAt;}
}
