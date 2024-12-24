package com.example.eduapp.etudiant;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.eduapp.R;
import com.example.eduapp.etudiant.tasks.InfoEtudRequestTask;
import com.example.eduapp.prof.LoginActivity;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class EtudiantActivity extends AppCompatActivity  {

    Button logout, assignment, emploi;
    TextView hello, classe;
    InfoEtudRequestTask infoEtudRequestTask = new InfoEtudRequestTask(new InfoEtudRequestTask.OnRequestCompletedListener() {
        @Override
        public void onRequestCompleted(String resultJson) {
            // Handle the JSONObject here
            try {
                JSONArray resultArray = new JSONArray(resultJson);
                JSONObject etudObject = resultArray.getJSONObject(0);
                hello.setText("Welcome "+etudObject.getString("etudiantNomComplet"));
                classe.setText("Classe: "+etudObject.getString("classeCode"));
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }            }
    });
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_etudiant);
        logout = findViewById(R.id.logout);
        assignment = findViewById(R.id.assbtn);
        hello = findViewById(R.id.hello);
        classe = findViewById(R.id.classe);
        emploi = findViewById(R.id.emploi);

        String storedEmail = getSharedPreferences("LoginPrefs", MODE_PRIVATE).getString("email", null);
        infoEtudRequestTask.execute(storedEmail);

        emploi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EtudiantActivity.this, EmploiActivity.class);
                startActivity(intent);
            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //send a http request to the server
                try {
                    SharedPreferences preferences = getSharedPreferences("LoginPrefs", MODE_PRIVATE);
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.remove("email");
                    editor.remove("type");
                    editor.remove("id");
                    editor.remove("classe_id");
//                  editor.remove("classe_code");
                    editor.apply();
                    Intent intent = new Intent(EtudiantActivity.this, LoginActivity.class);
                    startActivity(intent);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });

        assignment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(EtudiantActivity.this, AssignmentsListActivity.class);
                startActivity(intent);
            }
        });

    }


}