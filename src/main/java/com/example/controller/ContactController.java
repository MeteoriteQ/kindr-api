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
    public ResponseEntity<ContactResponse> createContact(

            @Valid
            @RequestBody ContactRequest request) {

        return ResponseEntity.ok(
                contactService.createContact(request));
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