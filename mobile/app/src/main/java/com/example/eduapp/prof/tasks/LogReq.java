package com.example.eduapp.prof.tasks;

import static com.example.eduapp.prof.Config.IP_ADRESS;

import android.os.AsyncTask;
import android.util.Log;

import com.example.eduapp.prof.LogReqCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class LogReq extends AsyncTask<Void, Void, String> {


        private static final String API_URL = IP_ADRESS+"/api/etudiants/login"; // Replace with your actual API endpoint

        private String email;
        private String password;
        private LogReqCallback callback;


    public LogReq(String email, String password, LogReqCallback callback) {
        this.email = email;
        this.password = password;
        this.callback = callback;
    }

        @Override
        protected String doInBackground(Void... params) {
            try {
                URL url = new URL(API_URL);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST");

                conn.setDoInput(true);
                conn.setDoOutput(true);

                conn.setRequestProperty("Content-Type", "application/json");

                JSONObject jsonInput = new JSONObject();
                jsonInput.put("email", email);
                jsonInput.put("password", password);

                byte[] postDataBytes = jsonInput.toString().getBytes(StandardCharsets.UTF_8);

                conn.setRequestProperty("Content-Length", String.valueOf(postDataBytes.length));

                try (OutputStream out = new BufferedOutputStream(conn.getOutputStream())) {
                    out.write(postDataBytes);
                }

                int responseCode = conn.getResponseCode();

                if (responseCode == HttpURLConnection.HTTP_OK || responseCode == HttpURLConnection.HTTP_CREATED) {

                    Log.e("POST Request", "success: " + responseCode);
                    BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "utf-8"));
                    String line = null;
                    StringBuilder sb = new StringBuilder();
                    while ((line = br.readLine()) != null) {
                    sb.append(line);
                    }
                    br.close();
                    Log.e("batata", "batata: " + sb.toString() );

                    return sb.toString();

                } else {
                    Log.e("POST Request", "Failed with response code: " + responseCode);
                    return "No user found";
                }
            } catch (JSONException | IOException e) {
                Log.e("POST Request", "Exception: " + e.getMessage());
            }
            return "Server Error";
        }
    @Override
    protected void onPostExecute(String success) {
        if (callback != null) {
            callback.onLogReqComplete(success);
        }
    }
    }
