package com.example.Repo;

import com.example.Model.Bid;
import com.example.Model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BidRepository extends JpaRepository<Bid, Long> {
    List<Bid> findBySellerId(Long sellerId);
    List<Bid> findBySeller(User seller);
    List<Bid> findByBuyer(User buyer);

}
