package com.fitness.activity.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;


import java.time.LocalDateTime;
import java.util.Map;

@Entity
@Table(name = "activities")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Activity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long userId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ActivityType type;

    @Column(nullable = false)
    private Integer duration; // in minutes

    private Integer caloriesBurned;

    private LocalDateTime startTime;

    /**
     * Stores additional metrics as JSON in MySQL
     * Requires MySQL 5.7+ or 8+
     */
    @Convert(converter = MapToJsonConverter.class)
    @Column(columnDefinition = "json")
    private Map<String, Object> additionalMetrics;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;
}
