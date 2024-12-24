package com.example.eduapp.etudiant.tasks;


import static com.example.eduapp.prof.Config.IP_ADRESS;

import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class InfoEtudRequestTask extends AsyncTask<String, Void, String> {
    private static final String TAG = InfoEtudRequestTask.class.getSimpleName();
    private final OnRequestCompletedListener listener;

    public InfoEtudRequestTask(OnRequestCompletedListener listener) {
        this.listener = listener;
    }

    @Override
    protected String doInBackground(String... params) {
        String email = params[0];
        String resultJson = null;

        try {
            URL url = new URL(IP_ADRESS + "/api/etudiants/InfoEtud?email=" + email);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

            try {
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                StringBuilder stringBuilder = new StringBuilder();

                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    stringBuilder.append(line).append("\n");
                }

                bufferedReader.close();

                // Parse the JSON string into a JSONObject
                resultJson = stringBuilder.toString();
            } finally {
                urlConnection.disconnect();
            }
        } catch (IOException e) {
            Log.e(TAG, "Error making InfoProf request", e);
        }

        return resultJson;
    }

    @Override
    protected void onPostExecute(String resultJson) {
        listener.onRequestCompleted(resultJson);
    }

    public interface OnRequestCompletedListener {
        void onRequestCompleted(String resultJson);
    }
}