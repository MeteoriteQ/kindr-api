package com.example.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.example.dto.*;
import com.example.Entity.*;
import com.example.repository.*;
import com.example.util.Ids;
import com.razorpay.Order;
import com.razorpay.RazorpayClient;
import com.razorpay.Utils;

@Service
public class PaymentService {
    private final RazorpayClient razorpay;
    private final PaymentRepository payments;
    private final DonationRepository donations;
    private final CampaignRepository campaigns;
    private final String keyId,secret;
    public PaymentService(PaymentRepository payments,DonationRepository donations,CampaignRepository campaigns,@Value("${razorpay.key.id:}") String keyId,@Value("${razorpay.key.secret:}") String secret) throws Exception {this.payments=payments;this.donations=donations;this.campaigns=campaigns;this.keyId=keyId;this.secret=secret;this.razorpay=(keyId!=null&&!keyId.isBlank()&&secret!=null&&!secret.isBlank())?new RazorpayClient(keyId,secret):null;}
    public boolean live(){return razorpay!=null&&keyId!=null&&!keyId.isBlank()&&secret!=null&&!secret.isBlank();}
    public Map<String,Object> config(){Map<String,Object> m=new LinkedHashMap<>();m.put("mode",live()?"razorpay":"test");m.put("keyId",live()?keyId:null);m.put("currency","INR");return m;}
    @Transactional
    public Map<String,Object> createOrder(User u,CreatePaymentRequest r) throws Exception {
        Campaign c=campaigns.findById(r.getCampaignId()).orElseThrow(()->new NoSuchElementException("Active campaign not found."));
        if(!"Active".equals(c.getStatus()))throw new NoSuchElementException("Active campaign not found.");
        BigDecimal amount=r.getAmount(); if(amount==null||amount.compareTo(BigDecimal.TEN)<0||amount.compareTo(new BigDecimal("10000000"))>0)throw new IllegalArgumentException("Donation must be between ₹10 and ₹1,00,00,000.");
        Payment p=new Payment();p.setId(Ids.id("pay"));p.setDonorId(String.valueOf(u.getId()));p.setCampaignId(c.getId());p.setAmount(amount);p.setAnonymous(r.isAnonymous());p.setMessage(trim300(r.getMessage()));p.setMethod(r.getMethod()==null?"card":r.getMethod());p.setStatus("Pending");p.setMode(live()?"razorpay":"test");
        if(live()){
            long paise=amount.movePointRight(2).longValueExact();JSONObject req=new JSONObject();req.put("amount",paise);req.put("currency","INR");req.put("receipt",p.getId());JSONObject notes=new JSONObject();notes.put("campaignId",c.getId());notes.put("paymentId",p.getId());req.put("notes",notes);Order order=razorpay.orders.create(req);p.setProviderOrderId(order.get("id"));
        }
        payments.save(p);Map<String,Object> out=new LinkedHashMap<>();out.put("payment",p);out.put("keyId",live()?keyId:null);return out;
    }
    @Transactional
    public Map<String,Object> confirm(User u,ConfirmPaymentRequest r) throws Exception {
        Payment p=payments.findByIdForUpdate(r.getPaymentId()).orElseThrow(()->new NoSuchElementException("Payment not found."));
        if(!Objects.equals(p.getDonorId(),String.valueOf(u.getId())))throw new SecurityException("This payment does not belong to you.");
        var existing=donations.findByPaymentId(p.getId()); if(existing.isPresent())return result(existing.get(),campaigns.findById(p.getCampaignId()).orElseThrow());
        if("Completed".equals(p.getStatus()))throw new IllegalStateException("Payment already processed.");
        if("razorpay".equals(p.getMode())){
            if(r.getRazorpayPaymentId()==null||r.getRazorpayOrderId()==null||r.getRazorpaySignature()==null)throw new IllegalArgumentException("Incomplete Razorpay verification data.");
            if(!Objects.equals(p.getProviderOrderId(),r.getRazorpayOrderId()))throw new IllegalArgumentException("Razorpay order does not match.");
            payments.findByProviderPaymentId(r.getRazorpayPaymentId()).ifPresent(other->{if(!Objects.equals(other.getId(),p.getId()))throw new IllegalArgumentException("Razorpay payment already processed.");});
            JSONObject o=new JSONObject();o.put("razorpay_order_id",p.getProviderOrderId());o.put("razorpay_payment_id",r.getRazorpayPaymentId());o.put("razorpay_signature",r.getRazorpaySignature());
            if(!Utils.verifyPaymentSignature(o,secret))throw new IllegalArgumentException("Payment signature verification failed.");
            p.setProviderPaymentId(r.getRazorpayPaymentId());p.setProviderSignature(r.getRazorpaySignature());
        } else if(!"success".equalsIgnoreCase(r.getTestResult())) throw new IllegalArgumentException("Test payment was not successful.");
        Campaign c=campaigns.findByIdForUpdate(p.getCampaignId()).orElseThrow(()->new NoSuchElementException("Campaign not found."));
        Donation d=new Donation();d.setId(Ids.id("don"));d.setPaymentId(p.getId());d.setProviderPaymentId(p.getProviderPaymentId());d.setDonorId(String.valueOf(u.getId()));d.setCampaignId(c.getId());d.setCampaignTitle(c.getTitle());d.setAmount(p.getAmount());d.setAnonymous(p.isAnonymous());d.setMessage(p.getMessage());d.setStatus("Completed");donations.save(d);
        c.setRaised((c.getRaised()==null?BigDecimal.ZERO:c.getRaised()).add(p.getAmount()));c.setDonorCount((c.getDonorCount()==null?0:c.getDonorCount())+1);if(c.getGoal()!=null&&c.getRaised().compareTo(c.getGoal())>=0)c.setStatus("Completed");campaigns.save(c);
        p.setStatus("Completed");p.setCompletedAt(LocalDateTime.now());payments.save(p);return result(d,c);
    }
    private static Map<String,Object> result(Donation d,Campaign c){Map<String,Object> m=new LinkedHashMap<>();m.put("donation",d);m.put("campaign",c);return m;}
    private static String trim300(String s){if(s==null)return "";s=s.trim();return s.length()>300?s.substring(0,300):s;}
}
