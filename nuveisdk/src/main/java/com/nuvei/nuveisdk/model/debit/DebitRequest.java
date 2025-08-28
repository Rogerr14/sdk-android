package com.nuvei.nuveisdk.model.debit;

import com.nuvei.nuveisdk.model.deleteCard.CardDelete;

public class DebitRequest {
    private UserDebit user;
    private DebitOrder order;
    private CardDelete card;

    public DebitRequest(UserDebit user, DebitOrder order, CardDelete card) {
        this.user = user;
        this.order = order;
        this.card = card;
    }
}
