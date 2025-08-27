package com.nuvei.nuveisdk.network;

import com.nuvei.nuveisdk.model.ListCardResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiService {

    @GET("/v2/card/list")
    Call<ListCardResponse> getAllCards(
            @Query("uid") String uid
    );
}
