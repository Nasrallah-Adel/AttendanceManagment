package com.example.mostafaaboelnasr.attendancemanagment.tutor_dir.adapters;

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
import com.example.mostafaaboelnasr.attendancemanagment.admin_dir.models.adaptersModels.StudentCardModel;
import com.example.mostafaaboelnasr.attendancemanagment.tutor_dir.diaogs.DialogCheckAttendedStudent;
import com.example.mostafaaboelnasr.attendancemanagment.tutor_dir.diaogs.DialogLiveSession;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

/**
 * Created by Mostafa Aboelnasr on 4/16/2018.
 */

public class StudentsAttendedLiveAdapter extends RecyclerView.Adapter<StudentsAttendedLiveAdapter.ViewHolder> {

    Context context;
    String type;
    public static ArrayList<StudentCardModel> studentCardModels;
    public static ArrayList<String> attentancesIDs;

    public StudentsAttendedLiveAdapter(Context context, String type) {
        studentCardModels = new ArrayList<>();
        attentancesIDs = new ArrayList<>();
        this.context = context;
        this.type = type;
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

//                        if (TutorActivity.mySharedPreferences.getString("id", "").equals("Tutors")) {
                        FirebaseDatabase.getInstance().getReference()
                                .child("University")
                                .child("Faculty")
                                .child("LiveSessions")
                                .child(DialogLiveSession.sessionID)
                                .child("Attendance")
                                .child(attentancesIDs.get(i))
                                .removeValue();
                        studentCardModels.remove(i);
                        attentancesIDs.remove(i);
                        DialogLiveSession.attendancesAdapter.notifyItemRemoved(i);
//                        }
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

        public TextView name;
        public ImageView delete;

        public ViewHolder(View itemView) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.student_name);
            delete = (ImageView) itemView.findViewById(R.id.deleteButton);
            if (type.equals("Sessions")) {
                delete.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public StudentsAttendedLiveAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.card_attended_student, viewGroup, false);
        StudentsAttendedLiveAdapter.ViewHolder viewHolder = new StudentsAttendedLiveAdapter.ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final StudentsAttendedLiveAdapter.ViewHolder viewHolder, final int i) {
        viewHolder.name.setText(studentCardModels.get(i).getName());
        viewHolder.name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DialogCheckAttendedStudent(context, studentCardModels.get(i).getId());
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
