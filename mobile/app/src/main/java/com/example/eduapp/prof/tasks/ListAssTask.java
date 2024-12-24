package com.example.eduapp.prof.tasks;

import static com.example.eduapp.prof.Config.IP_ADRESS;

import android.os.AsyncTask;
import android.util.Log;

import com.example.eduapp.prof.models.AssignmentModel;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class ListAssTask extends AsyncTask<Integer, Void, List<AssignmentModel>> {

    private OnRequestCompletedListener listener;

    public ListAssTask(OnRequestCompletedListener listener) {
        this.listener = listener;
    }

    @Override
    protected List<AssignmentModel> doInBackground(Integer... params) {
        int id = params[0];
        List<AssignmentModel> assList = new ArrayList<>();

        try {
            URL url = new URL(IP_ADRESS+"/api/profs/Listass?id=" + id);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

            try {
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                StringBuilder stringBuilder = new StringBuilder();

                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    stringBuilder.append(line).append("\n");
                }

                bufferedReader.close();

                // Parse the JSON string into a List of DemandeRattModel objects
                Gson gson = new Gson();
                Type listType = new TypeToken<List<AssignmentModel>>() {}.getType();
                try {
                    assList = gson.fromJson(stringBuilder.toString(), listType);
                    Log.d("ListAssTask", "JSON parsing successful");
                } catch (JsonSyntaxException e) {
                    Log.e("ListAssTask", "JSON parsing error: " + e.getMessage());
                }


            } finally {
                urlConnection.disconnect();
            }
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return assList;
    }

    @Override
    protected void onPostExecute(List<AssignmentModel> assList) {
        Log.d("ListAssTask", "onPostExecute: Called");
        if (listener != null) {
            listener.onRequestCompleted(assList);
        } else {
            Log.e("ListAssTask", "Listener is null");
        }
    }



    public interface OnRequestCompletedListener {
        void onRequestCompleted(List<AssignmentModel> assList);
    }


}
