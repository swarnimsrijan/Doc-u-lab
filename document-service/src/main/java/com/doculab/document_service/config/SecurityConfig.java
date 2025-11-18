//package com.docdost.document_service.config;
//
//import org.springframework.context.annotation.Configuration;
//import org.springframework.messaging.simp.config.MessageBrokerRegistry;
//import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
//import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
//import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;
//
//@Configuration
//@EnableWebSocketMessageBroker
//public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {
//
//    @Override
//    public void configureMessageBroker(MessageBrokerRegistry config) {
//        // Enable simple memory-based message broker for sending messages to clients
//        config.enableSimpleBroker("/topic", "/queue");
//
//        // Prefix for messages from clients
//        config.setApplicationDestinationPrefixes("/app");
//
//        // Prefix for user-specific messages
//        config.setUserDestinationPrefix("/user");
//    }
//
//    @Override
//    public void registerStompEndpoints(StompEndpointRegistry registry) {
//        // WebSocket endpoint for clients to connect
//        registry.addEndpoint("/ws")
//                .setAllowedOriginPatterns("*")
//                .withSockJS(); // Fallback for browsers that don't support WebSocket
//    }
//}
//
//// ============================================
//
//package com.docdost.document_service.websocket;
//
//import lombok.AllArgsConstructor;
//import lombok.Data;
//import lombok.NoArgsConstructor;
//
//import java.time.LocalDateTime;
//import java.util.UUID;
//
///**
// * Message for real-time document updates
// */
//@Data
//@NoArgsConstructor
//@AllArgsConstructor
//public class DocumentUpdateMessage {
//    private UUID documentId;
//    private UUID userId;
//    private String username;
//    private UpdateType updateType;
//    private String blockId; // For granular updates
//    private String content;
//    private LocalDateTime timestamp;
//
//    public enum UpdateType {
//        CONTENT_CHANGE,
//        CURSOR_MOVE,
//        USER_JOIN,
//        USER_LEAVE,
//        LOCK_ACQUIRED,
//        LOCK_RELEASED
//    }
//}
//
//// ============================================
//
//package com.docdost.document_service.websocket;
//
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.messaging.handler.annotation.DestinationVariable;
//import org.springframework.messaging.handler.annotation.MessageMapping;
//import org.springframework.messaging.handler.annotation.Payload;
//import org.springframework.messaging.simp.SimpMessagingTemplate;
//import org.springframework.stereotype.Controller;
//
//import java.security.Principal;
//import java.time.LocalDateTime;
//import java.util.UUID;
//
///**
// * WebSocket controller for real-time document collaboration
// * Note: Main collaboration logic should be in Collaboration Service
// * This is just for basic document updates
// */
//@Controller
//@RequiredArgsConstructor
//@Slf4j
//public class DocumentWebSocketController {
//
//    private final SimpMessagingTemplate messagingTemplate;
//
//    /**
//     * Handle document content updates from clients
//     */
//    @MessageMapping("/document/{documentId}/update")
//    public void handleDocumentUpdate(
//            @DestinationVariable UUID documentId,
//            @Payload DocumentUpdateMessage message,
//            Principal principal) {
//
//        log.debug("Received update for document {} from user {}",
//                documentId, principal.getName());
//
//        message.setTimestamp(LocalDateTime.now());
//        message.setDocumentId(documentId);
//
//        // Broadcast to all users subscribed to this document
//        messagingTemplate.convertAndSend(
//                "/topic/document/" + documentId,
//                message
//        );
//    }
//
//    /**
//     * Handle cursor position updates
//     */
//    @MessageMapping("/document/{documentId}/cursor")
//    public void handleCursorUpdate(
//            @DestinationVariable UUID documentId,
//            @Payload CursorPositionMessage message,
//            Principal principal) {
//
//        message.setTimestamp(LocalDateTime.now());
//
//        // Broadcast cursor position to other users
//        messagingTemplate.convertAndSend(
//                "/topic/document/" + documentId + "/cursors",
//                message
//        );
//    }
//
//    /**
//     * Notify when user joins document editing session
//     */
//    public void notifyUserJoined(UUID documentId, UUID userId, String username) {
//        DocumentUpdateMessage message = new DocumentUpdateMessage(
//                documentId,
//                userId,
//                username,
//                DocumentUpdateMessage.UpdateType.USER_JOIN,
//                null,
//                null,
//                LocalDateTime.now()
//        );
//
//        messagingTemplate.convertAndSend(
//                "/topic/document/" + documentId + "/presence",
//                message
//        );
//    }
//
//    /**
//     * Notify when user leaves document editing session
//     */
//    public void notifyUserLeft(UUID documentId, UUID userId, String username) {
//        DocumentUpdateMessage message = new DocumentUpdateMessage(
//                documentId,
//                userId,
//                username,
//                DocumentUpdateMessage.UpdateType.USER_LEAVE,
//                null,
//                null,
//                LocalDateTime.now()
//        );
//
//        messagingTemplate.convertAndSend(
//                "/topic/document/" + documentId + "/presence",
//                message
//        );
//    }
//}
//
//// ============================================
//
//package com.docdost.document_service.websocket;
//
//import lombok.AllArgsConstructor;
//import lombok.Data;
//import lombok.NoArgsConstructor;
//
//import java.time.LocalDateTime;
//import java.util.UUID;
//
///**
// * Message for cursor position tracking
// */
//@Data
//@NoArgsConstructor
//@AllArgsConstructor
//public class CursorPositionMessage {
//    private UUID userId;
//    private String username;
//    private String blockId;
//    private Integer cursorPosition; // Position within the block
//    private String color; // Color for user's cursor
//    private LocalDateTime timestamp;
//}
//
//// ============================================
//
//package com.docdost.document_service.config;
//
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.authentication.AuthenticationManager;
//import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
//import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
//import org.springframework.security.config.http.SessionCreationPolicy;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.security.web.SecurityFilterChain;
//import org.springframework.web.cors.CorsConfiguration;
//import org.springframework.web.cors.CorsConfigurationSource;
//import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
//
//import java.util.Arrays;
//
//@Configuration
//@EnableWebSecurity
//@EnableMethodSecurity
//public class SecurityConfig {
//
//    @Bean
//    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//        http
//                .csrf(csrf -> csrf.disable())
//                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
//                .sessionManagement(session ->
//                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
//                .authorizeHttpRequests(auth -> auth
//                        .requestMatchers("/ws/**").permitAll() // Allow WebSocket connections
//                        .requestMatchers("/api/v1/documents/**").authenticated()
//                        .anyRequest().permitAll()
//                );
//
//        // Add JWT authentication filter here when implemented
//
//        return http.build();
//    }
//
//    @Bean
//    public CorsConfigurationSource corsConfigurationSource() {
//        CorsConfiguration configuration = new CorsConfiguration();
//        configuration.setAllowedOriginPatterns(Arrays.asList("*"));
//        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
//        configuration.setAllowedHeaders(Arrays.asList("*"));
//        configuration.setAllowCredentials(true);
//
//        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
//        source.registerCorsConfiguration("/**", configuration);
//        return source;
//    }
//
//    @Bean
//    public PasswordEncoder passwordEncoder() {
//        return new BCryptPasswordEncoder();
//    }
//
//    @Bean
//    public AuthenticationManager authenticationManager(
//            AuthenticationConfiguration authConfig) throws Exception {
//        return authConfig.getAuthenticationManager();
//    }
//}