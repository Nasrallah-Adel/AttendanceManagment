package com.example.mostafaaboelnasr.attendancemanagment.tutor_dir.diaogs;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mostafaaboelnasr.attendancemanagment.tutor_dir.adapters.SessionsAdapter;
import com.example.mostafaaboelnasr.attendancemanagment.R;
import com.example.mostafaaboelnasr.attendancemanagment.tutor_dir.TutorActivity;
import com.example.mostafaaboelnasr.attendancemanagment.tutor_dir.fragments.TutorHomeFragment;
import com.example.mostafaaboelnasr.attendancemanagment.admin_dir.models.firebaseModels.HallModel;
import com.example.mostafaaboelnasr.attendancemanagment.tutor_dir.models.SessionModel;
import com.example.mostafaaboelnasr.attendancemanagment.admin_dir.models.firebaseModels.SubjectModel;
import com.example.mostafaaboelnasr.attendancemanagment.utils.CheckNetworkState;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by Mostafa Aboelnasr on 4/15/2018.
 */

public class DialogAddSession extends Dialog {

    DatabaseReference reference;
    Context context;
    public static Spinner spinner_days, spinner_months, spinner_years;
    public static Spinner hall, subject;
    RelativeLayout add;
    private ProgressBar progressBar;
    private ImageView ok, not_ok;
    TextView textView;
    public static ArrayList<String> hall_ids, hall_names;
    public static ArrayList<String> subject_ids, subject_names;

    boolean hallAvailable = true;

    public DialogAddSession(@NonNull final Context context) {
        super(context);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_add_session);
        getWindow().getAttributes().windowAnimations = R.style.MyCustomTheme;
        getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        this.context = context;

        init();

    }

    private void init() {
        hall_ids = new ArrayList<>();
        hall_names = new ArrayList<>();
        subject_ids = new ArrayList<>();
        subject_names = new ArrayList<>();

        spinner_days = findViewById(R.id.spinner_days);
        spinner_months = findViewById(R.id.spinner_months);
        spinner_years = findViewById(R.id.spinner_years);
        hall = findViewById(R.id.hall);
        subject = findViewById(R.id.subject);
        add = (RelativeLayout) findViewById(R.id.CreateButton);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        ok = (ImageView) findViewById(R.id.ok);
        not_ok = (ImageView) findViewById(R.id.not_ok);
        textView = (TextView) findViewById(R.id.textView);

        setCurrentDate();
        loadHalls();
        loadSubjects();
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setHallAvailability(hall_ids.get(hall.getSelectedItemPosition()));
            }
        });
    }

    private void addTOLiveSession() {
        add.setEnabled(false);
        ok.setVisibility(View.GONE);
        not_ok.setVisibility(View.GONE);
        textView.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);
        int hall_index = hall.getSelectedItemPosition();
        int subject_index = subject.getSelectedItemPosition();
        reference = FirebaseDatabase.getInstance().getReference()
                .child("University")
                .child("Faculty")
                .child("LiveSessions").push();
        final SessionModel sessionModel = new SessionModel(
                reference.getKey().toString() + ""
                , getTutorId() + ""
                , getCurrentDate() + ""
                , subject_ids.get(subject_index) + ""
                , hall_ids.get(hall_index) + "");

        reference.setValue(sessionModel);
        reference.child("available").setValue("No");


        new android.os.Handler().postDelayed(
                new Runnable() {
                    public void run() {
                        add.setEnabled(true);
                        ok.setVisibility(View.VISIBLE);
                        not_ok.setVisibility(View.GONE);
                        textView.setVisibility(View.VISIBLE);
                        progressBar.setVisibility(View.GONE);

                        SessionsAdapter.ids.add(sessionModel.getId());
                        SessionsAdapter.names.add(sessionModel.getTutorID());
                        SessionsAdapter.dates.add(sessionModel.getDate());

                        TutorHomeFragment.sessionsAdapter.notifyItemInserted(SessionsAdapter.ids.size() - 1);
//                                        setDefaultBorder(name);
//                                        name.setText("");
                    }
                }, 1200);
        dismiss();
        TutorHomeFragment.live_seasionLinearLayout.setVisibility(View.VISIBLE);
        TutorHomeFragment.create_seasionLinearLayout.setVisibility(View.GONE);
    }

    private void setCurrentDate() {
        ArrayAdapter adapter_days = ArrayAdapter.createFromResource(context, R.array.days, android.R.layout.simple_spinner_item);
        ArrayAdapter adapter_months = ArrayAdapter.createFromResource(context, R.array.months, android.R.layout.simple_spinner_item);
        ArrayAdapter adapter_years = ArrayAdapter.createFromResource(context, R.array.years, android.R.layout.simple_spinner_item);
        spinner_days.setAdapter(adapter_days);
        spinner_months.setAdapter(adapter_months);
        spinner_years.setAdapter(adapter_years);
        Calendar c = Calendar.getInstance();
        spinner_days.setSelection(c.get(Calendar.DAY_OF_MONTH) - 1);
        spinner_months.setSelection(c.get(Calendar.MONTH) + 1);
        spinner_years.setSelection(c.get(Calendar.YEAR) - 2018);
    }

    private String getCurrentDate() {
        return spinner_days.getSelectedItem() +
                "/" + spinner_months.getSelectedItem() + "/" + spinner_years.getSelectedItem();
    }

    private String getTutorId() {
        return TutorActivity.id;
    }

    private void loadHalls() {
        FirebaseDatabase.getInstance().getReference()
                .child("University")
                .child("Faculty")
                .child("Halls")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            try {
                                HallModel model = snapshot.getValue(HallModel.class);
                                hall_ids.add(model.getId());
                                hall_names.add(model.getName());
                            } catch (com.google.firebase.database.DatabaseException e) {
                            }
                        }
                        ArrayAdapter<String> adapter_halls = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, hall_names);
                        hall.setAdapter(adapter_halls);

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }
                });
    }

    private void loadSubjects() {
        FirebaseDatabase.getInstance().getReference()
                .child("University")
                .child("Faculty")
                .child("Subjects")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            try {
                                SubjectModel model = snapshot.getValue(SubjectModel.class);
                                subject_ids.add(model.getId());
                                subject_names.add(model.getName());
                            } catch (com.google.firebase.database.DatabaseException e) {
                            }
                        }
                        ArrayAdapter<String> adapter_subjects = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, subject_names);
                        subject.setAdapter(adapter_subjects);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }
                });
    }

    private void setHallAvailability(final String hallID) {
        hallAvailable = true;
        FirebaseDatabase.getInstance().getReference()
                .child("University")
                .child("Faculty")
                .child("LiveSessions")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            SessionModel sessionModel = snapshot.getValue(SessionModel.class);
                            if (sessionModel.getHallID().toString().equals(hallID)) {
                                hallAvailable = false;
                            }
                        }
                        if (hallAvailable) {
                            if (CheckNetworkState.isConnect(getContext())) {
                                addTOLiveSession();
                            } else {
                                add.setEnabled(true);
                                ok.setVisibility(View.GONE);
                                not_ok.setVisibility(View.VISIBLE);
                                textView.setVisibility(View.VISIBLE);
                                progressBar.setVisibility(View.GONE);
                            }
                        } else {
                            Toast.makeText(context, "Hall Currently not available", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Toast.makeText(context, "Check internet Connection! or message support team.", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
