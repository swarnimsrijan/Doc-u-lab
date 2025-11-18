package com.doculab.document_service.entity;

import com.doculab.document_service.enums.DocumentRole;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "document_permissions")
@Data
public class DocumentPermission {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "permission_id")
    private UUID permissionId;

    @Column(name = "document_id", nullable = false)
    private UUID documentId;

    @Column(name = "user_id", nullable = false)
    private UUID userId;

    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false)
    private DocumentRole role;

    @Column(name = "granted_by", nullable = false)
    private UUID grantedBy;

    @Column(name = "granted_at")
    private LocalDateTime grantedAt;

    @Column(name = "expires_at")
    private LocalDateTime expiresAt;

    @PrePersist
    protected void onCreate() {
        grantedAt = LocalDateTime.now();
    }
}

