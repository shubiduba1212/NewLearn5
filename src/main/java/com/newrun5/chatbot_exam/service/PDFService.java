package com.newrun5.chatbot_exam.service;

import com.newrun5.chatbot_exam.model.HistoryData;
import com.newrun5.chatbot_exam.repository.HistoryDataRepository;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@Service
public class PDFService {

    private List<Float> generateEmbedding(String content) {
        // 임베딩 생성 로직 (예시로 간단히 처리)
        return Arrays.asList(0.1f, 0.2f, 0.3f); // 실제 임베딩 생성 로직을 구현해주세요.
    }
}

