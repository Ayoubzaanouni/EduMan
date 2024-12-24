package com.example.eduapp.prof;


import static com.example.eduapp.prof.Config.IP_ADRESS;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.eduapp.R;
import com.example.eduapp.prof.tasks.InfoProfRequestTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ProfActivity extends AppCompatActivity {

    LinearLayout logout,emploi,demanderatt,listratt,createass,listass;
    ImageView image;

    TextView nom,bonjour;
    InfoProfRequestTask infoProfRequestTask = new InfoProfRequestTask(new InfoProfRequestTask.OnRequestCompletedListener() {
        @Override
        public void onRequestCompleted(String resultJson) {
            // Handle the JSONObject here
            try {
                JSONArray resultArray = new JSONArray(resultJson);
                if (resultArray.length() > 0) {
                    JSONObject profObject = resultArray.getJSONObject(0);
                    nom.setText("Welcome " + profObject.getString("nom"));
                }

            } catch (JSONException e) {
                throw new RuntimeException(e);
            }


        }
    });


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_prof);
        logout = findViewById(R.id.logoutprof);
        listratt = findViewById(R.id.listratt);
        bonjour = findViewById(R.id.bonjour);
        image = findViewById(R.id.imageProf);
        listass = findViewById(R.id.listass);
        emploi = findViewById(R.id.emploi);
        createass = findViewById(R.id.createAss);
        nom = findViewById(R.id.nom);
        demanderatt = findViewById(R.id.demandeRatt);
        //if hours > 0 and hours < 12 then bonjour.setText("Bonjour") else bonjour.setText("Bonsoir")

            java.time.LocalTime time = java.time.LocalTime.now();
            if (time.getHour() > 0 && time.getHour() < 12) {
                bonjour.setText("Bonjour");
            } else {
                bonjour.setText("Bonsoir");
            }

        String storedEmail = getSharedPreferences("LoginPrefs", MODE_PRIVATE).getString("email", null);
        String storedImage = getSharedPreferences("LoginPrefs", MODE_PRIVATE).getString("image", null);
        if (storedImage != null) {
            String imageUrl = IP_ADRESS+"/" + storedImage;
            Log.e("image", IP_ADRESS+"/" + storedImage);

            // Use Picasso to load and display the image
            Glide.with(this)
                    .load(imageUrl)
                    .skipMemoryCache(true)  // Skip caching in memory
                    .diskCacheStrategy(DiskCacheStrategy.NONE)  // Skip caching on disk
                    .into(image);        }
        else {
            image.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.ic_default, null));
        }
        infoProfRequestTask.execute(storedEmail);

        listratt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(ProfActivity.this, ListDemandeRatt.class);
                startActivity(intent);
            }
        });
        listass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProfActivity.this, ListAss.class);
                startActivity(intent);
            }
        });
        demanderatt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProfActivity.this, DemandeRatt.class);
                startActivity(intent);
            }
        });
        emploi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProfActivity.this, EmploiProf.class);
                startActivity(intent);
            }
        });
        createass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProfActivity.this, CreateAss.class);
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
                    editor.remove("image");
                    editor.apply();
                    Intent intent = new Intent(ProfActivity.this, LoginActivity.class);
                    startActivity(intent);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });
    }
}