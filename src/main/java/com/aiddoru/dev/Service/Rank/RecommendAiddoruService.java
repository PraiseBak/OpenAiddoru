package com.aiddoru.dev.Service.Rank;

import org.springframework.stereotype.Service;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class RecommendAiddoruService {
    private Map<String, List<Integer>> creators = new HashMap<>();

    public RecommendAiddoruService() {
        // Initialize the map with creator responses
        creators.put("GreatMoonAroma 대월향", List.of(3, 4, 2, 4, 5, 3, 4, 5, 4, 3, 2, 4, 2, 3, 5));
        creators.put("연다", List.of(4, 4, 4, 4, 4, 4, 4, 4, 4, 0, 0, 4, 0, 4, 4));
        creators.put("MaWang 마왕", List.of(5, 4, 4, 5, 4, 4, 5, 5, 5, 2, 2, 4, 2, 4, 3));
        creators.put("아이네 INE", List.of(4, 5, 3, 4, 3, 4, 5, 3, 4, 3, 3, 2, 3, 4, 3));
        creators.put("피아노캣 [PianoCat]", List.of(0, 4, 0, 5, 0, 4, 5, 5, 5, 4, 4, 0, 0, 4, 0));
    }

    public double calculateSimilarity(List<Integer> userResponses, List<Integer> creatorResponses) {
        double totalDifference = 0;
        for (int i = 0; i < userResponses.size(); i++) {
            totalDifference += Math.abs(userResponses.get(i) - creatorResponses.get(i));
        }
        return totalDifference; // Lower values indicate higher similarity
    }

    public String recommendCreator(List<Integer> userResponses) {
        String recommendedCreator = "";
        double minSimilarityScore = Double.MAX_VALUE;

        for (Map.Entry<String, List<Integer>> entry : creators.entrySet()) {
            String creatorName = entry.getKey();
            List<Integer> creatorResponses = entry.getValue();
            double similarityScore = calculateSimilarity(userResponses, creatorResponses);

            if (similarityScore < minSimilarityScore) {
                minSimilarityScore = similarityScore;
                recommendedCreator = creatorName;
            }
        }

        return recommendedCreator;
    }
}
