package com.example.eduapp.etudiant.tasks;

import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedOutputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class SendDepotTask extends AsyncTask<String, Void, Void> {

    @Override
    protected Void doInBackground(String... params) {
        String apiUrl = params[0];
        String jsonInputString = params[1];

        try {
            URL url = new URL(apiUrl);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

            // Set up the connection properties
            urlConnection.setRequestMethod("POST");
            urlConnection.setDoOutput(true);
            urlConnection.setRequestProperty("Content-Type", "application/json");
            urlConnection.setRequestProperty("Accept", "application/json");

            // Convert the JSON string to bytes and write to the connection's output stream
            byte[] input = jsonInputString.getBytes("utf-8");
            try (OutputStream os = new BufferedOutputStream(urlConnection.getOutputStream())) {
                os.write(input, 0, input.length);
            }

            // Get the response from the server (if needed)
            int responseCode = urlConnection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_CREATED) {
                // Successful request
                // You can handle the response here if necessary
                Log.e("SendAssignmentTask", "Request successful");
            } else {
                // Handle error
                Log.e("SendAssignmentTask", "Request failed");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
}
