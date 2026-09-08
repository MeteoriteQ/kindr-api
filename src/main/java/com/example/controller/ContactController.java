package com.example.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.dto.ContactRequest;
import com.example.dto.ContactResponse;
import com.example.service.ContactService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/contact")
public class ContactController {

    private final ContactService contactService;

    public ContactController(ContactService contactService) {
        this.contactService = contactService;
    }

    // ==========================================
    // SEND CONTACT MESSAGE
    // ==========================================

    @PostMapping
    public ResponseEntity<java.util.Map<String, Object>> createContact(

            @Valid
            @RequestBody ContactRequest request) {

        ContactResponse saved = contactService.createContact(request);
        return ResponseEntity.status(201).body(java.util.Map.of(
                "status", "success",
                "message", "Your message has been received. Thank you for contacting Kindr.",
                "contact", saved));
    }

    // ==========================================
    // GET ALL CONTACT MESSAGES
    // ==========================================

    @GetMapping
    public ResponseEntity<List<ContactResponse>>
    getAllContacts() {

        return ResponseEntity.ok(
                contactService.getAllContacts());
    }

    // ==========================================
    // GET CONTACT BY ID
    // ==========================================

    @GetMapping("/{id}")
    public ResponseEntity<ContactResponse>
    getContactById(

            @PathVariable Long id) {

        return ResponseEntity.ok(
                contactService.getContactById(id));
    }

    // ==========================================
    // DELETE CONTACT MESSAGE
    // ==========================================

    @DeleteMapping("/{id}")
    public ResponseEntity<String>
    deleteContact(

            @PathVariable Long id) {

        return ResponseEntity.ok(
                contactService.deleteContact(id));
    }
}
