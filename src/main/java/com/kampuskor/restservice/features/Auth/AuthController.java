package com.kampuskor.restservice.features.Auth;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.kampuskor.restservice.features.Auth.dto.*;
import com.kampuskor.restservice.features.User.User;
import com.kampuskor.restservice.features.User.UserRepository;
import com.kampuskor.restservice.utils.security.CustomUserDetails;
import com.kampuskor.restservice.utils.security.JwtUtil;

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PutMapping;


@RestController
@RequestMapping("/auth")
class AuthController {
    private final AuthenticationManager authenticationManager;
    private UserRepository userRepository;
    private BCryptPasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtils;

    public AuthController(AuthenticationManager authenticationManager, UserRepository userRepository, BCryptPasswordEncoder passwordEncoder, JwtUtil jwtUtils) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtils = jwtUtils;
    }

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    private ResponseEntity<Void> createUser(@Valid @RequestBody RegisterRequest request) {
        User user = new User();
        user.setName(request.getName());
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        userRepository.save(user);
        return ResponseEntity.created(null).build();
    }

    @PostMapping("/login")
    @ResponseStatus(HttpStatus.OK)
    private ResponseEntity<LoginResponse> loginUser(@Valid @RequestBody LoginRequest request) {
        Authentication authenticationRequest = UsernamePasswordAuthenticationToken
                .unauthenticated(request.getUsernameOrEmail(), request.getPassword());
        Authentication authenticationResponse = this.authenticationManager.authenticate(authenticationRequest);
        CustomUserDetails userDetails = (CustomUserDetails) authenticationResponse.getPrincipal();
        if (authenticationResponse.isAuthenticated()) {
            String token = jwtUtils.generateToken(userDetails.getId());
            return ResponseEntity.ok(new LoginResponse(token));
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    @GetMapping("/profile")
    private ResponseEntity<ProfileResponse> getUserById(Authentication authentication) {
        String currentAuthenticatedUsername = authentication.getName();
        return userRepository.findByUsernameOrEmail(currentAuthenticatedUsername, currentAuthenticatedUsername)
            .map(user -> new ProfileResponse(user.getName(), user.getUsername(), user.getEmail()))
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/profile")
    public ResponseEntity<Void> updateProfile(@Valid @RequestBody UpdateProfileRequest userUpdate, Authentication authentication) {
        String currentAuthenticatedUsername = authentication.getName();
        User user = userRepository.findByUsername(currentAuthenticatedUsername).orElse(null);
        
        if (user == null) {
            return ResponseEntity.notFound().build();
        }
        
        user.setName(userUpdate.name());
        user.setUsername(userUpdate.username());
        user.setEmail(userUpdate.email());
        userRepository.save(user);

        return ResponseEntity.noContent().build();
    }
}
