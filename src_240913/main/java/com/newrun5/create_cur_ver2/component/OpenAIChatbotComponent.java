package com.newrun5.create_cur_ver2.component;

import com.newrun5.create_cur_ver2.service.MongoSearchService;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.ai.openai.OpenAiChatOptions;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public class OpenAIChatbotComponent {

    private final OpenAiChatModel chatModel;

    @Autowired
    public OpenAIChatbotComponent(OpenAiChatModel chatModel) {
        this.chatModel = chatModel;
    }

    // OpenAI API 호출을 담당하는 메서드
    public String callOpenAiChat(String promptText) {
        // OpenAI API 호출을 위한 프롬프트 생성
        Prompt prompt = new Prompt(promptText, OpenAiChatOptions.builder().build());

        // OpenAI 모델 호출 및 결과 처리
        var response = chatModel.call(prompt);

        // OpenAI 응답에서 텍스트 추출
        var result = response.getResult().getOutput().getContent();

        return result;
    }
}
