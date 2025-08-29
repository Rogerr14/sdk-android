package com.nuvei.sdk.listCard.listener;

import com.nuvei.nuveisdk.model.listCard.CardItem;

public interface OnCardActionListener {
    void onDeleteCard(String cardToken);
    void onCardSelected(CardItem card);
}
