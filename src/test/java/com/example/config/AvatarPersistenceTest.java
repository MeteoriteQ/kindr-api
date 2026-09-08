package com.example.config;

import com.example.Entity.User;
import com.example.repository.UserRepository;
import com.example.service.AuthService;
import com.example.service.AvatarStorage;
import jakarta.persistence.EntityManager;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.nio.file.*;
import java.util.UUID;
import javax.imageio.ImageIO;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.mock.web.MockMultipartFile;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest(properties = "spring.jpa.hibernate.ddl-auto=update")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class AvatarPersistenceTest {
    @Autowired UserRepository users;
    @Autowired EntityManager entityManager;
    @TempDir Path directory;

    @Test void reloadsPhotoLocationFromDatabase() throws Exception {
        var user = new User();
        user.setFirstName("Avatar"); user.setLastName("Test");
        user.setEmail("avatar-" + UUID.randomUUID() + "@example.com");
        user.setPhone("1234567890"); user.setAccountType("Fundraiser"); user.setPassword("test-only");
        users.saveAndFlush(user);
        var bytes = new ByteArrayOutputStream();
        ImageIO.write(new BufferedImage(2, 2, BufferedImage.TYPE_INT_RGB), "png", bytes);
        new AvatarStorage(directory.toString(), users).save(user,
            new MockMultipartFile("image", "photo.png", "image/png", bytes.toByteArray()));
        Long id = user.getId();
        String path = user.getAvatarPath();
        entityManager.clear();
        var loaded = users.findById(id).orElseThrow();
        assertEquals(path, loaded.getAvatarPath());
        assertNull(loaded.getAvatarData());
        assertEquals(path, new AuthService(users, null, null).safe(loaded).get("avatarUrl"));
        assertArrayEquals(bytes.toByteArray(), Files.readAllBytes(directory.resolve(path.substring(9))));
    }
}
