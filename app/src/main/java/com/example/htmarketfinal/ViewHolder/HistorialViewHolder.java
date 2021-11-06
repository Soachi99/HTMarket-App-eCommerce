package com.example.htmarketfinal.ViewHolder;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.htmarketfinal.Interface.ItemClickListener;
import com.example.htmarketfinal.R;

public class HistorialViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public TextView textFecha, textState, textTotal;
    private ItemClickListener itemClickListener;

    public HistorialViewHolder(@NonNull View itemView) {
        super(itemView);

        textFecha = itemView.findViewById(R.id.Fecha_historial);
        textState = itemView.findViewById(R.id.Estado);
        textTotal = itemView.findViewById(R.id.Total_historial);
    }

    @Override
    public void onClick(View v) {
        itemClickListener.onClick(v, getAdapterPosition(), false);
    }

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }
}
