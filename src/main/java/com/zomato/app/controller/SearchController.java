package com.zomato.app.controller;

import com.zomato.app.entity.Food;
import com.zomato.app.service.ElasticSearchService;
import com.zomato.app.service.RecommendationService;
import com.zomato.app.service.SearchSuggestionService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/search")
@CrossOrigin(origins = "*")
public class SearchController {

    private final SearchSuggestionService suggestionService;
    private final ElasticSearchService elasticSearchService;
    private final RecommendationService recommendationService;

    public SearchController(SearchSuggestionService suggestionService,
                            ElasticSearchService elasticSearchService,
                            RecommendationService recommendationService) {
        this.suggestionService = suggestionService;
        this.elasticSearchService = elasticSearchService;
        this.recommendationService = recommendationService;
    }

    @GetMapping("/suggestions")
    public List<String> suggestions(@RequestParam(defaultValue = "") String query) {
        return suggestionService.suggest(query);
    }

    @GetMapping("/elastic")
    public List<Food> elastic(@RequestParam(defaultValue = "") String query) {
        return elasticSearchService.search(query);
    }

    @GetMapping("/recommendations")
    public List<Food> recommendations(@RequestParam(defaultValue = "1") Long restaurantId) {
        return recommendationService.recommend(restaurantId);
    }
}
