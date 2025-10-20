package com.kampuskor.restservice.features.Storage.GoogleCloud;

import java.io.IOException;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;

@Service
public class StorageService {
    private final Storage storage;
    private final String bucketName;

    public StorageService(Storage storage, String bucketName) {
        this.storage = storage;
        this.bucketName = bucketName;
    }

    public String extractFileNameFromUrl(String fileUrl) {
        return fileUrl.substring(
            fileUrl.indexOf(bucketName) + bucketName.length() + 1
        );
    }

    public String uploadFile(MultipartFile file, String destinationPath) throws IOException {
        String sanitizedFileName = file.getOriginalFilename()
            .replaceAll("[^a-zA-Z0-9._-]", "_"); // replace illegal chars with "_"

        String blobName = destinationPath + "/" + sanitizedFileName;
        BlobId blobId = BlobId.of(bucketName, blobName);
        BlobInfo blobInfo = BlobInfo
            .newBuilder(blobId)
            .setContentType(file.getContentType())
            .build();
        
        storage.create(blobInfo, file.getBytes());
        return String.format("https://storage.googleapis.com/%s/%s", bucketName, blobName);
    }

    public void deleteFile(String fileUrl) throws IOException {
        String blobName = extractFileNameFromUrl(fileUrl);
        BlobId blobId = BlobId.of(bucketName, blobName);
        storage.delete(blobId);
    }
}
