package com.motamax.model;

public class ViewDeliveryList {
    String id;
    String name;
    String mobile;
    String designation;
    String email;
    String address;
    String g_amount;
    String t_amount;
    String total_amount;
    String paid;

    public ViewDeliveryList(String id, String name, String mobile, String designation, String email, String address, String g_amount, String t_amount, String total_amount, String paid) {
        this.id = id;
        this.name = name;
        this.mobile = mobile;
        this.designation = designation;
        this.email = email;
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

    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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
