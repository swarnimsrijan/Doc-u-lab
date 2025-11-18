package com.doculab.document_service.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * WebSocket controller for real-time document collaboration
 * Note: Main collaboration logic should be in Collaboration Service
 * This is just for basic document updates
 */
@Controller
@RequiredArgsConstructor
@Slf4j
public class DocumentWebSocketController {

    private final SimpMessagingTemplate messagingTemplate;

    /**
     * Handle document content updates from clients
     */
    @MessageMapping("/document/{documentId}/update")
    public void handleDocumentUpdate(
            @DestinationVariable UUID documentId,
            @Payload DocumentUpdateMessage message,
            Principal principal) {

        log.debug("Received update for document {} from user {}",
                documentId, principal.getName());

        message.setTimestamp(LocalDateTime.now());
        message.setDocumentId(documentId);

        // Broadcast to all users subscribed to this document
        messagingTemplate.convertAndSend(
                "/topic/document/" + documentId,
                message
        );
    }

    /**
     * Handle cursor position updates
     */
    @MessageMapping("/document/{documentId}/cursor")
    public void handleCursorUpdate(
            @DestinationVariable UUID documentId,
            @Payload CursorPositionMessage message,
            Principal principal) {

        message.setTimestamp(LocalDateTime.now());

        // Broadcast cursor position to other users
        messagingTemplate.convertAndSend(
                "/topic/document/" + documentId + "/cursors",
                message
        );
    }

    /**
     * Notify when user joins document editing session
     */
    public void notifyUserJoined(UUID documentId, UUID userId, String username) {
        DocumentUpdateMessage message = new DocumentUpdateMessage(
                documentId,
                userId,
                username,
                DocumentUpdateMessage.UpdateType.USER_JOIN,
                null,
                null,
                LocalDateTime.now()
        );

        messagingTemplate.convertAndSend(
                "/topic/document/" + documentId + "/presence",
                message
        );
    }

    /**
     * Notify when user leaves document editing session
     */
    public void notifyUserLeft(UUID documentId, UUID userId, String username) {
        DocumentUpdateMessage message = new DocumentUpdateMessage(
                documentId,
                userId,
                username,
                DocumentUpdateMessage.UpdateType.USER_LEAVE,
                null,
                null,
                LocalDateTime.now()
        );

        messagingTemplate.convertAndSend(
                "/topic/document/" + documentId + "/presence",
                message
        );
    }
}
