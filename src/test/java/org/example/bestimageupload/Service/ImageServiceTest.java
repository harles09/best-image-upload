package org.example.bestimageupload.Service;

import org.example.bestimageupload.Model.Image;
import org.example.bestimageupload.Model.User;
import org.example.bestimageupload.Repository.ImageRepository;
import org.example.bestimageupload.Repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

class ImageServiceTest {
    private final ImageRepository imageRepository = mock(ImageRepository.class);
    private final UserRepository userRepository = mock(UserRepository.class);
    private final ImageService imageService = new ImageService(imageRepository, userRepository);

    @Test
    void storeImage() throws IOException {
        UUID userId = UUID.randomUUID();
        Path path = Paths.get("E:\\Coding\\Spring\\best-image-upload\\src\\test\\java\\org\\example\\bestimageupload\\assets\\297210698_10159261510298759_5901247806806568755_n.jpg");
        byte[] data = Files.readAllBytes(path);
        MultipartFile file = new MockMultipartFile("297210698_10159261510298759_5901247806806568755_n", "297210698_10159261510298759_5901247806806568755_n.jpg", "image/jpeg", data);

        when(imageRepository.findByFileName(anyString())).thenReturn(Optional.empty());
        when(userRepository.findById(any(UUID.class))).thenReturn(Optional.of(new User()));

        ResponseEntity<String> response = imageService.storeImage(file, userId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("File stored successfully: 297210698_10159261510298759_5901247806806568755_n.jpg", response.getBody());

        verify(imageRepository, times(1)).save(any(Image.class));
    }

    @Test
    void getImagesByUserId() {
        UUID userId = UUID.randomUUID();
        Image image = new Image();
        List<Image> images = Collections.singletonList(image);

        when(imageRepository.findByUserId(any(UUID.class))).thenReturn(images);

        ResponseEntity<?> response = imageService.getImagesByUserId(userId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(images, response.getBody());

        verify(imageRepository, times(1)).findByUserId(userId);
    }

    @Test
    void getImagesByImageId() {
        UUID imageId = UUID.randomUUID();
        Image image = new Image();
        Optional<Image> images = Optional.of(image);

        when(imageRepository.findById(any(UUID.class))).thenReturn(images);

        ResponseEntity<?> response = imageService.getImagesByImageId(imageId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(images, response.getBody());

        verify(imageRepository, times(1)).findById(imageId);
    }

    @Test
    void deleteImage() {
        String fileName = "297210698_10159261510298759_5901247806806568755_n.jpg";
        Image image = new Image();
        image.setFileName(fileName);

        when(imageRepository.findByFileName(anyString())).thenReturn(Optional.of(image));

        ResponseEntity<String> response = imageService.deleteImage(fileName);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Image deleted successfully: " + fileName, response.getBody());

        verify(imageRepository, times(1)).deleteByFileName(fileName);
    }

    @Test
    void deleteImageById() {
        UUID imageId = UUID.randomUUID();
        Image image = new Image();
        image.setId(imageId);

        when(imageRepository.findById(any(UUID.class))).thenReturn(Optional.of(image));

        ResponseEntity<String> response = imageService.deleteImageById(imageId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Image deleted successfully", response.getBody());

        verify(imageRepository, times(1)).deleteById(imageId);
    }
}