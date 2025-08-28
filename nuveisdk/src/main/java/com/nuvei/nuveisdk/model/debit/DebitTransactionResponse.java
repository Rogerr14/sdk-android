package com.nuvei.nuveisdk.model.debit;

import com.google.gson.annotations.SerializedName;

public class DebitTransactionResponse {
    private String status;
    @SerializedName("current_status")
    private String currentStatus;
    @SerializedName("payment_date")
    private String paymentDate;
    private Double amount;
    @SerializedName("authorization_code")
    private String authorizationCode;
    private int installments;
    @SerializedName("dev_reference")
    private String devReference;
    private String message;
    @SerializedName("carrier_code")
    private String carrierCode;
    private String id;
    @SerializedName("status_detail")
    private int statusDetail;
    @SerializedName("installments_type")
    private String installmentsType;
    @SerializedName("payment_method_type")
    private String paymentMethodType;
    @SerializedName("product_description")
    private String productDescription;
}
