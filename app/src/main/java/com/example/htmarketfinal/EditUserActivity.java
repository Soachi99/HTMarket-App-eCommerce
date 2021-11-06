package com.example.htmarketfinal;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.htmarketfinal.Model.Upload;
import com.example.htmarketfinal.Model.Usuarios;
import com.example.htmarketfinal.Prevalent.Prevalent;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

import io.paperdb.Paper;

public class EditUserActivity extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 1;

    private Button uploadImageBtn, saveBtn;
    private EditText inputName, inputEmail, inputContra;
    private DatabaseReference RootRef;
    private String Usuario_2, Correo_2, Nombre, Correo, Celular;

    private Uri mImageUri;
    private ImageView profileImage;

    private StorageReference storageRef;
    private DatabaseReference dataRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_user);

        ChargeData();

        uploadImageBtn = (Button) findViewById(R.id.foto);
        saveBtn = (Button) findViewById(R.id.Guardar);

        inputName = (EditText) findViewById(R.id.change_user);
        inputEmail = (EditText) findViewById(R.id.change_email);
        inputContra = (EditText) findViewById(R.id.Password);

        profileImage = (ImageView) findViewById(R.id.profile_image);

        storageRef = FirebaseStorage.getInstance().getReference("Usuarios");
        dataRef = FirebaseDatabase.getInstance().getReference();

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mImageUri != null){
                    uploadFile();
                }
                ChangeData();
            }
        });

        uploadImageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OpenFile();
            }
        });
    }

    private void OpenFile(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent,PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data!= null && data.getData() != null)
        {
            mImageUri = data.getData();
            Picasso.get().load(mImageUri).into(profileImage);
        }
    }

    private String getFileExtension(Uri uri){
        ContentResolver CR = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(CR.getType(uri));
    }

    private void uploadFile(){
        StorageReference fileRef = storageRef.child(Celular);

        fileRef.child(Celular).putFile(mImageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Toast.makeText(EditUserActivity.this, "Imagen de perfil actualizada", Toast.LENGTH_SHORT).show();

                Upload upload = new Upload(Celular, fileRef.getDownloadUrl().toString());
                dataRef.child("Uploads").child(Celular).setValue(upload);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(EditUserActivity.this, "Error de red, intente de nuevo en unos minutos", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void ChangeData(){
        Nombre = inputName.getText().toString();
        Correo = inputEmail.getText().toString();
        String Password = inputContra.getText().toString();


        if (TextUtils.isEmpty(Nombre))
        {
            Nombre = Usuario_2;
        }
        if (TextUtils.isEmpty(Correo))
        {
            Correo = Correo_2;
        }

        if(TextUtils.isEmpty(Password))
        {
            Toast.makeText(EditUserActivity.this, "Por favor escriba su contraseña para realizar el cambio de datos", Toast.LENGTH_SHORT).show();
        }
        else
        {
            Validacion(Nombre,Correo,Password);
        }
    }

    private void ChargeData() {
        Paper.init(this);
        Celular = Paper.book().read(Prevalent.CelularKey);

        RootRef = FirebaseDatabase.getInstance().getReference();

        RootRef.child("Usuarios").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (snapshot.exists()) {
                    Usuario_2 = snapshot.child(Celular).child("Usuario").getValue().toString();
                    Correo_2 = snapshot.child(Celular).child("Correo").getValue().toString();
                    inputName.setHint(Usuario_2);
                    inputEmail.setHint(Correo_2);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void Validacion(String nombre, String correo, String password){
        Paper.init(this);
        Celular = Paper.book().read(Prevalent.CelularKey);

        final DatabaseReference RootRef2;
        RootRef2 = FirebaseDatabase.getInstance().getReference();
        RootRef2.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (snapshot.exists()) {

                    Usuarios usersData = snapshot.child("Usuarios").child(Celular).getValue(Usuarios.class);

                    if (usersData.getContra().equals(password)) {

                        HashMap<String, Object> userdataMap = new HashMap<>();
                        userdataMap.put("Usuario", nombre);
                        userdataMap.put("Correo", correo);

                        RootRef2.child("Usuarios").child(Celular).updateChildren(userdataMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(EditUserActivity.this, "Datos actualizados", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(EditUserActivity.this, OptionsActivity.class);
                                    startActivity(intent);
                                }
                                else
                                {
                                    Toast.makeText(EditUserActivity.this, "Error en red, intenta de nuevo en unos minutos", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }
                    else {
                        Toast.makeText(EditUserActivity.this, "Contraseña incorrecta", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}