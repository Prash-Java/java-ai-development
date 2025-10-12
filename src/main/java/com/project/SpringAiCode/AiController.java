package com.project.SpringAiCode;


import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AiController {
    private final ChatClient chatClient;

//    Use this injection when it is certain that we are using openAI LLM only.
//    public AiController(OpenAiChatModel chatModel) {
//        this.chatClient = ChatClient.create(chatModel);
//    }

//    Use this if we want to make use of any LLM's like OpenAI, Gemini, Llama etc. It is more generic.
    public AiController(ChatClient.Builder chatClientBuilder){
        this.chatClient = chatClientBuilder.build();
    }

    @GetMapping("/api/{message}")
    public ResponseEntity<String> getResponseFromPrompt(@PathVariable String message){
        String response = "";
        ChatResponse chatResponse = chatClient
                .prompt(message)
                .call()
                .chatResponse();
        if (chatResponse != null){
            System.out.println(chatResponse.getMetadata().getModel());
            response = chatResponse
                    .getResult()
                    .getOutput()
                    .getText();
            return ResponseEntity.ok(response);
        }
        return ResponseEntity.ok("No Response To Send...");
//        return ResponseEntity.ok(chatClient.prompt(message).call().content());
//        return chatModel.call(message);
//        ChatModel is abstracted way to call open AI API, message is our prompt/question and it returns answer of the provided prompts.
    }
}
