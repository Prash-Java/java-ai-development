package com.project.SpringAiCode;


import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AiController {
    private final OpenAiChatModel chatModel;

    public AiController(OpenAiChatModel chatModel) {
        this.chatModel = chatModel;
    }

    @GetMapping("/api/{message}")
    public String getResponseFromPrompt(@PathVariable String message){
        return chatModel.call(message);
//        ChatModel is abstracted way to call open AI API, message is our prompt/question and it returns answer of the provided prompts.
    }
}
