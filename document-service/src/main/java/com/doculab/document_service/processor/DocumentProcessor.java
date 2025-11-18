//package com.docdost.document_service.processor;
//
//import com.docdost.document_service.enums.DocumentType;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import lombok.extern.slf4j.Slf4j;
//import org.apache.pdfbox.pdmodel.PDDocument;
//import org.apache.pdfbox.text.PDFTextStripper;
//import org.apache.poi.xwpf.usermodel.XWPFDocument;
//import org.apache.poi.xwpf.usermodel.XWPFParagraph;
//import org.springframework.stereotype.Component;
//import org.yaml.snakeyaml.Yaml;
//
//import java.io.ByteArrayInputStream;
//import java.io.InputStream;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//@Component
//@Slf4j
//public class DocumentProcessor {
//
//    private final ObjectMapper objectMapper;
//    private final Yaml yaml;
//
//    public DocumentProcessor(ObjectMapper objectMapper) {
//        this.objectMapper = objectMapper;
//        this.yaml = new Yaml();
//    }
//
//    /**
//     * Convert uploaded file to internal JSON format
//     */
//    public String convertToInternalFormat(byte[] fileData, String fileName, DocumentType targetType) {
//        String extension = getFileExtension(fileName).toLowerCase();
//
//        try {
//            switch (extension) {
//                case "pdf":
//                    return convertPdfToJson(fileData);
//                case "docx":
//                    return convertWordToJson(fileData);
//                case "md":
//                    return convertMarkdownToJson(fileData);
//                case "yaml":
//                case "yml":
//                    return convertYamlToJson(fileData);
//                case "xml":
//                    return convertXmlToJson(fileData);
//                default:
//                    return new String(fileData);
//            }
//        } catch (Exception e) {
//            log.error("Error converting document", e);
//            throw new RuntimeException("Failed to convert document: " + e.getMessage());
//        }
//    }
//
//    /**
//     * Convert PDF to JSON format
//     */
//    private String convertPdfToJson(byte[] pdfData) throws Exception {
//        try (PDDocument document = PDDocument.load(pdfData)) {
//            PDFTextStripper stripper = new PDFTextStripper();
//            String text = stripper.getText(document);
//
//            // Split into paragraphs
//            String[] paragraphs = text.split("\n\n");
//
//            Map<String, Object> jsonDoc = new HashMap<>();
//            jsonDoc.put("type", "json");
//            jsonDoc.put("version", 1);
//
//            Map<String, Object> content = new HashMap<>();
//            List<Map<String, Object>> blocks = new java.util.ArrayList<>();
//
//            int blockId = 1;
//            for (String para : paragraphs) {
//                if (para.trim().isEmpty()) continue;
//
//                Map<String, Object> block = new HashMap<>();
//                block.put("id", "block-" + blockId++);
//                block.put("type", "text");
//                block.put("data", para.trim());
//                block.put("formatting", Map.of("bold", false, "italic", false));
//                blocks.add(block);
//            }
//
//            content.put("blocks", blocks);
//            jsonDoc.put("content", content);
//
//            return objectMapper.writeValueAsString(jsonDoc);
//        }
//    }
//
//    /**
//     * Convert Word document to JSON format
//     */
//    private String convertWordToJson(byte[] wordData) throws Exception {
//        try (XWPFDocument document = new XWPFDocument(new ByteArrayInputStream(wordData))) {
//            List<XWPFParagraph> paragraphs = document.getParagraphs();
//
//            Map<String, Object> jsonDoc = new HashMap<>();
//            jsonDoc.put("type", "json");
//            jsonDoc.put("version", 1);
//
//            Map<String, Object> content = new HashMap<>();
//            List<Map<String, Object>> blocks = new java.util.ArrayList<>();
//
//            int blockId = 1;
//            for (XWPFParagraph para : paragraphs) {
//                String text = para.getText().trim();
//                if (text.isEmpty()) continue;
//
//                Map<String, Object> block = new HashMap<>();
//                block.put("id", "block-" + blockId++);
//                block.put("type", "text");
//                block.put("data", text);
//
//                // Detect basic formatting
//                boolean isBold = para.getRuns().stream()
//                        .anyMatch(run -> run.isBold());
//                boolean isItalic = para.getRuns().stream()
//                        .anyMatch(run -> run.isItalic());
//
//                block.put("formatting", Map.of("bold", isBold, "italic", isItalic));
//                blocks.add(block);
//            }
//
//            content.put("blocks", blocks);
//            jsonDoc.put("content", content);
//
//            return objectMapper.writeValueAsString(jsonDoc);
//        }
//    }
//
//    /**
//     * Convert Markdown to JSON format
//     */
//    private String convertMarkdownToJson(byte[] markdownData) throws Exception {
//        String markdown = new String(markdownData);
//        String[] lines = markdown.split("\n");
//
//        Map<String, Object> jsonDoc = new HashMap<>();
//        jsonDoc.put("type", "json");
//        jsonDoc.put("version", 1);
//
//        Map<String, Object> content = new HashMap<>();
//        List<Map<String, Object>> blocks = new java.util.ArrayList<>();
//
//        int blockId = 1;
//        StringBuilder currentParagraph = new StringBuilder();
//
//        for (String line : lines) {
//            if (line.trim().isEmpty()) {
//                if (currentParagraph.length() > 0) {
//                    addTextBlock(blocks, blockId++, currentParagraph.toString());
//                    currentParagraph = new StringBuilder();
//                }
//                continue;
//            }
//
//            // Handle headers
//            if (line.startsWith("#")) {
//                if (currentParagraph.length() > 0) {
//                    addTextBlock(blocks, blockId++, currentParagraph.toString());
//                    currentParagraph = new StringBuilder();
//                }
//
//                int level = 0;
//                while (level < line.length() && line.charAt(level) == '#') {
//                    level++;
//                }
//
//                Map<String, Object> block = new HashMap<>();
//                block.put("id", "block-" + blockId++);
//                block.put("type", "heading");
//                block.put("level", level);
//                block.put("data", line.substring(level).trim());
//                blocks.add(block);
//                continue;
//            }
//
//            // Handle code blocks
//            if (line.startsWith("```")) {
//                if (currentParagraph.length() > 0) {
//                    addTextBlock(blocks, blockId++, currentParagraph.toString());
//                    currentParagraph = new StringBuilder();
//                }
//
//                String language = line.substring(3).trim();
//                Map<String, Object> block = new HashMap<>();
//                block.put("id", "block-" + blockId++);
//                block.put("type", "code");
//                block.put("language", language.isEmpty() ? "plaintext" : language);
//                // Note: This is simplified - real implementation should handle multi-line code
//                blocks.add(block);
//                continue;
//            }
//
//            currentParagraph.append(line).append(" ");
//        }
//
//        if (currentParagraph.length() > 0) {
//            addTextBlock(blocks, blockId, currentParagraph.toString());
//        }
//
//        content.put("blocks", blocks);
//        jsonDoc.put("content", content);
//
//        return objectMapper.writeValueAsString(jsonDoc);
//    }
//
//    /**
//     * Convert YAML to JSON format
//     */
//    private String convertYamlToJson(byte[] yamlData) throws Exception {
//        String yamlString = new String(yamlData);
//        Object yamlObject = yaml.load(yamlString);
//
//        Map<String, Object> jsonDoc = new HashMap<>();
//        jsonDoc.put("type", "yaml");
//        jsonDoc.put("version", 1);
//        jsonDoc.put("rawContent", yamlString);
//        jsonDoc.put("parsedContent", yamlObject);
//
//        return objectMapper.writeValueAsString(jsonDoc);
//    }
//
//    /**
//     * Convert XML to JSON format
//     */
//    private String convertXmlToJson(byte[] xmlData) throws Exception {
//        String xmlString = new String(xmlData);
//
//        Map<String, Object> jsonDoc = new HashMap<>();
//        jsonDoc.put("type", "xml");
//        jsonDoc.put("version", 1);
//        jsonDoc.put("rawContent", xmlString);
//
//        return objectMapper.writeValueAsString(jsonDoc);
//    }
//
//    /**
//     * Validate document content based on type
//     */
//    public boolean validateDocument(String content, DocumentType type) {
//        try {
//            switch (type) {
//                case JSON:
//                    objectMapper.readTree(content);
//                    return true;
//                case YAML:
//                    yaml.load(content);
//                    return true;
//                case XML:
//                    // Basic XML validation
//                    return content.trim().startsWith("<") && content.trim().endsWith(">");
//                case MARKDOWN:
//                case MERMAID:
//                    return true; // Text-based, always valid
//                default:
//                    return false;
//            }
//        } catch (Exception e) {
//            log.error("Validation failed for type {}", type, e);
//            return false;
//        }
//    }
//
//    private void addTextBlock(List<Map<String, Object>> blocks, int blockId, String text) {
//        Map<String, Object> block = new HashMap<>();
//        block.put("id", "block-" + blockId);
//        block.put("type", "text");
//        block.put("data", text.trim());
//        block.put("formatting", Map.of("bold", false, "italic", false));
//        blocks.add(block);
//    }
//
//    private String getFileExtension(String fileName) {
//        int lastDot = fileName.lastIndexOf('.');
//        return lastDot == -1 ? "" : fileName.substring(lastDot + 1);
//    }
//}
//
//// ============================================
//
//package com.docdost.document_service.service;
//
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.stereotype.Service;
//import software.amazon.awssdk.core.sync.RequestBody;
//import software.amazon.awssdk.services.s3.S3Client;
//import software.amazon.awssdk.services.s3.model.GetObjectRequest;
//import software.amazon.awssdk.services.s3.model.PutObjectRequest;
//
//import java.io.IOException;
//import java.nio.file.Files;
//import java.nio.file.Path;
//import java.nio.file.Paths;
//import java.util.UUID;
//
//@Service
//@RequiredArgsConstructor
//@Slf4j
//public class StorageService {
//
//    private final S3Client s3Client;
//
//    @Value("${storage.type:local}")
//    private String storageType;
//
//    @Value("${storage.s3.bucket:docdost-documents}")
//    private String s3Bucket;
//
//    @Value("${storage.local.path:./storage/documents}")
//    private String localStoragePath;
//
//    @Value("${storage.max-content-db-size:1048576}") // 1MB default
//    private long maxContentDbSize;
//
//    /**
//     * Store document content. Returns S3 path or null if stored in DB
//     */
//    public String storeContent(UUID documentId, String content) {
//        byte[] contentBytes = content.getBytes();
//
//        // Store in DB if small enough
//        if (contentBytes.length <= maxContentDbSize) {
//            return null; // Indicates content should be stored in DB
//        }
//
//        // Store in external storage
//        String fileName = documentId.toString() + ".json";
//
//        if ("s3".equals(storageType)) {
//            return storeInS3(fileName, contentBytes);
//        } else {
//            return storeLocally(fileName, contentBytes);
//        }
//    }
//
//    /**
//     * Retrieve content from storage
//     */
//    public String retrieveContent(String location) {
//        try {
//            if (location.startsWith("s3://")) {
//                return retrieveFromS3(location);
//            } else {
//                return retrieveFromLocal(location);
//            }
//        } catch (Exception e) {
//            log.error("Failed to retrieve content from: {}", location, e);
//            throw new RuntimeException("Failed to retrieve document content");
//        }
//    }
//
//    private String storeInS3(String fileName, byte[] content) {
//        try {
//            PutObjectRequest putRequest = PutObjectRequest.builder()
//                    .bucket(s3Bucket)
//                    .key(fileName)
//                    .build();
//
//            s3Client.putObject(putRequest, RequestBody.fromBytes(content));
//            return "s3://" + s3Bucket + "/" + fileName;
//        } catch (Exception e) {
//            log.error("Failed to store in S3", e);
//            throw new RuntimeException("Failed to store document in S3");
//        }
//    }
//
//    private String storeLocally(String fileName, byte[] content) {
//        try {
//            Path directory = Paths.get(localStoragePath);
//            if (!Files.exists(directory)) {
//                Files.createDirectories(directory);
//            }
//
//            Path filePath = directory.resolve(fileName);
//            Files.write(filePath, content);
//            return "file://" + filePath.toString();
//        } catch (IOException e) {
//            log.error("Failed to store locally", e);
//            throw new RuntimeException("Failed to store document locally");
//        }
//    }
//
//    private String retrieveFromS3(String s3Path) throws IOException {
//        String key = s3Path.substring(s3Path.lastIndexOf("/") + 1);
//
//        GetObjectRequest getRequest = GetObjectRequest.builder()
//                .bucket(s3Bucket)
//                .key(key)
//                .build();
//
//        byte[] content = s3Client.getObject(getRequest).readAllBytes();
//        return new String(content);
//    }
//
//    private String retrieveFromLocal(String filePath) throws IOException {
//        String path = filePath.replace("file://", "");
//        return Files.readString(Paths.get(path));
//    }
//
//    /**
//     * Delete content from storage
//     */
//    public void deleteContent(String location) {
//        if (location == null) return;
//
//        try {
//            if (location.startsWith("s3://")) {
//                String key = location.substring(location.lastIndexOf("/") + 1);
//                s3Client.deleteObject(builder -> builder.bucket(s3Bucket).key(key));
//            } else {
//                String path = location.replace("file://", "");
//                Files.deleteIfExists(Paths.get(path));
//            }
//        } catch (Exception e) {
//            log.error("Failed to delete content: {}", location, e);
//        }
//    }
//}