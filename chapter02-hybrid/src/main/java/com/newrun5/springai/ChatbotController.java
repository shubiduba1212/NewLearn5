package com.newrun5.springai;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class ChatbotController
{
    @RequestMapping("/chat")
    public String chat()
    {
        return "hybrid.html";
    }

    @RequestMapping("/input")
    public String input()
    {
        return "input_article.html";
    }
}
