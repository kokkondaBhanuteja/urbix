package com.urbix.dto;

public class TownshipDTO {
    private String name;
    private String state;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getBase64Image() {
        return base64Image;
    }

    public void setBase64Image(String base64Image) {
        this.base64Image = base64Image;
    }

    private String description;
    private String base64Image;  // Holds Base64 image string

    public TownshipDTO(String name, String state, String description, String base64Image) {
        this.name = name;
        this.state = state;
        this.description = description;
        this.base64Image = base64Image;
    }

    // Getters & Setters
}

