package com.example.Repo;

import com.example.Model.TransactionOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransactionOrderRepository extends JpaRepository<TransactionOrder, Long> {
    @Query(value = "select * from transaction_order where buyer_id = ? or seller_id= ?",nativeQuery = true)
    List<TransactionOrder> findAllByBuyerAAndSeller(long id, long id1);

    @Query("SELECT t.category, COUNT(t) FROM TransactionOrder t GROUP BY t.category")
    List<Object[]> findCategorySales();

    @Query("SELECT t.status, COUNT(t) FROM TransactionOrder t GROUP BY t.status")
    List<Object[]> findStatusCounts();

    @Query("SELECT COUNT(t) FROM TransactionOrder t")
    Long findTotalSales();
}
