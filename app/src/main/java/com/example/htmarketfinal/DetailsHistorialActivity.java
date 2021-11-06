package com.example.htmarketfinal;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.os.Debug;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.htmarketfinal.Model.DetallesProductos;
import com.example.htmarketfinal.Prevalent.Prevalent;
import com.example.htmarketfinal.ViewHolder.DetailsViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import io.paperdb.Paper;

public class DetailsHistorialActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private String fecha, hora;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details_historial);

        recyclerView = findViewById(R.id.products_details_historial);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        fecha = getIntent().getStringExtra("Fecha");
        hora = getIntent().getStringExtra("Hora");

    }

    @Override
    protected void onStart() {
        super.onStart();

        Paper.init(this);
        String Celular = Paper.book().read(Prevalent.CelularKey);

        final DatabaseReference histRef = FirebaseDatabase.getInstance().getReference().child("Pedidos").child(Celular).child(fecha).child(hora).child("Productos");

        histRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                FirebaseRecyclerOptions<DetallesProductos> options = new FirebaseRecyclerOptions.Builder<DetallesProductos>()
                        .setQuery(histRef,DetallesProductos.class).build();

                FirebaseRecyclerAdapter<DetallesProductos, DetailsViewHolder> adapter = new FirebaseRecyclerAdapter<DetallesProductos, DetailsViewHolder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull DetailsViewHolder holder, int position, @NonNull DetallesProductos model)
                    {

                        holder.nameText.setText(model.getNombre());
                        holder.priceText.setText("$ " +model.getPrecio());
                        holder.quantityText.setText("Cantidad: "+model.getCantidad());

                        Long operation = Long.parseLong(model.getCantidad()) * Long.parseLong(model.getPrecio());
                        holder.totalPrice.setText("Total: $ " + Long.toString(operation));


                    }

                    @NonNull
                    @Override
                    public DetailsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.detailsproducts_items,parent,false);
                        DetailsViewHolder holder = new DetailsViewHolder(view);
                        return holder;
                    }
                };
                recyclerView.setAdapter(adapter);
                adapter.startListening();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}