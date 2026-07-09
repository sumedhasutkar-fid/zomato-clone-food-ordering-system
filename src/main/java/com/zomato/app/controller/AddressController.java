package com.zomato.app.controller;

import com.zomato.app.entity.CustomerAddress;
import com.zomato.app.service.AddressService;
import com.zomato.app.util.AuthUtil;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/addresses")
@CrossOrigin(origins = "*")
public class AddressController {

    private final AddressService service;
    private final AuthUtil authUtil;

    public AddressController(AddressService service, AuthUtil authUtil) {
        this.service = service;
        this.authUtil = authUtil;
    }

    @GetMapping
    public List<CustomerAddress> getAddresses() {
        return service.getAddresses(authUtil.currentUserEmail());
    }

    @PostMapping
    public CustomerAddress save(@RequestBody CustomerAddress address) {
        return service.save(authUtil.currentUserEmail(), address);
    }
}
