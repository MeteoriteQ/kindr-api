package com.example.config;

import com.example.controller.ContactController;
import com.example.service.ContactService;
import com.example.repository.ContactRepository;
import com.example.Exception.ApiExceptionHandler;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest(properties = "spring.jpa.hibernate.ddl-auto=update")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class ContactSubmissionTest {
    @Autowired ContactRepository contacts;

    @Test void savesSubmissionAndReturnsCreatedStatus() throws Exception {
        var mvc = MockMvcBuilders.standaloneSetup(new ContactController(new ContactService(contacts)))
            .setControllerAdvice(new ApiExceptionHandler()).build();
        long before = contacts.count();
        mvc.perform(post("/api/contact").contentType("application/json").content("""
            {"firstName":"Contact","lastName":"Test","email":"contact-test@example.com",
             "mobile":"9876543210","topic":"Technical issue","message":"Test contact submission."}
            """))
            .andExpect(status().isCreated()).andExpect(jsonPath("$.status").value("success"))
            .andExpect(jsonPath("$.contact.id").isNumber())
            .andExpect(jsonPath("$.contact.message").value("Test contact submission."));
        assertEquals(before + 1, contacts.count());
        mvc.perform(post("/api/contact").contentType("application/json").content("{}"))
            .andExpect(status().isBadRequest());
        assertEquals(before + 1, contacts.count());
    }
}
