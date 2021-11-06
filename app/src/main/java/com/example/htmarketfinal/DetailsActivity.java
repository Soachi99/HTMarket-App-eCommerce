package com.example.htmarketfinal;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.example.htmarketfinal.Model.Productos;
import com.example.htmarketfinal.Model.Usuarios;
import com.example.htmarketfinal.Prevalent.Prevalent;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
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
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

import io.paperdb.Paper;

public class DetailsActivity extends AppCompatActivity {

    private TextView textName, textDetails, textPrice;
    private ImageView imageProduct;
    private Button btnAddCart;
    private ElegantNumberButton numberButton;
    private String productName = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        productName = getIntent().getStringExtra("product_name");

        textName = (TextView) findViewById(R.id.product_name_details);
        textDetails = (TextView) findViewById(R.id.product_description_details);
        textPrice = (TextView) findViewById(R.id.product_price_details);
        imageProduct = (ImageView) findViewById(R.id.product_image_details);

        btnAddCart = (Button) findViewById(R.id.btn_add_car);
        numberButton = (ElegantNumberButton) findViewById(R.id.product_count);

        getProductsDetails(productName);

        btnAddCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addingToCartList();
            }
        });


    }

    private void addingToCartList() {

        Paper.init(this);

        String saveCurrentTime, saveCurrentDate;

        Calendar calForDate = Calendar.getInstance();
        SimpleDateFormat currentDate = new SimpleDateFormat("MMM dd, yyyy");
        saveCurrentDate = currentDate.format(calForDate.getTime());

        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss a");
        saveCurrentTime = currentTime.format(calForDate.getTime());

        DatabaseReference cartListRef = FirebaseDatabase.getInstance().getReference().child("Cart List");
        DatabaseReference productsRef = FirebaseDatabase.getInstance().getReference().child("Productos");

        String Celular = Paper.book().read(Prevalent.CelularKey);

        final HashMap<String, Object> cartMap = new HashMap<>();

        productsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {

                    if (dataSnapshot.child("nombre").getValue().toString().equals(productName)) {
                        Productos productos = dataSnapshot.getValue(Productos.class);

                        cartMap.put("image", productos.getImage().toString());
                        cartMap.put("nombre", productos.getNombre());
                        cartMap.put("precio", productos.getPrecio().toString());
                        cartMap.put("fecha", saveCurrentDate);
                        cartMap.put("tiempo", saveCurrentTime);
                        cartMap.put("Cantidad", numberButton.getNumber());

                        cartListRef.child("Usuario").child(Celular).child(saveCurrentDate).child(productos.getNombre())
                        .updateChildren(cartMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful())
                                {
                                    Toast.makeText(DetailsActivity.this, "Producto agregado al carrito", Toast.LENGTH_SHORT).show();

                                    Intent intent = new Intent(DetailsActivity.this, CartActivity.class);
                                    startActivity(intent);
                                }
                            }
                        })
                        ;
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }

    private void getProductsDetails(String productName) {

        DatabaseReference productsRef = FirebaseDatabase.getInstance().getReference().child("Productos");

        productsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists())
                {
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {

                        if(dataSnapshot.child("nombre").getValue().toString().equals(productName))
                        {
                            Productos productos = dataSnapshot.getValue(Productos.class);
                            textName.setText(productos.getNombre());
                            textDetails.setText(productos.getDescripcion());
                            textPrice.setText("$ " + productos.getPrecio().toString());

                            String imagen = productos.getImage().toString();

                            StorageReference StoreRef = FirebaseStorage.getInstance().getReference("Productos/" + imagen + ".jpg");

                            try {
                                File localfile = File.createTempFile("tempfile",".jpg");
                                StoreRef.getFile(localfile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                                    @Override
                                    public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                                        Bitmap bitmap = BitmapFactory.decodeFile(localfile.getAbsolutePath());
                                        imageProduct.setImageBitmap(bitmap);
                                    }
                                });
                            } catch (IOException e){


                            }


                        }
                    }



                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}