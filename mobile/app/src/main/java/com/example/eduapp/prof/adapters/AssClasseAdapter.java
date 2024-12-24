package com.example.eduapp.prof.adapters;

import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.eduapp.prof.ListDepotEtudClasseAss;
import com.example.eduapp.R;
import com.example.eduapp.prof.models.ClasseModel;

import java.util.List;

public class AssClasseAdapter extends RecyclerView.Adapter<AssClasseAdapter.ViewHolder> {
    private List<ClasseModel> assClasseList;

    public AssClasseAdapter(List<ClasseModel> assClasseList) {
        this.assClasseList = assClasseList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate your item layout and create a ViewHolder
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_classes_depot, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        // Bind data to the ViewHolder
        ClasseModel ass = assClasseList.get(position);
        holder.bind(ass);
    }

    @Override
    public int getItemCount() {
        return assClasseList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView ClasseName;
        private Button ShowAssignments;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            // Initialize views
            ClasseName = itemView.findViewById(R.id.classeAsss);
            ShowAssignments = itemView.findViewById(R.id.show_ass_classes);
        }

        public void bind(ClasseModel ass) {
            // Bind data to views
            Log.d("bindinngg", ass.getAssClasseName());
            ClasseName.setText(ass.getAssClasseName());
            ShowAssignments.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(itemView.getContext(), ListDepotEtudClasseAss.class);
                    intent.putExtra("ida",ass.getAssId());
                    intent.putExtra("idc",ass.getAssClasseId());
                    itemView.getContext().startActivity(intent);
                }
            });

        }



    }
}
