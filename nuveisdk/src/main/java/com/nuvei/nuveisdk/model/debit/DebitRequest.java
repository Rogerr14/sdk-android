package com.nuvei.nuveisdk.model.debit;

import com.google.gson.annotations.SerializedName;
import com.nuvei.nuveisdk.model.deleteCard.CardDelete;

public class DebitRequest {
    @SerializedName("user")
    private UserDebit user;
    @SerializedName("order")
    private DebitOrder order;
    @SerializedName("card")
    private CardDelete card;

    public DebitRequest(UserDebit user, DebitOrder order, CardDelete card) {
        this.user = user;
        this.order = order;
        this.card = card;
    }
}
