package org.example.bestimageupload.Controller;

import lombok.AllArgsConstructor;
import org.example.bestimageupload.Service.ImageService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@AllArgsConstructor
@RestController
@RequestMapping("/images")
public class ImageController {
    private ImageService imageService;

    @PostMapping
    public ResponseEntity<String> uploadImage(@RequestParam("file") MultipartFile file, @RequestParam("userId") UUID userId) {
        return imageService.storeImage(file, userId);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<?> getImageByUserId(@PathVariable("userId") UUID userId) {
        return imageService.getImagesByUserId(userId);
    }

    @GetMapping("/{imageId}")
    public ResponseEntity<?> getImageByImageId(@PathVariable("imageId") UUID imageId) {
        return imageService.getImagesByImageId(imageId);
    }

    @DeleteMapping
    public ResponseEntity<String> DeleteImageByFileName(@RequestParam("fileName") String fileName) {
        return imageService.deleteImage(fileName);
    }

    @DeleteMapping(value = "/{imageId}")
    public ResponseEntity<String> DeleteImageByImageId(@PathVariable("imageId") UUID id) {
        return imageService.deleteImageById(id);
    }
}
