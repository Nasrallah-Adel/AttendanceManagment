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
import android.widget.Toast;

import com.example.mostafaaboelnasr.attendancemanagment.R;
import com.example.mostafaaboelnasr.attendancemanagment.tutor_dir.TutorActivity;
import com.example.mostafaaboelnasr.attendancemanagment.tutor_dir.diaogs.DialogLiveSession;
import com.example.mostafaaboelnasr.attendancemanagment.tutor_dir.models.AttendanceModel;
import com.example.mostafaaboelnasr.attendancemanagment.tutor_dir.models.SessionModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

/**
 * Created by Mostafa Aboelnasr on 4/16/2018.
 */

public class StudentsLiveAdapter extends RecyclerView.Adapter<StudentsLiveAdapter.ViewHolder> {

    Context context;
    public static ArrayList<String> ids, names;
    DatabaseReference reference;
    String sessionID;

    public StudentsLiveAdapter(Context context) {
        ids = new ArrayList<>();
        names = new ArrayList<>();
        this.context = context;
        setSessionID();
    }

    boolean isAttend = false;

    private AlertDialog AskOption(final int i) {
        AlertDialog myQuittingDialogBox = new AlertDialog.Builder(context)
                //set message, title, and icon
                .setTitle("Delete")
                .setMessage("Do you want to Add")
                .setIcon(R.drawable.ic_person_add_black_24dp)
                .setPositiveButton("Add", new DialogInterface.OnClickListener() {

                    public void onClick(final DialogInterface dialog, int whichButton) {
                        //your deleting code
                        FirebaseDatabase.getInstance().getReference()
                                .child("University")
                                .child("Faculty")
                                .child("LiveSessions")
                                .child(DialogLiveSession.sessionID)
                                .child("Attendance")
                                .addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        isAttend = false;
                                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                            AttendanceModel model = snapshot.getValue(AttendanceModel.class);
                                            if (model.getStudentID().equals(ids.get(i))) {
                                                isAttend = true;
                                            }
                                        }
                                        if (!isAttend) {
                                            reference = FirebaseDatabase.getInstance().getReference()
                                                    .child("University")
                                                    .child("Faculty")
                                                    .child("LiveSessions")
                                                    .child(sessionID)
                                                    .child("Attendance").push();
                                            AttendanceModel sessionModel = new AttendanceModel(
                                                    reference.getKey().toString() + ""
                                                    , ids.get(i) + ""
                                                    , sessionID + "");
                                            reference.setValue(sessionModel);

                                            ids.remove(i);
                                            names.remove(i);
                                            DialogLiveSession.studentsAdapter.notifyItemRemoved(i);
                                        } else {
                                            Toast.makeText(context, "Already Attended", Toast.LENGTH_SHORT).show();
                                        }
                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {
                                        Toast.makeText(context, "Check internet Connection! or message support team.", Toast.LENGTH_SHORT).show();
                                    }
                                });


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
        public ImageView add;

        public ViewHolder(View itemView) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.student_name);
            add = (ImageView) itemView.findViewById(R.id.add_student);
        }
    }

    @Override
    public StudentsLiveAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.card_students_live, viewGroup, false);
        StudentsLiveAdapter.ViewHolder viewHolder = new StudentsLiveAdapter.ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final StudentsLiveAdapter.ViewHolder viewHolder, final int i) {
        viewHolder.name.setText(names.get(i));

        viewHolder.add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AskOption(i).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return ids.size();
    }


    private void setSessionID() {
        FirebaseDatabase.getInstance().getReference()
                .child("University")
                .child("Faculty")
                .child("LiveSessions")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            SessionModel sessionModel = snapshot.getValue(SessionModel.class);
                            if (sessionModel.getTutorID().equals(TutorActivity.mySharedPreferences.getString("id", ""))) {
                                sessionID = sessionModel.getId();
                            }
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Toast.makeText(context, "Check internet Connection! or message support team.", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}