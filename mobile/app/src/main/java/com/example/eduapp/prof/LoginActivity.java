package com.example.eduapp.prof;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.eduapp.R;
import com.example.eduapp.etudiant.EtudiantActivity;
import com.example.eduapp.prof.tasks.LogReq;

public class LoginActivity extends AppCompatActivity implements LogReqCallback{

    private static final String PREFS_NAME = "LoginPrefs";

    private static final String PREF_EMAIL = "email";
    private static final String PREF_IMAGE = "image";
    private static final String USER_ID = "id";
    private static final String PREF_CLASSE_ID = "classe_id";

    private static final String PREF_TYPE = "type";


    private EditText usernameEditText, passwordEditText;
    private Button loginButton;

    @Override
    public void onLogReqComplete(String success) {
        String[] parts = success.split(",");
        if (parts[0].equals("Etudiant")) {
            // The asynchronous task was successful
            // Add your code here to handle the success case
            Log.d("Main", "InfoReq success");
            SharedPreferences.Editor editor = getSharedPreferences(PREFS_NAME, MODE_PRIVATE).edit();
            editor.putString(PREF_EMAIL, usernameEditText.getText().toString());
            editor.putString(PREF_TYPE, "Etudiant");
            editor.putString(USER_ID, parts[1]);
            editor.putString(PREF_CLASSE_ID, parts[2]);
//            editor.putString(PREF_CASSE_CODE, parts[3]);
            editor.apply();
            Intent intent = new Intent(LoginActivity.this, EtudiantActivity.class);
//            intent.putExtra("classe", parts[2]);
            startActivity(intent);
        }
        else if(parts[0].equals("Prof")){
            Log.d("Main", "InfoReq success");
            SharedPreferences.Editor editor = getSharedPreferences(PREFS_NAME, MODE_PRIVATE).edit();
            editor.putString(PREF_EMAIL, usernameEditText.getText().toString());
            editor.putString(PREF_TYPE, "Prof");
            editor.putString(USER_ID, parts[1]);
            if (parts.length > 2){
                editor.putString(PREF_IMAGE, parts[2]);
            }

            editor.apply();
            Intent intent = new Intent(LoginActivity.this, ProfActivity.class);
            startActivity(intent);
        }
        else {
            Toast.makeText(this, "Login failed", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        String storedEmail = getSharedPreferences(PREFS_NAME, MODE_PRIVATE).getString(PREF_EMAIL, null);
        String storedType = getSharedPreferences(PREFS_NAME, MODE_PRIVATE).getString(PREF_TYPE, null);
        if (storedEmail != null && !storedEmail.equals("email")) {
            if (storedType.equals("Etudiant")) {
                Intent intent = new Intent(LoginActivity.this, EtudiantActivity.class);
                startActivity(intent);
            }
            else if(storedType.equals("Prof")){
                Intent intent = new Intent(LoginActivity.this, ProfActivity.class);
                startActivity(intent);
            }
        }

        usernameEditText = findViewById(R.id.editTextUsername);
        passwordEditText = findViewById(R.id.editTextPassword);
        loginButton = findViewById(R.id.buttonLogin);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (usernameEditText.getText().toString().isEmpty() || passwordEditText.getText().toString().isEmpty()) {
                    // Display a toast message indicating that fields are required
                    Toast.makeText(LoginActivity.this, "Username and password are required", Toast.LENGTH_SHORT).show();
                }
                else {
                    //send a http request to the server
                    try {
                        LogReq lgq = new LogReq(usernameEditText.getText().toString(), passwordEditText.getText().toString(), LoginActivity.this);
                        lgq.execute();

                        //if user exists

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });

    }
}
