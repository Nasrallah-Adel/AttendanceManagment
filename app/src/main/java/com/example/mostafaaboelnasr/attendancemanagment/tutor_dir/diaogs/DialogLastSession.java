package com.example.mostafaaboelnasr.attendancemanagment.tutor_dir.diaogs;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mostafaaboelnasr.attendancemanagment.R;
import com.example.mostafaaboelnasr.attendancemanagment.admin_dir.Adapters.StudentsAdapter;
import com.example.mostafaaboelnasr.attendancemanagment.admin_dir.models.adaptersModels.StudentCardModel;
import com.example.mostafaaboelnasr.attendancemanagment.admin_dir.models.firebaseModels.HallModel;
import com.example.mostafaaboelnasr.attendancemanagment.admin_dir.models.firebaseModels.StudentModel;
import com.example.mostafaaboelnasr.attendancemanagment.admin_dir.models.firebaseModels.SubjectModel;
import com.example.mostafaaboelnasr.attendancemanagment.tutor_dir.TutorActivity;
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
 * Created by Mostafa Aboelnasr on 4/17/2018.
 */

public class DialogLastSession extends Dialog {
    TextView subjectNameTextView, dateTextView, hallTextView;
    Context context;
    String subjectID, subjectName;
    String hallID, hallName;
    public static String sessionID;

    CenterZoomLayoutManager attendancesLayoutManager;
    public static StudentsAttendedLiveAdapter attendancesAdapter;
    RecyclerView attendancesRecyclerView;

    public DialogLastSession(@NonNull Context context, String sessionID) {
        super(context);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_last_session);
        getWindow().getAttributes().windowAnimations = R.style.MyAnimation_Window;
        this.sessionID = sessionID;
        this.context = context;

        subjectNameTextView = (TextView) findViewById(R.id.subjectName);
//        tutorNameTextView = (TextView) findViewById(R.id.tutorName);
        dateTextView = (TextView) findViewById(R.id.date);
        hallTextView = (TextView) findViewById(R.id.hall);

//        setSessionID();
        setSessionData(sessionID);
        attendedStudentSearchByName();
        show();
    }

//    private void setSessionID() {
//        FirebaseDatabase.getInstance().getReference()
//                .child("University")
//                .child("Faculty")
//                .child("Sessions")
//                .addListenerForSingleValueEvent(new ValueEventListener() {
//                    @Override
//                    public void onDataChange(DataSnapshot dataSnapshot) {
//                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
//                            SessionModel sessionModel = snapshot.getValue(SessionModel.class);
//                            if (sessionModel.getTutorID().equals(TutorActivity.mySharedPreferences.getString("id", ""))) {
//                                sessionID = sessionModel.getId();
//                                setSessionData(sessionID);
//                                attendedStudentSearchByName();
////                                setAvailability();
//                            }
//                        }
//                    }
//
//                    @Override
//                    public void onCancelled(DatabaseError databaseError) {
//                        Toast.makeText(context, "Check internet Connection! or message support team.", Toast.LENGTH_SHORT).show();
//                    }
//                });
//    }

    private void setSessionData(String sessionID) {
        FirebaseDatabase.getInstance().getReference()
                .child("University")
                .child("Faculty")
                .child("Sessions")
                .child(sessionID)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        SessionModel sessionModel = dataSnapshot.getValue(SessionModel.class);
                        subjectID = sessionModel.getSubID();
                        hallID = sessionModel.getHallID();
                        subName(subjectID);
                        hallName(hallID);
                        dateTextView.setText(sessionModel.getDate());
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Toast.makeText(context, "Check internet Connection! or message support team.", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void subName(String subjectID) {
        FirebaseDatabase.getInstance().getReference()
                .child("University")
                .child("Faculty")
                .child("Subjects")
                .child(subjectID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                SubjectModel subjectModel = dataSnapshot.getValue(SubjectModel.class);
                subjectName = subjectModel.getName();
                subjectNameTextView.setText(subjectName);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(context, "Check internet Connection! or message support team.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void hallName(String haleID) {
        FirebaseDatabase.getInstance().getReference()
                .child("University")
                .child("Faculty")
                .child("Halls")
                .child(haleID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                HallModel hallModel = dataSnapshot.getValue(HallModel.class);
                hallName = hallModel.getName();
                hallTextView.setText(hallName);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(context, "Check internet Connection! or message support team.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void attendedStudentSearchByName() {

        attendancesRecyclerView = (RecyclerView) findViewById(R.id.StudentsAttendanceRecycler_view);
        attendancesLayoutManager = new CenterZoomLayoutManager(context, CenterZoomLayoutManager.VERTICAL, false);
        attendancesRecyclerView.setLayoutManager(attendancesLayoutManager);

        attendancesAdapter = new StudentsAttendedLiveAdapter(context,"Sessions");
        attendancesRecyclerView.setAdapter(attendancesAdapter);
        StudentsAttendedLiveAdapter.studentCardModels = new ArrayList<>();
        StudentsAttendedLiveAdapter.attentancesIDs = new ArrayList<>();

        FirebaseDatabase.getInstance().getReference()
                .child("University")
                .child("Faculty")
                .child("Sessions")
                .child(sessionID)
                .child("Attendance")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            final AttendanceModel model = snapshot.getValue(AttendanceModel.class);
                            FirebaseDatabase.getInstance().getReference()
                                    .child("University")
                                    .child("Faculty")
                                    .child("Students")
                                    .child(model.getStudentID()).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    try {
                                        StudentModel modelStudent = dataSnapshot.getValue(StudentModel.class);
                                        StudentsAttendedLiveAdapter.studentCardModels
                                                .add(new StudentCardModel(modelStudent.getId() + ""
                                                        , modelStudent.getName() + ""
                                                        , modelStudent.getLevel() + ""));
                                        StudentsAttendedLiveAdapter.attentancesIDs.add(model.getId());
                                        attendancesAdapter.notifyItemInserted(StudentsAdapter.studentCardModels.size() - 1);
                                    } catch (NullPointerException e) {
                                    }
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {
                                    Toast.makeText(context, "Check internet Connection! or message support team.", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Toast.makeText(context, "Check internet Connection! or message support team.", Toast.LENGTH_SHORT).show();
                    }
                });

    }
}
