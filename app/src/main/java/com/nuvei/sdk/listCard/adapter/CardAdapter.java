package com.nuvei.sdk.listCard.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.nuvei.nuveisdk.model.listCard.CardItem;
import com.nuvei.sdk.R;


import java.util.List;

public class CardAdapter extends RecyclerView.Adapter<CardAdapter.CardViewHolder>{
    private List<CardItem> cardList;

    public CardAdapter(List<CardItem> cardList) {
        this.cardList = cardList;
    }
    @NonNull
    @Override
    public CardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_item, parent, false);
        return new CardViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CardViewHolder holder, int position) {
        CardItem card = cardList.get(position);
        // Aquí debes enlazar los datos del objeto Card a las vistas del ViewHolder
        holder.cardName.setText("Name: " + card.getHolderName());
        // ... (y así sucesivamente para los demás campos)
    }

    @Override
    public int getItemCount() {
        return cardList.size();
    }

    static class CardViewHolder extends RecyclerView.ViewHolder {
        ImageView cardLogo;
        ImageView deleteButton;
        TextView cardName;
        // ... Y los demás TextViews

        CardViewHolder(@NonNull View itemView) {
            super(itemView);
            cardLogo = itemView.findViewById(R.id.card_logo);
            deleteButton = itemView.findViewById(R.id.delete_button);
            cardName = itemView.findViewById(R.id.card_name);
            // ...
        }
    }
}
