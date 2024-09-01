package com.mentoring.springai;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class ChatbotController
{
  @RequestMapping("/chat")
  public String chat()
  {
    return "chat.html";
  }
}