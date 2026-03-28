package com.doculab.document_service.websocket;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Message for cursor position tracking
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CursorPositionMessage {
    private UUID userId;
    private String username;
    private String blockId;
    private Integer cursorPosition; // Position within the block
    private String color; // Color for user's cursor
    private LocalDateTime timestamp;
}