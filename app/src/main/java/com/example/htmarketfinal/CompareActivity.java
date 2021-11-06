package com.example.htmarketfinal;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.htmarketfinal.Model.Productos;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;

public class CompareActivity extends AppCompatActivity  {

    Spinner opcion1, opcion2;
    ImageView imagen1, imagen2;
    TextView precio1, precio2;

    Long Precio_p1, Precio_p2;

    Boolean band = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compare);

        opcion1 = (Spinner) findViewById(R.id.spinnerone);
        opcion2 = (Spinner) findViewById(R.id.spinnertwo);

        imagen1 = (ImageView) findViewById(R.id.producto_comparar_1);
        imagen2 = (ImageView) findViewById(R.id.producto_comparar_2);

        precio1 = (TextView) findViewById(R.id.precio_comparar_1);
        precio2 = (TextView) findViewById(R.id.precio_comparar_2);


        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.opciones_productos, android.R.layout.simple_spinner_item);

        opcion1.setAdapter(adapter);
        opcion2.setAdapter(adapter);

        DatabaseReference productsRef = FirebaseDatabase.getInstance().getReference().child("Productos");

        opcion1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String producto_one = parent.getItemAtPosition(position).toString();

                productsRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                            if (dataSnapshot.child("nombre").getValue().toString().equals(producto_one)){
                                Productos productos = dataSnapshot.getValue(Productos.class);

                                Precio_p1 = productos.getPrecio();

                                precio1.setText("Precio: " + Precio_p1.toString() );

                                String imagen = productos.getImage().toString();

                                if (band == true)
                                {
                                    CompararPrecio();
                                }

                                StorageReference StoreRef = FirebaseStorage.getInstance().getReference("Productos/" + imagen + ".jpg");

                                try {
                                    File localfile = File.createTempFile("tempfile",".jpg");
                                    StoreRef.getFile(localfile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                                        @Override
                                        public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                                            Bitmap bitmap = BitmapFactory.decodeFile(localfile.getAbsolutePath());
                                            imagen1.setImageBitmap(bitmap);
                                        }
                                    });


                                } catch (IOException e){


                                }


                            }

                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });




        opcion2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String producto_two = parent.getItemAtPosition(position).toString();

                productsRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                            if (dataSnapshot.child("nombre").getValue().toString().equals(producto_two)){
                                Productos productos = dataSnapshot.getValue(Productos.class);

                                Precio_p2 = productos.getPrecio();

                                precio2.setText("Precio: " + Precio_p2.toString() );
                                band = true;
                                CompararPrecio();

                                String imagen = productos.getImage().toString();


                                StorageReference StoreRef = FirebaseStorage.getInstance().getReference("Productos/" + imagen + ".jpg");

                                try {
                                    File localfile = File.createTempFile("tempfile",".jpg");
                                    StoreRef.getFile(localfile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                                        @Override
                                        public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                                            Bitmap bitmap = BitmapFactory.decodeFile(localfile.getAbsolutePath());
                                            imagen2.setImageBitmap(bitmap);
                                        }
                                    });


                                } catch (IOException e){


                                }
                            }

                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });




    }

    private void CompararPrecio() {

        String P1 = precio1.getText().toString();
        Integer L1 = P1.length();
        String precio_s1 = P1.substring(8,L1);
        Precio_p1 = Long.parseLong(precio_s1);

        String P2 = precio2.getText().toString();
        Integer L2 = P2.length();
        String precio_s2 = P2.substring(8,L2);
        Precio_p2 = Long.parseLong(precio_s2);


        if (Precio_p1 < Precio_p2)
        {
            precio1.setTextColor(Color.GREEN);
            precio2.setTextColor(Color.RED);
        }
        else if (Precio_p2 < Precio_p1)
        {
            precio1.setTextColor(Color.RED);
            precio2.setTextColor(Color.GREEN);
        }
        else
        {
            precio1.setTextColor(Color.GRAY);
            precio2.setTextColor(Color.GRAY);
        }
    }


}