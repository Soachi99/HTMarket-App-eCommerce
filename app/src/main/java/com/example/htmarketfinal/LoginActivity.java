package com.example.htmarketfinal;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import com.example.htmarketfinal.Model.Usuarios;
import com.example.htmarketfinal.Prevalent.Prevalent;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import io.paperdb.Paper;

public class LoginActivity extends AppCompatActivity {

    private Button loginbtn, registerbtn;
    private EditText InputNumber, InputContra;
    private Switch Remember;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        loginbtn = (Button) findViewById(R.id.logbtn);
        registerbtn = (Button) findViewById(R.id.resbtn);

        InputNumber = (EditText) findViewById(R.id.user_input_number);
        InputContra = (EditText) findViewById(R.id.contra_input_text);

        Remember = (Switch) findViewById(R.id.switch1);

        Paper.init(this);

        registerbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });

        loginbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                loginUser();
            }
        });
    }

    private void loginUser()
    {
        String Numero = InputNumber.getText().toString();
        String Contra = InputContra.getText().toString();

        if(TextUtils.isEmpty(Numero))
        {
            Toast.makeText(this, "Por favor escriba su numero de celular", Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(Contra))
        {
            Toast.makeText(this, "Por favor escriba su contraseña", Toast.LENGTH_SHORT).show();
        }
        else
        {
            AllowAccess(Numero,Contra);
        }

        
    }

    private void AllowAccess(String numero, String contra)
    {
        if (Remember.isChecked())
        {
            Paper.book().write(Prevalent.CelularKey, numero);
            Paper.book().write(Prevalent.ContraKey, contra);
        }

        final DatabaseReference RootRef;
        RootRef = FirebaseDatabase.getInstance().getReference();

        RootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.child("Usuarios").child(numero).exists())
                {
                    Usuarios usersData = snapshot.child("Usuarios").child(numero).getValue(Usuarios.class);

                    if (usersData.getCelular().equals(numero))
                    {
                        if (usersData.getContra().equals(contra))
                        {
                            Toast.makeText(LoginActivity.this, "Iniciando sesión", Toast.LENGTH_SHORT).show();

                            Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                            startActivity(intent);
                        }
                        else
                        {
                            Toast.makeText(LoginActivity.this, "Contraseña incorrecta", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
                else
                {
                    Toast.makeText(LoginActivity.this, "La cuenta con el numero " + numero + " no se encuentra registrada", Toast.LENGTH_SHORT).show();

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}