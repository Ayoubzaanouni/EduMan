package com.example.eduapp.prof;

import static com.example.eduapp.prof.Config.IP_ADRESS;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.eduapp.R;
import com.example.eduapp.prof.models.IdValuePair;
import com.example.eduapp.prof.models.MyApi;
import com.example.eduapp.prof.tasks.FetchClassesTask;
import com.example.eduapp.prof.tasks.FetchMatieresTask;
import com.example.eduapp.prof.tasks.SendAssignmentTask;
import com.google.android.flexbox.FlexboxLayout;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class CreateAss extends AppCompatActivity implements FetchClassesTask.FetchClassesTaskListener, FetchMatieresTask.FetchMatieresTaskListener {
    private static final int PICK_FILE_REQUEST_CODE = 123;
    Button submit;
    LinearLayout returne;
    TextView tvSelectedFile;
    EditText titre,description;
    DatePicker deadLine;

    ImageView upload;
    int selectedMatiere;
    Spinner matiereSpinner;
    private FlexboxLayout classesContainer;
    private MyApi myApi,myApiass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_assignment);
        returne = findViewById(R.id.returnlist_ass);
        upload = findViewById(R.id.upload);
        tvSelectedFile = findViewById(R.id.tvSelectedFile);
        titre = findViewById(R.id.title_ass);
        submit = findViewById(R.id.submit_ass);
        description = findViewById(R.id.description_ass);
        deadLine = findViewById(R.id.deadline_ass);
        classesContainer = findViewById(R.id.classesContainer);

        matiereSpinner = findViewById(R.id.matiere_ass);
        new FetchMatieresTask(this).execute();
        new FetchClassesTask(this).execute();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(IP_ADRESS+"/api/profs/") // replace with your actual base URL
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        myApi = retrofit.create(MyApi.class);

        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseFile();
            }
        });
        returne.setOnClickListener(v -> finish());

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (titre.getText().toString().isEmpty() || description.getText().toString().isEmpty() || tvSelectedFile.getText().toString().equals("selected file"))
                {
                    Toast.makeText(CreateAss.this, "Missing Fields", Toast.LENGTH_SHORT).show();
                }
                else {
                    try {
                        submitForm();
                    } catch (FileNotFoundException e) {
                        throw new RuntimeException(e);
                    }
                }
            }

            private void submitForm() throws FileNotFoundException {
                List<Integer> selectedClassIds = new ArrayList<>();
                for (int i = 0; i < classesContainer.getChildCount(); i++) {
                    View view = classesContainer.getChildAt(i);

                    if (view instanceof CheckBox) {
                        CheckBox checkBox = (CheckBox) view;

                        if (checkBox.isChecked()) {
                            int classId = checkBox.getId();
                            selectedClassIds.add(classId);
                        }
                    }
                }


                String selectedTitre = titre.getText().toString();
                String selectedDescription = description.getText().toString();
                String selectedFile = tvSelectedFile.getText().toString();
                String storedId = getSharedPreferences("LoginPrefs", MODE_PRIVATE).getString("id", null);

                Uri selectedFileUri = Uri.parse(selectedFile);
                String fileExtension = getFileExtension(selectedFileUri);
                InputStream inputStream = getContentResolver().openInputStream(Uri.parse(selectedFile));
                File file = createTempFileFromInputStream(inputStream, fileExtension);

                RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
                MultipartBody.Part imagePart = MultipartBody.Part.createFormData("file", file.getName(), requestFile);

                // Call the API to upload the image
                Call<ResponseBody> call = myApi.uploadFile(imagePart);
                call.enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        // Handle success
                        if (response.isSuccessful()) {
                            Toast.makeText(CreateAss.this, "Assignment added successfully", Toast.LENGTH_SHORT).show();
                            try {
                                JSONObject obj = new JSONObject(response.body().string());

                                Log.d("bodyyyy",obj.getString("fileName") );
                                // Replace the placeholder values with your actual data
                                String apiUrl = IP_ADRESS+"/api/profs/assignment";
                                int day = deadLine.getDayOfMonth();
                                int month = deadLine.getMonth() + 1; // Month is zero-based
                                int year = deadLine.getYear();
                                String selectedDate = String.format("%02d/%02d/%d", day, month, year);
                                String jsonRequest = "{ \"title\": \""+selectedTitre+"\", \"Description\": \""+selectedDescription+"\", \"Deadline\": \""+convertDateFormat(selectedDate)+"\", \"FileName\": \"files/assignments/"+obj.getString("fileName")+"\", \"MatiereId\": "+selectedMatiere+", \"ProfId\": "+storedId+", \"SelectedClasses\": "+selectedClassIds+" }";
                                Log.e("jsonass",jsonRequest);
                                SendAssignmentTask sendAssignmentTask = new SendAssignmentTask();
                                sendAssignmentTask.execute(apiUrl, jsonRequest);
                                finish();

                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            } catch (JSONException e) {
                                throw new RuntimeException(e);
                            }
                        } else {
                            // Handle error
                            Toast.makeText(CreateAss.this, "Upload failed", Toast.LENGTH_SHORT).show();
                        }
                    }


                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        // Handle failure
                        Toast.makeText(CreateAss.this, "Network error", Toast.LENGTH_SHORT).show();
                    }
                });









            }
        });

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
    public static String convertDateFormat(String inputDateStr) {
        // Input date format
        SimpleDateFormat inputFormat = new SimpleDateFormat("dd/MM/yyyy");

        // Output date format
        SimpleDateFormat outputFormat = new SimpleDateFormat("yyyy-MM-dd");

        try {
            // Parse the input date string
            Date inputDate = inputFormat.parse(inputDateStr);

            // Format the date to the desired output format
            return outputFormat.format(inputDate);
        } catch (ParseException e) {
            e.printStackTrace();
            // Handle the exception or return an error message if needed
            return null;
        }
    }
    private void chooseFile() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*");

        // Restrict the file types to PDF, images, Word documents, and PowerPoint presentations
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
            tvSelectedFile.setText(selectedImageUri.toString());

            // Create a file part from the selected image URI

        }
    }

    @Override
    public void onFetchClassesCompleted(Map<Integer, String> textValues) {
        for (Map.Entry<Integer, String> entry : textValues.entrySet()) {
            Integer classId = entry.getKey();
            String className = entry.getValue();
            CheckBox checkBox = new CheckBox(this);
            checkBox.setText(className);
            checkBox.setId(classId);
            classesContainer.addView(checkBox);
        }
    }


    @Override
    public void onFetchMatieresCompleted(Map<Integer, String> textValues) {
        // Handle the result, for example, update UI or process the text values
        Log.e("ssssss", textValues.toString());

        // Populate the Spinner with the text values and corresponding IDs
        if (!textValues.isEmpty()) {
            try {
                List<IdValuePair> valuesList = new ArrayList<>();

                // Populate the valuesList with IdValuePair objects
                for (Map.Entry<Integer, String> entry : textValues.entrySet()) {
                    valuesList.add(new IdValuePair(entry.getKey(), entry.getValue()));
                }

                // Set up the Spinner adapter
                ArrayAdapter<IdValuePair> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, valuesList);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                // Set the adapter to the Spinner
                matiereSpinner.setAdapter(adapter);

                // Optionally, set a listener to get the selected item and its ID
                matiereSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                        IdValuePair selectedPair = (IdValuePair) parentView.getSelectedItem();
                        int selectedId = selectedPair.getId();
                        String selectedValue = selectedPair.getValue();
                        selectedMatiere = selectedId;
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parentView) {
                        // Handle the case where nothing is selected
                    }
                });

            } catch (Exception e) {
                Log.e("TAG", "Error handling fetch classes result", e);
            }
        }
    }




}