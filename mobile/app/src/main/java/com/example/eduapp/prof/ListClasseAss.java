package com.example.eduapp.prof;

import android.os.Bundle;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.eduapp.R;
import com.example.eduapp.prof.adapters.AssClasseAdapter;
import com.example.eduapp.prof.models.ClasseModel;
import com.example.eduapp.prof.tasks.ListClassAssTask;

import java.util.List;

public class ListClasseAss extends AppCompatActivity implements ListClassAssTask.OnRequestCompletedListener {
    private RecyclerView recyclerView;
    private AssClasseAdapter adapter;
    private LinearLayout returnratt;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_class_ass);

        recyclerView = findViewById(R.id.recyclerViewClassAss);
        returnratt = findViewById(R.id.linearLayout);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        returnratt.setOnClickListener(v -> finish());
        ListClassAssTask listClasseAssRequestTask = new ListClassAssTask(this);
        int assId = getIntent().getIntExtra("assId",0);
        listClasseAssRequestTask.execute(Integer.valueOf(assId));
    }

    @Override
    public void onRequestCompleted(List<ClasseModel> assClasseList) {
        adapter = new AssClasseAdapter(assClasseList);
        recyclerView.setAdapter(adapter);
    }
}
