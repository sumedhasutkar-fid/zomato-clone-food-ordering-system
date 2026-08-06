package com.zomato.app.controller;

import com.zomato.app.entity.FavoriteFood;
import com.zomato.app.service.FavoriteService;
import com.zomato.app.util.AuthUtil;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/favorites")
@CrossOrigin(origins = "*")
public class FavoriteController {

    private final FavoriteService service;
    private final AuthUtil authUtil;

    public FavoriteController(FavoriteService service, AuthUtil authUtil) {
        this.service = service;
        this.authUtil = authUtil;
    }

    @GetMapping
    public List<FavoriteFood> list() {
        return service.list(authUtil.currentUserEmail());
    }

    @PostMapping("/{foodId}")
    public FavoriteFood toggle(@PathVariable Long foodId) {
        return service.toggle(authUtil.currentUserEmail(), foodId);
    }
}
