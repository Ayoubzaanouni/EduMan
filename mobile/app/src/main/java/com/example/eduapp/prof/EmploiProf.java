package com.example.eduapp.prof;

import static com.example.eduapp.prof.Config.IP_ADRESS;

import android.app.DownloadManager;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.eduapp.R;

public class EmploiProf extends AppCompatActivity {

    ImageView download;
    LinearLayout returnratt;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prof_emploi);
        returnratt = findViewById(R.id.returnlist_ass);
        download = findViewById(R.id.download);
        returnratt.setOnClickListener(v -> finish());
// Inside the onCreate method
        download.setOnClickListener(v -> downloadImage());

// Add a new method to handle image download


        // Assuming you have an ImageView in your layout with the ID "imageView"
        String storedId = getSharedPreferences("LoginPrefs", MODE_PRIVATE).getString("id", null);

        ImageView imageView = findViewById(R.id.emploiImage);

        // URL of the image you want to load
        String imageUrl = IP_ADRESS+"/api/profs/emploi/"+storedId;
        Log.e("mmm",IP_ADRESS+"/api/profs/emploi/"+storedId);

        // Use Picasso to load and display the image
        Glide.with(this)
                .load(imageUrl)
                .skipMemoryCache(true)  // Skip caching in memory
                .diskCacheStrategy(DiskCacheStrategy.NONE)  // Skip caching on disk
                .into(imageView);

    }
    private void downloadImage() {
        Toast.makeText(this, "Downloading emploi...", Toast.LENGTH_SHORT).show();
        String storedId = getSharedPreferences("LoginPrefs", MODE_PRIVATE).getString("id", null);

        // URL of the image you want to download
        String imageUrl = IP_ADRESS+"/api/profs/emploi/" + storedId;

        // Use DownloadManager to initiate the download
        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(imageUrl));
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        request.setTitle("Image Download");
        request.setDescription("Downloading image...");

        // Set the destination directory and file name
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, "image.jpg");

        // Get the download service and enqueue the request
        DownloadManager downloadManager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
        if (downloadManager != null) {
            downloadManager.enqueue(request);
        }
    }
}
