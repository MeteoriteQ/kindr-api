package com.example.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.example.Entity.ContactMessage;
import com.example.dto.ContactRequest;
import com.example.dto.ContactResponse;
import com.example.repository.ContactRepository;

@Service
public class ContactService {

    private final ContactRepository contactRepository;

    public ContactService(ContactRepository contactRepository) {
        this.contactRepository = contactRepository;
    }

    // ==========================================
    // CREATE CONTACT MESSAGE
    // ==========================================

    public ContactResponse createContact(ContactRequest request) {

        ContactMessage contact = new ContactMessage();

        contact.setFirstName(request.getFirstName());
        contact.setLastName(request.getLastName());
        contact.setEmail(request.getEmail());
        contact.setMobile(request.getMobile());
        contact.setTopic(request.getTopic());
        contact.setMessage(request.getMessage());

        ContactMessage savedContact =
                contactRepository.save(contact);

        return mapToResponse(savedContact);
    }

    // ==========================================
    // GET ALL CONTACT MESSAGES
    // ==========================================

    public List<ContactResponse> getAllContacts() {

        return contactRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    // ==========================================
    // GET CONTACT BY ID
    // ==========================================

    public ContactResponse getContactById(Long id) {

        ContactMessage contact =
                contactRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException(
                            "Contact message not found with ID: " + id));

        return mapToResponse(contact);
    }

    // ==========================================
    // DELETE CONTACT MESSAGE
    // ==========================================

    public String deleteContact(Long id) {

        ContactMessage contact =
                contactRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException(
                            "Contact message not found with ID: " + id));

        contactRepository.delete(contact);

        return "Contact message deleted successfully";
    }

    // ==========================================
    // ENTITY TO RESPONSE
    // ==========================================

    private ContactResponse mapToResponse(
            ContactMessage contact) {

        ContactResponse response =
                new ContactResponse();

        response.setId(contact.getId());
        response.setFirstName(contact.getFirstName());
        response.setLastName(contact.getLastName());
        response.setEmail(contact.getEmail());
        response.setMobile(contact.getMobile());
        response.setTopic(contact.getTopic());
        response.setMessage(contact.getMessage());

        return response;
    }
}