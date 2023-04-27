package com.example.Repo;

import com.example.Model.TransactionOrder;
import com.example.Payment.PayPalOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PayPalOrderRepository extends JpaRepository<PayPalOrder, Long> {

    @Query(value = "select * from paypal_orders where buyer_id = ? or seller_id= ?",nativeQuery = true)
    List<PayPalOrder> findAllByBuyerAAndSeller(long id, long id1);
    @Query("SELECT p.category, COUNT(p) FROM PayPalOrder p GROUP BY p.category")
    List<Object[]> findCategorySalesByPayPalOrder();
    @Query("SELECT COUNT(p) FROM PayPalOrder p")
    Long findTotalSales();
    @Query("SELECT t.orderStatus, COUNT(t) FROM PayPalOrder t GROUP BY t.orderStatus")
    List<Object[]> findStatusCounts();

}
