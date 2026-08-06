package com.zomato.app.controller;

import com.zomato.app.service.RedisCacheService;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/cache")
@CrossOrigin(origins = "*")
public class RedisCacheController {

    private final RedisCacheService service;

    public RedisCacheController(RedisCacheService service) {
        this.service = service;
    }

    @GetMapping
    public Map<String, String> all() {
        return service.all();
    }

    @PostMapping
    public Map<String, String> put(@RequestBody Map<String, String> request) {
        return service.put(request.get("key"), request.get("value"));
    }
}
