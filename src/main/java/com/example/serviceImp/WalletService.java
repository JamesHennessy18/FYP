package com.example.serviceImp;

import com.example.Model.Item;
import com.example.Model.User;
import com.example.Model.Wallet;
import com.example.Repo.WalletRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WalletService {
    @Autowired
    WalletRepository walletRepository;

    public void delete(Long walletId) {
        walletRepository.deleteById(walletId);
    }


}
