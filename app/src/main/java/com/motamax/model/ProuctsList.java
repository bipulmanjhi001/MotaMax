package com.motamax.model;

public class ProuctsList {
     private String id,date,reference,customer_id,name,mobile;
     private String gstin,address,state,g_amount,total_amount;

    public ProuctsList(String id, String date, String reference, String customer_id,
                       String name, String mobile, String gstin, String address,
                       String state, String g_amount, String total_amount) {
        this.id = id;
        this.date = date;
        this.reference = reference;
        this.customer_id = customer_id;
        this.name = name;
        this.mobile = mobile;
        this.gstin = gstin;
        this.address = address;
        this.state = state;
        this.g_amount = g_amount;
        this.total_amount = total_amount;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public String getCustomer_id() {
        return customer_id;
    }

    public void setCustomer_id(String customer_id) {
        this.customer_id = customer_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getGstin() {
        return gstin;
    }

    public void setGstin(String gstin) {
        this.gstin = gstin;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getG_amount() {
        return g_amount;
    }

    public void setG_amount(String g_amount) {
        this.g_amount = g_amount;
    }

    public String getTotal_amount() {
        return total_amount;
    }

    public void setTotal_amount(String total_amount) {
        this.total_amount = total_amount;
    }
}
