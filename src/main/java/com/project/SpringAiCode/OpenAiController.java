package com.project.SpringAiCode;


import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.memory.MessageWindowChatMemory;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
public class OpenAiController {
    private final ChatClient chatClient;
    ChatMemory chatMemory = MessageWindowChatMemory.builder().build();

    // Using Advisor, we can have memory advisor and LLM will remember the earlier chats and context.
    public OpenAiController(ChatClient.Builder chatClientBuilder) {
        this.chatClient = chatClientBuilder
                .defaultAdvisors(MessageChatMemoryAdvisor
                        .builder(chatMemory)
                        .build())
                .build();
    }

    // Prompts will be static and like this => localhost:8080/api/what is java? This will be done by Https.Get microservice.
    @GetMapping("/api/{message}")
    public ResponseEntity<String> getResponseFromPrompt(@PathVariable String message) {
        String response = "";
        ChatResponse chatResponse = chatClient
                .prompt(message)
                .call()
                .chatResponse();
        if (chatResponse != null) {
            System.out.println(chatResponse.getMetadata().getModel());
            response = chatResponse
                    .getResult()
                    .getOutput()
                    .getText();
            return ResponseEntity.ok(response);
        }
        return ResponseEntity.ok("No Response To Send...");
    }

//    Prompts will be Parametric/Dynamic like this => localhost:8080/api/recommend?programmingLanguage=Java&bookLanguage=English served by Https.POST microservice.
    @PostMapping("/api/recommend")
    public String recommendBooks(@RequestParam String programmingLanguage, @RequestParam String bookLanguage){
        String template = """
                Recommend me a {programmingLanguage} book written in {bookLanguage} language.
                """;
        PromptTemplate promptTemplate = new PromptTemplate(template);
        Prompt prompt = promptTemplate.create(Map.of("programmingLanguage", programmingLanguage, "bookLanguage", bookLanguage));
        return chatClient
                .prompt(prompt)
                .call()
                .content();
    }
}






//    Use this injection when it is certain that we are using openAI LLM only.
//    public OpenAiController(OpenAiChatModel chatModel) {
//        this.chatClient = ChatClient.create(chatModel);
//    }

//    Use this if we want to make use of any LLM's like OpenAI, Gemini, Llama etc. It is more generic. This chat client would not remember the earlier chat via LLM,
//    public OpenAiController(ChatClient.Builder chatClientBuilder){
//        this.chatClient = chatClientBuilder.build();
//    }

    // Using Advisor, we can have memory advisor and LLM will remember the earlier chats and context.
//    public OpenAiController(ChatClient.Builder chatClientBuilder) {
//        this.chatClient = chatClientBuilder
//                .defaultAdvisors(new InMemoryChatMemoryRepository(new InMemoryChatMemoryRepository()))
//                .build();
//    }

    //        return ResponseEntity.ok(chatClient.prompt(message).call().content());
//        return chatModel.call(message);
//        ChatModel is abstracted way to call open AI API, message is our prompt/question and it returns answer of the provided prompts.

