package com.nuvei.sdk.home;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.button.MaterialButton;
import com.google.gson.Gson;
import com.nuvei.nuveisdk.Nuvei;
import com.nuvei.nuveisdk.model.debit.DebitOrder;
import com.nuvei.nuveisdk.model.debit.DebitRequest;
import com.nuvei.nuveisdk.model.debit.DebitResponse;
import com.nuvei.nuveisdk.model.debit.UserDebit;
import com.nuvei.nuveisdk.model.deleteCard.CardDelete;
import com.nuvei.nuveisdk.model.listCard.CardItem;
import com.nuvei.nuveisdk.model.listCard.MessageResponse;
import com.nuvei.sdk.R;
import com.nuvei.sdk.listCard.ListCard;
import com.nuvei.sdk.refund.RefundActivity;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class HomeActivity extends AppCompatActivity {
    private String tokenCard;
    private ExecutorService executorService = Executors.newSingleThreadExecutor();
    private ActivityResultLauncher<Intent> cardSelectionLauncher;
    private ProgressBar progressBar;
    private MaterialButton paymentButton;
    private  TextView cardView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_home);
        cardView =  findViewById(R.id.card_status_message);
        progressBar = findViewById(R.id.progress_bar);
        paymentButton = findViewById(R.id.pay_order_button);
        cardSelectionLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                        Intent data = result.getData();
                        String cardName = data.getStringExtra("CARD_NAME");
                        String cardNumber = data.getStringExtra("CARD_NUMBER");
                        String cardToken = data.getStringExtra("CARD_TOKEN");
                        tokenCard = data.getStringExtra("CARD_TOKEN");
                        cardView.setText("Name: " + cardName + "\n" + "Number: **** " + cardNumber);
                    }
                }
        );

        paymentButton.setOnClickListener(view -> {
            if(tokenCard != null){

                processmentPay(tokenCard);
            }

        });
        cardView.setOnClickListener(view ->
        {
            Intent intent = new Intent(HomeActivity.this, ListCard.class);
            cardSelectionLauncher.launch(intent);
        });
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void showLoading(boolean isLoading) {
        if (isLoading) {
            progressBar.setVisibility(View.VISIBLE);
            paymentButton.setEnabled(false); // Disable the button while loading
            // Disable card selection
        } else {
            progressBar.setVisibility(View.GONE);
            paymentButton.setEnabled(true); // Re-enable the button

        }
    }

    public void processmentPay (String  token){
        showLoading(true);
        UserDebit user = new UserDebit("4", "rruiz@viamatica.com");
        DebitOrder debit = new DebitOrder(10.0, "payment", "", 0.0, 0 ,0 );
        CardDelete card = new CardDelete(token);

        DebitRequest request  = new DebitRequest(user, debit, card);
        executorService.execute(() -> {
            try {
                DebitResponse response = Nuvei.processDebit(request);

                runOnUiThread(() -> {
                    showLoading(false); // Hide the loading indicator
                    if (response != null) {
                        Gson gson = new Gson();
                        cardView.setText("Select card"+ "\n" + "To continue you need to select one");
                        String debitResponseJson = gson.toJson(response);
                        Intent intent = new Intent(HomeActivity.this, RefundActivity.class);
                        intent.putExtra("DEBIT_RESPONSE", debitResponseJson);
                        startActivity(intent);
                    } else {
                        Toast.makeText(HomeActivity.this, "Payment failed. Response is null.", Toast.LENGTH_SHORT).show();
                    }
                });
            } catch (Exception e) {
                runOnUiThread(() -> {
                    showLoading(false);
                    Toast.makeText(HomeActivity.this, "Exception: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
                e.printStackTrace();
            }
        });
    }
}