package com.kampuskor.restservice.features.CourseContent;

import java.io.IOException;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.kampuskor.restservice.features.Course.CourseRepository;
import com.kampuskor.restservice.features.Course.Model.Course;
import com.kampuskor.restservice.features.CourseContent.DTO.UploadRequest;
import com.kampuskor.restservice.features.CourseSection.CourseSection;
import com.kampuskor.restservice.features.CourseSection.CourseSectionRepository;
import com.kampuskor.restservice.features.Storage.GoogleCloud.StorageService;
import com.kampuskor.restservice.features.User.User;
import com.kampuskor.restservice.features.User.UserRepository;

@Service
public class CourseContentService {
    private final StorageService storageService;
    private final UserRepository userRepository;
    private final CourseRepository courseRepository;
    private final CourseSectionRepository courseSectionRepository;
    private final CourseContentRepository courseContentRepository;

    public CourseContentService(
        StorageService storageService,
        UserRepository userRepository,
        CourseRepository courseRepository,
        CourseSectionRepository courseSectionRepository, 
        CourseContentRepository courseContentRepository
    ) {
        this.storageService = storageService;
        this.userRepository = userRepository;
        this.courseRepository = courseRepository;
        this.courseSectionRepository = courseSectionRepository;
        this.courseContentRepository = courseContentRepository;
    }

    public CourseContent uploadCourseContent(
        UploadRequest uploadRequest, 
        Long courseId, 
        Long sectionId, 
        Long instructorId,
        String authenticatedUsername
    ) throws IOException {
        if (!courseSectionRepository.existsByIdAndCourseIdAndInstructorId(sectionId, courseId, instructorId)) {
            throw new IllegalArgumentException("Section not found or you don't have permission to access it.");
        }

        User instructor = userRepository.findByUsername(authenticatedUsername)
            .orElseThrow(() -> new IllegalArgumentException("Instructor not found"));
        Course course = courseRepository.findByIdAndInstructor(courseId, instructor)
            .orElseThrow(() -> new IllegalArgumentException("Course not found"));
        CourseSection courseSection = courseSectionRepository.findById(sectionId)
            .orElseThrow(() -> new IllegalArgumentException("Course section not found"));

        String uploadedFileUrl = storageService.uploadFile(
            uploadRequest.file(), 
            "courses/" + courseId + "/sections/" + sectionId + "/contents"
        );
        CourseContent courseContent = new CourseContent();
        
        courseContent.setDisplayname(uploadRequest.displayName());
        courseContent.setFileName(storageService.extractFileNameFromUrl(uploadedFileUrl));
        courseContent.setFileUrl(uploadedFileUrl);
        courseContent.setContentType(uploadRequest.file().getContentType());
        courseContent.setFileSize(uploadRequest.file().getSize());
        courseContent.setCourse(course);
        courseContent.setSection(courseSection);
        courseContent.setInstructor(instructor);
        
        return courseContentRepository.save(courseContent);
    }

    public Page<CourseContent> getCourseContents(
        Long courseId,
        Long sectionId,
        Long authenticatedUserId,
        String authenticatedUserRole,
        int page,
        int size,
        Sort sort
    ) {
        if (authenticatedUserRole.equals("ROLE_STUDENT")) {
            if (!courseRepository.existsByIdAndStudentsId(courseId, authenticatedUserId)) {
                throw new IllegalArgumentException("Access denied");
            }
        }

        if (authenticatedUserRole.equals("ROLE_INSTRUCTOR")) {
            if (!courseRepository.existsByIdAndInstructorId(courseId, authenticatedUserId)) {
                throw new IllegalArgumentException("Access denied");
            }
        }

        if (!courseSectionRepository.existsByIdAndCourseId(sectionId, courseId)) {
            throw new IllegalArgumentException("Section not found");
        }

        return courseContentRepository.findByCourseIdAndSectionId(
            courseId,
            sectionId,
            PageRequest.of(page, size, sort)
        );
    }

    public void deleteCourseContent(
        Long courseId,
        Long sectionId,
        Long contentId,
        Long instructorId
    ) throws IOException {
        CourseContent courseContent = courseContentRepository.findByIdAndCourseIdAndSectionIdAndInstructorId(
            contentId, 
            courseId, 
            sectionId, 
            instructorId
        ).orElseThrow(() -> new IllegalArgumentException("Content not found"));
        
        courseContentRepository.delete(courseContent);
        storageService.deleteFile(courseContent.getFileUrl());
    }
}
