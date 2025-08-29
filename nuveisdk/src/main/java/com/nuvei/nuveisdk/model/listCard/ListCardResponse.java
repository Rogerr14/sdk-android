package com.nuvei.nuveisdk.model.listCard;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ListCardResponse {
    private List<CardItem> cards;
    @SerializedName("result_size")
    private int resultSize;


    public List<CardItem> getCards() {
        return cards;
    }

    public int getResultSize() {
        return resultSize;
    }
}

