package com.example.Model;

import javax.persistence.*;

@Entity
@Table(name = "wallet")
public class Wallet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long walletID;
    @Column(nullable = false, length = 100)
    private String paypalEmail;

    @Column(nullable = true, length = 45)
    private String cryptoCurrency;

    @Column(nullable = true, length = 200)
    private String cryptoAddress;


    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id")
    private User user;

    public long getWalletID() {
        return walletID;
    }

    public void setWalletID(long walletID) {
        this.walletID = walletID;
    }

    public String getCryptoCurrency() {
        return cryptoCurrency;
    }

    public void setCryptoCurrency(String cryptoCurrency) {
        this.cryptoCurrency = cryptoCurrency;
    }

    public String getPaypalEmail() {
        return paypalEmail;
    }

    public void setPaypalEmail(String paypalEmail) {
        this.paypalEmail = paypalEmail;
    }

    public String getCryptoAddress() {
        return cryptoAddress;
    }

    public void setCryptoAddress(String cryptoAddress) {
        this.cryptoAddress = cryptoAddress;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
