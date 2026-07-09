package com.zomato.app.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
public class SwaggerController {

    @GetMapping("/swagger-json")
    public Map<String, Object> docs() {
        return Map.of(
                "title", "Zomato Demo Practice API",
                "version", "1.0",
                "description", "Swagger-style endpoint summary for practice project",
                "endpoints", List.of(
                        "POST /auth/register",
                        "POST /auth/login",
                        "POST /auth/refresh",
                        "GET /restaurants",
                        "GET /categories",
                        "GET /foods/search?restaurantId=1&query=&page=0&size=12&sortBy=name",
                        "POST/PUT/DELETE /foods - ADMIN only",
                        "GET/POST/DELETE /cart - CUSTOMER",
                        "GET/POST /addresses - CUSTOMER",
                        "GET/POST /orders - CUSTOMER",
                        "POST /payments/mock/{orderId}",
                        "GET/POST /reviews"
                )
        );
    }
}
