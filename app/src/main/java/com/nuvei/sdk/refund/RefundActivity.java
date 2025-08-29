package com.nuvei.sdk.refund;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.button.MaterialButton;
import com.google.gson.Gson;
import com.nuvei.nuveisdk.Nuvei;
import com.nuvei.nuveisdk.model.debit.DebitResponse;
import com.nuvei.nuveisdk.model.debit.DebitTransactionResponse;
import com.nuvei.nuveisdk.model.refund.OrderRefund;
import com.nuvei.nuveisdk.model.refund.RefundRequest;
import com.nuvei.nuveisdk.model.refund.RefundResponse;
import com.nuvei.nuveisdk.model.refund.TransactionRefund;
import com.nuvei.sdk.R;
import com.nuvei.sdk.home.HomeActivity;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class RefundActivity extends AppCompatActivity {

    private DebitResponse debitResponse;
    private RefundResponse refundResponse;
    private ProgressBar progressBar;
    private MaterialButton refundButton;
    private ExecutorService executorService = Executors.newSingleThreadExecutor();
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_refund);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        TextView transactionIdTextView = findViewById(R.id.transaction_value);
        TextView amountTextView = findViewById(R.id.amount_value);
        TextView authorizationCodeTextView = findViewById(R.id.authorization_value);
        TextView statusTextView = findViewById(R.id.status_value);
        progressBar = findViewById(R.id.progress_bar);
        String debitResponseJson = getIntent().getStringExtra("DEBIT_RESPONSE");
        if(debitResponseJson != null){
            Gson gson = new Gson();
            debitResponse = gson.fromJson(debitResponseJson, DebitResponse.class);
            transactionIdTextView.setText(debitResponse.getTransaction().getId());
            amountTextView.setText("$" + debitResponse.getTransaction().getAmount().toString());
            authorizationCodeTextView.setText(debitResponse.getTransaction().getAuthorizationCode());
            statusTextView.setText(debitResponse.getTransaction().getStatus());

        }

         refundButton = findViewById(R.id.refund_button);


        refundButton.setEnabled(refundResponse == null);

        refundButton.setOnClickListener(view -> {
refundPayment();
        });
    }


    private void showLoading(boolean isLoading) {
        if (isLoading) {
            progressBar.setVisibility(View.VISIBLE);
            refundButton.setEnabled(false); // Disable the button while loading
        } else {
            progressBar.setVisibility(View.GONE);
            refundButton.setEnabled(true); // Re-enable the button
        }
    }


    private void refundPayment(){

        showLoading(true);


        executorService.execute(() -> {
            try {
                DebitTransactionResponse transationInfo =debitResponse.getTransaction();

                TransactionRefund transactionRefund = new TransactionRefund(transationInfo.getId(), null);
                OrderRefund orderRefund =  new OrderRefund(debitResponse.getTransaction().getAmount());
                refundResponse =  Nuvei.refund(transactionRefund, orderRefund, true);

                runOnUiThread(() -> {
                    showLoading(false);
                    if (refundResponse != null) {
                        refundButton.setEnabled(false);
                        refundButton.setBackgroundColor(Color.parseColor("#FF666666"));
                        refundButton.setTextColor(Color.parseColor("#FF000000"));
                        Toast.makeText(RefundActivity.this,"Refund status: "+  refundResponse.getStatus(), Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(RefundActivity.this, "Refund response is null.", Toast.LENGTH_SHORT).show();
                    }
                });
            } catch (Exception e) {
                runOnUiThread(() -> {
                    showLoading(false);
                    Toast.makeText(RefundActivity.this, "Exception: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
                e.printStackTrace();
            }
        });
    }
}
