package com.nuvei.nuveisdk.model.debit;

import com.google.gson.annotations.SerializedName;

public class DebitCardResponse {
    private String bin;
    @SerializedName("expiry_year")
    private String expiryYear;
    @SerializedName("expiry_month")
    private String expiryMonth;
    @SerializedName("transaction_reference")
    private String transactionReference;
    private String type;
    private String number;
    private String origin;
}
