package com.doculab.document_service.entity;

import com.doculab.document_service.enums.DocumentStatus;
import com.doculab.document_service.enums.DocumentType;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

@Entity
@Table(name = "documents")
@Data
public class Document {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "document_id")
    private UUID documentId;

    @Column(name = "owner_id", nullable = false)
    private UUID ownerId;

    @Column(name = "title", nullable = false)
    private String title;

    @Enumerated(EnumType.STRING)
    @Column(name = "document_type", nullable = false)
    private DocumentType documentType;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private DocumentStatus status = DocumentStatus.DRAFT;

    @Column(name = "version")
    private Integer version = 1;

    @Column(name = "content_location")
    private String contentLocation; // S3 path for large files

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "metadata", columnDefinition = "jsonb")
    private Map<String, Object> metadata;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
