package com.zomato.app.service;

import com.zomato.app.entity.Wallet;
import com.zomato.app.exception.InvalidRequestException;
import com.zomato.app.repository.WalletRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class WalletService {

    private final WalletRepository repository;

    public WalletService(WalletRepository repository) {
        this.repository = repository;
    }

    public Wallet getWallet(String email) {
        return repository.findByUserEmail(email)
                .orElseGet(() -> repository.save(new Wallet(email, 500)));
    }

    @Transactional
    public Wallet addMoney(String email, double amount) {
        Wallet wallet = getWallet(email);
        wallet.setBalance(wallet.getBalance() + amount);
        return repository.save(wallet);
    }

    @Transactional
    public Wallet debit(String email, double amount) {
        Wallet wallet = getWallet(email);
        if (wallet.getBalance() < amount) {
            throw new InvalidRequestException("Wallet balance is low");
        }
        wallet.setBalance(wallet.getBalance() - amount);
        return repository.save(wallet);
    }
}
