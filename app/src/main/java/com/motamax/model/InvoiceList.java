package com.motamax.model;

public class InvoiceList {

    private String id,date,name,cust_id,mobile,mode;
    private String address,total_amount,paid,dues,gstin,g_amount,t_amount;

    public InvoiceList(String id, String date, String name, String cust_id, String mobile, String mode, String address, String total_amount, String paid, String dues, String gstin, String g_amount, String t_amount) {
        this.id = id;
        this.date = date;
        this.name = name;
        this.cust_id = cust_id;
        this.mobile = mobile;
        this.mode = mode;
        this.address = address;
        this.total_amount = total_amount;
        this.paid = paid;
        this.dues = dues;
        this.gstin = gstin;
        this.g_amount = g_amount;
        this.t_amount = t_amount;
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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
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

    public String getDues() {
        return dues;
    }

    public void setDues(String dues) {
        this.dues = dues;
    }

    public String getGstin() {
        return gstin;
    }

    public void setGstin(String gstin) {
        this.gstin = gstin;
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
}
