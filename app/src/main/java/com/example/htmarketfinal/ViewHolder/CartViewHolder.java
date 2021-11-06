package com.example.htmarketfinal.ViewHolder;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.htmarketfinal.Interface.ItemClickListener;
import com.example.htmarketfinal.R;

public class CartViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public TextView textProductName, textProductQuantity, textProductPrice, textTotalPrice, textFinalTotal;
    private ItemClickListener itemClickListener;

    public CartViewHolder(@NonNull View itemView) {
        super(itemView);

        textProductName = itemView.findViewById(R.id.product_name_cart);
        textProductQuantity = itemView.findViewById(R.id.product_quantity);
        textProductPrice = itemView.findViewById(R.id.product_price_cart);
        textTotalPrice = itemView.findViewById(R.id.product_price_cart_total);

    }

    @Override
    public void onClick(View v) {
        itemClickListener.onClick(v, getAdapterPosition(), false);
    }

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }
}
