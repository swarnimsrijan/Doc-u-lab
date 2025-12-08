package com.doculab.document_service.websocket;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Message for real-time document updates
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DocumentUpdateMessage {
    private UUID documentId;
    private UUID userId;
    private String username;
    private UpdateType updateType;
    private String blockId; // For granular updates
    private String content;
    private LocalDateTime timestamp;

    public enum UpdateType {
        CONTENT_CHANGE,
        CURSOR_MOVE,
        USER_JOIN,
        USER_LEAVE,
        LOCK_ACQUIRED,
        LOCK_RELEASED
    }
}