package com.example.serviceImp;

import com.example.Model.*;
import com.example.Repo.TransactionOrderRepository;
import com.example.Repo.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TransactionServiceImpl {

    @Autowired
    TransactionOrderRepository transactionOrderRepository;

    @Autowired
    UserRepository userRepository;

    public List<TransactionOrder> getTransactions(long id) {
        System.out.println(id);
        return transactionOrderRepository.findAllByBuyerAAndSeller(id,id);
    }

    public void saveTransaction(InvoiceRequest invoiceRequest, InvoiceResponse invoiceResponse, Item item) {

        TransactionOrder transactionOrder= new TransactionOrder();
        System.out.println(invoiceRequest.getBuyerId());
        System.out.println(invoiceRequest.getSellerId());
        User user= userRepository.findById(invoiceRequest.getBuyerId()).get();
        User seller= userRepository.findById(invoiceRequest.getSellerId()).get();
        transactionOrder.setBuyer(user);
        transactionOrder.setPaymentUrl(invoiceResponse.getPayment_url());
        transactionOrder.setAcceptanceCount(0);
        transactionOrder.setSeller(seller);
        transactionOrder.setPhotosImagePath(item.getPhotosImagePaths()[0]);
        transactionOrder.setCategory(item.getCategory());
        transactionOrder.setMarkedByBuyer(false);
        transactionOrder.setMarkedBySeller(false);
        transactionOrder.setAddress(user.getAddress1() + ", " + user.getAddress2() + ", " + user.getCountry());
        transactionOrder.setItemName(item.getItemName());
        transactionOrder.setItemPrice(item.getItemPrice());
        transactionOrder.setStatus("Processing");
        transactionOrder.setOrderId(String.valueOf(invoiceResponse.getId()));

        transactionOrderRepository.save(transactionOrder);
    }

    public void updateTransactionStatus(long transactionId, long userId) {

        TransactionOrder transactionOrder= transactionOrderRepository.findById(transactionId).get();
        User user= userRepository.findById(userId).get();


        if(user.getId() == (transactionOrder.getBuyer().getId())) {
            transactionOrder.setStatus("Accepted & Confirmed By Buyer");
            transactionOrder.setMarkedByBuyer(true);
            transactionOrder.setAcceptanceCount(transactionOrder.getAcceptanceCount() + 1);
        }

        else if(user.getId() == (transactionOrder.getSeller().getId())) {
            transactionOrder.setAcceptanceCount(transactionOrder.getAcceptanceCount() + 1);
            transactionOrder.setStatus("Accepted & Confirmed By Seller");
            transactionOrder.setMarkedBySeller(true);
        }
        if(transactionOrder.isMarkedByBuyer() && transactionOrder.isMarkedBySeller() ){
            transactionOrder.setStatus("Accepted & Confirmed By Both Seller & Buyer");
        }


        transactionOrderRepository.save(transactionOrder);
    }

}
