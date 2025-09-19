package com.kampuskor.restservice.features.Course;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PathVariable;

@RestController
@RequestMapping("/courses")
class CourseController {
    @GetMapping
    private ResponseEntity<Void> findAll() {
        return ResponseEntity.ok().build();
    }
    
    @PostMapping
    private ResponseEntity<Void> createCourse() {
        //TODO: process POST request
        
        return ResponseEntity.created(null).build();
    }
    
    @PutMapping("/{id}")
    private ResponseEntity<Void> updateCourse(@PathVariable Long id) {
        //TODO: process PUT request

        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    private ResponseEntity<Void> deleteCourse(@PathVariable Long id) {
        //TODO: process DELETE request

        return ResponseEntity.noContent().build();
    }
}
