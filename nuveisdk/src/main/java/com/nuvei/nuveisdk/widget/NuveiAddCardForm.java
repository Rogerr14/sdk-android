package com.nuvei.nuveisdk.widget;

import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.os.AsyncTask;
import android.os.Build;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
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
import com.nuvei.nuveisdk.model.addCard.BrowserResponse;
import com.nuvei.nuveisdk.model.addCard.CardInfoModel;
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
import java.util.logging.Logger;

import retrofit2.Call;
import retrofit2.Response;


public class NuveiAddCardForm extends LinearLayout {


    private AddCardFormListener listener;
    private Context context;

    private CardView cardFront;
    private CardView cardBack;
    private TextInputEditText cardNumberEditText;
    private TextInputEditText holderNameEditText;
    private TextInputEditText expiryDateEditText;
    private TextInputEditText cvcCodeEditText;

    private ConstraintLayout cardFrontLayout, cardBackLayout;

    private  TextInputEditText otpEditText;
    private  TextInputLayout otpInputLayout;

    private TextInputLayout cardNumberInputLayout;
    private TextInputLayout holderNameInputLayout;
    private TextInputLayout expiryDateInputLayout;
    private TextInputLayout cvcInputLayout;

    private TextView numberCardTV;
    private TextView nameHolderTV;
    private TextView expiryDateTV;
    private TextView cvcValueTV;

    private ImageView cardImage;
    private LinearLayout otpForm;
    private  MaterialButton addCardButton;
    private String transactionId = "";
    private boolean isOtpActive = false;
    private boolean isOtpValid = true;
    private boolean isFront = true;
    private static final int DELAY_3DS_MS = 5000;
    private String tokenCres = "";
    private String referenceId = "";
    private String valueOtp = "";
    private String typeVerifyOtp = "";
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

        cardFront = findViewById(R.id.card_front);
        cardFrontLayout = findViewById(R.id.card_front_layout);
        cardBack = findViewById(R.id.card_back);
        cardBackLayout = findViewById(R.id.card_back_layout);
        cardNumberEditText= findViewById(R.id.card_number_input);
        holderNameEditText= findViewById(R.id.holder_name_input);
        expiryDateEditText = findViewById(R.id.expiry_date_input);
        otpEditText = findViewById(R.id.otp_input);
         cvcCodeEditText= findViewById(R.id.cvv_input);
         cardImage = findViewById(R.id.card_image);
        cardBack.setRotationY(180f);

        // Mejorar perspectiva para el efecto 3D
        float scale = context.getResources().getDisplayMetrics().density;
        cardFront.setCameraDistance(8000 * scale);
        cardBack.setCameraDistance(8000 * scale);


        cardNumberInputLayout = findViewById(R.id.card_number_input_layout);
        holderNameInputLayout = findViewById(R.id.holder_name_input_layout);
        expiryDateInputLayout = findViewById(R.id.expiry_date_input_layout);
        cvcInputLayout = findViewById(R.id.cvv_input_layout);
        otpInputLayout = findViewById(R.id.otp_input_layout);


        otpForm = findViewById(R.id.otp_form);
        numberCardTV = findViewById(R.id.tv_card_number);
        nameHolderTV = findViewById(R.id.tv_name_value);
        expiryDateTV = findViewById(R.id.tv_date_value);
        cvcValueTV = findViewById(R.id.tv_cvc_value);



       addCardButton = findViewById(R.id.button_add_card);


       addCardButton.setOnClickListener(view ->
               {
                   try {
                       if(isOtpActive){
                           valueOtp = otpEditText.getText().toString();
                           verifyByOtp();
                       }else {


                           addCard();
                       }
                   } catch (IOException e) {
                       throw new RuntimeException(e);
                   }
               }
       );

        cvcCodeEditText.setOnFocusChangeListener(new OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                flipCard();
            }
        });

        cvcCodeEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                String cvc = s.toString().trim();
                cvcValueTV.setText(cvc.isEmpty() ? "***" : cvc);
            }
        });

       cardNumberEditText.addTextChangedListener(new TextWatcher() {
           private String current = "";
                                                     @Override
                                                     public void afterTextChanged(Editable editable) {

                                                     }

                                                     @Override
                                                     public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                                                     }

                                                     @Override
                                                     public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                                                         if(!charSequence.toString().equals(current)){
                                                             cardNumberEditText.removeTextChangedListener(this);
                                                             String raw = charSequence.toString().replaceAll("\\D", "");
                                                             String formatted = CardHelper.formatCardNumber(raw);
                                                             CardInfoModel cardInfoModel = CardHelper.getCardInfo(raw);
                                                             cardImage.setImageDrawable(ContextCompat.getDrawable(context, cardInfoModel.getIconRes()));

                                                             cardFrontLayout.setBackground(new GradientDrawable(GradientDrawable.Orientation.LEFT_RIGHT, cardInfoModel.getGradientColor()));

                                                             cardBackLayout.setBackground(new GradientDrawable(GradientDrawable.Orientation.LEFT_RIGHT, cardInfoModel.getGradientColor()));

                                                             InputFilter[] fArray = new InputFilter[1];
                                                             fArray[0] = new InputFilter.LengthFilter(cardInfoModel.getCvcNumber());
                                                             cvcCodeEditText.setFilters(fArray);
                                                             current = formatted;
                                                             Log.v("format", formatted);
                                                             cardNumberEditText.setText(formatted);
                                                             cardNumberEditText.setSelection(formatted.length());
                                                             cardNumberEditText.addTextChangedListener(this);
                                                             if(formatted.isEmpty()){
                                                                 numberCardTV.setText("**** **** **** ****");
                                                             }else{
                                                                 numberCardTV.setText(formatted);
                                                             }

                                                         }




                                                     }
                                                 }
       );

        holderNameEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable editable) {

            }

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                holderNameEditText.removeTextChangedListener(this);
                if(charSequence.toString().isEmpty()){
                    nameHolderTV.setText("JHON DOE");
                }else{
                    nameHolderTV.setText(charSequence.toString());
                }
                holderNameEditText.addTextChangedListener(this);
            }
        });


        expiryDateEditText.addTextChangedListener(new TextWatcher() {
            private String previous = "";
            @Override
            public void afterTextChanged(Editable editable) {

            }

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String formatted = CardHelper.formatExpiryInput(charSequence.toString());


                    expiryDateEditText.removeTextChangedListener(this);
                    expiryDateEditText.setText(formatted);
                    expiryDateEditText.setSelection(formatted.length()); // Cursor al final
                    expiryDateEditText.addTextChangedListener(this);

                // Actualizar tarjeta o mostrar placeholder
                expiryDateTV.setText(formatted.isEmpty() ? "MM/YY" : formatted);
            }
        });

    }

    public void setFormListener(AddCardFormListener listener) {
        this.listener = listener;
    }

  private void addCard() throws IOException {
      cardNumberInputLayout.setError(null);
      cardNumberInputLayout.setErrorEnabled(false);
      holderNameInputLayout.setError(null);
      holderNameInputLayout.setErrorEnabled(false);
      expiryDateInputLayout.setError(null);
      cvcInputLayout.setErrorEnabled(false);
      cvcInputLayout.setError(null);

      String cardNumber = cardNumberEditText.getText().toString().replaceAll("\\D", "");
      String expiry = expiryDateEditText.getText().toString();
      String holderName = holderNameEditText.getText().toString().trim();
      String cvc = cvcCodeEditText.getText().toString().trim();

      boolean hasError = false;
      if (cardNumber.isEmpty() || !CardHelper.validLuhnNumber(cardNumber)) {
          cardNumberInputLayout.setErrorEnabled(true);
          cardNumberInputLayout.setError("Número de tarjeta inválido");
          hasError = true;
      }

      if (holderName.isEmpty()) {
          holderNameInputLayout.setErrorEnabled(true);
          holderNameInputLayout.setError("Nombre del titular es requerido");
          hasError = true;
      }
      String expiryError = CardHelper.validateExpiryDate(expiry);
      if (expiryError != null) {
          expiryDateInputLayout.setErrorEnabled(true);
          expiryDateInputLayout.setError(expiryError);
          hasError = true;
      }

      if (cvc.isEmpty() || cvc.length() < 3) {
          cvcInputLayout.setErrorEnabled(true);
          cvcInputLayout.setError("CVC inválido");
          hasError = true;
      }

      if (hasError) return;

      if (listener != null) listener.onLoading(true);
      String[] expiryDate = expiryDateEditText.getText().toString().split("/");
        int expiryMonth = Integer.parseInt(expiryDate[0]);
        int expiryYear = CardHelper.completeYear(Integer.parseInt(expiryDate[1]));
       String cleanNumber = cardNumberEditText.getText().toString().replaceAll("\\D", "");
//
        UserDebit user = new UserDebit("4", "erick.guillen@nuvei.com");
      CardModel card = new CardModel(cleanNumber, holderNameEditText.getText().toString(), expiryMonth, expiryYear, cvcCodeEditText.getText().toString(), "vi");
        ThreeDS2Data threeDS2Data = new ThreeDS2Data("https://lantechco.ec/img/callback3DS.php", "browser");
        BrowserInfo browserInfo = GlobalHelper.getBrowserInfo(context);
        ExtraParams extraParams = new ExtraParams(threeDS2Data, browserInfo);
        AddCardRequest addCardRequest = new AddCardRequest(user, card, extraParams);

      ApiService apiService = ApiClient.getClient(Nuvei.getAppCode(), Nuvei.getAppKey()).create(ApiService.class);
      Call<AddCardResponse> call = apiService.addCard(addCardRequest);
      call.enqueue(new retrofit2.Callback<AddCardResponse>() {
          @Override
          public void onResponse(Call<AddCardResponse> call, Response<AddCardResponse> response) {
              // siempre vuelve al hilo donde post(...) se ejecute; para asegurarnos usamos post() sobre la vista:
              post(() -> {
                  Log.v("vacio", String.valueOf(listener != null));
                  if (listener != null) listener.onLoading(false);

                  if (response.isSuccessful() && response.body() != null) {
                      handleAddCardResponse(response.body());
                      Log.v("RESPONSE BODY ", response.body().toString());
                  } else {
                      String errorBody = "";
                      try {
                          if (response.errorBody() != null) errorBody = response.errorBody().string();
                      } catch (IOException e) { errorBody = e.getMessage(); }
                      if (listener != null) listener.onError(new ErrorResponseModel(new ErrorData("api", "Error en el servidor", errorBody)));
                  }
              });
          }

          @Override
          public void onFailure(Call<AddCardResponse> call, Throwable t) {
              post(() -> {
                  if (listener != null) {
                      listener.onLoading(false);
                      listener.onError(new ErrorResponseModel(new ErrorData("network", "Error de red", t.getMessage())));
                  }
              });
          }
      });
//            handleAddCardResponse(response.body());
//        }
}


    private void handleAddCardResponse(AddCardResponse response) {
        if (response.getCard() == null) {
            if (listener != null) listener.onError(new ErrorResponseModel(new ErrorData("", "", "")));
            return;
        }

        String status = response.getCard().getStatus();
        switch (status) {
            case "valid":
                if (listener != null)  listener.onSuccess(true);
                if (listener != null) listener.onLoading(false);
                clearForm();
                break;
            case "pending":
                isOtpActive = true;
                typeVerifyOtp ="BY_OTP";

                transactionId = response.getCard().getTransactionReference();
                valueOtp = otpEditText.getText().toString();
                otpForm.setVisibility(View.VISIBLE);
                addCardButton.setText("Verify by Otp");
                break;
            case "review":
                Log.v("Revision", "Revision por 3ds");
                if (response.getThe3Ds() != null && response.getThe3Ds().getBrowserResponse() != null) {
                   verifyBy3ds(response.getThe3Ds().getBrowserResponse());
                }
                break;
            case "rejected":
                if (listener != null) listener.onError(new ErrorResponseModel(new ErrorData("", "", "")));
                clearForm();
                break;
            default:
                if (listener != null) listener.onError(new ErrorResponseModel(new ErrorData("", "", "")));
                break;
        }
    }


    private  void  verifyBy3ds(BrowserResponse browserResponse){
        if(browserResponse != null && browserResponse.getChallengeRequest() != null && !browserResponse.getChallengeRequest().isEmpty()){

        }else{

        }

    }


    private void verifyByOtp() throws IOException {
        if(listener != null)        listener.onLoading(true);
        executorService.execute(()->{
        ApiService apiService = ApiClient.getClient(Nuvei.getServerCode(), Nuvei.getServerKey()).create(ApiService.class);
        OtpRequest otpRequest = new OtpRequest(
                new UserDelete("4"),
                new TransactionRequest(transactionId),
                typeVerifyOtp,
                valueOtp
                ,true
        );
            Call<OtpResponse> call = apiService.verify(otpRequest);
            Response<OtpResponse> response = null;
            try {
                response = call.execute(); // Ejecutado en hilo de fondo
            } catch (IOException e) {
                post(() -> {
                    if (listener != null) listener.onLoading(false);
                });
                return;
            }

            if(response.isSuccessful() && response.body() != null){
                final OtpResponse finalResponse = response.body();
                post(() -> {
                    handleVerifyOtp(finalResponse);
                });
            } else {
                // Manejo de errores de API también debe ir en el hilo principal
                final Response<OtpResponse> finalErrorResponse = response;
                post(() -> {
                    if (listener != null) listener.onLoading(false);
                    // Manejar error HTTP
                });
            }
        });

    }



private void flipCard() {
    float scale = getResources().getDisplayMetrics().density;
    cardFront.setCameraDistance(scale *8000);
    cardBack.setCameraDistance(scale *8000);

    AnimatorSet setOut = (AnimatorSet) AnimatorInflater.loadAnimator(this.context, R.animator.card_flip_out);
    AnimatorSet setIn = (AnimatorSet) AnimatorInflater.loadAnimator(this.context, R.animator.card_flip_in);

    setOut.setTarget(isFront ?  cardFront : cardBack);
    setIn.setTarget(isFront ?  cardBack : cardFront);

    setOut.start();
    setIn.start();

    isFront = !isFront;

}
    private void handleVerifyOtp(OtpResponse response){
        if(listener != null) listener.onLoading(false);
        switch (response.getTransactionOtpresponse().getStatus_detail()){
            case 31:
                otpEditText.setText("");
                isOtpValid = true;
                break;
            case 32:
                isOtpActive = false;
                clearForm();

                if (listener != null)   listener.onSuccess(true);
                break;
            case 33:
                clearForm();
                if (listener != null)    listener.onSuccess(false);
                break;
            default:
                break;
        }
    }

    //validations
    private boolean validateAddCardForm() {
        // Implement your specific validation rules here
        return !cardNumberEditText.getText().toString().isEmpty() &&
                !holderNameEditText.getText().toString().isEmpty() &&
                !expiryDateEditText.getText().toString().isEmpty() &&
                !cvcCodeEditText.getText().toString().isEmpty();
    }

    private void clearForm(){
        cardNumberEditText.setText("");
        holderNameEditText.setText("");
        expiryDateEditText.setText("");
        cvcCodeEditText.setText("");
    }

    private boolean validateOtpForm() {
        return otpEditText.getText().toString().length() == 6;
    }

}
