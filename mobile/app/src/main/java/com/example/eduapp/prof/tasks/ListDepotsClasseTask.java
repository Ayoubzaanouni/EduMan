package com.example.eduapp.prof.tasks;

import static com.example.eduapp.prof.Config.IP_ADRESS;

import android.os.AsyncTask;
import android.util.Log;

import com.example.eduapp.prof.models.DepotEtudModel;
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

public class ListDepotsClasseTask extends AsyncTask<Integer, Void, List<DepotEtudModel>> {

    private OnRequestCompletedListener listener;

    public ListDepotsClasseTask(OnRequestCompletedListener listener) {
        this.listener = listener;
    }

    @Override
    protected List<DepotEtudModel> doInBackground(Integer... params) {
        int ida = params[0];
        int idc = params[1];
        List<DepotEtudModel> assDepotList = new ArrayList<>();

        try {
            URL url = new URL(IP_ADRESS+"/api/profs/depots?ida=" + ida+"&idc=" +idc);
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
                Type listType = new TypeToken<List<DepotEtudModel>>() {}.getType();
                try {
                    assDepotList = gson.fromJson(stringBuilder.toString(), listType);
                    Log.d("DepotEtudModel", "JSON parsing successful");
                } catch (JsonSyntaxException e) {
                    Log.e("DepotEtudModel", "JSON parsing error: " + e.getMessage());
                }


            } finally {
                urlConnection.disconnect();
            }
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return assDepotList;
    }

    @Override
    protected void onPostExecute(List<DepotEtudModel> assDepotList) {
        Log.d("DepotEtudModel", "onPostExecute: Called");
        if (listener != null) {
            listener.onRequestCompleted(assDepotList);
        } else {
            Log.e("DepotEtudModel", "Listener is null");
        }
    }



    public interface OnRequestCompletedListener {
        void onRequestCompleted(List<DepotEtudModel> assDepotList);
    }


}
