package com.example.htmarketfinal.Adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.sax.StartElementListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.example.htmarketfinal.DetailsActivity;
import com.example.htmarketfinal.Interface.ItemClickListener;
import com.example.htmarketfinal.Model.Productos;
import com.example.htmarketfinal.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {


    Context context;

    ArrayList<Productos> list;

    public MyAdapter(Context context, ArrayList<Productos> list) {
        this.context = context;
        this.list = list;

    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_product_list,parent,false);
        return  new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        Productos productos = list.get(position);
        holder.productName.setText(productos.getNombre());
        holder.descriptText.setText(productos.getDescripcion());
        holder.priceText.setText("$ " + productos.getPrecio().toString());

        String imagen = productos.getImage().toString();

        StorageReference StoreRef = FirebaseStorage.getInstance().getReference("Productos/" + imagen + ".jpg");

        try {
            File localfile = File.createTempFile("tempfile",".jpg");
            StoreRef.getFile(localfile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                    Bitmap bitmap = BitmapFactory.decodeFile(localfile.getAbsolutePath());
                    holder.imageProduct.setImageBitmap(bitmap);
                }
            });
        } catch (IOException e){


        }
    }



    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class  MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{


        TextView productName, descriptText, priceText;
        ImageView imageProduct;
        public ItemClickListener listener;


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            productName = itemView.findViewById(R.id.product_name);
            descriptText = itemView.findViewById(R.id.product_description);
            priceText = itemView.findViewById(R.id.product_price);
            imageProduct = itemView.findViewById(R.id.product_image);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(v.getContext(), DetailsActivity.class);
                    intent.putExtra("product_name",productName.getText());

                    v.getContext().startActivity(intent);

                }
            });

        }

        public void setItemClickListener(ItemClickListener listener){
            this.listener = listener;

        }


        @Override
        public void onClick(View v) {
            listener.onClick(v, getAdapterPosition(), false);

        }
    }

}
