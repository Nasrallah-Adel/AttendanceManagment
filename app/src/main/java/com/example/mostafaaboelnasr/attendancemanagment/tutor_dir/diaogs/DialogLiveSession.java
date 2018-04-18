package com.example.mostafaaboelnasr.attendancemanagment.tutor_dir.diaogs;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mostafaaboelnasr.attendancemanagment.admin_dir.Adapters.StudentsAdapter;
import com.example.mostafaaboelnasr.attendancemanagment.tutor_dir.adapters.StudentsLiveAdapter;
import com.example.mostafaaboelnasr.attendancemanagment.tutor_dir.adapters.StudentsAttendedLiveAdapter;
import com.example.mostafaaboelnasr.attendancemanagment.utils.CenterZoomLayoutManager;
import com.example.mostafaaboelnasr.attendancemanagment.R;
import com.example.mostafaaboelnasr.attendancemanagment.tutor_dir.TutorActivity;
import com.example.mostafaaboelnasr.attendancemanagment.tutor_dir.fragments.TutorHomeFragment;
import com.example.mostafaaboelnasr.attendancemanagment.admin_dir.models.adaptersModels.StudentCardModel;
import com.example.mostafaaboelnasr.attendancemanagment.tutor_dir.models.AttendanceModel;
import com.example.mostafaaboelnasr.attendancemanagment.admin_dir.models.firebaseModels.HallModel;
import com.example.mostafaaboelnasr.attendancemanagment.tutor_dir.models.SessionModel;
import com.example.mostafaaboelnasr.attendancemanagment.admin_dir.models.firebaseModels.StudentModel;
import com.example.mostafaaboelnasr.attendancemanagment.admin_dir.models.firebaseModels.SubjectModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

/**
 * Created by Mostafa Aboelnasr on 4/15/2018.
 */

public class DialogLiveSession extends Dialog {
    DatabaseReference reference;
    public static String sessionID;
    public static Context context;
    TextView subjectNameTextView, hallTextView, dateTextView;
    String subjectID, subjectName;
    String hallID, hallName;

    //used for pause ,open,end live session
    LinearLayout end_live_session;
    TextView pauseLiveTextView;

    //
    boolean available;

    ArrayList<AttendanceModel> attendaceModels = new ArrayList<>();

    //     allstudents
    CenterZoomLayoutManager studentslayoutManager;
    public static StudentsLiveAdapter studentsAdapter;
    RecyclerView studentsRecyclerView;
    //     students attended
    CenterZoomLayoutManager attendancesLayoutManager;
    public static StudentsAttendedLiveAdapter attendancesAdapter;
    RecyclerView attendancesRecyclerView;

    public DialogLiveSession(@NonNull final Context context) {
        super(context);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_live_session);
        getWindow().getAttributes().windowAnimations = R.style.MyCustomTheme;
//        getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        this.context = context;
        init();
    }

    private void init() {
        end_live_session = (LinearLayout) findViewById(R.id.end_live_session);
        subjectNameTextView = (TextView) findViewById(R.id.subjectName);
        hallTextView = (TextView) findViewById(R.id.hall);
        dateTextView = (TextView) findViewById(R.id.date);
        pauseLiveTextView = (TextView) findViewById(R.id.pauseLiveTextView);

        setSessionID();
        studentSearchByName("");
        end_live_session.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                endSession();
            }
        });
        pauseLiveTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (available) {
                    FirebaseDatabase.getInstance().getReference()
                            .child("University")
                            .child("Faculty")
                            .child("LiveSessions")
                            .child(DialogLiveSession.sessionID)
                            .child("available")
                            .setValue("Yes");
                    pauseLiveTextView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_wifi_tethering_black_24dp, 0, 0, 0);
                    available = false;
                } else if (!available) {
                    FirebaseDatabase.getInstance().getReference()
                            .child("University")
                            .child("Faculty")
                            .child("LiveSessions")
                            .child(DialogLiveSession.sessionID)
                            .child("available")
                            .setValue("No");
                    pauseLiveTextView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_portable_wifi_off_black_24dp, 0, 0, 0);
                    available = true;
                }
            }
        });
    }

    private void endSession() {
        attendaceModels = new ArrayList<>();
        FirebaseDatabase.getInstance().getReference()
                .child("University")
                .child("Faculty")
                .child("LiveSessions")
                .child(sessionID)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        SessionModel sessionModel = dataSnapshot.getValue(SessionModel.class);

                        addEndedSession(sessionModel);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Toast.makeText(DialogLiveSession.context, "Check internet Connection! or message support team.", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void loadAttendedStudents() {
        FirebaseDatabase.getInstance().getReference()
                .child("University")
                .child("Faculty")
                .child("LiveSessions")
                .child(DialogLiveSession.sessionID)
                .child("Attendance").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (final DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    final AttendanceModel attendanceModel = snapshot.getValue(AttendanceModel.class);
                    FirebaseDatabase.getInstance().getReference()
                            .child("University")
                            .child("Faculty")
                            .child("Sessions")
                            .child(reference.getKey())
                            .child("Attendance")
                            .child(attendanceModel.getId())
                            .setValue(attendanceModel);
                }
                removeLiveSession();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(DialogLiveSession.context, "Check internet Connection! or message support team.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void addEndedSession(SessionModel sessionModel) {
        reference = FirebaseDatabase.getInstance().getReference()
                .child("University")
                .child("Faculty")
                .child("Sessions")
                .child(sessionModel.getId());
        reference.setValue(sessionModel);
        loadAttendedStudents();
    }

    private void removeLiveSession() {
        FirebaseDatabase.getInstance().getReference()
                .child("University")
                .child("Faculty")
                .child("LiveSessions")
                .child(DialogLiveSession.sessionID).removeValue();
        TutorHomeFragment.live_seasionLinearLayout.setVisibility(View.GONE);
        TutorHomeFragment.create_seasionLinearLayout.setVisibility(View.VISIBLE);
        dismiss();
    }

    private void setSessionData(String sessionID) {
        FirebaseDatabase.getInstance().getReference()
                .child("University")
                .child("Faculty")
                .child("LiveSessions")
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

    private void studentSearchByName(final String name) {

        studentsRecyclerView = (RecyclerView) findViewById(R.id.addStudentsAttendanceRecycler_view);
        studentslayoutManager = new CenterZoomLayoutManager(context, CenterZoomLayoutManager.HORIZONTAL, false);
        studentsRecyclerView.setLayoutManager(studentslayoutManager);

        studentsAdapter = new StudentsLiveAdapter(context);
        studentsRecyclerView.setAdapter(studentsAdapter);
        StudentsLiveAdapter.ids = new ArrayList<>();
        StudentsLiveAdapter.names = new ArrayList<>();

        FirebaseDatabase.getInstance().getReference()
                .child("University")
                .child("Faculty")
//                .child("Tutors")
//                .child("-L9r3R2A4-mc_t6tzmzO")
                .child("Students")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            StudentModel studentModel = snapshot.getValue(StudentModel.class);
                            if (studentModel.getName().contains(name)) {
                                StudentsLiveAdapter.names.add(studentModel.getName());
                                StudentsLiveAdapter.ids.add(studentModel.getId());
                                studentsAdapter.notifyItemInserted(StudentsLiveAdapter.ids.size() - 1);
                            }
                        }
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

        attendancesAdapter = new StudentsAttendedLiveAdapter(context, "LiveSessions");
        attendancesRecyclerView.setAdapter(attendancesAdapter);
        StudentsAttendedLiveAdapter.studentCardModels = new ArrayList<>();
        StudentsAttendedLiveAdapter.attentancesIDs = new ArrayList<>();

        FirebaseDatabase.getInstance().getReference()
                .child("University")
                .child("Faculty")
                .child("LiveSessions")
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
                                setSessionData(sessionID);
                                attendedStudentSearchByName();
                                setAvailability();
                            }
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Toast.makeText(context, "Check internet Connection! or message support team.", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void setAvailability() {
        available = false;
        FirebaseDatabase.getInstance().getReference()
                .child("University")
                .child("Faculty")
                .child("LiveSessions")
                .child(DialogLiveSession.sessionID)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
//                            SessionModel sessionModel = snapshot.getValue(SessionModel.class);
//                            if (sessionModel.getTutorID().equals(TutorActivity.mySharedPreferences.getString("id", ""))) {
//                                sessionID = sessionModel.getId();
//                                setSessionData(sessionID);
//                                attendedStudentSearchByName();
//                            }
                        if (dataSnapshot.child("available").getValue().toString().equals("Yes")) {
                            available = true;
                        } else {
                            available = false;
                        }

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Toast.makeText(context, "Check internet Connection! or message support team.", Toast.LENGTH_SHORT).show();
                    }
                });
    }

}
