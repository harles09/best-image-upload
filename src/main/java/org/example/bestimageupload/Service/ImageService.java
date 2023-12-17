package org.example.bestimageupload.Service;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import net.coobird.thumbnailator.Thumbnails;
import org.example.bestimageupload.Config.TrackExecutionTime;
import org.example.bestimageupload.Model.Image;
import org.example.bestimageupload.Model.User;
import org.example.bestimageupload.Repository.ImageRepository;
import org.example.bestimageupload.Repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@AllArgsConstructor
public class ImageService {
    private ImageRepository imageRepository;
    private UserRepository userRepository;
    @TrackExecutionTime
    public ResponseEntity<String> storeImage(MultipartFile file, UUID userId) {
        String fileName = StringUtils.cleanPath(file.getOriginalFilename());

        // Check if the file's name contains invalid characters
        if(fileName.contains("..")) {
            return new ResponseEntity<>("Sorry! Filename contains invalid path sequence " + fileName, HttpStatus.BAD_REQUEST);
        }

        try {
            // Check if the image with the same filename already exists
            Optional<Image> existingImage = imageRepository.findByFileName(fileName);
            if(existingImage.isPresent()) {
                return new ResponseEntity<>("Error: File with the same name already exists!", HttpStatus.CONFLICT);
            }

            // Check if the user exists
            Optional<User> userOptional = userRepository.findById(userId);
            if(!userOptional.isPresent()) {
                return new ResponseEntity<>("Error: User not found with id " + userId, HttpStatus.NOT_FOUND);
            }

            // Compress the image
            byte[] imageData;
            try (InputStream inputStream = new ByteArrayInputStream(file.getBytes());
                 ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {

                Thumbnails.of(inputStream)
                        .size(800, 800)  // New size of the image
                        .outputFormat("JPEG")  // Output format of the image
                        .outputQuality(0.8)  // Quality of the image from 0.0 to 1.0
                        .toOutputStream(outputStream);

                imageData = outputStream.toByteArray();
            }

            // Store the image
            Image image = new Image();
            image.setFileName(fileName);
            image.setFileType(file.getContentType());
            image.setData(imageData);
            image.setSize((long) imageData.length);
            image.setUploadTime(new Timestamp(System.currentTimeMillis()));
            image.setUser(userOptional.get());

            imageRepository.save(image);

            return new ResponseEntity<>("File stored successfully: " + fileName, HttpStatus.OK);
        } catch (IOException ex) {
            return new ResponseEntity<>("Could not store file " + fileName + ". Please try again!", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @Transactional
    @TrackExecutionTime
    public ResponseEntity<?> getImagesByUserId(UUID userId) {
        List<Image> images = imageRepository.findByUserId(userId);
        if(images.isEmpty()){
            return new ResponseEntity<>("No images found for the user", HttpStatus.NOT_FOUND);
        } else {
            return new ResponseEntity<>(images, HttpStatus.OK);
        }
    }

    @Transactional
    @TrackExecutionTime
    public ResponseEntity<?> getImagesByImageId(UUID imageId) {
        Optional<Image> images = imageRepository.findById(imageId);
        if(images.isEmpty()){
            return new ResponseEntity<>("No images found", HttpStatus.NOT_FOUND);
        } else {
            return new ResponseEntity<>(images, HttpStatus.OK);
        }
    }
    @Transactional
    @TrackExecutionTime
    public ResponseEntity<String> deleteImage(String fileName) {
        Optional<Image> existingImage = imageRepository.findByFileName(fileName);
        if(existingImage.isPresent()) {
            imageRepository.deleteByFileName(fileName);
            return new ResponseEntity<>("Image deleted successfully: " + fileName, HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Error: Image not found with filename " + fileName, HttpStatus.NOT_FOUND);
        }
    }
    @Transactional
    @TrackExecutionTime
    public ResponseEntity<String> deleteImageById(UUID id){
        Optional<Image> existingImage = imageRepository.findById(id);
        if(existingImage.isPresent()) {
            imageRepository.deleteById(id);
            return new ResponseEntity<>("Image deleted successfully", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Error: Image not found", HttpStatus.NOT_FOUND);
        }
    }
}
