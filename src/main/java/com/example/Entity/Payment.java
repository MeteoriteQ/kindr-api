package com.example.Entity;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import jakarta.persistence.*;
@Entity
@Table(name="payments", uniqueConstraints={@UniqueConstraint(name="uk_rzp_order",columnNames="provider_order_id"),@UniqueConstraint(name="uk_rzp_payment",columnNames="provider_payment_id")})
public class Payment {
 @Id private String id;
 @Column(nullable=false,length=80) private String donorId;
 @Column(nullable=false) private Long campaignId;
 @Column(nullable=false,precision=15,scale=2) private BigDecimal amount;
 @Column(nullable=false) private boolean anonymous;
 @Column(length=300) private String message;
 @Column(nullable=false,length=30) private String method;
 @Column(nullable=false,length=30) private String status;
 @Column(nullable=false,length=30) private String mode;
 @Column(name="provider_order_id",unique=true,length=100) private String providerOrderId;
 @Column(name="provider_payment_id",unique=true,length=100) private String providerPaymentId;
 @Column(length=500) private String providerSignature;
 @Column(nullable=false) private LocalDateTime createdAt;
 private LocalDateTime completedAt;
 @PrePersist void pre(){if(createdAt==null)createdAt=LocalDateTime.now();}
 public String getId(){return id;} public void setId(String v){id=v;}
 public String getDonorId(){return donorId;} public void setDonorId(String v){donorId=v;}
 public Long getCampaignId(){return campaignId;} public void setCampaignId(Long v){campaignId=v;}
 public BigDecimal getAmount(){return amount;} public void setAmount(BigDecimal v){amount=v;}
 public boolean isAnonymous(){return anonymous;} public void setAnonymous(boolean v){anonymous=v;}
 public String getMessage(){return message;} public void setMessage(String v){message=v;}
 public String getMethod(){return method;} public void setMethod(String v){method=v;}
 public String getStatus(){return status;} public void setStatus(String v){status=v;}
 public String getMode(){return mode;} public void setMode(String v){mode=v;}
 public String getProviderOrderId(){return providerOrderId;} public void setProviderOrderId(String v){providerOrderId=v;}
 public String getProviderPaymentId(){return providerPaymentId;} public void setProviderPaymentId(String v){providerPaymentId=v;}
 public String getProviderSignature(){return providerSignature;} public void setProviderSignature(String v){providerSignature=v;}
 public LocalDateTime getCreatedAt(){return createdAt;} public LocalDateTime getCompletedAt(){return completedAt;} public void setCompletedAt(LocalDateTime v){completedAt=v;}
}
