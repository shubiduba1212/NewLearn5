package com.newrun5.chatbot_exam.controller;

import com.newrun5.chatbot_exam.service.ChatService;
import com.newrun5.chatbot_exam.service.OpenAIService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
public class ChatController {

    @Autowired
    private ChatService chatService;

    @MutationMapping
    public List<String> chat(String message, int count) {
        return chatService.handleMessage(message, count);
    }
}

