package com.example.eduapp.prof.tasks;

import static com.example.eduapp.prof.Config.IP_ADRESS;

import android.os.AsyncTask;
import android.util.Log;

import com.example.eduapp.prof.models.Demande;
import com.google.gson.Gson;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

// Add this class to your project
public class SubmitDemandeTask extends AsyncTask<Void, Void, Boolean> {

    private Demande demandeRatt;
    private SubmitDemandeListener listener;

    public SubmitDemandeTask(Demande demandeRatt, SubmitDemandeListener listener) {
        this.demandeRatt = demandeRatt;
        this.listener = listener;
    }

    @Override
    protected Boolean doInBackground(Void... params) {
        try {
            URL url = new URL(IP_ADRESS+"/api/profs/"); // Replace with your actual API endpoint
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

            try {
                urlConnection.setRequestMethod("POST");
                urlConnection.setRequestProperty("Content-Type", "application/json");
                urlConnection.setDoOutput(true);

                // Convert DemandeRatt object to JSON
                Gson gson = new Gson();
                String requestBody = gson.toJson(demandeRatt);
                Log.e("mmm",requestBody);

                // Write the JSON data to the output stream
                urlConnection.getOutputStream().write(requestBody.getBytes());

                int responseCode = urlConnection.getResponseCode();
                return responseCode == HttpURLConnection.HTTP_CREATED; // Adjust based on your API response code for success

            } finally {
                urlConnection.disconnect();
            }
        } catch (IOException e) {
            Log.e("SubmitDemandeTask", "Error making POST request", e);
            return false;
        }
    }

    @Override
    protected void onPostExecute(Boolean success) {
        if (listener != null) {
            listener.onSubmitDemandeCompleted(success);
        }
    }

    public interface SubmitDemandeListener {
        void onSubmitDemandeCompleted(boolean success);
    }
}
