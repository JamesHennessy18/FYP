package com.example.serviceImp;

import com.example.Model.Bid;
import com.example.Model.User;
import com.example.Repo.BidRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BidServiceImp {

    @Autowired
    private BidRepository bidRepository;

    public List<Bid> findBidsForSeller(User seller) {

        return bidRepository.findBySeller(seller);
    }
    public List<Bid> findBidsByBuyer(User buyer) {

        return bidRepository.findByBuyer(buyer);
    }
    public void acceptBid(Long bidId) {
        Bid bid = bidRepository.findById(bidId).get();
        bid.setStatus("Accepted");
        bidRepository.save(bid);
    }

    public void declineBid(Long bidId) {
        Bid bid = bidRepository.findById(bidId).get();
        bid.setStatus("Declined");
        bidRepository.save(bid);
    }

}
