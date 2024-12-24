package com.example.eduapp.prof.tasks;

import static com.example.eduapp.prof.Config.IP_ADRESS;

import android.os.AsyncTask;

import com.example.eduapp.prof.models.DemandeRattModel;
import com.google.gson.Gson;
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

public class ListRattRequestTask extends AsyncTask<Integer, Void, List<DemandeRattModel>> {

    private OnRequestCompletedListener listener;

    public ListRattRequestTask(OnRequestCompletedListener listener) {
        this.listener = listener;
    }

    @Override
    protected List<DemandeRattModel> doInBackground(Integer... params) {
        int id = params[0];
        List<DemandeRattModel> demandeList = new ArrayList<>();

        try {
            URL url = new URL(IP_ADRESS+"/api/profs/listratt?id=" + id);
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
                Type listType = new TypeToken<List<DemandeRattModel>>(){}.getType();
                demandeList = gson.fromJson(stringBuilder.toString(), listType);

            } finally {
                urlConnection.disconnect();
            }
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return demandeList;
    }

    @Override
    protected void onPostExecute(List<DemandeRattModel> demandeList) {
        listener.onRequestCompleted(demandeList);
    }

    public interface OnRequestCompletedListener {
        void onRequestCompleted(List<DemandeRattModel> demandeList);
    }
}
