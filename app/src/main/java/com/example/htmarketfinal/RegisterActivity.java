package com.example.htmarketfinal;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;


import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class RegisterActivity extends AppCompatActivity {

    private Button CreateAccountButton;
    private EditText InputUsuario, InputPass, InputPass2, InputCell, InputEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        CreateAccountButton = (Button) findViewById(R.id.regisbtn2);
        InputUsuario = (EditText) findViewById(R.id.register_user);
        InputPass = (EditText) findViewById(R.id.editTextTextPassword);
        InputPass2 = (EditText) findViewById(R.id.editTextTextPassword2);
        InputCell = (EditText) findViewById(R.id.celular);
        InputEmail = (EditText) findViewById(R.id.email);

        CreateAccountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CreateAccount();
            }
        });

    }

    private void CreateAccount()
    {
        String Usuario = InputUsuario.getText().toString();
        String Password = InputPass.getText().toString();
        String Password2 = InputPass2.getText().toString();
        String Celular = InputCell.getText().toString();
        String Correo = InputEmail.getText().toString();

        if(TextUtils.isEmpty(Usuario))
        {
            Toast.makeText(this, "Por favor escriba su nombre de usuario", Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(Password) || TextUtils.isEmpty(Password2))
        {
            Toast.makeText(this, "Por favor escriba su contraseña", Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(Celular))
        {
            Toast.makeText(this, "Por favor escriba su numero de celular", Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(Correo))
        {
            Toast.makeText(this, "Por favor escriba su correo electronico", Toast.LENGTH_SHORT).show();
        }
        else if (!(TextUtils.equals(Password,Password2)))
        {
            Toast.makeText(this, "Verifique su contraseña", Toast.LENGTH_SHORT).show();
        }
        else {
            ValidarDatos(Usuario,Password,Celular,Correo);
        }

    }

    private void ValidarDatos(String usuario, String password, String celular, String correo)
    {
        final DatabaseReference RootRef;
        RootRef = FirebaseDatabase.getInstance().getReference();

        RootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot)
            {
                if (!(snapshot.child("Usuarios").child(celular).exists()))
                {
                    HashMap<String, Object> userdataMap = new HashMap<>();
                    userdataMap.put("Usuario", usuario);
                    userdataMap.put("Contra", password);
                    userdataMap.put("Celular", celular);
                    userdataMap.put("Correo", correo);

                    RootRef.child("Usuarios").child(celular).updateChildren(userdataMap)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task)
                                {
                                    if (task.isSuccessful())
                                    {
                                        Toast.makeText(RegisterActivity.this, "Tu cuenta ha sido creada", Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                                        startActivity(intent);
                                        Intent intent2 = new Intent(RegisterActivity.this, LoginActivity.class);
                                        startActivity(intent2);
                                    }
                                    else
                                    {
                                        Toast.makeText(RegisterActivity.this, "Error en red, intenta de nuevo en unos minutos", Toast.LENGTH_SHORT).show();

                                    }
                                }
                            });
                }
                else
                {
                    Toast.makeText(RegisterActivity.this, "Ya hay un usuario registrado con este numero de celular", Toast.LENGTH_SHORT).show();

                    Toast.makeText(RegisterActivity.this, "Intenta nuevamente usando otro numero", Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                    startActivity(intent);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

}