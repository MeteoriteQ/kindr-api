package com.example.config;

import com.example.Entity.User;
import com.example.repository.UserRepository;
import com.example.service.AvatarStorage;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.nio.file.*;
import javax.imageio.ImageIO;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.springframework.mock.web.MockMultipartFile;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AvatarStorageTest {
    @TempDir Path directory;
    UserRepository users = mock(UserRepository.class);

    MockMultipartFile photo() throws Exception {
        var output = new ByteArrayOutputStream();
        ImageIO.write(new BufferedImage(2, 2, BufferedImage.TYPE_INT_RGB), "png", output);
        return new MockMultipartFile("image", "../../photo.png", "image/png", output.toByteArray());
    }

    @Test void storesFileAndPersistsPathWithUniqueReplacement() throws Exception {
        var storage = new AvatarStorage(directory.toString(), users);
        var user = new User();
        user.setAvatarData(new byte[]{1});
        storage.save(user, photo());
        String first = user.getAvatarPath();
        assertTrue(first.startsWith("/uploads/avatar-"));
        assertArrayEquals(photo().getBytes(), Files.readAllBytes(directory.resolve(first.substring(9))));
        assertNull(user.getAvatarData());
        assertEquals("image/png", user.getAvatarContentType());
        verify(users).saveAndFlush(user);
        storage.save(user, photo());
        assertNotEquals(first, user.getAvatarPath());
    }

    @Test void rejectsInvalidAndOversizedFiles() {
        var storage = new AvatarStorage(directory.toString(), users);
        assertThrows(IllegalArgumentException.class, () -> storage.save(new User(),
            new MockMultipartFile("image", "fake.png", "image/png", "<script>bad</script>".getBytes())));
        assertThrows(IllegalArgumentException.class, () -> storage.save(new User(),
            new MockMultipartFile("image", "big.png", "image/png", new byte[5 * 1024 * 1024 + 1])));
        verifyNoInteractions(users);
    }

    @Test void failedDatabaseSaveRemovesNewFileAndPreservesPreviousPhoto() throws Exception {
        var user = new User();
        user.setAvatarPath("/uploads/previous.png");
        when(users.saveAndFlush(user)).thenThrow(new IllegalStateException("Database unavailable"));
        assertThrows(IllegalStateException.class, () -> new AvatarStorage(directory.toString(), users).save(user, photo()));
        assertEquals("/uploads/previous.png", user.getAvatarPath());
        try (var files = Files.list(directory)) { assertEquals(0, files.count()); }
    }
}
