package com.example.config;
import com.example.Entity.*;
import com.example.dto.CampaignRequest;
import com.example.repository.CampaignRepository;
import com.example.service.CampaignService;
import java.math.BigDecimal;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockMultipartFile;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CampaignImageTest {
 CampaignRepository repo = mock(CampaignRepository.class);
 CampaignService service = new CampaignService(repo, null, null);
 CampaignRequest request() {
  CampaignRequest r = new CampaignRequest();
  r.setTitle("Test"); r.setCategory("Medical"); r.setCity("Test"); r.setStory("Test");
  r.setGoal(BigDecimal.valueOf(100));
  when(repo.save(any(Campaign.class))).thenAnswer(i -> i.getArgument(0));
  return r;
 }
 @Test void storesUrl() {
  CampaignRequest r=request(); r.setImage("https://example.com/photo.png");
  Campaign c=service.create(new User(),r,null);
  assertEquals(r.getImage(),c.getImage()); assertNull(c.getImageData());
 }
 @Test void storesUploadBytes() {
  byte[] bytes={(byte)137,80,78,71,13,10,26,10,0};
  Campaign c=service.create(new User(),request(),new MockMultipartFile("imageFile","photo.png","image/png",bytes));
  assertArrayEquals(bytes,c.getImageData()); assertEquals("image/png",c.getImageContentType());
 }
 @Test void rejectsInvalidSources() {
  CampaignRequest r=request(); r.setImage("javascript:alert(1)");
  assertThrows(IllegalArgumentException.class,()->service.create(new User(),r,null));
  assertThrows(IllegalArgumentException.class,()->service.create(new User(),request(),new MockMultipartFile("imageFile","a.png","image/png",new byte[]{1,2})));
 }
}
