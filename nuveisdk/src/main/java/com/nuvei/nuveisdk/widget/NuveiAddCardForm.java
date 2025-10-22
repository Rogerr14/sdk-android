package com.nuvei.nuveisdk.widget;

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
import androidx.core.content.ContextCompat;

import com.google.android.material.button.MaterialButton;
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
    private CardView cardView;
    private EditText cardNumberEditText;
    private EditText holderNameEditText;
    private EditText expiryDateEditText;
    private EditText cvcCodeEditText;


    private TextInputLayout cardNumberInputLayout;
    private TextInputLayout holderNameInputLayout;
    private TextInputLayout expiryDateInputLayout;
    private TextInputLayout cvcInputLayout;

    private TextView numberCardTV;
    private TextView nameHolderTV;
    private TextView expiryDateTV;

    private EditText otpCodeEditText;
    private TextView otpCodeLabel;
    private ImageView cardImage;
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
         cardImage = findViewById(R.id.card_image);
         cardView = findViewById(R.id.card_widget);


        cardNumberInputLayout = findViewById(R.id.card_number_input_layout);
        holderNameInputLayout = findViewById(R.id.holder_name_input_layout);
        expiryDateInputLayout = findViewById(R.id.expiry_date_input_layout);
        cvcInputLayout = findViewById(R.id.cvv_input_layout);

         numberCardTV = findViewById(R.id.tv_card_number);
         nameHolderTV = findViewById(R.id.tv_name_value);
         expiryDateTV = findViewById(R.id.tv_date_value);



       addCardButton = findViewById(R.id.button_add_card);


       addCardButton.setOnClickListener(view ->
               {
                   try {
                       addCard();
                   } catch (IOException e) {
                       throw new RuntimeException(e);
                   }
               }
       );

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
                                                             cardView.setBackground(new GradientDrawable(GradientDrawable.Orientation.LEFT_RIGHT, cardInfoModel.getGradientColor()));

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

  private void addCard() throws IOException {
      cardNumberInputLayout.setError(null);
      holderNameInputLayout.setError(null);
      expiryDateInputLayout.setError(null);
      cvcInputLayout.setError(null);

      String cardNumber = cardNumberEditText.getText().toString().replaceAll("\\D", "");
      String expiry = expiryDateEditText.getText().toString();
      String holderName = holderNameEditText.getText().toString().trim();
      String cvc = cvcCodeEditText.getText().toString().trim();

      boolean hasError = false;

      // Validar número de tarjeta
      if (cardNumber.isEmpty() || !CardHelper.validLuhnNumber(cardNumber)) {
          cardNumberInputLayout.setError("Número de tarjeta inválido");
          hasError = true;
      }

      // Validar nombre
      if (holderName.isEmpty()) {
          holderNameInputLayout.setError("Nombre del titular es requerido");
          hasError = true;
      }

      // Validar fecha
      String expiryError = CardHelper.validateExpiryDate(expiry);
      if (expiryError != null) {
          expiryDateInputLayout.setError(expiryError);
          hasError = true;
      }

      // Validar CVC
      if (cvc.isEmpty() || cvc.length() < 3) {
          cvcInputLayout.setError("CVC inválido");
          hasError = true;
      }

      if (hasError) return;


      String[] expiryDate = expiryDateEditText.getText().toString().split("/");
        int expiryMonth = Integer.parseInt(expiryDate[0]);
        int expiryYear = CardHelper.completeYear(Integer.parseInt(expiryDate[1]));
       String cleanNumber = cardNumberEditText.getText().toString().replaceAll("\\D", "");
//
        UserDebit user = new UserDebit("3", "correo@dd.com");
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
                  if (listener != null) listener.onLoading(false);

                  if (response.isSuccessful() && response.body() != null) {
                      // usa tu handler para estados (valid/pending/rejected/etc)
                     Log.v("response ok ", response.body().toString());
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
    private void handleVeifyOtp(OtpResponse response){
        switch (response.getTransactionOtpresponse().getStatus_detail()){
            case 31:
                otpCodeEditText.setText("");
                isOtpValid = true;
                break;
            case 32:
                isOtpActive = false;
                clearForm();
                listener.onSuccess(true);
                break;
            case 33:
                clearForm();
                listener.onSuccess(false);
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
        otpCodeEditText.setText("");
    }

    private boolean validateOtpForm() {
        return otpCodeEditText.getText().toString().length() == 6;
    }

}
