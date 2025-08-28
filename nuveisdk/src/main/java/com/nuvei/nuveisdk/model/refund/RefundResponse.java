package com.nuvei.nuveisdk.model.refund;

import com.nuvei.nuveisdk.model.listCard.CardItem;

public class RefundResponse {
    private String message; // For simple success message
    private String status;
    private RefundTransaction transaction;
    private String detail;
    private CardItem card;

    // Getters
    public String getMessage() {
        return message;
    }

    public String getStatus() {
        return status;
    }

    public RefundTransaction getTransaction() {
        return transaction;
    }

    public String getDetail() {
        return detail;
    }

    public CardItem getCard() {
        return card;
    }
}
