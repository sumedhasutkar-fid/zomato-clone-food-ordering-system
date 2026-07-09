package com.zomato.app.service;

import com.zomato.app.entity.CustomerAddress;
import com.zomato.app.repository.CustomerAddressRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AddressService {

    private final CustomerAddressRepository repository;

    public AddressService(CustomerAddressRepository repository) {
        this.repository = repository;
    }

    public List<CustomerAddress> getAddresses(String email) {
        return repository.findByUserEmail(email);
    }

    public CustomerAddress save(String email, CustomerAddress address) {
        address.setUserEmail(email);
        return repository.save(address);
    }
}
