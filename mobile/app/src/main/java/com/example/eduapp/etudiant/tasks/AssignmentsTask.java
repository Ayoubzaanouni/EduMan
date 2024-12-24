package com.example.eduapp.etudiant.tasks;



import static com.example.eduapp.prof.Config.IP_ADRESS;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;


import com.example.eduapp.etudiant.models.Assignment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class AssignmentsTask extends AsyncTask<String, Void, List<Assignment>> {

    private final Context context;
    private final FetchAssignmentsTaskListener listener;

    public AssignmentsTask(Context context, FetchAssignmentsTaskListener listener) {
        this.context = context;
        this.listener = listener;
    }

    @Override
    protected List<Assignment> doInBackground(String... params) {
        List<Assignment> assignments = new ArrayList<>();

        // Retrieve shared preferences using the provided context
        String stored_class_id = context.getSharedPreferences("LoginPrefs", Context.MODE_PRIVATE)
                .getString("classe_id", null);

        try {
            URL url = new URL(IP_ADRESS +"/api/etudiants/Assignments?idc=" + stored_class_id);
            Log.d("AssignmentsTask", "Fetching data from" + stored_class_id);
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
                JSONArray jsonArray = new JSONArray(stringBuilder.toString());

                // Process each JSON object in the array
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject item = jsonArray.getJSONObject(i);

                    int id = item.getInt("id");

                    // Check for null values for each field
                    String title = item.isNull("title") ? "" : item.getString("title");
                    String description = item.isNull("description") ? "" : item.getString("description");

                    LocalDateTime deadline = null;
                    if (!item.isNull("deadline") && !item.getString("deadline").equals("null")) {
                        deadline = LocalDateTime.parse(item.getString("deadline"));
                    }

                    String fileName = item.isNull("fileName") ? "" : item.getString("fileName");
                    String profName = item.isNull("profName") ? "" : item.getString("profName");
                    String matiereName = item.isNull("matiereName") ? "" : item.getString("matiereName");

                    Assignment assignment = new Assignment(id, title, description, deadline, fileName, profName, matiereName);
                    assignments.add(assignment);
                }

            } finally {
                urlConnection.disconnect();
            }
            Log.d("AssignmentsTask", "Fetched data: " + assignments.toString());
        } catch (IOException | JSONException e) {
            Log.e("AssignmentsTask", "Error fetching assignments data", e);
        }

        return assignments;
    }

    @Override
    protected void onPostExecute(List<Assignment> assignments) {
        // Notify the listener with the result
        if (listener != null) {
            listener.onFetchAssignmentsCompleted(assignments);
        }
    }

    // Interface to notify the calling class about the completion
    public interface FetchAssignmentsTaskListener {
        void onFetchAssignmentsCompleted(List<Assignment> assignments);
    }
}