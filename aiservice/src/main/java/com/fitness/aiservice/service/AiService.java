package com.fitness.aiservice.service;

import com.fitness.aiservice.config.GeminiClient;
import com.fitness.aiservice.dto.AskRequest;
import com.fitness.aiservice.entity.ChatMessage;
import com.fitness.aiservice.repository.ChatMessageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AiService {

    private final GeminiClient geminiClient;

    private final ChatMessageRepository chatMessageRepository;

   public String askChat(AskRequest request){

      List<ChatMessage> oldData = chatMessageRepository.findByConversationIdOrderByTimestampAsc(request.getConversationId());
       StringBuilder conversationHistory = new StringBuilder();
       for(ChatMessage message : oldData){
           conversationHistory.append(message.getRole())
                   .append(": ")
                   .append(message.getContent())
                   .append("\n");
       }
       conversationHistory.append("USER: ").append(request.getPrompt()).append("\n");
       System.out.println("PROMPT LENGTH = " + conversationHistory.length());
       String  aiReponseChat = geminiClient.generateContent(conversationHistory.toString());

       chatMessageRepository.save(ChatMessage.builder()
                       .conversationId(request.getConversationId())
                       .role("USER")
                       .content(request.getPrompt())
               .build());

       chatMessageRepository.save(ChatMessage.builder()
                       .conversationId(request.getConversationId())
                       .role("AI")
                       .content(aiReponseChat)
               .build());
       return aiReponseChat;
   }



//
//    public String askGemini(String prompt) {
//        return geminiClient.generateContent(prompt);
//    }
}
