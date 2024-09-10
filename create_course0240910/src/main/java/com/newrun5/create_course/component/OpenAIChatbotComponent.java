package com.newrun5.create_course.component;

import com.newrun5.create_course.CurriculumCreationFunction;
import org.springframework.ai.chat.messages.SystemMessage;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.ai.openai.OpenAiChatOptions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

@Component
public class OpenAIChatbotComponent {

    @Autowired
    private OpenAiChatModel chatModel;
    @Autowired
    private CurriculumCreationFunction curriculumCreationFunction;

    public String getChatResponse(String systemMessage, String userMessage) {
        // SystemMessage와 UserMessage 객체를 생성합니다.
        SystemMessage msgSys = new SystemMessage(systemMessage);
        UserMessage msgUser = new UserMessage(userMessage);

        // 메시지 리스트를 생성합니다.
        List<Object> list = new ArrayList<>();
        list.add(msgUser);
        list.add(msgSys);

        // Prompt를 생성합니다.
        Prompt prompt = new Prompt(list.toString(), OpenAiChatOptions.builder()
                .withFunction("curriculumCreationFunction").build());

        // 모델 호출 및 결과 처리
        String result = chatModel.call(prompt).getResult().getOutput().toString();

        // JSON 파싱을 통해 textContent 추출
        JSONObject jsonResult = new JSONObject(result);
        String textContent = jsonResult.optString("textContent", "No text content available");

        return textContent;
    }

//    public void configureFunctions() {
//        chatModel.registerFunction("curriculumCreationFunction",
//                (topic, reason, difficulty) -> curriculumCreationFunction.createCurriculum(topic, reason, difficulty));
//    }
//
//    public String getChatResponse(String topic, String reason, String difficulty) {
//        return chatModel.call("curriculumCreationFunction", topic, reason, difficulty);
//    }
}

