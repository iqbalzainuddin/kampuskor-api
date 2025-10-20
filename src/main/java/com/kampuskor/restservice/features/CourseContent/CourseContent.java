package com.kampuskor.restservice.features.CourseContent;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.kampuskor.restservice.features.Course.Model.Course;
import com.kampuskor.restservice.features.CourseSection.CourseSection;
import com.kampuskor.restservice.features.User.User;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "course_contents")
public class CourseContent {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 255, name = "display_name")
    private String displayname;

    @Column(nullable = false, length = 255, name = "file_name")
    private String fileName;

    @Column(nullable = false, length = 512, name = "file_url")
    private String fileUrl;

    @Column(nullable = false, length = 100, name = "content_type")
    private String contentType;

    @Column(nullable = false, name = "file_size")
    private Long fileSize;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "course_id", nullable = false)
    private Course course;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "section_id", nullable = false)
    private CourseSection section;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "instructor_id", nullable = false)
    private User instructor;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false, nullable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    public Long getId() {
        return id;
    }

    public String getDisplayname() {
        return displayname;
    }

    public String getFileName() {
        return fileName;
    }

    public String getFileUrl() {
        return fileUrl;
    }

    public String getContentType() {
        return contentType;
    }

    public Long getFileSize() {
        return fileSize;
    }

    public Course getCourse() {
        return course;
    }

    public CourseSection getSection() {
        return section;
    }

    public User getInstructor() {
        return instructor;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setDisplayname(String displayname) {
        this.displayname = displayname;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public void setFileUrl(String fileUrl) {
        this.fileUrl = fileUrl;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public void setFileSize(Long fileSize) {
        this.fileSize = fileSize;
    }

    public void setCourse(Course course) {
        this.course = course;
    }

    public void setSection(CourseSection section) {
        this.section = section;
    }

    public void setInstructor(User instructor) {
        this.instructor = instructor;
    }
}
