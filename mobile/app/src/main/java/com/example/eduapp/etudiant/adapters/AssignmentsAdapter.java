// AssignmentsAdapter.java
package com.example.eduapp.etudiant.adapters;

import static android.content.Context.MODE_PRIVATE;


import static com.example.eduapp.prof.Config.IP_ADRESS;

import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.example.eduapp.R;
import com.example.eduapp.etudiant.CreateDepotActivity;
import com.example.eduapp.etudiant.models.Assignment;


import java.time.LocalDateTime;
import java.util.List;

public class AssignmentsAdapter extends RecyclerView.Adapter<AssignmentsAdapter.ViewHolder> {

    private List<Assignment> assignments;

    public AssignmentsAdapter(List<Assignment> assignments) {
        this.assignments = assignments;
    }

    // Method to update the dataset and notify the adapter about the change
    public void updateData(List<Assignment> newAssignments) {
        assignments.clear();
        assignments.addAll(newAssignments);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.assignment_item_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {



        Assignment assignment = assignments.get(position);

        holder.viewbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String fileUrl = assignment.getFileName();
                String url = IP_ADRESS+"/" + fileUrl;
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(url));
                v.getContext().startActivity(intent);
            }
        });

        holder.depot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int Assignment_id = assignment.getId();
                String user_id = v.getContext().getSharedPreferences("LoginPrefs", MODE_PRIVATE)
                        .getString("id", null);
            ;   //intent to createDepot Activity
                Intent intent = new Intent(v.getContext(), CreateDepotActivity.class);
                intent.putExtra("Assignment_id",Assignment_id);
                Log.e("Assignmentss", String.valueOf(Assignment_id));


                v.getContext().startActivity(intent);
            }
        });



        holder.textViewAssignmentTitle.setText(assignment.getTitle());
        holder.textViewAssignmentDescription.setText(assignment.getDescription());

        LocalDateTime deadline = assignment.getDeadline();
        if (deadline != null) {
            holder.textViewAssignmentDeadline.setText(deadline.toString());
        } else {
            holder.textViewAssignmentDeadline.setText("No Deadline"); // or any default value
        }

//        holder.textViewAssignmentFileName.setText(assignment.getFileName());
        holder.textViewAssignmentProfName.setText(assignment.getProfName());
        holder.textViewAssignmentMatiereName.setText(assignment.getMatiereName());
        // Add more fields as needed
    }


    @Override
    public int getItemCount() {
        return assignments.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView textViewAssignmentTitle;
        Button viewbtn, depot;
        TextView textViewAssignmentDescription;
        TextView textViewAssignmentDeadline;
//        TextView textViewAssignmentFileName;
        TextView textViewAssignmentProfName;
        TextView textViewAssignmentMatiereName;
        // Add more TextViews for additional fields

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            depot = itemView.findViewById(R.id.depot);
            viewbtn = itemView.findViewById(R.id.viewbtn);
            textViewAssignmentTitle = itemView.findViewById(R.id.textViewAssignmentTitle);
            textViewAssignmentDescription = itemView.findViewById(R.id.textViewAssignmentDescription);
            textViewAssignmentDeadline = itemView.findViewById(R.id.textViewAssignmentDeadline);
//            textViewAssignmentFileName = itemView.findViewById(R.id.textViewAssignmentFileName);
            textViewAssignmentProfName = itemView.findViewById(R.id.textViewAssignmentProfName);
            textViewAssignmentMatiereName = itemView.findViewById(R.id.textViewAssignmentMatiereName);
            // Initialize additional TextViews here
        }
    }
}
