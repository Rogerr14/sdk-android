package com.nuvei.nuveisdk;

import android.util.Log;

import com.nuvei.nuveisdk.model.ApiException;
import com.nuvei.nuveisdk.model.ErrorResponseModel;
import com.nuvei.nuveisdk.model.debit.DebitOrder;
import com.nuvei.nuveisdk.model.debit.DebitRequest;
import com.nuvei.nuveisdk.model.debit.DebitResponse;
import com.nuvei.nuveisdk.model.debit.UserDebit;
import com.nuvei.nuveisdk.model.deleteCard.CardDelete;
import com.nuvei.nuveisdk.model.deleteCard.DeleteCardRequest;
import com.nuvei.nuveisdk.model.deleteCard.UserDelete;
import com.nuvei.nuveisdk.model.listCard.ListCardResponse;
import com.nuvei.nuveisdk.model.listCard.MessageResponse;
import com.nuvei.nuveisdk.model.refund.OrderRefund;
import com.nuvei.nuveisdk.model.refund.RefundRequest;
import com.nuvei.nuveisdk.model.refund.RefundResponse;
import com.nuvei.nuveisdk.model.refund.TransactionRefund;
import com.nuvei.nuveisdk.network.ApiClient;
import com.nuvei.nuveisdk.network.ApiService;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Response;

public class Nuvei {
    private static String appCode;
    private static String appKey;
    private static String serverCode;
    private static String serverKey;
    private static boolean testMode =true;

    /**
     * Initializes the library environment with the provided configuration.
     * Codes and keys are provided by the nuvei team.
     *
     * @param appCode The application code.
     * @param appKey The application key.
     * @param serverCode The server code.
     * @param serverKey The server key.
     * @param testMode Indicates whether to use the test environment.
     */

    public static void initEnvironment(String appCode, String appKey, String serverCode, String serverKey, boolean testMode){
        Nuvei.appCode = appCode;
        Nuvei.appKey = appKey;
        Nuvei.serverCode = serverCode;
        Nuvei.serverKey = serverCode;
        Nuvei.testMode = testMode;
        Log.v("iniciado", Nuvei.serverKey);
    }

    public static boolean isTestMode(){
        return  Nuvei.testMode;
    }

    public static ListCardResponse listCards(String idUser) throws IOException {
        ApiService apiService = ApiClient.getClient(Nuvei.serverCode, Nuvei.serverKey).create(ApiService.class);
        Call<ListCardResponse> call = apiService.getAllCards(idUser);
        Response<ListCardResponse> response = call.execute();
        if(response.isSuccessful() && response.body() != null){
            return response.body();
        }else{
            String errorBody = response.errorBody() != null ? response.errorBody().string() : "{}";
            try {
                ErrorResponseModel errorResponse = new com.google.gson.Gson().fromJson(errorBody, ErrorResponseModel.class);

                throw new ApiException(errorResponse.getError().getType(), errorResponse.getError().getHelp());
            } catch (Exception e) {

                throw new IOException("API error: " + response.code() + " - " + response.message());
            }
        }

    }


    public  static MessageResponse deleteCard(String idUser, String tokenCard)throws IOException{
        ApiService apiService = ApiClient.getClient(Nuvei.serverCode, Nuvei.serverKey).create(ApiService.class);
        CardDelete cardToDelete = new CardDelete(tokenCard);
        UserDelete userToDeleteCard = new UserDelete(idUser);
        DeleteCardRequest request = new DeleteCardRequest(cardToDelete, userToDeleteCard);

        Call<MessageResponse> call = apiService.deleteCard(request);
        Response<MessageResponse> response = call.execute();
        if (response.isSuccessful() && response.body() != null) {
            return response.body();
        } else {
            throw new IOException("API error: " + response.code() + " - " + response.message());
        }
    }

    public static RefundResponse refund(String authToken, String transactionId, Long referenceLabel, Double amount, boolean moreInfo) throws IOException {
        ApiService apiService = ApiClient.getClient(Nuvei.serverCode, Nuvei.serverKey).create(ApiService.class);

        TransactionRefund transactionRefund = new TransactionRefund(transactionId, referenceLabel);
        OrderRefund orderRefund = amount != null ? new OrderRefund(amount) : null;

        // Pass a Boolean object for `moreInfo` to allow nullability if needed, otherwise just a primitive.
        Boolean moreInfoObj = moreInfo;

        RefundRequest request = new RefundRequest(transactionRefund, orderRefund, moreInfoObj);

        Call<RefundResponse> call = apiService.refund(request);
        Response<RefundResponse> response = call.execute();

        if (response.isSuccessful() && response.body() != null) {
            return response.body();
        } else {
            // Re-use your existing error handling logic
            throw new IOException("API error: " + response.code() + " - " + response.message());
        }
    }



    public static DebitResponse processDebit(String authToken, String userId, String userEmail,
                                             Double amount, String description, String devReference,
                                             Double vat, String cardToken) throws IOException {

        ApiService apiService = ApiClient.getClient(Nuvei.serverCode, Nuvei.serverKey).create(ApiService.class);

        UserDebit user = new UserDebit(userId, userEmail);
        DebitOrder order = new DebitOrder(amount, description, devReference, vat);
        CardDelete card = new CardDelete(cardToken);
        DebitRequest request = new DebitRequest(user, order, card);

        Call<DebitResponse> call = apiService.debit(request);
        Response<DebitResponse> response = call.execute();

        if (response.isSuccessful() && response.body() != null) {
            return response.body();
        } else {
            // Re-use your existing error handling logic
            throw new IOException("API error: " + response.code() + " - " + response.message());
        }
    }
}
