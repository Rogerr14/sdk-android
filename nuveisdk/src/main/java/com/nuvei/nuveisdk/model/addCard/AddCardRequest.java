package com.nuvei.nuveisdk.model.addCard;

import com.nuvei.nuveisdk.model.debit.UserDebit;

public class AddCardRequest {
    private UserDebit user;
    private CardModel card;
    private ExtraParams extra_params;

    public AddCardRequest(UserDebit user, CardModel card, ExtraParams extraParams) {
        this.user = user;
        this.card = card;
        this.extra_params = extraParams;
    }
}
