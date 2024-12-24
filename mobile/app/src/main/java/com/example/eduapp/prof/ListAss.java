package com.example.eduapp.prof;

import android.os.Bundle;
import android.util.Log;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.eduapp.R;
import com.example.eduapp.prof.adapters.AssAdapter;
import com.example.eduapp.prof.models.AssignmentModel;
import com.example.eduapp.prof.tasks.ListAssTask;

import java.util.List;

public class ListAss extends AppCompatActivity implements ListAssTask.OnRequestCompletedListener {

    private RecyclerView recyclerView;
    private AssAdapter adapter;
    private LinearLayout returnratt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_ass_prof);

        recyclerView = findViewById(R.id.recyclerViewAss);
        returnratt = findViewById(R.id.returnlist_ass);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        returnratt.setOnClickListener(v -> finish());

        // Make the API request
        ListAssTask listAssRequestTask = new ListAssTask(this);
        String storedId = getSharedPreferences("LoginPrefs", MODE_PRIVATE).getString("id", null);
        listAssRequestTask.execute(Integer.valueOf(storedId));
    }

    @Override
    public void onRequestCompleted(List<AssignmentModel> assList) {
        // Initialize and set the adapter
        Log.e("aloo", assList.get(0).getAssignmentDeadLine());
        adapter = new AssAdapter(assList);
        recyclerView.setAdapter(adapter);
    }
}

