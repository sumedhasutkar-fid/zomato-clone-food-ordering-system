package com.zomato.app.controller;

import com.zomato.app.entity.Wallet;
import com.zomato.app.service.WalletService;
import com.zomato.app.util.AuthUtil;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/wallet")
@CrossOrigin(origins = "*")
public class WalletController {

    private final WalletService service;
    private final AuthUtil authUtil;

    public WalletController(WalletService service, AuthUtil authUtil) {
        this.service = service;
        this.authUtil = authUtil;
    }

    @GetMapping
    public Wallet wallet() {
        return service.getWallet(authUtil.currentUserEmail());
    }

    @PostMapping("/add")
    public Wallet add(@RequestBody Map<String, Object> request) {
        return service.addMoney(authUtil.currentUserEmail(), Double.parseDouble(request.get("amount").toString()));
    }
}
