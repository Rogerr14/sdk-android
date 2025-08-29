package com.nuvei.nuveisdk.model.debit;

public class DebitResponse {
    private DebitTransactionResponse transaction;
    private DebitCardResponse card;


    public DebitTransactionResponse getTransaction() {
        return transaction;
    }

    public DebitCardResponse getCard() {
        return card;
    }
}

