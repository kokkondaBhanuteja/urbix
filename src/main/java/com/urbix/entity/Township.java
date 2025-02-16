package com.urbix.entity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "townships")
public class Township {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String state;

    @Lob  // Important: This ensures binary storage for images
    @Column(name = "image_file",columnDefinition = "LONGBLOB") // Required for PostgreSQL
    private byte[] imageFile;

    @Column(length = 500)
    private String description;

    public byte[] getImage() {
        return imageFile;
    }

    public void setImage(byte[] image) {
        this.imageFile = image;
    }
    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
