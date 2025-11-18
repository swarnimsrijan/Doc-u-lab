package com.doculab.document_service.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "document_versions")
@Data
public class DocumentVersion {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "version_id")
    private UUID versionId;

    @Column(name = "document_id", nullable = false)
    private UUID documentId;

    @Column(name = "version_number", nullable = false)
    private Integer versionNumber;

    @Column(name = "content", nullable = false, columnDefinition = "TEXT")
    private String content;

    @Column(name = "published_by", nullable = false)
    private UUID publishedBy;

    @Column(name = "published_at")
    private LocalDateTime publishedAt;

    @Column(name = "change_summary")
    private String changeSummary;

    @PrePersist
    protected void onCreate() {
        publishedAt = LocalDateTime.now();
    }
}
