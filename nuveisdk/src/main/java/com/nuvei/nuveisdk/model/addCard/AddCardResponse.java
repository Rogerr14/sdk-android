package com.nuvei.nuveisdk.model.addCard;

import com.google.gson.annotations.SerializedName;

public class AddCardResponse {
    @SerializedName("card")
    private CardResponse card;
    @SerializedName("3ds")
    private The3dsResponse the3Ds;

    public CardResponse getCard() { return card; }
    public void setCard(CardResponse value) { this.card = value; }

    public The3dsResponse getThe3Ds() { return the3Ds; }
    public void setThe3Ds(The3dsResponse value) { this.the3Ds = value; }

}
