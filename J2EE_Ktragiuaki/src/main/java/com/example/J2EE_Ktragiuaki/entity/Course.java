package com.example.J2EE_Ktragiuaki.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "courses")
public class Course {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 120)
    private String name;

    @Column(nullable = false)
    private int credits;

    @Column(nullable = false, length = 120)
    private String lecturer;

    @Column(nullable = false, length = 255)
    private String imageUrl;

    protected Course() {
    }

    public Course(String name, int credits, String lecturer, String imageUrl) {
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

    public int getCredits() {
        return credits;
    }

    public String getLecturer() {
        return lecturer;
    }

    public String getImageUrl() {
        return imageUrl;
    }
    
}
