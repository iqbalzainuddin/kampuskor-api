package com.kampuskor.restservice.features.CourseContent;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kampuskor.restservice.common.response.PageResponse;
import com.kampuskor.restservice.features.CourseContent.DTO.UploadRequest;
import com.kampuskor.restservice.utils.security.AuthUtils;

import jakarta.validation.Valid;

import java.io.IOException;
import java.net.URI;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;

@RestController
@RequestMapping("/courses/{courseId}/sections/{sectionId}/contents")
class CourseContentController {
    private final AuthUtils authUtils;
    private final CourseContentService courseContentService;

    public CourseContentController(
        AuthUtils authUtils,
        CourseContentService courseContentService
    ) {
        this.authUtils = authUtils;
        this.courseContentService = courseContentService;
    }

    @PostMapping("/upload")
    private ResponseEntity<Void> uploadCourseMaterial(
        @PathVariable Long courseId,
        @PathVariable Long sectionId,
        @Valid @ModelAttribute UploadRequest uploadRequest,
        Authentication authentication
    ) throws IOException {
        Long authenticatedUserId = authUtils.getAuthenticatedUserId();

        CourseContent courseContent = courseContentService.uploadCourseContent(
            uploadRequest,
            courseId,
            sectionId,
            authenticatedUserId,
            authentication.getName()
        );

        URI location = URI.create(courseContent.getFileUrl());

        return ResponseEntity.created(location).build();
    }
    
    @GetMapping
    private ResponseEntity<PageResponse<CourseContent>> getCourseContent(
        @PathVariable Long courseId,
        @PathVariable Long sectionId,
        Pageable pageable,
        Authentication authentication
    ) {
        Long authenticatedUserId = authUtils.getAuthenticatedUserId();

        Page<CourseContent> courseContents = courseContentService.getCourseContents(
            courseId, 
            sectionId, 
            authenticatedUserId, 
            authentication.getAuthorities().iterator().next().toString(), 
            pageable.getPageNumber(), 
            pageable.getPageSize(), 
            pageable.getSortOr(Sort.by(Sort.Direction.ASC, "id"))
        );

        return ResponseEntity.ok(
            new PageResponse<>(
                courseContents.getNumber(),
                courseContents.getNumberOfElements(),
                courseContents.getTotalPages(),
                courseContents.getTotalElements(),
                courseContents.stream().toList()
            )
        );
    }
    
    @DeleteMapping("/{contentId}")
    private ResponseEntity<Void> deleteCourseContent(
        @PathVariable Long courseId,
        @PathVariable Long sectionId,
        @PathVariable Long contentId
    ) throws IOException {
        Long authenticatedUserId = authUtils.getAuthenticatedUserId();
        courseContentService.deleteCourseContent(courseId, sectionId, contentId, authenticatedUserId);
        return ResponseEntity.noContent().build();
    }
}
