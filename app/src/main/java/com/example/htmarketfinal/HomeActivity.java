package com.example.htmarketfinal;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.htmarketfinal.Adapters.MyAdapter;
import com.example.htmarketfinal.Interface.ItemClickListener;
import com.example.htmarketfinal.Model.Productos;


import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;


import java.io.IOException;
import java.util.ArrayList;

public class HomeActivity extends AppCompatActivity {

    private Button CuentaButton, CasaButton, CarritoButton, FavoButton;

    private DatabaseReference productsRef;
    private ItemClickListener listener;
    private RecyclerView recyclerView;
    MyAdapter myAdapter;
    ArrayList<Productos> list;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        recyclerView = findViewById(R.id.recycler_menu);

        productsRef = FirebaseDatabase.getInstance().getReference().child("Productos");

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        CuentaButton = (Button) findViewById(R.id.cuenta);
        CasaButton = (Button) findViewById(R.id.casabutton);
        CarritoButton = (Button) findViewById(R.id.carritobutton);
        FavoButton = (Button) findViewById(R.id.favoritobutton);

        list = new ArrayList<>();
        myAdapter = new MyAdapter(this, list);
        recyclerView.setAdapter(myAdapter);
        
        productsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        Productos productos = dataSnapshot.getValue(Productos.class);
                        list.add(productos);
                    }
                }
                myAdapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(HomeActivity.this, "Error", Toast.LENGTH_SHORT).show();
            }
        });


        CuentaButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, OptionsActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.right_in,R.anim.right_out);
            }
        });

        CarritoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, CartActivity.class);
                startActivity(intent);
            }
        });





    }




}