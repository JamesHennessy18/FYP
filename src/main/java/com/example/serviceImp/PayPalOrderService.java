package com.example.serviceImp;

import com.example.Model.Item;
import com.example.Model.TransactionOrder;
import com.example.Model.User;
import com.example.Payment.PayPalOrder;
import com.example.Repo.PayPalOrderRepository;
import com.example.Repo.UserRepository;
import com.example.serviceImp.ItemServiceImp;
import com.paypal.api.payments.Payment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PayPalOrderService {
    @Autowired
    private PayPalOrderRepository payPalOrderRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ItemServiceImp itemService;

    public PayPalOrder saveOrder(Payment payment, Long itemId, long buyerId, long sellerId) {

        PayPalOrder order = new PayPalOrder();
        Item item = itemService.findById(itemId);
        User buyer = userRepository.findById(buyerId).orElse(null);
        User seller = userRepository.findById(sellerId).orElse(null);

        order.setPrice(Double.valueOf(payment.getTransactions().get(0).getAmount().getTotal()));
        order.setCurrency(payment.getTransactions().get(0).getAmount().getCurrency());
        order.setMethod(payment.getPayer().getPaymentMethod());
        order.setIntent(payment.getIntent());
        order.setDescription(payment.getTransactions().get(0).getDescription());
        order.setStatus(payment.getState());
        order.setItem(itemService.findById(itemId));
        order.setCategory(item.getCategory());
        order.setAddress(buyer.getAddress1() + ", " + buyer.getAddress2() + ", " + buyer.getCountry());
        order.setPhotosImagePath(item.getPhotosImagePaths()[0]);
        order.setAcceptanceCount(0);
        order.setBuyer(buyer);
        order.setSeller(seller);
        order.setItemName(item.getItemName());
        order.setMarkedByBuyer(false);
        order.setMarkedBySeller(false);
        order.setOrderStatus("Processing");

        return payPalOrderRepository.save(order);
    }

    public List<PayPalOrder> getPayPalOrders(long id) {
        return payPalOrderRepository.findAllByBuyerAAndSeller(id,id);
    }

    public void updateOrderStatus(long orderId, long userId) {
        PayPalOrder order = payPalOrderRepository.findById(orderId).orElse(null);
        User user = userRepository.findById(userId).orElse(null);


        if (user.getId() == order.getBuyer().getId()) {
            order.setOrderStatus("Accepted & Confirmed By Buyer");
            order.setMarkedByBuyer(true);
            order.setAcceptanceCount(order.getAcceptanceCount() + 1);
        } else if (user.getId() == order.getSeller().getId()) {
            order.setAcceptanceCount(order.getAcceptanceCount() + 1);
            order.setOrderStatus("Accepted & Confirmed By Seller");
            order.setMarkedBySeller(true);
        }

        if (order.isMarkedByBuyer() && order.isMarkedBySeller()) {
            order.setOrderStatus("Accepted & Confirmed By Both Seller & Buyer");
        }

        payPalOrderRepository.save(order);
    }
}

