package com.example.eduapp.prof;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.eduapp.R;
import com.example.eduapp.prof.models.Demande;
import com.example.eduapp.prof.tasks.FetchClassesTask;
import com.example.eduapp.prof.tasks.FetchMatieresTask;
import com.example.eduapp.prof.tasks.SubmitDemandeTask;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Map;

public class DemandeRatt extends AppCompatActivity  implements FetchClassesTask.FetchClassesTaskListener, FetchMatieresTask.FetchMatieresTaskListener{

    LinearLayout returnratt;
    Button submitButton;
    Spinner typeSpinner;
    DatePicker datePicker;
    TimePicker heureDebutPicker, heureFinPicker;
    Spinner matiereSpinner, classeSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_demande_ratt);

        returnratt = findViewById(R.id.returnlist_ass);
        returnratt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        new FetchClassesTask(this).execute();
        new FetchMatieresTask(this).execute();


        submitButton = findViewById(R.id.submit_ass);
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle the submit button click here
                handleFormSubmission();
            }
        });

        // Initialize Spinner adapters and set data
        typeSpinner = findViewById(R.id.type);
        ArrayAdapter<CharSequence> typeAdapter = ArrayAdapter.createFromResource(
                this, R.array.type_options, android.R.layout.simple_spinner_item);
        typeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        typeSpinner.setAdapter(typeAdapter);

        matiereSpinner = findViewById(R.id.matiere_ass);

        classeSpinner = findViewById(R.id.classe);

        // Initialize Date and Time Pickers
        datePicker = findViewById(R.id.deadline_ass);
        heureDebutPicker = findViewById(R.id.heurdebut);
        heureFinPicker = findViewById(R.id.heurfin);
    }
    @Override
    public void onFetchClassesCompleted(Map<Integer, String> textValues) {
        // Handle the result, for example, update UI or process the text values
        Log.e("ssssss", textValues.toString());

        // Populate the Spinner with the text values
        if (!textValues.isEmpty()) {
            try {
                //get all the values of the hashmap
                Collection<String> values = textValues.values();

                // Convert the Collection to an ArrayList
                ArrayList<String> arrayList = new ArrayList<>(values);
                // Set up the Spinner adapter
                ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, arrayList);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                // Set the adapter to the Spinner
                classeSpinner.setAdapter(adapter);

            } catch (Exception e) {
                Log.e("TAG", "Error handling fetch classes result", e);
            }
        }
    }

    @Override
    public void onFetchMatieresCompleted(Map<Integer, String> textValues) {
        // Handle the result, for example, update UI or process the text values
        Log.e("ssssss", textValues.toString());
        Collection<String> values = textValues.values();

        // Convert the Collection to an ArrayList
        ArrayList<String> arrayList = new ArrayList<>(values);
        // Populate the Spinner with the text values
        if (!textValues.isEmpty()) {
            try {
                // Set up the Spinner adapter
                ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, arrayList);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                // Set the adapter to the Spinner
                matiereSpinner.setAdapter(adapter);

            } catch (Exception e) {
                Log.e("TAG", "Error handling fetch classes result", e);
            }
        }
    }
    private void handleFormSubmission() {
        // Get the selected values
        String selectedType = typeSpinner.getSelectedItem().toString();
        String selectedMatiere = matiereSpinner.getSelectedItem().toString();
        String selectedClasse = classeSpinner.getSelectedItem().toString();

        // Get the selected date
        int day = datePicker.getDayOfMonth();
        int month = datePicker.getMonth() + 1; // Month is zero-based
        int year = datePicker.getYear();
        String selectedDate = String.format("%02d/%02d/%d", day, month, year);

        // Get the selected time for Heure Debut
        int heureDebutHour = heureDebutPicker.getHour();
        int heureDebutMinute = heureDebutPicker.getMinute();
        String selectedHeureDebut = String.format("%02d:%02d", heureDebutHour, heureDebutMinute);

        // Get the selected time for Heure Fin
        int heureFinHour = heureFinPicker.getHour();
        int heureFinMinute = heureFinPicker.getMinute();
        String selectedHeureFin = String.format("%02d:%02d", heureFinHour, heureFinMinute);

        // You can now use the retrieved values as needed, for example, display them or send to a server.
        // For now, let's display a toast message with the selected values.
        String message = "Type: " + selectedType + "\n"
                + "Matiere: " + selectedMatiere + "\n"
                + "Classe: " + selectedClasse + "\n"
                + "Date: " + selectedDate + "\n"
                + "Heure Debut: " + selectedHeureDebut + "\n"
                + "Heure Fin: " + selectedHeureFin;

                Log.e("fin",message);
        Demande demandeRatt = new Demande();
        demandeRatt.setType(selectedType);
        demandeRatt.setMatiere(selectedMatiere);
        demandeRatt.setDateRatt(convertDateFormat(selectedDate));
        demandeRatt.setClasse(selectedClasse);
        demandeRatt.setHeureDebut(selectedHeureDebut);
        demandeRatt.setHeureFin(selectedHeureFin);
        String storedId = getSharedPreferences("LoginPrefs", MODE_PRIVATE).getString("id", null);
        demandeRatt.setProfId(Integer.parseInt(storedId));
                new SubmitDemandeTask(demandeRatt, new SubmitDemandeTask.SubmitDemandeListener() {
            @Override
            public void onSubmitDemandeCompleted(boolean success) {
                if (success) {
                    // Handle successful submission, for example, show a success message
                    Toast.makeText(DemandeRatt.this, "Demande submitted successfully", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    // Handle submission failure, for example, show an error message
                    Toast.makeText(DemandeRatt.this, "Failed to submit demande", Toast.LENGTH_SHORT).show();
                }
            }
        }).execute();
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

}
