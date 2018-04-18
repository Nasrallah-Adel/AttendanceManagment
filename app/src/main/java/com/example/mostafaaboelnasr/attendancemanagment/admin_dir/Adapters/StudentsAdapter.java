package com.example.mostafaaboelnasr.attendancemanagment.admin_dir.Adapters;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.mostafaaboelnasr.attendancemanagment.R;
import com.example.mostafaaboelnasr.attendancemanagment.tutor_dir.TutorActivity;
import com.example.mostafaaboelnasr.attendancemanagment.admin_dir.dialogs.DialogCheckStudent;
import com.example.mostafaaboelnasr.attendancemanagment.admin_dir.models.adaptersModels.StudentCardModel;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

/**
 * Created by Mostafa Aboelnasr on 4/11/2018.
 */

public class StudentsAdapter extends RecyclerView.Adapter<StudentsAdapter.ViewHolder> {

    Context context;
    public static ArrayList<StudentCardModel> studentCardModels;

    public StudentsAdapter(Context context) {
        studentCardModels = new ArrayList<>();
        this.context = context;
    }


    private AlertDialog AskOption(final int i) {
        AlertDialog myQuittingDialogBox = new AlertDialog.Builder(context)
                //set message, title, and icon
                .setTitle("Delete")
                .setMessage("Do you want to Delete")
                .setIcon(R.drawable.ic_delete_forever_black_24dp)

                .setPositiveButton("Delete", new DialogInterface.OnClickListener() {

                    public void onClick(final DialogInterface dialog, int whichButton) {
                        //your deleting code
                        if (TutorActivity.mySharedPreferences.getString("id", "").equals("Admins")) {
                            FirebaseDatabase.getInstance().getReference()
                                    .child("University")
                                    .child("Faculty")
                                    .child("Students")
                                    .removeValue();
                            studentCardModels.remove(i);
                        }
                        final Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                //Do something after 100ms
                                dialog.dismiss();
                            }
                        }, 500);
                    }

                })

                .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, int which) {

                        final Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                //Do something after 100ms
                                dialog.dismiss();
                            }
                        }, 500);

                    }
                })
                .create();
        return myQuittingDialogBox;

    }

    class ViewHolder extends RecyclerView.ViewHolder {

        public TextView name, level;
        public ImageView delete;

        public ViewHolder(View itemView) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.student_name);
            level = (TextView) itemView.findViewById(R.id.student_level);
            delete = (ImageView) itemView.findViewById(R.id.deleteButton);
        }
    }

    @Override
    public StudentsAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.card_student, viewGroup, false);
        StudentsAdapter.ViewHolder viewHolder = new StudentsAdapter.ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final StudentsAdapter.ViewHolder viewHolder, final int i) {
        viewHolder.name.setText(studentCardModels.get(i).getName());
        viewHolder.level.setText(studentCardModels.get(i).getLevel());
        viewHolder.name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DialogCheckStudent(context, studentCardModels.get(i).getId(), i);
            }
        });
        viewHolder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AskOption(i).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return studentCardModels.size();
    }
}