package com.kampuskor.restservice.utils.security;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class PasswordHashGenerator {
    public static void main(String[] args) {
        if (args.length == 0) {
            System.out.println("❌ Please provide a password as an argument");
            System.exit(1);
        }

        String rawPassword = args[0];
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12);
        String hashedPassword = encoder.encode(rawPassword);

        System.out.println("Bcrypt hash: " + hashedPassword);
    }
}