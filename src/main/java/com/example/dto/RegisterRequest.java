package com.example.dto;

import com.fasterxml.jackson.annotation.JsonAlias;
import jakarta.validation.constraints.*;

public class RegisterRequest {
    @NotBlank(message="First name is required") private String firstName;
    @NotBlank(message="Last name is required") private String lastName;
    @NotBlank(message="Email is required") @Email(message="Enter a valid email address") private String email;
    // Exact frontend field is `phone`; mobileNumber is accepted only for old backend/Postman compatibility.
    @NotBlank(message="Mobile number is required") @JsonAlias("mobileNumber") private String phone;
    @NotBlank(message="Account type is required") private String accountType;
    @NotBlank(message="Password is required") @Size(min=6,message="Password must contain at least 6 characters") private String password;
    @NotBlank(message="Confirm password is required") private String confirmPassword;
    public String getFirstName(){return firstName;} public void setFirstName(String v){firstName=v;}
    public String getLastName(){return lastName;} public void setLastName(String v){lastName=v;}
    public String getEmail(){return email;} public void setEmail(String v){email=v;}
    public String getPhone(){return phone;} public void setPhone(String v){phone=v;}
    public String getAccountType(){return accountType;} public void setAccountType(String v){accountType=v;}
    public String getPassword(){return password;} public void setPassword(String v){password=v;}
    public String getConfirmPassword(){return confirmPassword;} public void setConfirmPassword(String v){confirmPassword=v;}
}
