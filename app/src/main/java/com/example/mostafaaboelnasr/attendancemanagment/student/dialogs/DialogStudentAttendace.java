package com.example.mostafaaboelnasr.attendancemanagment.student.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.Window;
import android.widget.Toast;

import com.example.mostafaaboelnasr.attendancemanagment.R;
import com.example.mostafaaboelnasr.attendancemanagment.admin_dir.Adapters.StudentsAdapter;
import com.example.mostafaaboelnasr.attendancemanagment.admin_dir.models.adaptersModels.StudentCardModel;
import com.example.mostafaaboelnasr.attendancemanagment.admin_dir.models.firebaseModels.StudentModel;
import com.example.mostafaaboelnasr.attendancemanagment.student.StudentActivity;
import com.example.mostafaaboelnasr.attendancemanagment.tutor_dir.adapters.SessionsAdapter;
import com.example.mostafaaboelnasr.attendancemanagment.tutor_dir.adapters.StudentsAttendedLiveAdapter;
import com.example.mostafaaboelnasr.attendancemanagment.tutor_dir.models.AttendanceModel;
import com.example.mostafaaboelnasr.attendancemanagment.tutor_dir.models.SessionModel;
import com.example.mostafaaboelnasr.attendancemanagment.utils.CenterZoomLayoutManager;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

/**
 * Created by Mostafa Aboelnasr on 4/18/2018.
 */

public class DialogStudentAttendace extends Dialog {
    String id;

    CenterZoomLayoutManager attendancesLayoutManager;
    public static SessionsAdapter attendancesAdapter;
    RecyclerView attendancesRecyclerView;

    public DialogStudentAttendace(@NonNull final Context context, final String id) {
        super(context);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_student_attendace);
        getWindow().getAttributes().windowAnimations = R.style.MyAnimation_Window;
        this.id = id;
        attendancesRecyclerView = (RecyclerView) findViewById(R.id.sessionsAttendanceRecycler_view);
        attendancesLayoutManager = new CenterZoomLayoutManager(context, CenterZoomLayoutManager.VERTICAL, false);
        attendancesRecyclerView.setLayoutManager(attendancesLayoutManager);

        attendancesAdapter = new SessionsAdapter(context);
        attendancesRecyclerView.setAdapter(attendancesAdapter);
        SessionsAdapter.ids = new ArrayList<>();
        SessionsAdapter.names = new ArrayList<>();
        SessionsAdapter.dates = new ArrayList<>();

        FirebaseDatabase.getInstance().getReference()
                .child("University")
                .child("Faculty")
                .child("Sessions")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            final SessionModel model = snapshot.getValue(SessionModel.class);
                            if (model.getId().equals(id)) {
                                FirebaseDatabase.getInstance().getReference()
                                        .child("University")
                                        .child("Faculty")
                                        .child("Sessions")
                                        .child(model.getId())
                                        .child("Attendance")
                                        .addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(DataSnapshot dataSnapshot) {
                                                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                                    final AttendanceModel attendanceModel = snapshot.getValue(AttendanceModel.class);
                                                    if (attendanceModel.getStudentID().equals(StudentActivity.mySharedPreferences.getString("id", ""))) {
                                                        SessionsAdapter.ids.add(model.getId());
                                                        SessionsAdapter.names.add("");
                                                        SessionsAdapter.dates.add(model.getDate());
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
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Toast.makeText(context, "Check internet Connection! or message support team.", Toast.LENGTH_SHORT).show();
                    }
                });
        show();
    }
}
