package com.example.config;

import com.example.controller.CampaignController;
import com.example.Entity.Campaign;
import com.example.Entity.User;
import com.example.dto.CampaignRequest;
import com.example.service.CampaignService;
import com.example.service.SessionService;
import jakarta.servlet.http.Cookie;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.mock.web.MockMultipartFile;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CampaignController.class)
class CampaignCreationTest {
    @Autowired MockMvc mvc;
    @MockitoBean CampaignService service;
    @MockitoBean SessionService sessions;

    @Test
    void bindsJsonCampaignFields() throws Exception {
        User owner = new User();
        when(sessions.fromToken("session")).thenReturn(owner);
        when(service.create(eq(owner), any(), isNull())).thenReturn(new Campaign());
        mvc.perform(post("/api/campaigns").cookie(new Cookie("kindr_session", "session"))
                .contentType("application/json").content("""
                {"title":"Medical care","category":"Medical","city":"Hyderabad",
                 "story":"Help with treatment","goal":100000,"image":"https://example.com/image.jpg",
                 "organizer":"Test organizer","phone":"9999999999"}
                """))
            .andExpect(status().isCreated());
        ArgumentCaptor<CampaignRequest> request = ArgumentCaptor.forClass(CampaignRequest.class);
        verify(service).create(eq(owner), request.capture(), isNull());
        CampaignRequest body = request.getValue();
        assertEquals("Medical care", body.getTitle());
        assertEquals("Medical", body.getCategory());
        assertEquals("Hyderabad", body.getCity());
        assertEquals("Help with treatment", body.getStory());
        assertEquals(100000, body.getGoal().intValueExact());
        assertEquals("https://example.com/image.jpg", body.getImage());
        assertEquals("Test organizer", body.getOrganizer());
        assertEquals("9999999999", body.getPhone());
    }

    @Test
    void preservesMultipartUpload() throws Exception {
        User owner = new User();
        when(sessions.fromToken("session")).thenReturn(owner);
        when(service.create(eq(owner), any(), any())).thenReturn(new Campaign());
        MockMultipartFile image = new MockMultipartFile("imageFile", "photo.png", "image/png", new byte[]{1, 2});
        mvc.perform(multipart("/api/campaigns").file(image)
                .cookie(new Cookie("kindr_session", "session"))
                .param("title", "Medical care").param("category", "Medical")
                .param("city", "Hyderabad").param("story", "Help with treatment").param("goal", "100000"))
            .andExpect(status().isCreated());
        verify(service).create(eq(owner), argThat(r -> "Medical care".equals(r.getTitle())
                && r.getGoal().intValueExact() == 100000), eq(image));
    }

    @Test
    void rejectsUnauthenticatedJsonCreation() throws Exception {
        mvc.perform(post("/api/campaigns").contentType("application/json").content("{}"))
            .andExpect(status().isUnauthorized());
        verifyNoInteractions(service);
    }
}
