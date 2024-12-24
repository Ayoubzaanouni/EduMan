package com.example.eduapp.prof.adapters;

import static com.example.eduapp.prof.Config.IP_ADRESS;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.eduapp.prof.ListClasseAss;
import com.example.eduapp.R;
import com.example.eduapp.prof.models.AssignmentModel;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class AssAdapter extends RecyclerView.Adapter<AssAdapter.ViewHolder> {
    private List<AssignmentModel> assList;

    public AssAdapter(List<AssignmentModel> assList) {
        this.assList = assList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate your item layout and create a ViewHolder
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_assignment, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        // Bind data to the ViewHolder
        AssignmentModel ass = assList.get(position);
        holder.bind(ass);
    }

    @Override
    public int getItemCount() {
        return assList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView AssignmentTitle;
        private ImageView AssignmentDesc;
        private TextView AssignmentDeadLine;
        private Button show_ass_classe;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            // Initialize views
            AssignmentTitle = itemView.findViewById(R.id.classeAss);
            AssignmentDesc = itemView.findViewById(R.id.desc_ass);
            AssignmentDeadLine = itemView.findViewById(R.id.deadlinass);
            show_ass_classe = itemView.findViewById(R.id.show_ass_classe);
        }

        public void bind(AssignmentModel ass) {
            // Bind data to views
            Log.d("bindinngg", ass.getAssignmentTitle());
            AssignmentTitle.setText(ass.getAssignmentTitle());
            AssignmentDesc.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showDescriptionDialog(ass.getAssignmentDesc(), ass.getAssignmentMatiere(),ass.getAssignmentFile());
                }
            });
            String assignmentDeadlineString = ass.getAssignmentDeadLine();

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
            Date assignmentDeadline;
            try {
                assignmentDeadline = sdf.parse(assignmentDeadlineString);
            } catch (ParseException e) {
                e.printStackTrace(); // Handle parsing exception if needed
                return;
            }

            Date currentDate = new Date();
            AssignmentDeadLine.setText(ass.getAssignmentDeadLine());
            if (currentDate.after(assignmentDeadline)) {
                AssignmentDeadLine.setTextColor(Color.RED);
            } else {
                AssignmentDeadLine.setTextColor(Color.BLACK);
            }

            show_ass_classe.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(itemView.getContext(), ListClasseAss.class);
                    intent.putExtra("assId",ass.getAssignmentId());
                    itemView.getContext().startActivity(intent);
                }
            });
        }
        private void showDescriptionDialog(String description, String matiere,String url) {
            Context context = itemView.getContext();
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle("Assignment Description")
                    .setMessage("Description: " + description + "\n\nMatiere: " + matiere)
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
            Intent intent = new Intent(android.content.Intent.ACTION_VIEW);
            intent.setData(android.net.Uri.parse(url));
            itemView.getContext().startActivity(intent);
        }
    }
}
