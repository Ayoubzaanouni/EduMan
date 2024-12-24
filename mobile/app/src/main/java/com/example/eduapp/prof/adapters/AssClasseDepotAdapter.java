package com.example.eduapp.prof.adapters;

import static com.example.eduapp.prof.Config.IP_ADRESS;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.eduapp.R;
import com.example.eduapp.prof.models.DepotEtudModel;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AssClasseDepotAdapter extends RecyclerView.Adapter<AssClasseDepotAdapter.ViewHolder> {
    private List<DepotEtudModel> assClasseDepotList;

    public AssClasseDepotAdapter(List<DepotEtudModel> assClasseDepotList) {
        this.assClasseDepotList = assClasseDepotList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate your item layout and create a ViewHolder
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_item_depots_classe, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        // Bind data to the ViewHolder
        DepotEtudModel ass = assClasseDepotList.get(position);
        holder.bind(ass);
    }

    @Override
    public int getItemCount() {
        return assClasseDepotList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView EtudName;
        private ImageView Details;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            // Initialize views
            EtudName = itemView.findViewById(R.id.nomEtud);
            Details = itemView.findViewById(R.id.details);

        }

        public void bind(DepotEtudModel ass) {
            // Bind data to views
            EtudName.setText(ass.getDepotEtudiantNom()+" "+ass.getDepotEtudiantClasse());

            Details.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showDescriptionDialog(ass.getDepotEtudiantNom(),ass.getDepotEtudiantFile(),ass.getDepotEtudiantClasse(),ass.getDepotEtudiantTime());
                }
            });


        }

        public String formatDateTimeString(String input) {
            String[] dateTimeParts = input.split("T");
            String[] heur_minute = dateTimeParts[1].split(":");
            String heur =heur_minute[0];
            String minute =heur_minute[1];
            return dateTimeParts[0] +" "+heur+":"+minute;
        }

        private void showDescriptionDialog(String nom,String url,String classe,String time) {
            Context context = itemView.getContext();
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle("Assignment de "+nom+" "+classe)
                    .setMessage(formatDateTimeString(time))
                    .setPositiveButton("Close", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    })
                    .setNegativeButton("File", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            openFilePreview(url);
                        }
                    });

            AlertDialog dialog = builder.create();
            dialog.show();
        }

        private void openFilePreview(String fileUrl) {
            String url = IP_ADRESS+"/" + fileUrl;
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(android.net.Uri.parse(url));
            itemView.getContext().startActivity(intent);
        }
    }
}
