package com.example.Model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.ArrayList;
import java.util.Date;

@JsonIgnoreProperties(ignoreUnknown = true)
public class InvoiceResponse {

    public int id;
    public String status;
    public String title;
    public boolean do_not_convert;
    public String orderable_type;
    public int orderable_id;
    public String price_currency;
    public String price_amount;
    public boolean lightning_network;
    public String receive_currency;
    public String receive_amount;
    public Date created_at;
    public String order_id;
    public String payment_url;
    public String underpaid_amount;
    public String overpaid_amount;
    public boolean is_refundable;
    public ArrayList<Object> refunds;
    public ArrayList<Object> voids;
    public ArrayList<Object> fees;
    public String token;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public boolean isDo_not_convert() {
        return do_not_convert;
    }

    public void setDo_not_convert(boolean do_not_convert) {
        this.do_not_convert = do_not_convert;
    }

    public String getOrderable_type() {
        return orderable_type;
    }

    public void setOrderable_type(String orderable_type) {
        this.orderable_type = orderable_type;
    }

    public int getOrderable_id() {
        return orderable_id;
    }

    public void setOrderable_id(int orderable_id) {
        this.orderable_id = orderable_id;
    }

    public String getPrice_currency() {
        return price_currency;
    }

    public void setPrice_currency(String price_currency) {
        this.price_currency = price_currency;
    }

    public String getPrice_amount() {
        return price_amount;
    }

    public void setPrice_amount(String price_amount) {
        this.price_amount = price_amount;
    }

    public boolean isLightning_network() {
        return lightning_network;
    }

    public void setLightning_network(boolean lightning_network) {
        this.lightning_network = lightning_network;
    }

    public String getReceive_currency() {
        return receive_currency;
    }

    public void setReceive_currency(String receive_currency) {
        this.receive_currency = receive_currency;
    }

    public String getReceive_amount() {
        return receive_amount;
    }

    public void setReceive_amount(String receive_amount) {
        this.receive_amount = receive_amount;
    }

    public Date getCreated_at() {
        return created_at;
    }

    public void setCreated_at(Date created_at) {
        this.created_at = created_at;
    }

    public String getOrder_id() {
        return order_id;
    }

    public void setOrder_id(String order_id) {
        this.order_id = order_id;
    }

    public String getPayment_url() {
        return payment_url;
    }

    public void setPayment_url(String payment_url) {
        this.payment_url = payment_url;
    }

    public String getUnderpaid_amount() {
        return underpaid_amount;
    }

    public void setUnderpaid_amount(String underpaid_amount) {
        this.underpaid_amount = underpaid_amount;
    }

    public String getOverpaid_amount() {
        return overpaid_amount;
    }

    public void setOverpaid_amount(String overpaid_amount) {
        this.overpaid_amount = overpaid_amount;
    }

    public boolean isIs_refundable() {
        return is_refundable;
    }

    public void setIs_refundable(boolean is_refundable) {
        this.is_refundable = is_refundable;
    }

    public ArrayList<Object> getRefunds() {
        return refunds;
    }

    public void setRefunds(ArrayList<Object> refunds) {
        this.refunds = refunds;
    }

    public ArrayList<Object> getVoids() {
        return voids;
    }

    public void setVoids(ArrayList<Object> voids) {
        this.voids = voids;
    }

    public ArrayList<Object> getFees() {
        return fees;
    }

    public void setFees(ArrayList<Object> fees) {
        this.fees = fees;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

}