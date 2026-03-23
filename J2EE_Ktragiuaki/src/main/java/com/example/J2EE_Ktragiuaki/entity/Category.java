package com.example.J2EE_Ktragiuaki.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "course")
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 120)
    private String name;

    @Column(nullable = false)
    private int credits;

    @Column(nullable = false, length = 120)
    private String lecturer;

    @Column(name = "image", nullable = false, length = 255)
    private String imageUrl;

    protected Category() {
    }

    public Category(String name, int credits, String lecturer, String imageUrl) {
        this.name = name;
        this.credits = credits;
        this.lecturer = lecturer;
        this.imageUrl = imageUrl;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCredits() {
        return credits;
    }

    public void setCredits(int credits) {
        this.credits = credits;
    }

    public String getLecturer() {
        return lecturer;
    }

    public void setLecturer(String lecturer) {
        this.lecturer = lecturer;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getDisplayImageUrl() {
        if (imageUrl == null || imageUrl.isBlank()) {
            return "/images/course-1.svg";
        }

        String normalizedImage = imageUrl.trim().toLowerCase();
        if (normalizedImage.startsWith("http://") || normalizedImage.startsWith("https://") || normalizedImage.startsWith("/")) {
            return imageUrl;
        }

        return switch (normalizedImage) {
            case "java.jpg" -> "/images/course-1.svg";
            case "database.jpg" -> "/images/course-3.svg";
            case "network.jpg" -> "/images/course-6.svg";
            case "business.jpg" -> "/images/course-5.svg";
            case "english.jpg" -> "/images/course-2.svg";
            case "uiux.jpg" -> "/images/course-4.svg";
            default -> "/images/course-1.svg";
        };
    }
}
