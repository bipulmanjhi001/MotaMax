package com.motamax.model;

public class DeliveryModel {
   private String id,date,name,cust_id,mobile,mode,email,gstin,address,g_amount,t_amount,total_amount,paid;

    public DeliveryModel(String id, String date, String name, String cust_id, String mobile, String mode, String email, String gstin, String address, String g_amount, String t_amount, String total_amount, String paid) {
        this.id = id;
        this.date = date;
        this.name = name;
        this.cust_id = cust_id;
        this.mobile = mobile;
        this.mode = mode;
        this.email = email;
        this.gstin = gstin;
        this.address = address;
        this.g_amount = g_amount;
        this.t_amount = t_amount;
        this.total_amount = total_amount;
        this.paid = paid;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCust_id() {
        return cust_id;
    }

    public void setCust_id(String cust_id) {
        this.cust_id = cust_id;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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

    public String getG_amount() {
        return g_amount;
    }

    public void setG_amount(String g_amount) {
        this.g_amount = g_amount;
    }

    public String getT_amount() {
        return t_amount;
    }

    public void setT_amount(String t_amount) {
        this.t_amount = t_amount;
    }

    public String getTotal_amount() {
        return total_amount;
    }

    public void setTotal_amount(String total_amount) {
        this.total_amount = total_amount;
    }

    public String getPaid() {
        return paid;
    }

    public void setPaid(String paid) {
        this.paid = paid;
    }
}
