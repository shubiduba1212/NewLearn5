package com.newrun5.create_course.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/chatbot")
public class ChatbotController {

    // HTML 파일을 반환하는 메서드
    @GetMapping
    public String getChatbotPage() {
        return "chat.html";
    }
}

