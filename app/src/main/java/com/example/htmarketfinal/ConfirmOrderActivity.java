package com.example.htmarketfinal;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.example.htmarketfinal.Model.Cart;
import com.example.htmarketfinal.Model.Productos;
import com.example.htmarketfinal.Prevalent.Prevalent;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

import io.paperdb.Paper;

public class ConfirmOrderActivity extends AppCompatActivity {

    private ToggleButton moneyCheck;
    private Button editCart, confirmBuy;
    private TextView totalProducts, cost, total;
    private EditText addressText, detailsText;
    private Long Total;
    private String productsTotal = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_order);

        moneyCheck = (ToggleButton) findViewById(R.id.efectivo);

        editCart = (Button) findViewById(R.id.editcart);
        confirmBuy = (Button) findViewById(R.id.confirmBuy);

        totalProducts = (TextView) findViewById(R.id.textTotal_Productos);
        cost = (TextView) findViewById(R.id.textCosto);
        total = (TextView) findViewById(R.id.textTotal);

        addressText = (EditText) findViewById(R.id.textAddress);
        detailsText = (EditText) findViewById(R.id.textDetails);

        editCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ConfirmOrderActivity.this, CartActivity.class);
                startActivity(intent);
            }
        });

        confirmBuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkData();
            }
        });


        calculoTotal();


    }

    private void checkData() {
        String Address = addressText.getText().toString();
        String Details = detailsText.getText().toString();


        if(TextUtils.isEmpty(Address))
        {
            Toast.makeText(this, "Por favor escriba la dirección de entrega", Toast.LENGTH_SHORT).show();
        }
        if(!moneyCheck.isChecked())
        {
            Toast.makeText(this, "Por selecciona el metodo de pago", Toast.LENGTH_SHORT).show();
        }
        else
        {
            addDomicilio();
        }

    }

    private void addDomicilio() {
        Paper.init(this);
        String Address = addressText.getText().toString();
        String Details = detailsText.getText().toString();
        final String saveCurrentTime, saveCurrentDate;

        Calendar calForDate = Calendar.getInstance();
        SimpleDateFormat currentDate = new SimpleDateFormat("MMM dd, yyyy");
        saveCurrentDate = currentDate.format(calForDate.getTime());

        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss a");
        saveCurrentTime = currentTime.format(calForDate.getTime());

        String Celular = Paper.book().read(Prevalent.CelularKey);

        DatabaseReference orderListRef = FirebaseDatabase.getInstance().getReference().child("Pedidos").child(Celular).child(saveCurrentDate).child(saveCurrentTime);
        DatabaseReference productRef = FirebaseDatabase.getInstance().getReference().child("Cart List").child("Usuario").child(Celular)
                                        .child(saveCurrentDate);

        final HashMap<String, Object> orderMap = new HashMap<>();
        final HashMap<String, Object> productsMap = new HashMap<>();

        orderMap.put("Total", Total);
        orderMap.put("Telefono", Celular);
        orderMap.put("Dirección", Address);
        orderMap.put("Detalles", Details);
        orderMap.put("fecha", saveCurrentDate);
        orderMap.put("tiempo", saveCurrentTime);
        orderMap.put("Estado", "En curso");

        orderListRef.updateChildren(orderMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful())
                {
                    productRef.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                Cart productos = dataSnapshot.getValue(Cart.class);
                                productsMap.put("Nombre", productos.getNombre());
                                productsMap.put("Cantidad", productos.getCantidad());
                                productsMap.put("Precio", productos.getPrecio());

                                orderListRef.child("Productos").child(productos.getNombre()).updateChildren(productsMap);
                            }
                            productRef.removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful())
                                    {
                                        Toast.makeText(ConfirmOrderActivity.this, "Su orden se ha cargado al sistema", Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(ConfirmOrderActivity.this, HomeActivity.class);
                                        startActivity(intent);
                                    }
                                }
                            });
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
            }
        });







    }

    private void calculoTotal() {
        Long costo = 3500L;

        productsTotal = getIntent().getStringExtra("Total");

        Total = Long.parseLong(productsTotal) + costo;

        totalProducts.setText("$ " + productsTotal);
        cost.setText("$ " + Long.toString(costo));
        total.setText("$ " + Long.toString(Total));


    }
}