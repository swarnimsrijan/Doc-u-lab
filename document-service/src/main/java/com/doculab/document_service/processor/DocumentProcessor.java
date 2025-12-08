package com.doculab.document_service.processor;

import com.doculab.document_service.enums.DocumentType;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.springframework.stereotype.Component;
import org.yaml.snakeyaml.Yaml;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@Slf4j
public class DocumentProcessor {

    private final ObjectMapper objectMapper;
    private final Yaml yaml;

    public DocumentProcessor(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
        this.yaml = new Yaml();
    }

    /**
     * Convert uploaded file to internal JSON format
     */
    public String convertToInternalFormat(byte[] fileData, String fileName, DocumentType targetType) {
        String extension = getFileExtension(fileName).toLowerCase();

        try {
            switch (extension) {
//                case "pdf":
//                    return convertPdfToJson(fileData);
                case "docx":
                    return convertWordToJson(fileData);
                case "md":
                    return convertMarkdownToJson(fileData);
                case "yaml":
                case "yml":
                    return convertYamlToJson(fileData);
                case "xml":
                    return convertXmlToJson(fileData);
                default:
                    return new String(fileData);
            }
        } catch (Exception e) {
            log.error("Error converting document", e);
            throw new RuntimeException("Failed to convert document: " + e.getMessage());
        }
    }

    /**
     * Convert PDF to JSON format
     */
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

    /**
     * Convert Word document to JSON format
     */
    private String convertWordToJson(byte[] wordData) throws Exception {
        try (XWPFDocument document = new XWPFDocument(new ByteArrayInputStream(wordData))) {
            List<XWPFParagraph> paragraphs = document.getParagraphs();

            Map<String, Object> jsonDoc = new HashMap<>();
            jsonDoc.put("type", "json");
            jsonDoc.put("version", 1);

            Map<String, Object> content = new HashMap<>();
            List<Map<String, Object>> blocks = new java.util.ArrayList<>();

            int blockId = 1;
            for (XWPFParagraph para : paragraphs) {
                String text = para.getText().trim();
                if (text.isEmpty()) continue;

                Map<String, Object> block = new HashMap<>();
                block.put("id", "block-" + blockId++);
                block.put("type", "text");
                block.put("data", text);

                // Detect basic formatting
                boolean isBold = para.getRuns().stream()
                        .anyMatch(run -> run.isBold());
                boolean isItalic = para.getRuns().stream()
                        .anyMatch(run -> run.isItalic());

                block.put("formatting", Map.of("bold", isBold, "italic", isItalic));
                blocks.add(block);
            }

            content.put("blocks", blocks);
            jsonDoc.put("content", content);

            return objectMapper.writeValueAsString(jsonDoc);
        }
    }

    /**
     * Convert Markdown to JSON format
     */
    private String convertMarkdownToJson(byte[] markdownData) throws Exception {
        String markdown = new String(markdownData);
        String[] lines = markdown.split("\n");

        Map<String, Object> jsonDoc = new HashMap<>();
        jsonDoc.put("type", "json");
        jsonDoc.put("version", 1);

        Map<String, Object> content = new HashMap<>();
        List<Map<String, Object>> blocks = new java.util.ArrayList<>();

        int blockId = 1;
        StringBuilder currentParagraph = new StringBuilder();

        for (String line : lines) {
            if (line.trim().isEmpty()) {
                if (currentParagraph.length() > 0) {
                    addTextBlock(blocks, blockId++, currentParagraph.toString());
                    currentParagraph = new StringBuilder();
                }
                continue;
            }

            // Handle headers
            if (line.startsWith("#")) {
                if (currentParagraph.length() > 0) {
                    addTextBlock(blocks, blockId++, currentParagraph.toString());
                    currentParagraph = new StringBuilder();
                }

                int level = 0;
                while (level < line.length() && line.charAt(level) == '#') {
                    level++;
                }

                Map<String, Object> block = new HashMap<>();
                block.put("id", "block-" + blockId++);
                block.put("type", "heading");
                block.put("level", level);
                block.put("data", line.substring(level).trim());
                blocks.add(block);
                continue;
            }

            // Handle code blocks
            if (line.startsWith("```")) {
                if (currentParagraph.length() > 0) {
                    addTextBlock(blocks, blockId++, currentParagraph.toString());
                    currentParagraph = new StringBuilder();
                }

                String language = line.substring(3).trim();
                Map<String, Object> block = new HashMap<>();
                block.put("id", "block-" + blockId++);
                block.put("type", "code");
                block.put("language", language.isEmpty() ? "plaintext" : language);
                // Note: This is simplified - real implementation should handle multi-line code
                blocks.add(block);
                continue;
            }

            currentParagraph.append(line).append(" ");
        }

        if (currentParagraph.length() > 0) {
            addTextBlock(blocks, blockId, currentParagraph.toString());
        }

        content.put("blocks", blocks);
        jsonDoc.put("content", content);

        return objectMapper.writeValueAsString(jsonDoc);
    }

    /**
     * Convert YAML to JSON format
     */
    private String convertYamlToJson(byte[] yamlData) throws Exception {
        String yamlString = new String(yamlData);
        Object yamlObject = yaml.load(yamlString);

        Map<String, Object> jsonDoc = new HashMap<>();
        jsonDoc.put("type", "yaml");
        jsonDoc.put("version", 1);
        jsonDoc.put("rawContent", yamlString);
        jsonDoc.put("parsedContent", yamlObject);

        return objectMapper.writeValueAsString(jsonDoc);
    }

    /**
     * Convert XML to JSON format
     */
    private String convertXmlToJson(byte[] xmlData) throws Exception {
        String xmlString = new String(xmlData);

        Map<String, Object> jsonDoc = new HashMap<>();
        jsonDoc.put("type", "xml");
        jsonDoc.put("version", 1);
        jsonDoc.put("rawContent", xmlString);

        return objectMapper.writeValueAsString(jsonDoc);
    }

    /**
     * Validate document content based on type
     */
    public boolean validateDocument(String content, DocumentType type) {
        try {
            switch (type) {
                case JSON:
                    objectMapper.readTree(content);
                    return true;
                case YAML:
                    yaml.load(content);
                    return true;
                case XML:
                    // Basic XML validation
                    return content.trim().startsWith("<") && content.trim().endsWith(">");
                case MARKDOWN:
                case MERMAID:
                    return true; // Text-based, always valid
                default:
                    return false;
            }
        } catch (Exception e) {
            log.error("Validation failed for type {}", type, e);
            return false;
        }
    }

    private void addTextBlock(List<Map<String, Object>> blocks, int blockId, String text) {
        Map<String, Object> block = new HashMap<>();
        block.put("id", "block-" + blockId);
        block.put("type", "text");
        block.put("data", text.trim());
        block.put("formatting", Map.of("bold", false, "italic", false));
        blocks.add(block);
    }

    private String getFileExtension(String fileName) {
        int lastDot = fileName.lastIndexOf('.');
        return lastDot == -1 ? "" : fileName.substring(lastDot + 1);
    }
}

