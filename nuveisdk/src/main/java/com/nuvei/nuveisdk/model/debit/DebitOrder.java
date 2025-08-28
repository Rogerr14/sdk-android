package com.nuvei.nuveisdk.model.debit;

import com.google.gson.annotations.SerializedName;

public class DebitOrder {
    private Double amount;
    private String description;
    @SerializedName("dev_reference")
    private String devReference;
    private Double vat;

    public DebitOrder(Double amount, String description, String devReference, Double vat) {
        this.amount = amount;
        this.description = description;
        this.devReference = devReference;
        this.vat = vat;
    }
}
