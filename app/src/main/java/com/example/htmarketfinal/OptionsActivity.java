package com.example.htmarketfinal;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.htmarketfinal.Model.Upload;
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
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;

import io.paperdb.Paper;

public class OptionsActivity extends AppCompatActivity {

    private TextView TextUser, TextCorreo;
    private Button logoutButton, historialButton, compareButton, preferenceButton, editButton, scanButton;
    private DatabaseReference RootRef;
    private StorageReference StorRef;
    private ImageView profileFoto;
    private Uri mImageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_options);

        editButton = (Button) findViewById(R.id.editar);
        historialButton = (Button) findViewById(R.id.Historial);
        compareButton = (Button) findViewById(R.id.Comparar);
        scanButton = (Button) findViewById(R.id.escaner);
        preferenceButton = (Button) findViewById(R.id.preferencias);
        logoutButton = (Button) findViewById(R.id.cerrar);

        profileFoto = (ImageView) findViewById(R.id.profile_image_op);

        VisibleName();

        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(OptionsActivity.this, EditUserActivity.class);
                startActivity(intent);
            }
        });


        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Paper.book().destroy();

                Intent intent = new Intent(OptionsActivity.this, MainActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.fade_in,R.anim.fade_out);
                Toast.makeText(OptionsActivity.this, "Su sesión a finalizado", Toast.LENGTH_SHORT).show();
            }
        });

        historialButton.setOnClickListener((new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(OptionsActivity.this, HistorialActivity.class);
                startActivity(intent);
            }
        }));

        scanButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(OptionsActivity.this, "Proximamente", Toast.LENGTH_SHORT).show();
            }
        });

        compareButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(OptionsActivity.this, CompareActivity.class);
                startActivity(intent);
            }
        });

        preferenceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(OptionsActivity.this, PreferencesActivity.class);
                startActivity(intent);
            }
        });








    }

    private void VisibleName() {

        Paper.init(this);
        String Celular = Paper.book().read(Prevalent.CelularKey);

        TextUser = (TextView) findViewById(R.id.textousuario);
        TextCorreo = (TextView) findViewById(R.id.textocorreo);

        RootRef = FirebaseDatabase.getInstance().getReference();
        StorRef = FirebaseStorage.getInstance().getReference("Usuarios/" + Celular +"/"+ Celular);


        RootRef.child("Usuarios").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (snapshot.exists())
                {
                    String Usuario = snapshot.child(Celular).child("Usuario").getValue().toString();
                    String Correo = snapshot.child(Celular).child("Correo").getValue().toString();
                    TextUser.setText(Usuario);
                    TextCorreo.setText(Correo);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        try {
            File localfile = File.createTempFile("tempfile",".jpg");
            StorRef.getFile(localfile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                    Bitmap bitmap = BitmapFactory.decodeFile(localfile.getAbsolutePath());
                    profileFoto.setImageBitmap(bitmap);
                }
            });
        } catch (IOException e){


        }









    }
}