package com.example.eduapp.prof;

import android.os.Bundle;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.eduapp.R;
import com.example.eduapp.prof.adapters.DemandeRattAdapter;
import com.example.eduapp.prof.models.DemandeRattModel;
import com.example.eduapp.prof.tasks.ListRattRequestTask;

import java.util.List;

public class ListDemandeRatt extends AppCompatActivity implements ListRattRequestTask.OnRequestCompletedListener {

    private RecyclerView recyclerView;
    private DemandeRattAdapter adapter;
    private LinearLayout returnratt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_ratt);

        recyclerView = findViewById(R.id.recyclerViewAss);
        returnratt = findViewById(R.id.returnlist_ass);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        returnratt.setOnClickListener(v -> finish());

        // Make the API request
        ListRattRequestTask listRattRequestTask = new ListRattRequestTask(this);
        String storedId = getSharedPreferences("LoginPrefs", MODE_PRIVATE).getString("id", null);
        listRattRequestTask.execute(Integer.valueOf(storedId));
    }

    @Override
    public void onRequestCompleted(List<DemandeRattModel> demandeList) {
        // Initialize and set the adapter
        adapter = new DemandeRattAdapter(demandeList);
        recyclerView.setAdapter(adapter);
    }
}

