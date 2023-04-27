package com.example.Payment;
import com.example.Model.Item;
import com.example.Model.User;

import javax.persistence.*;


@Entity
@Table(name = "paypalOrders")
public class PayPalOrder {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long orderId;
    private double price;
    private String currency;
    private String method;
    private String intent;
    private String description;

    private String status;

    @Column(nullable = false)
    private String category;

    @Column(nullable = false)
    private String address;

    @Column(nullable = false)
    private String photosImagePath;

    @Column()
    private Integer acceptanceCount;
    @Column()
    private String orderStatus;

    @Column()
    private String itemName;
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

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "itemId")
    private Item item;

    public long getOrderId() {
        return orderId;
    }

    public void setOrderId(long orderId) {
        this.orderId = orderId;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getIntent() {
        return intent;
    }

    public void setIntent(String intent) {
        this.intent = intent;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Item getItem() {
        return item;
    }

    public void setItem(Item item) {
        this.item = item;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public User getBuyer() {
        return buyer;
    }

    public void setBuyer(User buyer) {
        this.buyer = buyer;
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

    public User getSeller() {
        return seller;
    }

    public void setSeller(User seller) {
        this.seller = seller;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getPhotosImagePath() {
        return photosImagePath;
    }

    public void setPhotosImagePath(String photosImagePath) {
        this.photosImagePath = photosImagePath;
    }

    public Integer getAcceptanceCount() {
        return acceptanceCount;
    }

    public void setAcceptanceCount(Integer acceptanceCount) {
        this.acceptanceCount = acceptanceCount;
    }
}
