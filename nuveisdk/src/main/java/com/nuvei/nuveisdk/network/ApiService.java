package com.nuvei.nuveisdk.network;

import com.nuvei.nuveisdk.model.addCard.AddCardRequest;
import com.nuvei.nuveisdk.model.addCard.AddCardResponse;
import com.nuvei.nuveisdk.model.debit.DebitRequest;
import com.nuvei.nuveisdk.model.debit.DebitResponse;
import com.nuvei.nuveisdk.model.deleteCard.DeleteCardRequest;
import com.nuvei.nuveisdk.model.listCard.ListCardResponse;
import com.nuvei.nuveisdk.model.listCard.MessageResponse;
import com.nuvei.nuveisdk.model.otpResponse.OtpRequest;
import com.nuvei.nuveisdk.model.otpResponse.OtpResponse;
import com.nuvei.nuveisdk.model.refund.RefundRequest;
import com.nuvei.nuveisdk.model.refund.RefundResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface ApiService {

    @GET("/v2/card/list")
    Call<ListCardResponse> getAllCards(
            @Query("uid") String uid
    );


    @POST("/v2/card/delete")
    Call<MessageResponse> deleteCard(
            @Body DeleteCardRequest request
    );


    @POST("/v2/transaction/refund/")
    Call<RefundResponse> refund(@Body RefundRequest request);

    @POST("/v2/transaction/debit/")
    Call<DebitResponse> debit(@Body DebitRequest request);

    @POST("/v2/card/add")
    Call<AddCardResponse> addCard(@Body AddCardRequest request);

    @POST("/v2/transaction/verify")
    Call<OtpResponse> verify(@Body OtpRequest request);


}
