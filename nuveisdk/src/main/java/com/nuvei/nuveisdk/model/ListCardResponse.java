package com.nuvei.nuveisdk.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ListCardResponse {
    private List<CardItem> cards;
    @SerializedName("result_size")
    private int resultSize;
}

