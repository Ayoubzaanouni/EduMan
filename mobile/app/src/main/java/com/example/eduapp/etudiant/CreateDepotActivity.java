package com.example.eduapp.etudiant;

import static com.example.eduapp.prof.Config.IP_ADRESS;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.eduapp.R;
import com.example.eduapp.etudiant.models.MyApi;
import com.example.eduapp.etudiant.tasks.SendDepotTask;
import com.example.eduapp.prof.CreateAss;
import com.example.eduapp.prof.tasks.SendAssignmentTask;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
//import com.example.eduapp.tasks.DepotTask;

public class CreateDepotActivity extends AppCompatActivity {

    public static final int PICK_FILE_REQUEST_CODE = 12;
    Button returndc, upload, submit;
    TextView tvfileuploaded;
    MyApi myApi ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_depot);

        returndc = findViewById(R.id.returndc);
        upload = findViewById(R.id.upload);
        submit = findViewById(R.id.submit);
        tvfileuploaded = findViewById(R.id.tvfileuploaded);

        returndc.setOnClickListener(v -> finish());
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(IP_ADRESS+"/api/etudiants/") // replace with your actual base URL
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        myApi = retrofit.create(MyApi.class);

        upload.setOnClickListener(v -> {
            chooseFile();
        });
        submit.setOnClickListener( v -> {

            String selectedFile = tvfileuploaded.getText().toString();
            Uri selectedFileUri = Uri.parse(selectedFile);
            String fileExtension = getFileExtension(selectedFileUri);
            InputStream inputStream = null;
            try {
                inputStream = getContentResolver().openInputStream(Uri.parse(selectedFile));
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            }
            File file = createTempFileFromInputStream(inputStream, fileExtension);

            RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
            MultipartBody.Part imagePart = MultipartBody.Part.createFormData("file", file.getName(), requestFile);
            Call<ResponseBody> call = myApi.uploadFile(imagePart);
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    // Handle success
                    if (response.isSuccessful()) {
                        Toast.makeText(CreateDepotActivity.this, "Assignment added successfully", Toast.LENGTH_SHORT).show();
                        try {
                            JSONObject obj = new JSONObject(response.body().string());

                            Log.d("bodyyyy",obj.getString("fileName") );
                            // Replace the placeholder values with your actual data
                            String apiUrl = IP_ADRESS+"/api/etudiants/CreateFakeDepot";
                            int ida = getIntent().getIntExtra("Assignment_id",0);
                            //shared prefrence
                            String storedId = getSharedPreferences("LoginPrefs", MODE_PRIVATE).getString("id", null);
                            String jsonRequest = "{ \"AssignmentId\": \""+ida+"\", \"EtudiantId\": \""+storedId+"\", \"FileName\": \""+obj.getString("fileName")+"\" }";
                            Log.e("jsonass",jsonRequest);
                            SendDepotTask sendDepotTask = new SendDepotTask();
                            sendDepotTask.execute(apiUrl, jsonRequest);
                            finish();

                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }
                    } else {
                        // Handle error
                        Toast.makeText(CreateDepotActivity.this, "Upload failed", Toast.LENGTH_SHORT).show();
                    }
                }


                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    // Handle failure
                    Toast.makeText(CreateDepotActivity.this, "Network error", Toast.LENGTH_SHORT).show();
                }
            });
        });


    }
    private static File createTempFileFromInputStream(InputStream inputStream, String fileExtension) {
        try {

            // Create a temporary file
            File tempFile = File.createTempFile("tempFile", "."+fileExtension);

            // Copy the content of the InputStream to the temporary file
            try (OutputStream outputStream = new FileOutputStream(tempFile)) {
                byte[] buffer = new byte[4096];
                int bytesRead;
                while ((bytesRead = inputStream.read(buffer)) != -1) {
                    outputStream.write(buffer, 0, bytesRead);
                }
            }

            return tempFile;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
    private String getFileExtension(Uri uri) {
        String fileExtension = null;

        if (uri.getScheme() != null && uri.getScheme().equals("content")) {
            // If the URI scheme is "content", use the ContentResolver to get the file extension
            MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
            fileExtension = mimeTypeMap.getExtensionFromMimeType(getContentResolver().getType(uri));
        } else {
            // If the URI scheme is "file", extract the file extension from the path
            fileExtension = MimeTypeMap.getFileExtensionFromUrl(uri.getPath());
        }

        return fileExtension;
    }
    private void chooseFile() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*");
        String[] mimeTypes = {"application/pdf", "image/*", "application/msword", "application/vnd.openxmlformats-officedocument.wordprocessingml.document", "application/vnd.ms-powerpoint", "application/vnd.openxmlformats-officedocument.presentationml.presentation"};
        intent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes);

        startActivityForResult(intent, PICK_FILE_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_FILE_REQUEST_CODE && resultCode == RESULT_OK && data != null && data.getData() != null) {
            // Get the selected image URI
            Uri selectedImageUri = data.getData();
            tvfileuploaded.setText(selectedImageUri.toString());

        }
    }
}