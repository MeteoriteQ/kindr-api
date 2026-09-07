package com.example.dto;
import java.math.BigDecimal;
public class CreatePaymentRequest { private Long campaignId; private String method,message; private BigDecimal amount; private boolean anonymous;
 public Long getCampaignId(){return campaignId;} public void setCampaignId(Long v){campaignId=v;}
 public String getMethod(){return method;} public void setMethod(String v){method=v;}
 public String getMessage(){return message;} public void setMessage(String v){message=v;}
 public BigDecimal getAmount(){return amount;} public void setAmount(BigDecimal v){amount=v;}
 public boolean isAnonymous(){return anonymous;} public void setAnonymous(boolean v){anonymous=v;}
}
