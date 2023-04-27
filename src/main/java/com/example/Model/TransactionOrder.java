package com.example.Model;

import javax.persistence.*;

@Entity
public class TransactionOrder {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long transactionId;

    @Column
    private String orderId;
    @Column(nullable = false, length = 45)
    private String itemName;
    @Column(nullable = false)
    private int itemPrice;
    @Column()
    private String photosImagePath;

    @Column()
    private String category;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "buyer_id")
    private User buyer;

    @Column(nullable = false)
    private boolean markedByBuyer;

    @Column(nullable = false)
    private boolean markedBySeller;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "seller_id")
    private User seller;

    @Column()
    private String status;

    @Column()
    private Integer acceptanceCount;

    @Column()
    private String paymentUrl;

    @Column()
    private String address;

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPaymentUrl() {
        return paymentUrl;
    }

    public void setPaymentUrl(String paymentUrl) {
        this.paymentUrl = paymentUrl;
    }

    public boolean isMarkedByBuyer() {
        return markedByBuyer;
    }

    public void setMarkedByBuyer(boolean markedByBuyer) {
        this.markedByBuyer = markedByBuyer;
    }

    public boolean isMarkedBySeller() {
        return markedBySeller;
    }

    public void setMarkedBySeller(boolean markedBySeller) {
        this.markedBySeller = markedBySeller;
    }

    public Integer getAcceptanceCount() {
        return acceptanceCount;
    }

    public void setAcceptanceCount(Integer acceptanceCount) {
        this.acceptanceCount = acceptanceCount;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public long getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(long transactionId) {
        this.transactionId = transactionId;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public int getItemPrice() {
        return itemPrice;
    }

    public void setItemPrice(int itemPrice) {
        this.itemPrice = itemPrice;
    }

    public User getBuyer() {
        return buyer;
    }

    public void setBuyer(User buyer) {
        this.buyer = buyer;
    }

    public User getSeller() {
        return seller;
    }

    public void setSeller(User seller) {
        this.seller = seller;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPhotosImagePath() {
        return photosImagePath;
    }

    public void setPhotosImagePath(String photosImagePath) {
        this.photosImagePath = photosImagePath;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}
