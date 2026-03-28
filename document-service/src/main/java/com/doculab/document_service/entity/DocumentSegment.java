package com.doculab.document_service.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "document_segments")
@Data
public class DocumentSegment {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "segment_id")
    private UUID segmentId;

    @Column(name = "document_id", nullable = false)
    private UUID documentId;

    @Column(name = "block_id", nullable = false)
    private String blockId;

    @Column(name = "last_edited_by")
    private UUID lastEditedBy;

    @Column(name = "last_edited_at")
    private LocalDateTime lastEditedAt;
}