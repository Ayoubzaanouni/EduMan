package com.example.eduapp.prof;

import android.os.Bundle;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.eduapp.R;
import com.example.eduapp.prof.adapters.AssClasseDepotAdapter;
import com.example.eduapp.prof.models.DepotEtudModel;
import com.example.eduapp.prof.tasks.ListDepotsClasseTask;

import java.util.List;

public class ListDepotEtudClasseAss extends AppCompatActivity implements ListDepotsClasseTask.OnRequestCompletedListener {

    private RecyclerView recyclerView;
    private AssClasseDepotAdapter adapter;
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
        ListDepotsClasseTask listDepotsClasseRequestTask = new ListDepotsClasseTask(this);
        int ida = getIntent().getIntExtra("ida",0);
        int idc = getIntent().getIntExtra("idc",0);
        listDepotsClasseRequestTask.execute(Integer.valueOf(ida),Integer.valueOf(idc));
    }

    @Override
    public void onRequestCompleted(List<DepotEtudModel> assDepotList) {
        // Initialize and set the adapter
        adapter = new AssClasseDepotAdapter(assDepotList);
        recyclerView.setAdapter(adapter);
    }
}

