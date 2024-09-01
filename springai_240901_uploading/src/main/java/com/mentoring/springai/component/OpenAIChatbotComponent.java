package com.mentoring.springai.component;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.SystemMessage;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.model.function.FunctionCallback;
import org.springframework.ai.model.function.FunctionCallbackContext;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.ai.openai.OpenAiChatOptions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class OpenAIChatbotComponent
{
  @Autowired
  OpenAiChatModel chatModel;

  @Autowired
  FunctionCallbackContext functionCallbackContext;

  public Message getChatResponse(String systemMessage, String userMessage)
  {
    Message msgSys = new SystemMessage(systemMessage);
    Message msgUser = new UserMessage(userMessage);

    List<Message> list = new ArrayList<>();

    list.add(msgUser);
    list.add(msgSys);

    Prompt prompt = new Prompt(list, OpenAiChatOptions.builder().withFunction("testFunction").build());

    Message result =  chatModel.call(prompt).getResult().getOutput();
    return result;
  }
}