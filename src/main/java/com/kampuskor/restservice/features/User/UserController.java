package com.kampuskor.restservice.features.User;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import com.kampuskor.restservice.features.User.dto.*;

import jakarta.validation.Valid;

import java.net.URI;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PathVariable;


@RestController
@RequestMapping("/users")
class UserController {
    private UserRepository userRepository;
    private BCryptPasswordEncoder passwordEncoder;

    public UserController(UserRepository userRepository, BCryptPasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping
    private ResponseEntity<UsersResponse> findAllUsers(Pageable pageable) {
        Page<User> page = userRepository.findAll(
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
    

    @PostMapping
    private ResponseEntity<User> createUser(@Valid @RequestBody CreateUserRequest request, UriComponentsBuilder uriBuilder) {
        User user = new User();
        
        user.setName(request.getName());
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRoleType(request.getRoleType());
        User savedUser = userRepository.save(user);

        String locationBasePath = switch(savedUser.getRoleType()) {
            case A -> "admins";
            case I -> "instructors";
            case S -> "students";
        };
        URI location = uriBuilder
            .path("/{basePath}/{id}")
            .buildAndExpand(locationBasePath, savedUser.getId())
            .toUri();
        
        return ResponseEntity.created(location).build();
    }
    
    @PutMapping("/{username}")
    public ResponseEntity<Void> updateUser(@PathVariable String username, @Valid @RequestBody UpdateUserRequest updateUser) {
        User user = userRepository.findByUsername(username).orElse(null);
        
        if (user == null) {
            return ResponseEntity.notFound().build();
        }

        user.setName(updateUser.name());
        user.setUsername(updateUser.username());
        user.setEmail(updateUser.email());
        user.setRoleType(updateUser.roleType());
        userRepository.save(user);

        return ResponseEntity.noContent().build();
    }
}
