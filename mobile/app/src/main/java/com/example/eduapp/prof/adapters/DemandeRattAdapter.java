package com.example.eduapp.prof.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.eduapp.R;
import com.example.eduapp.prof.models.DemandeRattModel;

import java.util.List;

public class DemandeRattAdapter extends RecyclerView.Adapter<DemandeRattAdapter.ViewHolder> {
    private List<DemandeRattModel> demandeList;

    public DemandeRattAdapter(List<DemandeRattModel> demandeList) {
        this.demandeList = demandeList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate your item layout and create a ViewHolder
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_demande_ratt, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        // Bind data to the ViewHolder
        DemandeRattModel demande = demandeList.get(position);
        holder.bind(demande);
    }

    @Override
    public int getItemCount() {
        return demandeList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView demandeTypeTextView;
        private TextView demandeDateRattTextView;
        private TextView demandeHeurDebutRattTextView;
        private TextView demandeHeurFinRattTextView;
        private TextView demandeMatiereTextView;
        private TextView demandeClasseTextView;



        // Other views for other fields

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            // Initialize views
            demandeTypeTextView = itemView.findViewById(R.id.typeass);
            demandeDateRattTextView = itemView.findViewById(R.id.desc_ass);
            demandeHeurDebutRattTextView = itemView.findViewById(R.id.demandeHeureDebutTextView);
            demandeHeurFinRattTextView = itemView.findViewById(R.id.demandeHeureFinTextView);
            demandeMatiereTextView = itemView.findViewById(R.id.classeAss);
            demandeClasseTextView = itemView.findViewById(R.id.deadlinass);


            // Initialize other views
        }

        public void bind(DemandeRattModel demande) {
            // Bind data to views
            demandeTypeTextView.setText(demande.getDemandeType());
            demandeDateRattTextView.setText(demande.getDemandeDateRatt());
            demandeHeurDebutRattTextView.setText(demande.getDemandeHeureDebut());
            demandeHeurFinRattTextView.setText(demande.getDemandeHeureFin());
            demandeMatiereTextView.setText(demande.getDemandeMatiere());
            demandeClasseTextView.setText(demande.getDemandeClasse());
            // Bind other data
        }
    }
}

