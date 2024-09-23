package com.newrun5.chatbot_exam.controller;

import com.newrun5.chatbot_exam.service.PDFService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

@RestController
@RequestMapping("/api")
public class FileUploadController {

    @Autowired
    private PDFService pdfService;

    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public String uploadPDF(@RequestPart("file") MultipartFile file) {
        try {
            // 파일 저장
            File convFile = new File(System.getProperty("java.io.tmpdir") + "/" + file.getOriginalFilename());
            file.transferTo(convFile);

            // PDF 처리
            pdfService.processPDF(convFile);

            return "파일 업로드 및 처리 성공";
        } catch (IOException e) {
            e.printStackTrace();
            return "파일 업로드 실패";
        }
    }
}

