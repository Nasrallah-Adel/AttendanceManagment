package com.example.mostafaaboelnasr.attendancemanagment.student.adapters;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.mostafaaboelnasr.attendancemanagment.MainActivity;
import com.example.mostafaaboelnasr.attendancemanagment.R;
import com.example.mostafaaboelnasr.attendancemanagment.admin_dir.Adapters.SubjectsAdapter;
import com.example.mostafaaboelnasr.attendancemanagment.admin_dir.models.adaptersModels.SubjectCardModel;
import com.example.mostafaaboelnasr.attendancemanagment.student.RecognizeActivity;
import com.example.mostafaaboelnasr.attendancemanagment.student.StudentActivity;
import com.example.mostafaaboelnasr.attendancemanagment.student.dialogs.DialogStudentAttendace;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

/**
 * Created by Mostafa Aboelnasr on 4/17/2018.
 */

public class SubjectsQueueAdapter extends RecyclerView.Adapter<SubjectsQueueAdapter.ViewHolder> {

    Context context;
    public static ArrayList<SubjectCardModel> subjectCardModels;

    public SubjectsQueueAdapter(Context context) {
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
    public SubjectsQueueAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.card_subject_queue, viewGroup, false);
        SubjectsQueueAdapter.ViewHolder viewHolder = new SubjectsQueueAdapter.ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final SubjectsQueueAdapter.ViewHolder viewHolder, final int i) {
        viewHolder.name.setText(subjectCardModels.get(i).getName());
        viewHolder.attendance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DialogStudentAttendace(context, subjectCardModels.get(i).getId());
            }
        });
        viewHolder.registe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, RecognizeActivity.class);
                intent.putExtra("SubjectId", subjectCardModels.get(i).getId() + "");
                intent.putExtra("StudentId", StudentActivity.mySharedPreferences.getString("id", "") + "");
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return subjectCardModels.size();
    }
}