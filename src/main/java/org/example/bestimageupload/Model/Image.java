package org.example.bestimageupload.Model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.sql.Timestamp;
import java.util.UUID;

@Entity
@Table(name = "table_image")
@Data
@AllArgsConstructor
@ToString
@NoArgsConstructor
public class Image {
    @Id
    @GeneratedValue
    @Column(name = "image_id")
    private UUID id;
    private String fileName;
    private String fileType;
    private Long size;
    private Timestamp uploadTime;
    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "user_id")
    private User user;
    @Lob
    private byte[] data;
}
