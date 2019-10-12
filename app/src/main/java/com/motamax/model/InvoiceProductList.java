package com.motamax.model;

public class InvoiceProductList {
    private String id,sales_id,hsn_code,stock,mrp,rate,gst_rate,quantity;
    private String taxable_amount,gst_amount,amount,p_name,name;

    public InvoiceProductList(String id, String sales_id, String hsn_code, String stock, String mrp,
                              String rate, String gst_rate, String quantity, String taxable_amount,
                              String gst_amount, String amount, String p_name, String name) {
        this.id = id;
        this.sales_id = sales_id;
        this.hsn_code = hsn_code;
        this.stock = stock;
        this.mrp = mrp;
        this.rate = rate;
        this.gst_rate = gst_rate;
        this.quantity = quantity;
        this.taxable_amount = taxable_amount;
        this.gst_amount = gst_amount;
        this.amount = amount;
        this.p_name = p_name;
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSales_id() {
        return sales_id;
    }

    public void setSales_id(String sales_id) {
        this.sales_id = sales_id;
    }

    public String getHsn_code() {
        return hsn_code;
    }

    public void setHsn_code(String hsn_code) {
        this.hsn_code = hsn_code;
    }

    public String getStock() {
        return stock;
    }

    public void setStock(String stock) {
        this.stock = stock;
    }

    public String getMrp() {
        return mrp;
    }

    public void setMrp(String mrp) {
        this.mrp = mrp;
    }

    public String getRate() {
        return rate;
    }

    public void setRate(String rate) {
        this.rate = rate;
    }

    public String getGst_rate() {
        return gst_rate;
    }

    public void setGst_rate(String gst_rate) {
        this.gst_rate = gst_rate;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getTaxable_amount() {
        return taxable_amount;
    }

    public void setTaxable_amount(String taxable_amount) {
        this.taxable_amount = taxable_amount;
    }

    public String getGst_amount() {
        return gst_amount;
    }

    public void setGst_amount(String gst_amount) {
        this.gst_amount = gst_amount;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getP_name() {
        return p_name;
    }

    public void setP_name(String p_name) {
        this.p_name = p_name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
