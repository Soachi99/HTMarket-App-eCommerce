package com.example.htmarketfinal;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.htmarketfinal.Model.Usuarios;
import com.example.htmarketfinal.Prevalent.Prevalent;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import io.paperdb.Paper;

public class MainActivity extends AppCompatActivity {

    private Button loginbtn, registbtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        loginbtn = (Button) findViewById(R.id.loginbtn);
        registbtn = (Button) findViewById(R.id.regisbtn);

        Paper.init(this);

        loginbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });

        registbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });

        String Celular = Paper.book().read(Prevalent.CelularKey);
        String Contra = Paper.book().read(Prevalent.ContraKey);

        if (Celular != "" && Contra != "")
        {
            if (!TextUtils.isEmpty(Celular) && !TextUtils.isEmpty(Contra))
            {
                AllowAccess(Celular,Contra);
            }
        }
    }

    private void AllowAccess(String celular, String contra)
    {
        final DatabaseReference RootRef;
        RootRef = FirebaseDatabase.getInstance().getReference();

        RootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.child("Usuarios").child(celular).exists())
                {
                    Usuarios usersData = snapshot.child("Usuarios").child(celular).getValue(Usuarios.class);

                    if (usersData.getCelular().equals(celular))
                    {
                        if (usersData.getContra().equals(contra))
                        {
                            Toast.makeText(MainActivity.this, "Sesión iniciada", Toast.LENGTH_SHORT).show();

                            Intent intent = new Intent(MainActivity.this, HomeActivity.class);
                            startActivity(intent);
                        }
                        else
                        {
                            Toast.makeText(MainActivity.this, "Contraseña incorrecta", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
                else
                {
                    Toast.makeText(MainActivity.this, "La cuenta con el numero " + celular + " no se encuentra registrada", Toast.LENGTH_SHORT).show();

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}