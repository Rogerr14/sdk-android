package com.nuvei.sdk.listCard;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;
import com.nuvei.nuveisdk.Nuvei;
import com.nuvei.nuveisdk.model.listCard.CardItem;
import com.nuvei.nuveisdk.model.listCard.ListCardResponse;
import com.nuvei.nuveisdk.model.listCard.MessageResponse;
import com.nuvei.sdk.R;
import com.nuvei.sdk.addCard.AddCardActivity;
import com.nuvei.sdk.listCard.adapter.CardAdapter;
import com.nuvei.sdk.listCard.listener.OnCardActionListener;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ListCard extends AppCompatActivity implements OnCardActionListener {

    private ExecutorService executorService = Executors.newSingleThreadExecutor();
    private RecyclerView recyclerView;
    private CardAdapter cardAdapter;
    private ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_list_card);
        progressBar = findViewById(R.id.progress_bar);
        recyclerView = findViewById(R.id.card_list_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        MaterialButton buttonReload = findViewById(R.id.reload_button);
        buttonReload.setOnClickListener(v-> {
            recyclerView.setAdapter(null);
            loadCards();
        });
        MaterialButton addCardButton = findViewById(R.id.add_card_button);
        addCardButton.setOnClickListener(v->{
            Intent intent = new Intent(ListCard.this, AddCardActivity.class);
            startActivity(intent);
        });
        loadCards();
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void showLoading(boolean isLoading) {
        if (isLoading) {
            progressBar.setVisibility(View.VISIBLE);
        } else {
            progressBar.setVisibility(View.GONE);
        }
    }

    @Override
    public void onDeleteCard(String cardToken) {
        runOnUiThread(()-> showLoading(true));
        executorService.execute(() -> {
            try {
                MessageResponse message = Nuvei.deleteCard("4", cardToken);
                runOnUiThread(() -> {
                    showLoading(false);
                    if (message != null ) {
                        Toast.makeText(ListCard.this, "Card deleted successfully", Toast.LENGTH_SHORT).show();
                        loadCards(); // Recarga la lista
                    } else {
                        Toast.makeText(ListCard.this, "Error deleting card", Toast.LENGTH_SHORT).show();
                    }
                });
            } catch (Exception e) {


                runOnUiThread(() -> {
                    showLoading(false);
                    Toast.makeText(ListCard.this, "Exception: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
                e.printStackTrace();
            }
        });
    }

    @Override
    public void onCardSelected(CardItem card) {
        Intent resultIntent = new Intent();
        resultIntent.putExtra("CARD_NAME", card.getHolderName());
        resultIntent.putExtra("CARD_NUMBER", card.getNumber());
        resultIntent.putExtra("CARD_TOKEN", card.getToken());
        setResult(RESULT_OK, resultIntent);
        finish();
    }

    public void loadCards(){
        showLoading(true);
        executorService.execute(()->{
            try {
                ListCardResponse response = Nuvei.listCards("4");
                List<CardItem> cardList = response.getCards();

                runOnUiThread(()->{
                    showLoading(false);
                    if(cardList != null){
                        cardAdapter = new CardAdapter(cardList, this);
                        recyclerView.setAdapter(cardAdapter);
                    }
                });
            } catch (Exception e) {
                showLoading(false);
                runOnUiThread(() -> Toast.makeText(ListCard.this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show());
                e.printStackTrace();
            }
        });
    }

}
