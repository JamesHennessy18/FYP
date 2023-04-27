package com.example.Repo;

import com.example.Model.User;
import com.example.Model.Wallet;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface WalletRepository extends CrudRepository<Wallet, Long> {

    public Wallet findByUser(User user);

    @Query("SELECT w FROM Wallet w WHERE w.walletID = ?1")
    Wallet findByWalletId(Long walletId);

}
