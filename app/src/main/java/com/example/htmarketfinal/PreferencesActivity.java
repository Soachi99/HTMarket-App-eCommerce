package com.example.htmarketfinal;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Switch;
import android.widget.Toast;

import com.example.htmarketfinal.Prevalent.Prevalent;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

import io.paperdb.Paper;

public class PreferencesActivity extends AppCompatActivity {

    Switch ArtritisS, CancerS, ColesterolS, PielS, DiabetesS, EstrenimientoS, PesoS, EstresS;
    Integer Artritis, Cancer, Colesterol, Piel, Diabetes, Estrenimiento, Peso, Estres;
    Button ConfirmButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preferences);

        ArtritisS = (Switch) findViewById(R.id.Artritis);
        CancerS = (Switch) findViewById(R.id.Cancer);
        ColesterolS = (Switch) findViewById(R.id.Colesterol);
        PielS = (Switch) findViewById(R.id.Piel);
        DiabetesS = (Switch) findViewById(R.id.Diabetes);
        EstrenimientoS = (Switch) findViewById(R.id.Estrenimiento);
        PesoS = (Switch) findViewById(R.id.Peso);
        EstresS = (Switch) findViewById(R.id.Estres);

        ConfirmButton = (Button) findViewById(R.id.PreferencesButton);

        ConfirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CheckData();
            }
        });

    }

    private void CheckData() {

        if (ArtritisS.isChecked()){
            Artritis = 1;
        }
        else {
            Artritis = 0;
        }

        if (CancerS.isChecked()){
            Cancer = 1;
        }
        else {
            Cancer = 0;
        }

        if (ColesterolS.isChecked()){
            Colesterol = 1;
        }
        else {
            Colesterol = 0;
        }

        if (PielS.isChecked()){
            Piel = 1;
        }
        else {
            Piel = 0;
        }

        if (DiabetesS.isChecked()){
            Diabetes = 1;
        }
        else {
            Diabetes = 0;
        }

        if (EstrenimientoS.isChecked()){
            Estrenimiento = 1;
        }
        else {
            Estrenimiento = 0;
        }

        if (PesoS.isChecked()){
            Peso = 1;
        }
        else {
            Peso = 0;
        }

        if (EstresS.isChecked()){
            Estres = 1;
        }
        else {
            Estres = 0;
        }

        SaveData();
    }

    private void SaveData() {
        Paper.init(this);
        final HashMap<String, Object> prefMap = new HashMap<>();
        String Celular = Paper.book().read(Prevalent.CelularKey);

        DatabaseReference PrefRef = FirebaseDatabase.getInstance().getReference().child("Preferencia").child(Celular);

        prefMap.put("Artritis",Artritis);
        prefMap.put("Cancer",Cancer);
        prefMap.put("Colesterol",Colesterol);
        prefMap.put("Piel",Piel);
        prefMap.put("Diabetes",Diabetes);
        prefMap.put("Estreñimiento",Estrenimiento);
        prefMap.put("Peso",Peso);
        prefMap.put("Estres",Estres);

        PrefRef.updateChildren(prefMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful())
                {
                    Toast.makeText(PreferencesActivity.this, "Preferencias guardadas correctamente", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(PreferencesActivity.this, HomeActivity.class);
                    startActivity(intent);
                }
            }
        });


    }


}