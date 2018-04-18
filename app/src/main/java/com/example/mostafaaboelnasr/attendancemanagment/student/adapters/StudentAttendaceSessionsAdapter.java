package com.example.mostafaaboelnasr.attendancemanagment.student.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.mostafaaboelnasr.attendancemanagment.R;
import com.example.mostafaaboelnasr.attendancemanagment.admin_dir.models.adaptersModels.SubjectCardModel;

import java.util.ArrayList;

/**
 * Created by Mostafa Aboelnasr on 4/18/2018.
 */

public class StudentAttendaceSessionsAdapter  extends RecyclerView.Adapter<StudentAttendaceSessionsAdapter.ViewHolder> {

    Context context;
    public static ArrayList<SubjectCardModel> subjectCardModels;

    public StudentAttendaceSessionsAdapter(Context context) {
        subjectCardModels = new ArrayList<>();
        this.context = context;
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        public TextView name, attendance, registe;

        public ViewHolder(View itemView) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.subject_name);
            attendance = (TextView) itemView.findViewById(R.id.attendance);
            registe = (TextView) itemView.findViewById(R.id.registe);
        }
    }

    @Override
    public StudentAttendaceSessionsAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.card_subject_queue, viewGroup, false);
        StudentAttendaceSessionsAdapter.ViewHolder viewHolder = new StudentAttendaceSessionsAdapter.ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final StudentAttendaceSessionsAdapter.ViewHolder viewHolder, final int i) {
        viewHolder.name.setText(subjectCardModels.get(i).getName());
        viewHolder.attendance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return subjectCardModels.size();
    }
}