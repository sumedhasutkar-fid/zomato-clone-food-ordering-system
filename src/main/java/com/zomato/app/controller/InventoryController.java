package com.zomato.app.controller;

import com.zomato.app.entity.InventoryItem;
import com.zomato.app.service.InventoryService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/inventory")
@CrossOrigin(origins = "*")
public class InventoryController {

    private final InventoryService service;

    public InventoryController(InventoryService service) {
        this.service = service;
    }

    @GetMapping
    public List<InventoryItem> all() {
        return service.all();
    }

    @PostMapping
    public InventoryItem save(@RequestBody InventoryItem item) {
        return service.save(item);
    }
}
