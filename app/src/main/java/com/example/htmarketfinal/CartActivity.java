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
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.htmarketfinal.Model.Cart;
import com.example.htmarketfinal.Prevalent.Prevalent;
import com.example.htmarketfinal.ViewHolder.CartViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import io.paperdb.Paper;

public class CartActivity extends AppCompatActivity {

    private Button homeButton , favButton, confirmButton;
    private TextView totalPrice;
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private Long Total = 0L;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        recyclerView = findViewById(R.id.product_list);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        homeButton = (Button) findViewById(R.id.casabutton2);
        favButton = (Button) findViewById(R.id.favoritobutton2);
        confirmButton = (Button) findViewById(R.id.confirm_products);

        totalPrice = (TextView) findViewById(R.id.price_total_cart);

        homeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CartActivity.this, HomeActivity.class);
                startActivity(intent);
            }
        });

        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (Total > 0) {
                    Intent intent = new Intent(CartActivity.this, ConfirmOrderActivity.class);
                    intent.putExtra("Total", Long.toString(Total));
                    startActivity(intent);
                }
                else
                {
                    Toast.makeText(CartActivity.this, "No hay productos en tu carrito", Toast.LENGTH_SHORT).show();
                }
            }
        });


    }

    @Override
    protected void onStart() {
        super.onStart();

        Paper.init(this);
        String Celular = Paper.book().read(Prevalent.CelularKey);
        Calendar calForDate = Calendar.getInstance();
        SimpleDateFormat currentDate = new SimpleDateFormat("MMM dd, yyyy");
        String CurrentDate = currentDate.format(calForDate.getTime());


        final DatabaseReference cartRef = FirebaseDatabase.getInstance().getReference().child("Cart List").child("Usuario").child(Celular);

        FirebaseRecyclerOptions<Cart> options = new FirebaseRecyclerOptions.Builder<Cart>().setQuery(cartRef.child(CurrentDate), Cart.class).build();

        FirebaseRecyclerAdapter<Cart, CartViewHolder> adapter = new FirebaseRecyclerAdapter<Cart, CartViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull CartViewHolder holder, int position, @NonNull Cart model) {

                holder.textProductName.setText(model.getNombre());
                holder.textProductQuantity.setText("Cantidad: " + model.getCantidad());
                holder.textProductPrice.setText( "$" + model.getPrecio() + " C/U");

                Long operation = Long.parseLong(model.getCantidad()) * Long.parseLong(model.getPrecio());
                Total = Total + operation;

                holder.textTotalPrice.setText("Total: $ "+ Long.toString(operation));

                totalPrice.setText("Precio Total: $" + Total);

                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v)
                    {
                        CharSequence option[] = new CharSequence[]
                                {
                                        "Editar",
                                        "Remover"
                                };
                        AlertDialog.Builder builder = new AlertDialog.Builder(CartActivity.this);
                        builder.setTitle("Opciones del producto");

                        builder.setItems(option, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if(which == 0)
                                {
                                    Intent intent = new Intent(CartActivity.this, DetailsActivity.class);
                                    intent.putExtra("product_name", model.getNombre());
                                    startActivity(intent);
                                }
                                if(which == 1)
                                {
                                    cartRef.child(CurrentDate).child(model.getNombre()).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful())
                                            {
                                                Toast.makeText(CartActivity.this, "Producto removido", Toast.LENGTH_SHORT).show();
                                                Intent intent = new Intent(CartActivity.this, CartActivity.class);
                                                startActivity(intent);
                                            }
                                        }
                                    });
                                }

                            }
                        });
                        builder.show();
                    }
                });
            }

            @NonNull
            @Override
            public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cart_items, parent, false);
                CartViewHolder holder = new CartViewHolder(view);
                return holder;
            }
        };

        recyclerView.setAdapter(adapter);
        adapter.startListening();


    }
}