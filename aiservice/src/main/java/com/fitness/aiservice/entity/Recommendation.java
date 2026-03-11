package com.fitness.aiservice.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "recommendations")
public class Recommendation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long activityId;
    private Long userId;
    private String activityType;
    @Column(columnDefinition = "TEXT")
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
