package com.fitness.aiservice.controller;

import com.fitness.aiservice.model.Recommendation;
import com.fitness.aiservice.repository.RecommendationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/recommendations")
@RequiredArgsConstructor
public class RecommendationController {

    private final RecommendationRepository recommendationRepository;

    @GetMapping("/activity/{activityId}")
    public ResponseEntity<Recommendation> getByActivityId(
            @PathVariable String activityId
    ) {
        return recommendationRepository.findByActivityId(activityId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Recommendation>> getByUserId(
            @PathVariable String userId
    ) {
        return ResponseEntity.ok(
                recommendationRepository.findByUserId(userId)
        );
    }
}
