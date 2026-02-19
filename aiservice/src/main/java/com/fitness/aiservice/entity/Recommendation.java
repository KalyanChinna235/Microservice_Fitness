package com.fitness.aiservice.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data
@Builder
@Table(name = "recommendations")
public class Recommendation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String activityId;
    private String userId;
    private String activityType;
    private String recommendation;

    @ElementCollection
    @CollectionTable(
            name = "recommendation_improvements",
            joinColumns = @JoinColumn(name = "recommendation_id")
    )
    @Column(name = "improvement")
    private List<String> improvements;

    @ElementCollection
    @CollectionTable(
            name = "recommendation_suggestions",
            joinColumns = @JoinColumn(name = "recommendation_id")
    )
    @Column(name = "suggestion")
    private List<String> suggestions;

    @ElementCollection
    @CollectionTable(
            name = "recommendation_safety",
            joinColumns = @JoinColumn(name = "recommendation_id")
    )
    @Column(name = "safety")
    private List<String> safety;

    @CreationTimestamp
    private LocalDateTime createdAt;
}
