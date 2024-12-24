package com.example.eduapp.etudiant;



import static com.example.eduapp.prof.Config.IP_ADRESS;

import android.app.DownloadManager;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.eduapp.R;

public class EmploiActivity extends AppCompatActivity {

    Button returnratt,download;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emploi);
        returnratt = findViewById(R.id.return_emploi);
        download = findViewById(R.id.download);
        returnratt.setOnClickListener(v -> finish());
// Inside the onCreate method
        download.setOnClickListener(v -> downloadImage());


        // Retrieve shared preferences using the provided context
        String stored_class_id = getSharedPreferences("LoginPrefs", MODE_PRIVATE)
                .getString("classe_id", null);

        ImageView imageView = findViewById(R.id.emploiView);

        // URL of the image you want to load
        String imageUrl = IP_ADRESS + "/api/etudiants/emploi/"+stored_class_id;
        Log.e("emploi",IP_ADRESS +"/api/etudiants/emploi/"+stored_class_id);

        // Use Picasso to load and display the image
        Glide.with(this)
                .load(imageUrl)
                .skipMemoryCache(true)  // Skip caching in memory
                .diskCacheStrategy(DiskCacheStrategy.NONE)  // Skip caching on disk
                .into(imageView);

    }
    private void downloadImage() {
        Toast.makeText(this, "Downloading emploi...", Toast.LENGTH_SHORT).show();
        String stored_class_id = getSharedPreferences("LoginPrefs", MODE_PRIVATE)
                .getString("classe_id", null);

        // URL of the image you want to download
        String imageUrl = IP_ADRESS + "/api/etudiants/emploi/" + stored_class_id;

        // Use DownloadManager to initiate the download
        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(imageUrl));
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        request.setTitle("Image Download");
        request.setDescription("Downloading image...");

        // Set the destination directory and file name
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, "emploi.jpg");

        // Get the download service and enqueue the request
        DownloadManager downloadManager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
        if (downloadManager != null) {
            downloadManager.enqueue(request);
        }
    }
}