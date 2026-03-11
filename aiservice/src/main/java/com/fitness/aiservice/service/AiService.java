package com.fitness.aiservice.service;

import com.fitness.aiservice.config.GeminiClient;
import com.fitness.aiservice.dto.AskRequest;
import com.fitness.aiservice.entity.ChatMessage;
import com.fitness.aiservice.entity.Conversation;
import com.fitness.aiservice.exception.ResourceNotFoundException;
import com.fitness.aiservice.repository.ChatMessageRepository;
import com.fitness.aiservice.repository.ConversationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AiService {

    private final GeminiClient geminiClient;

    private final ChatMessageRepository chatMessageRepository;

    private final ConversationRepository conversationRepository;


    public String askChat(AskRequest request) {

        Conversation conversation;
        // Create new conversation if not exists
        if (request.getConversationId() != null && request.getConversationId() == 0) {
            conversation = Conversation.builder()
                    .userId(1L) // In real application, get user ID from auth context
                    .build();
            conversation = conversationRepository.save(conversation);
        } else {
            conversation = conversationRepository.findById(request.getConversationId())
                    .orElseThrow(() -> new ResourceNotFoundException("Conversation not found"));
        }
        // Load previous messages
        List<ChatMessage> oldData = chatMessageRepository.findByConversationIdOrderByCreatedAtAsc(conversation.getId());
        StringBuilder conversationHistory = new StringBuilder();
        for (ChatMessage message : oldData) {
            conversationHistory.append(message.getRole())
                    .append(": ")
                    .append(message.getChat())
                    .append("\n");
        }
        conversationHistory.append("USER: ").append(request.getPrompt()).append("\n");
        String aiReponseChat = geminiClient.generateContent(conversationHistory.toString());

        chatMessageRepository.save(ChatMessage.builder()
                .conversation(conversation)
                .role("USER")
                .chat(request.getPrompt())
                .build());

        chatMessageRepository.save(ChatMessage.builder()
                .conversation(conversation)
                .role("AI")
                .chat(aiReponseChat)
                .build());
        return aiReponseChat;
    }

}
