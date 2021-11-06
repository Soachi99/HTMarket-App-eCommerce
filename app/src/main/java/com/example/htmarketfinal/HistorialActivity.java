package com.example.htmarketfinal;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;

import com.example.htmarketfinal.Model.Historial;
import com.example.htmarketfinal.Prevalent.Prevalent;
import com.example.htmarketfinal.ViewHolder.HistorialViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

import io.paperdb.Paper;

public class HistorialActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_historial);

        recyclerView = findViewById(R.id.Historial_items);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);


    }

    @Override
    protected void onStart() {
        super.onStart();

        Paper.init(this);
        String Celular = Paper.book().read(Prevalent.CelularKey);


        final DatabaseReference histRef = FirebaseDatabase.getInstance().getReference().child("Pedidos").child(Celular);

        histRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        FirebaseRecyclerOptions<Historial> options = new FirebaseRecyclerOptions.Builder<Historial>().setQuery(dataSnapshot.getRef(), Historial.class).build();

                        FirebaseRecyclerAdapter<Historial, HistorialViewHolder> adapter = new FirebaseRecyclerAdapter<Historial, HistorialViewHolder>(options) {
                            @Override
                            protected void onBindViewHolder(@NonNull HistorialViewHolder holder, int position, @NonNull Historial model) {
                                holder.textFecha.setText("Fecha: " + model.getFecha());
                                holder.textState.setText("Estado del pedido: " + model.getEstado());
                                holder.textTotal.setText("Total: " + model.getTotal().toString());

                                holder.itemView.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        if (model.getEstado().equals("En curso"))
                                        {
                                            CharSequence option[] = new CharSequence[]
                                                    {
                                                            "Cancelar pedido",
                                                            "Detalles"
                                                    };
                                            AlertDialog.Builder builder = new AlertDialog.Builder(HistorialActivity.this);
                                            builder.setTitle("Opciones");

                                            builder.setItems(option, new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    if (which == 0)
                                                    {
                                                        String fecha = model.getFecha();
                                                        String hora = model.getTiempo();
                                                        final HashMap<String, Object> cancelProduct = new HashMap<>();
                                                        cancelProduct.put("Estado", "Cancelado");
                                                        histRef.child(fecha).child(hora).updateChildren(cancelProduct);
                                                    }
                                                    if (which == 1)
                                                    {
                                                        Intent intent = new Intent(HistorialActivity.this, DetailsHistorialActivity.class);
                                                        String fecha = model.getFecha();
                                                        String hora = model.getTiempo();
                                                        intent.putExtra("Fecha", fecha);
                                                        intent.putExtra("Hora", hora);
                                                        startActivity(intent);

                                                    }
                                                }
                                            });
                                            builder.show();
                                        }
                                    }
                                });

                            }

                            @NonNull
                            @Override
                            public HistorialViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.historial_items,parent,false);
                                HistorialViewHolder holder = new HistorialViewHolder(view);
                                return holder;
                            }
                        };

                        recyclerView.setAdapter(adapter);
                        adapter.startListening();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });




    }
}

