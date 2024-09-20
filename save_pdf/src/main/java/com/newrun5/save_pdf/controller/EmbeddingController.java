package com.newrun5.save_pdf.controller;

import com.newrun5.save_pdf.service.EmbeddingService;
import com.newrun5.save_pdf.service.PDFProcessingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;

@CrossOrigin(origins = "http://localhost:8080")  // 필요한 클라이언트 도메인 허용
@RestController
@RequestMapping("/api/embedding")
public class EmbeddingController {

    @Autowired
    private EmbeddingService embeddingService;

    @Autowired
    private PDFProcessingService pdfProcessingService;

    @PostMapping("/upload-pdf")
    public ResponseEntity<?> uploadPdf(@RequestParam("pdfFile") MultipartFile file) {
        if (file.isEmpty()) {
            return ResponseEntity.badRequest().body("File is empty");
        }

        // Save the PDF file temporarily
        File tempFile;
        try {
            tempFile = File.createTempFile("uploaded-", ".pdf");
            file.transferTo(tempFile);
        } catch (IOException e) {
            return ResponseEntity.status(500).body("File saving failed");
        }

        // Extract text and images from the PDF
        List<String> texts = pdfProcessingService.extractTextFromPdf(tempFile.getAbsolutePath());
        List<String> images = pdfProcessingService.extractImagesFromPdf(tempFile.getAbsolutePath());

        // Process extracted content and save to MongoDB
        for (String text : texts) {
            float[] textEmbedding = embeddingService.getTextEmbedding(text);
            for (String image : images) {
                float[] imageEmbedding = embeddingService.getImageEmbedding(image);
                embeddingService.saveDataItem("Sample Title", text, textEmbedding, imageEmbedding);
            }
        }

        return ResponseEntity.ok().body("{ \"success\": true, \"message\": \"PDF processed and saved to MongoDB\" }");
    }
}


