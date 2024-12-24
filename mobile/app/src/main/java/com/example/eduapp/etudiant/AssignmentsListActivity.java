// AssignmentsListActivity.java
package com.example.eduapp.etudiant;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.eduapp.R;
import com.example.eduapp.etudiant.adapters.AssignmentsAdapter;
import com.example.eduapp.etudiant.models.Assignment;
import com.example.eduapp.etudiant.tasks.AssignmentsTask;

import java.util.ArrayList;
import java.util.List;

public class AssignmentsListActivity extends AppCompatActivity implements AssignmentsTask.FetchAssignmentsTaskListener {

    private RecyclerView recyclerView;
    private AssignmentsAdapter assignmentsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assignments_list);

        recyclerView = findViewById(R.id.recyclerViewAssignments);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Initialize the adapter with an empty list
        assignmentsAdapter = new AssignmentsAdapter(new ArrayList<>());

        // Set the adapter to the RecyclerView
        recyclerView.setAdapter(assignmentsAdapter);

        // Execute the AsyncTask to fetch assignments data
        new AssignmentsTask(this, this).execute();

        // Log the fact that the activity is created
        Log.d("AssignmentsListActivity", "AssignmentsListActivity created");
    }

    @Override
    public void onFetchAssignmentsCompleted(List<Assignment> assignments) {
        // Log the received data
        for (Assignment assignment : assignments) {
            Log.d("AssignmentsListActivity", "Assignment: " + assignment.getTitle());
        }
        // Update the adapter data and notify the RecyclerView about the change
        assignmentsAdapter.updateData(assignments);
    }
}
