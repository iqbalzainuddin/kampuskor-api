package com.kampuskor.restservice.features.User;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.kampuskor.restservice.features.User.dto.*;
import com.kampuskor.restservice.utils.security.CustomUserDetails;
import com.kampuskor.restservice.utils.security.JwtUtil;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@RestController
@RequestMapping("/auth")
class UserController {
    private final AuthenticationManager authenticationManager;
    private UserRepository userRepository;
    private BCryptPasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtils;

    public UserController(AuthenticationManager authenticationManager, UserRepository userRepository, BCryptPasswordEncoder passwordEncoder, JwtUtil jwtUtils) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtils = jwtUtils;
    }

    @GetMapping("/profile")
    private ResponseEntity<ProfileResponse> getUserById(Authentication authentication) {
        String currentAuthenticatedEmail = authentication.getName();
        return userRepository.findByEmail(currentAuthenticatedEmail)
            .map(user -> new ProfileResponse(user.getName(), user.getEmail()))
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    private ResponseEntity<Void> createUser(@RequestBody RegisterRequest request) {
        User user = new User();
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        userRepository.save(user);
        return ResponseEntity.created(null).build();
    }

    @PostMapping("/login")
    @ResponseStatus(HttpStatus.OK)
    private ResponseEntity<LoginResponse> loginUser(@RequestBody LoginRequest request) {
        Authentication authenticationRequest = UsernamePasswordAuthenticationToken
                .unauthenticated(request.getEmail(), request.getPassword());
        Authentication authenticationResponse = this.authenticationManager.authenticate(authenticationRequest);
        CustomUserDetails userDetails = (CustomUserDetails) authenticationResponse.getPrincipal();
        if (authenticationResponse.isAuthenticated()) {
            String token = jwtUtils.generateToken(userDetails.getUsername());
            return ResponseEntity.ok(new LoginResponse(token));
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }
}
