package com.kampuskor.restservice.config;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.google.auth.Credentials;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.auth.oauth2.ServiceAccountCredentials;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;

@Configuration
public class GoogleCloudConfig {
    @Value("${gcp.credentials.file:}")
    private String credentialFilePath;

    @Value("${gcp.storage.bucket}")
    private String bucketName;

    @Bean
    public Storage storage() throws IOException {
        Credentials credentials;

        if (credentialFilePath == null || credentialFilePath.isBlank()) {
            credentials = GoogleCredentials.getApplicationDefault();
        } else if (credentialFilePath.startsWith("classpath:")) {
            String path = credentialFilePath.replace("classpath:", "");
            credentials = ServiceAccountCredentials.fromStream(getClass().getResourceAsStream(path));
        } else {
            credentials = ServiceAccountCredentials.fromStream(new java.io.FileInputStream(credentialFilePath));
        }

        return StorageOptions.newBuilder().setCredentials(credentials).build().getService();
    }

    @Bean
    public String gcsBucketName() {
        return bucketName;
    }
}
