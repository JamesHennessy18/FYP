package com.example.Repo;

import com.example.Model.SellerRating;
import com.example.Model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SellerRatingRepository extends JpaRepository<SellerRating, Long> {
    Optional<SellerRating> findBySellerAndBuyer(User seller, User buyer);

    List<SellerRating> findBySeller(User seller);
}
