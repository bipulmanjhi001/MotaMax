package com.motamax.model;

public class CustList {

    private String ids,dates,amount,customer_id,mode;

    public CustList(String ids, String dates, String amount, String customer_id, String mode) {
        this.ids = ids;
        this.dates = dates;
        this.amount = amount;
        this.customer_id = customer_id;
        this.mode = mode;
    }

    public String getIds() {
        return ids;
    }

    public void setIds(String ids) {
        this.ids = ids;
    }

    public String getDates() {
        return dates;
    }

    public void setDates(String dates) {
        this.dates = dates;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getCustomer_id() {
        return customer_id;
    }

    public void setCustomer_id(String customer_id) {
        this.customer_id = customer_id;
    }

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }
}
