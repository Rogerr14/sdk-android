package com.nuvei.sdk.addCard;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.button.MaterialButton;
import com.nuvei.nuveisdk.model.ErrorResponseModel;
import com.nuvei.nuveisdk.model.addCard.AddCardFormListener;
import com.nuvei.nuveisdk.widget.NuveiAddCardForm;
import com.nuvei.sdk.R;

public class AddCardActivity extends AppCompatActivity {


    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_add_card);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });



        NuveiAddCardForm addCardForm = findViewById(R.id.card_form_widget);
         progressBar = findViewById(R.id.progress_bar);

        addCardForm.setFormListener(new AddCardFormListener() {
            @Override
            public void onSuccess(boolean success) {

            }

            @Override
            public void onError(ErrorResponseModel error) {

            }

            @Override
            public void onLoading(boolean isLoading) {
                Log.e("NuveiSDK", "Error: " + isLoading);
                showLoading(isLoading);
            }


        });


    }


    private void showLoading(boolean isLoading) {
        if (isLoading) {
            progressBar.setVisibility(View.VISIBLE);
        } else {
            progressBar.setVisibility(View.GONE);
        }
    }

}