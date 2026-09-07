package com.example.config;

import com.example.Entity.Campaign;
import com.example.Entity.Donation;
import com.example.Entity.Payment;
import com.example.Entity.CampaignUpdate;
import com.example.repository.*;
import java.math.BigDecimal;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest(properties = "spring.jpa.hibernate.ddl-auto=update")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class CampaignIdentityTest {
    @Autowired CampaignRepository campaigns;

    private Campaign campaign() {
        Campaign c = new Campaign();
        c.setOwnerId("test-owner");
        c.setOrganizer("Identity test");
        c.setTitle("Identity test");
        c.setCategory("Medical");
        c.setGoal(BigDecimal.valueOf(100));
        c.setCity("Test");
        c.setStory("Transactional ID verification");
        return c;
    }

    @Test
    void mysqlGeneratesIncreasingIdsAndReferencesUseLong() {
        Campaign first = campaign();
        Campaign second = campaign();
        first.setImage("https://example.com/photo.jpg?token=" + "a".repeat(2000));
        assertNull(first.getId());
        campaigns.saveAndFlush(first);
        campaigns.saveAndFlush(second);
        assertTrue(first.getId() > 0);
        assertEquals(first.getImage(), campaigns.findById(first.getId()).orElseThrow().getImage());
        assertTrue(second.getId() > first.getId());
        assertEquals(first.getId(), campaigns.findByIdForUpdate(first.getId()).orElseThrow().getId());
        Donation donation = new Donation();
        Payment payment = new Payment();
        CampaignUpdate update = new CampaignUpdate();
        donation.setCampaignId(first.getId());
        payment.setCampaignId(first.getId());
        update.setCampaignId(first.getId());
        assertEquals(first.getId(), donation.getCampaignId());
        assertEquals(first.getId(), payment.getCampaignId());
        assertEquals(first.getId(), update.getCampaignId());
    }
}
