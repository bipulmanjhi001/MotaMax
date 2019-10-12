package com.motamax.model;

public class Popup_ListModel2 {
    private String  id,rates, qtys, taxs, gsts, discounts, mrps,c_names,p_names;
    private boolean checkeda=false;

    public Popup_ListModel2(String id, String rates, String qtys, String taxs, String gsts, String discounts, String mrps, String c_names, String p_names, boolean checkeda) {
        this.id = id;
        this.rates = rates;
        this.qtys = qtys;
        this.taxs = taxs;
        this.gsts = gsts;
        this.discounts = discounts;
        this.mrps = mrps;
        this.c_names = c_names;
        this.p_names = p_names;
        this.checkeda = checkeda;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRates() {
        return rates;
    }

    public void setRates(String rates) {
        this.rates = rates;
    }

    public String getQtys() {
        return qtys;
    }

    public void setQtys(String qtys) {
        this.qtys = qtys;
    }

    public String getTaxs() {
        return taxs;
    }

    public void setTaxs(String taxs) {
        this.taxs = taxs;
    }

    public String getGsts() {
        return gsts;
    }

    public void setGsts(String gsts) {
        this.gsts = gsts;
    }

    public String getDiscounts() {
        return discounts;
    }

    public void setDiscounts(String discounts) {
        this.discounts = discounts;
    }

    public String getMrps() {
        return mrps;
    }

    public void setMrps(String mrps) {
        this.mrps = mrps;
    }

    public String getC_names() {
        return c_names;
    }

    public void setC_names(String c_names) {
        this.c_names = c_names;
    }

    public String getP_names() {
        return p_names;
    }

    public void setP_names(String p_names) {
        this.p_names = p_names;
    }

    public boolean isCheckeda() {
        return checkeda;
    }

    public void setCheckeda(boolean checkeda) {
        this.checkeda = checkeda;
    }
}
