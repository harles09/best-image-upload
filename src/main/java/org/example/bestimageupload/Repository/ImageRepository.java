package org.example.bestimageupload.Repository;

import org.example.bestimageupload.Model.Image;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ImageRepository extends JpaRepository<Image, UUID> {
    Optional<Image> findByFileName(String fileName);

    List<Image> findByUserId(UUID userId);

    void deleteByFileName(String fileName);
}
