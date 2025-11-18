package com.doculab.document_service.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "document_content")
@Data
public class DocumentContent {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "content_id")
    private UUID contentId;

    @Column(name = "document_id", nullable = false, unique = true)
    private UUID documentId;

    @Column(name = "content", nullable = false, columnDefinition = "TEXT")
    private String content;

    @Column(name = "content_hash")
    private String contentHash;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }
}

