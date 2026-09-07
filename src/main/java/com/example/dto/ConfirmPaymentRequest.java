package com.example.dto;
import com.fasterxml.jackson.annotation.JsonProperty;
public class ConfirmPaymentRequest {
 private String paymentId,testResult;
 @JsonProperty("razorpay_payment_id") private String razorpayPaymentId;
 @JsonProperty("razorpay_order_id") private String razorpayOrderId;
 @JsonProperty("razorpay_signature") private String razorpaySignature;
 public String getPaymentId(){return paymentId;} public void setPaymentId(String v){paymentId=v;}
 public String getTestResult(){return testResult;} public void setTestResult(String v){testResult=v;}
 public String getRazorpayPaymentId(){return razorpayPaymentId;} public void setRazorpayPaymentId(String v){razorpayPaymentId=v;}
 public String getRazorpayOrderId(){return razorpayOrderId;} public void setRazorpayOrderId(String v){razorpayOrderId=v;}
 public String getRazorpaySignature(){return razorpaySignature;} public void setRazorpaySignature(String v){razorpaySignature=v;}
}
