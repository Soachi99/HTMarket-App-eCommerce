package com.example.htmarketfinal.ViewHolder;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.htmarketfinal.Interface.ItemClickListener;
import com.example.htmarketfinal.R;

public class DetailsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public TextView nameText, quantityText, priceText, totalPrice;
    private ItemClickListener itemClickListener;


    public DetailsViewHolder(@NonNull View itemView) {
        super(itemView);

        nameText = itemView.findViewById(R.id.product_name_cart_hist);
        quantityText = itemView.findViewById(R.id.product_quantity_hist);
        priceText = itemView.findViewById(R.id.product_price_cart_hist);
        totalPrice = itemView.findViewById(R.id.product_price_cart_total_hist);

    }

    @Override
    public void onClick(View v) {
        itemClickListener.onClick(v,getAdapterPosition(),false);

    }

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }
}
