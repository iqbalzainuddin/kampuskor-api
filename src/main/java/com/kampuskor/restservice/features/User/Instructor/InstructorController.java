package com.kampuskor.restservice.features.User.Instructor;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kampuskor.restservice.features.User.User;
import com.kampuskor.restservice.features.User.UserRepository;
import com.kampuskor.restservice.features.User.dto.UsersResponse;
import com.kampuskor.restservice.features.User.enums.RoleType;

@RestController
@RequestMapping("/instructors")
class InstructorController {
    private UserRepository userRepository;
    
    public InstructorController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping
    private ResponseEntity<UsersResponse> findAll(Pageable pageable) {
        Page<User> page = userRepository.findByRoleType(
            RoleType.I, 
            PageRequest.of(
                pageable.getPageNumber(),
                pageable.getPageSize(),
                pageable.getSortOr(Sort.by(Sort.Direction.ASC, "id"))
            )
        );
        UsersResponse response = new UsersResponse(
            page.getNumber(),
            page.getNumberOfElements(),
            page.getTotalPages(),
            page.getTotalElements(),
            page.getContent()
        );
        return ResponseEntity.ok(response);
    }
}
