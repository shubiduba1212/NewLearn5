package com.newrun5.save_pdf.controller;

import com.newrun5.save_pdf.model.DataItem;
import com.newrun5.save_pdf.repository.DataItemRepository;
import com.newrun5.save_pdf.service.EmbeddingService;
import com.newrun5.save_pdf.service.PdfService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

@CrossOrigin(origins = "http://localhost:8080")  // 필요한 클라이언트 도메인 허용
@RestController
@RequestMapping("/api/pdf")
public class PdfController {

    @Autowired
    private PdfService pdfService;

    @Autowired
    private DataItemRepository dataItemRepository;

    @Autowired
    private EmbeddingService embeddingService;

    @PostMapping("/upload")
    public String uploadPdf(@RequestParam("file") MultipartFile file) throws IOException {
        File convFile = new File(System.getProperty("java.io.tmpdir") + "/" + file.getOriginalFilename());
        file.transferTo(convFile);

        // PDF에서 텍스트 추출
        String extractedText = pdfService.extractTextFromPdf(convFile);

        // 임베딩 생성
        String embedding = embeddingService.generateEmbedding(extractedText);

        // 데이터 저장
        DataItem dataItem = new DataItem();
        dataItem.setTitle(file.getOriginalFilename());
        dataItem.setContent(extractedText);
        dataItem.setEmbedding(embedding);
        dataItemRepository.save(dataItem);

        return "PDF 파일이 업로드 및 처리되었습니다.";
    }
}


