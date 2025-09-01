package com.nuvei.nuveisdk.widget;

import android.content.Context;
import android.os.AsyncTask;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.google.android.material.button.MaterialButton;
import com.nuvei.nuveisdk.Nuvei;
import com.nuvei.nuveisdk.R;
import com.nuvei.nuveisdk.helpers.CardHelper;
import com.nuvei.nuveisdk.helpers.GlobalHelper;
import com.nuvei.nuveisdk.model.ErrorData;
import com.nuvei.nuveisdk.model.ErrorResponseModel;
import com.nuvei.nuveisdk.model.addCard.AddCardFormListener;
import com.nuvei.nuveisdk.model.addCard.AddCardRequest;
import com.nuvei.nuveisdk.model.addCard.AddCardResponse;
import com.nuvei.nuveisdk.model.addCard.BrowserInfo;
import com.nuvei.nuveisdk.model.addCard.CardModel;
import com.nuvei.nuveisdk.model.addCard.CardResponse;
import com.nuvei.nuveisdk.model.addCard.ExtraParams;
import com.nuvei.nuveisdk.model.addCard.ThreeDS2Data;
import com.nuvei.nuveisdk.model.debit.UserDebit;
import com.nuvei.nuveisdk.model.deleteCard.UserDelete;
import com.nuvei.nuveisdk.model.otpResponse.OtpRequest;
import com.nuvei.nuveisdk.model.otpResponse.OtpResponse;
import com.nuvei.nuveisdk.model.otpResponse.TransactionRequest;
import com.nuvei.nuveisdk.network.ApiClient;
import com.nuvei.nuveisdk.network.ApiService;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import retrofit2.Call;
import retrofit2.Response;


public class NuveiAddCardForm extends LinearLayout {


    private AddCardFormListener listener;
    private Context context;

    private EditText cardNumberEditText;
    private EditText holderNameEditText;
    private EditText expiryDateEditText;
    private EditText cvcCodeEditText;
    private EditText otpCodeEditText;
    private TextView otpCodeLabel;
    private LinearLayout otpForm;
    private  MaterialButton addCardButton;
    private String transactionId = "";
    private boolean isOtpActive = false;
    private boolean isOtpValid = true;
    private final ExecutorService executorService = Executors.newSingleThreadExecutor();


    public NuveiAddCardForm(Context context) {
        super(context);
        this.context = context;
        init();
    }

    public NuveiAddCardForm(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        init();
    }

    public NuveiAddCardForm(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        init();

    }



    public void init(){
        LayoutInflater.from(context).inflate(R.layout.card_form_layout, this, true);
         cardNumberEditText= findViewById(R.id.card_number_input);
         holderNameEditText= findViewById(R.id.holder_name_input);
         expiryDateEditText = findViewById(R.id.expiry_date_input);
         cvcCodeEditText= findViewById(R.id.cvv_input);





       addCardButton = findViewById(R.id.button_add_card);
//       addCardButton.setOnClickListener(v->{
//           try {
//               addCard();
//           } catch (IOException e) {
//               throw new RuntimeException(e);
//           }
//       });
//        addCardButton.setOnClickListener(v->{
//            clearForm();
//            Log.v("hola", "button add");
//            try {
//                AddCardResponse cardResponse = addCard(context);
//                switch (cardResponse.getCard().getStatus()){
//                    case "valid":
//                        break;
//                    case "pending":
//                        break;
//                    case "review":
//                        break;
//                    case "rejected":
//                        break;
//                    default:
//                        break;
//                }
//            } catch (IOException e) {
//                throw new RuntimeException(e);
//            }
//        });



    }


    public void setFormListener(AddCardFormListener listener) {
        this.listener = listener;
    }

//    private void addCard() throws IOException {
//
//        String[] expiryDate = expiryDateEditText.getText().toString().split("/");
//        int expiryMonth = Integer.parseInt(expiryDate[0]);
//        int expiryYear = CardHelper.completeYear(Integer.parseInt(expiryDate[1]));
//        String cleanNumber = cardNumberEditText.getText().toString().replaceAll(" ", "");
//
//        ApiService apiService = ApiClient.getClient(Nuvei.getAppCode(), Nuvei.getAppKey()).create(ApiService.class);
//        UserDebit user = new UserDebit("3", "correo@dd.com");
//        CardModel card = new CardModel(cleanNumber, holderNameEditText.getText().toString(), expiryMonth, expiryYear, cvcCodeEditText.getText().toString(), "vi");
//        ThreeDS2Data threeDS2Data = new ThreeDS2Data("https://lantechco.ec/img/callback3DS.php", "browser");
//        BrowserInfo browserInfo = GlobalHelper.getBrowserInfo(context);
//        ExtraParams extraParams = new ExtraParams(threeDS2Data, browserInfo);
//        AddCardRequest addCardRequest = new AddCardRequest(user, card, extraParams);
//        Call<AddCardResponse> call = apiService.addCard(addCardRequest);
//        Response<AddCardResponse> response = call.execute();
//        if(response.isSuccessful() && response.body() != null){
//            handleAddCardResponse(response.body());
//        }
//    }
//
//
//    private void handleAddCardResponse(AddCardResponse response) {
//        if (response.getCard() == null) {
//            if (listener != null) listener.onError(new ErrorResponseModel(new ErrorData("", "", "")));
//            return;
//        }
//
//        String status = response.getCard().getStatus();
//        switch (status) {
//            case "valid":
//
//                listener.onSuccess(true);
//                listener.onLoading(false);
//                clearForm();
//                break;
//            case "pending":
//                isOtpActive = true;
//                transactionId = response.getCard().getTransactionReference();
//                otpForm.setVisibility(View.VISIBLE);
//                addCardButton.setText("Verify by Otp");
//                break;
//            case "review":
//                if (response.getThe3Ds() != null && response.getThe3Ds().getBrowserResponse() != null) {
////                    verifyBy3ds(response.getThe3Ds().getBrowserResponse().getChallengeRequest());
//                }
//                break;
//            case "rejected":
//                if (listener != null) listener.onError(new ErrorResponseModel(new ErrorData("", "", "")));
//                clearForm();
//                break;
//            default:
//                if (listener != null) listener.onError(new ErrorResponseModel(new ErrorData("", "", "")));
//                break;
//        }
//    }
//
//
//    private void verifyByOtp() throws IOException {
//        listener.onLoading(true);
//        executorService.execute(()->{
//
//
//        ApiService apiService = ApiClient.getClient(Nuvei.getServerCode(), Nuvei.getServerKey()).create(ApiService.class);
//        OtpRequest otpRequest = new OtpRequest(
//                new UserDelete("4"),
//                new TransactionRequest(""),
//                "",
//                ""
//                ,true
//        );
//        Call<OtpResponse> call = apiService.verify(otpRequest);
//            Response<OtpResponse> response = null;
//            try {
//                response = call.execute();
//            } catch (IOException e) {
//                throw new RuntimeException(e);
//            }
//
//            if(response.isSuccessful() && response.body() != null){
//
//            handleVeifyOtp(response.body());
//        }
//        });
//
//    }
//
//
//    private void handleVeifyOtp(OtpResponse response){
//        switch (response.getTransactionOtpresponse().getStatus_detail()){
//            case 31:
//                otpCodeEditText.setText("");
//                isOtpValid = true;
//                break;
//            case 32:
//                isOtpActive = false;
//                clearForm();
//                listener.onSuccess(true);
//                break;
//            case 33:
//                clearForm();
//                listener.onSuccess(false);
//                break;
//            default:
//                break;
//        }
//    }
//
//
//    //validations
//    private boolean validateAddCardForm() {
//        // Implement your specific validation rules here
//        return !cardNumberEditText.getText().toString().isEmpty() &&
//                !holderNameEditText.getText().toString().isEmpty() &&
//                !expiryDateEditText.getText().toString().isEmpty() &&
//                !cvcCodeEditText.getText().toString().isEmpty();
//    }
//
//    private void clearForm(){
//        cardNumberEditText.setText("");
//        holderNameEditText.setText("");
//        expiryDateEditText.setText("");
//        cvcCodeEditText.setText("");
//        otpCodeEditText.setText("");
//    }
//
//    private boolean validateOtpForm() {
//        return otpCodeEditText.getText().toString().length() == 6;
//    }

}
