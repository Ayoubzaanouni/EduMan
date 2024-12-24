package com.example.eduapp.prof.tasks;

// FetchClassesTask.java

import static com.example.eduapp.prof.Config.IP_ADRESS;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class FetchClassesTask extends AsyncTask<Void, Void, Map<Integer, String>> {

    private FetchClassesTaskListener listener;

    public FetchClassesTask(FetchClassesTaskListener listener) {
        this.listener = listener;
    }

    @Override
    protected Map<Integer, String> doInBackground(Void... params) {
        Map<Integer,String> textValues = new HashMap<Integer,String>();

        try {
            URL url = new URL(IP_ADRESS+"/api/profs/classes"); // Update with your actual server address
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

            try {
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                StringBuilder stringBuilder = new StringBuilder();

                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    stringBuilder.append(line).append("\n");
                }

                bufferedReader.close();

                // Parse the JSON string into a JSONArray
                JSONArray outerArray = new JSONArray(stringBuilder.toString());

                // Process each inner array and extract text values
                for (int i = 0; i < outerArray.length(); i++) {
                    JSONArray innerArray = outerArray.getJSONArray(i);
                    for (int j = 0; j < innerArray.length(); j++) {
                        JSONObject item = innerArray.getJSONObject(j);
                        String textValue = item.optString("text", ""); // Adjust "text" to the actual key in your JSON
                        int value = item.optInt("value", 0); // Adjust "value" to the actual key in your JSON
                        textValues.put(value,textValue);
                    }
                }

            } finally {
                urlConnection.disconnect();
            }
        } catch (IOException | JSONException e) {
            Log.e("TAG", "Error making classes request", e);
        }

        return textValues;
    }

    @Override
    protected void onPostExecute(Map<Integer, String> textValues) {
        // Notify the listener with the result
        if (listener != null) {
            listener.onFetchClassesCompleted(textValues);
        }
    }

    // Interface to notify the calling class about the completion
    public interface FetchClassesTaskListener {
        void onFetchClassesCompleted(Map<Integer, String> textValues);
    }
}
