package com.example.service;

import java.io.IOException;
import java.nio.file.*;
import java.util.UUID;
import javax.imageio.ImageIO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import com.example.Entity.User;
import com.example.repository.UserRepository;

@Service
public class AvatarStorage {
    private final Path root;
    private final UserRepository users;

    public AvatarStorage(@Value("${kindr.upload-dir:uploads}") String directory, UserRepository users) {
        this.root = Path.of(directory).toAbsolutePath().normalize();
        this.users = users;
    }

    public void save(User user, MultipartFile image) throws IOException {
        if (image == null || image.isEmpty()) throw new IllegalArgumentException("Choose a profile photo.");
        if (image.getSize() > 5L * 1024 * 1024) throw new IllegalArgumentException("Choose a photo smaller than 5 MB.");
        String format;
        byte[] bytes = image.getBytes();
        try (var stream = ImageIO.createImageInputStream(new java.io.ByteArrayInputStream(bytes))) {
            var readers = ImageIO.getImageReaders(stream);
            if (!readers.hasNext()) throw new IllegalArgumentException("Choose a valid JPEG or PNG photo.");
            var reader = readers.next();
            try {
                reader.setInput(stream);
                format = reader.getFormatName().toLowerCase(java.util.Locale.ROOT);
                if (!format.equals("jpeg") && !format.equals("png")) throw new IllegalArgumentException("Choose a JPEG or PNG photo.");
                if ((long) reader.getWidth(0) * reader.getHeight(0) > 25_000_000L)
                    throw new IllegalArgumentException("Choose a photo smaller than 25 megapixels.");
                reader.read(0);
            } finally { reader.dispose(); }
        } catch (IOException error) {
            throw new IllegalArgumentException("Choose a valid JPEG or PNG photo.", error);
        }
        Files.createDirectories(root);
        String filename = "avatar-" + UUID.randomUUID() + (format.equals("jpeg") ? ".jpg" : ".png");
        Path destination = root.resolve(filename);
        Files.write(destination, bytes, StandardOpenOption.CREATE_NEW);
        String previousPath = user.getAvatarPath();
        String previousType = user.getAvatarContentType();
        byte[] previousData = user.getAvatarData();
        try {
            user.setAvatarPath("/uploads/" + filename);
            user.setAvatarContentType("image/" + format);
            user.setAvatarData(null);
            users.saveAndFlush(user);
        } catch (RuntimeException error) {
            user.setAvatarPath(previousPath);
            user.setAvatarContentType(previousType);
            user.setAvatarData(previousData);
            Files.deleteIfExists(destination);
            throw error;
        }
    }
}
