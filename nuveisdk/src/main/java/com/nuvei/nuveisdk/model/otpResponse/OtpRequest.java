package com.nuvei.nuveisdk.model.otpResponse;

import com.google.gson.annotations.SerializedName;
import com.nuvei.nuveisdk.model.deleteCard.UserDelete;

public class OtpRequest {
    @SerializedName("user")
    private UserDelete user;
    @SerializedName("transaction")
    private TransactionRequest transaction;
    private String type;
    private String value;
    @SerializedName("more_info")
    private boolean moreInfo;

    public OtpRequest(UserDelete user, TransactionRequest transaction, String type, String value, boolean moreInfo) {
        this.user = user;
        this.transaction = transaction;
        this.type = type;
        this.value = value;
        this.moreInfo = moreInfo;
    }
}
