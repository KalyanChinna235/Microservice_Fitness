package com.fitness.aiservice.service;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.fitness.aiservice.config.GeminiClient;
import com.fitness.aiservice.dto.AiRecommendationResponse;
import com.fitness.aiservice.entity.Activity;
import com.fitness.aiservice.entity.Recommendation;
import com.fitness.aiservice.repository.RecommendationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class ActivityAiService {

    private final GeminiClient geminiClient;
    private final RecommendationRepository recommendationRepository;

    public void generateRecommendation(Activity activity) {
        String prompt = createPropmtForActivity(activity);

        String aiResponse = geminiClient.generateContent(prompt);

        ObjectMapper mapper = new ObjectMapper();
        AiRecommendationResponse aiData;

        try {
            aiData = mapper.readValue(aiResponse, AiRecommendationResponse.class);
        } catch (Exception e) {
            throw new RuntimeException("Failed to parse AI response", e);
        }

        Recommendation recommendation = Recommendation.builder()
                .activityId(activity.getId())
                .userId(activity.getUserId())
                .activityType(activity.getType().name())
                .recommendation(aiData.getRecommendation())
                .improvements(aiData.getImprovements())
                .suggestions(aiData.getSuggestions())
                .safety(aiData.getSafety())
                .build();

        recommendationRepository.save(recommendation);
    }

//    public void generateAndSaveRecommendation(Activity activity) {
//        String aiResponse = generateRecommendation(activity);
//
//        Recommendation recommendation = Recommendation.builder()
//                .activityId(activity.getId())
//                .userId(activity.getUserId())
//                .activityType(activity.getType().name())
//                .recommendation(aiResponse)
//                .build();
//
//        recommendationRepository.save(recommendation);
//
//        log.info("Recommendation saved for activity {}", activity.getId());
//    }

    private String createPropmtForActivity(Activity activity) {
        return """
                You are a fitness AI coach.

                Analyze the following activity and give structured recommendations.

                Activity Type: %s
                Duration: %s
                Calories Burned: %s

                Respond ONLY in JSON format like this:

                {
                  "recommendation": "short summary",
                  "improvements": ["point1","point2"],
                  "suggestions": ["point1","point2"],
                  "safety": ["point1","point2"]
                }
                """.formatted(
                activity.getType(),
                activity.getDuration(),
                activity.getCaloriesBurned()
        );
    }
}
