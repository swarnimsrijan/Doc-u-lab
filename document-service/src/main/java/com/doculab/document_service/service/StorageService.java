package com.doculab.document_service.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class StorageService {

    private final S3Client s3Client;

    @Value("${storage.type:local}")
    private String storageType;

    @Value("${storage.s3.bucket:docdost-documents}")
    private String s3Bucket;

    @Value("${storage.local.path:./storage/documents}")
    private String localStoragePath;

    @Value("${storage.max-content-db-size:1048576}") // 1MB default
    private long maxContentDbSize;

    /**
     * Store document content. Returns S3 path or null if stored in DB
     */
    public String storeContent(UUID documentId, String content) {
        byte[] contentBytes = content.getBytes();

        // Store in DB if small enough
        if (contentBytes.length <= maxContentDbSize) {
            return null; // Indicates content should be stored in DB
        }

        // Store in external storage
        String fileName = documentId.toString() + ".json";

        if ("s3".equals(storageType)) {
            return storeInS3(fileName, contentBytes);
        } else {
            return storeLocally(fileName, contentBytes);
        }
    }

    /**
     * Retrieve content from storage
     */
    public String retrieveContent(String location) {
        try {
            if (location.startsWith("s3://")) {
                return retrieveFromS3(location);
            } else {
                return retrieveFromLocal(location);
            }
        } catch (Exception e) {
            log.error("Failed to retrieve content from: {}", location, e);
            throw new RuntimeException("Failed to retrieve document content");
        }
    }

    private String storeInS3(String fileName, byte[] content) {
        try {
            PutObjectRequest putRequest = PutObjectRequest.builder()
                    .bucket(s3Bucket)
                    .key(fileName)
                    .build();

            s3Client.putObject(putRequest, RequestBody.fromBytes(content));
            return "s3://" + s3Bucket + "/" + fileName;
        } catch (Exception e) {
            log.error("Failed to store in S3", e);
            throw new RuntimeException("Failed to store document in S3");
        }
    }

    private String storeLocally(String fileName, byte[] content) {
        try {
            Path directory = Paths.get(localStoragePath);
            if (!Files.exists(directory)) {
                Files.createDirectories(directory);
            }

            Path filePath = directory.resolve(fileName);
            Files.write(filePath, content);
            return "file://" + filePath.toString();
        } catch (IOException e) {
            log.error("Failed to store locally", e);
            throw new RuntimeException("Failed to store document locally");
        }
    }

    private String retrieveFromS3(String s3Path) throws IOException {
        String key = s3Path.substring(s3Path.lastIndexOf("/") + 1);

        GetObjectRequest getRequest = GetObjectRequest.builder()
                .bucket(s3Bucket)
                .key(key)
                .build();

        byte[] content = s3Client.getObject(getRequest).readAllBytes();
        return new String(content);
    }

    private String retrieveFromLocal(String filePath) throws IOException {
        String path = filePath.replace("file://", "");
        return Files.readString(Paths.get(path));
    }

    /**
     * Delete content from storage
     */
    public void deleteContent(String location) {
        if (location == null) return;

        try {
            if (location.startsWith("s3://")) {
                String key = location.substring(location.lastIndexOf("/") + 1);
                s3Client.deleteObject(builder -> builder.bucket(s3Bucket).key(key));
            } else {
                String path = location.replace("file://", "");
                Files.deleteIfExists(Paths.get(path));
            }
        } catch (Exception e) {
            log.error("Failed to delete content: {}", location, e);
        }
    }
}