package com.example.mostafaaboelnasr.attendancemanagment.student.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.NonNull;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mostafaaboelnasr.attendancemanagment.R;
import com.example.mostafaaboelnasr.attendancemanagment.admin_dir.models.adaptersModels.SubjectCardModel;
import com.example.mostafaaboelnasr.attendancemanagment.admin_dir.models.firebaseModels.SubjectModel;
import com.example.mostafaaboelnasr.attendancemanagment.student.StudentActivity;
import com.example.mostafaaboelnasr.attendancemanagment.student.StudentHomeFragment;
import com.example.mostafaaboelnasr.attendancemanagment.student.adapters.SubjectsQueueAdapter;
import com.example.mostafaaboelnasr.attendancemanagment.tutor_dir.adapters.SessionsAdapter;
import com.example.mostafaaboelnasr.attendancemanagment.tutor_dir.fragments.TutorHomeFragment;
import com.example.mostafaaboelnasr.attendancemanagment.tutor_dir.models.SessionModel;
import com.example.mostafaaboelnasr.attendancemanagment.utils.CheckNetworkState;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

/**
 * Created by Mostafa Aboelnasr on 4/18/2018.
 */

public class DialogAddSubjectQueue extends Dialog {
    Context context;
    DatabaseReference reference;
    public static Spinner subject;
    public static ArrayList<String> subject_ids, subject_names;
    RelativeLayout add;
    private ProgressBar progressBar;
    SwipeRefreshLayout swipe_refresh;
    private ImageView ok, not_ok;
    TextView textView;
    int subject_index;
    boolean subjectAvailable = true;

    public DialogAddSubjectQueue(@NonNull Context context) {
        super(context);
        this.context = context;
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_add_subject_queue);
        getWindow().getAttributes().windowAnimations = R.style.MyAnimation_Window;
//        getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        subject_ids = new ArrayList<>();
        subject_names = new ArrayList<>();
        subject = findViewById(R.id.subject);
        add = (RelativeLayout) findViewById(R.id.CreateButton);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        ok = (ImageView) findViewById(R.id.ok);
        not_ok = (ImageView) findViewById(R.id.not_ok);
        textView = (TextView) findViewById(R.id.textView);
        loadSubjects();
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                subject_index = subject.getSelectedItemPosition();
                checkSubjectQueue(subject_ids.get(subject_index));
            }
        });
        show();
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

    private void checkSubjectQueue(final String subjectId) {
        subjectAvailable = true;
        FirebaseDatabase.getInstance().getReference()
                .child("University")
                .child("Faculty")
                .child("Students")
                .child(StudentActivity.mySharedPreferences.getString("id", ""))
                .child("Subjects")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            try {
                                SubjectModel model = snapshot.getValue(SubjectModel.class);
                                if (model.getId().equals(subjectId)) {
                                    subjectAvailable = false;
                                }
                            } catch (com.google.firebase.database.DatabaseException e) {
                            }
                        }
                        if (subjectAvailable) {
                            add.setEnabled(false);
                            ok.setVisibility(View.GONE);
                            not_ok.setVisibility(View.GONE);
                            textView.setVisibility(View.GONE);
                            progressBar.setVisibility(View.VISIBLE);
//                int hall_index = hall.getSelectedItemPosition();
                            subject_index = subject.getSelectedItemPosition();
                            reference = FirebaseDatabase.getInstance().getReference()
                                    .child("University")
                                    .child("Faculty")
                                    .child("Students")
                                    .child(StudentActivity.mySharedPreferences.getString("id", ""))
                                    .child("Subjects").push();
                            final SubjectModel sessionModel = new SubjectModel(
                                    subject_ids.get(subject_index) + ""
                                    , subject_names.get(subject_index) + "");

                            reference.setValue(sessionModel);


                            new android.os.Handler().postDelayed(
                                    new Runnable() {
                                        public void run() {
                                            add.setEnabled(true);
                                            ok.setVisibility(View.VISIBLE);
                                            not_ok.setVisibility(View.GONE);
                                            textView.setVisibility(View.VISIBLE);
                                            progressBar.setVisibility(View.GONE);

                                            SubjectsQueueAdapter.subjectCardModels.add(new SubjectCardModel(subject_ids.get(subject_index) + "", subject_names.get(subject_index) + ""));

                                            StudentHomeFragment.subjectsQueueAdapter.notifyItemInserted(SubjectsQueueAdapter.subjectCardModels.size() - 1);
//                                        setDefaultBorder(name);
//                                        name.setText("");
                                        }
                                    }, 1200);

                            Toast.makeText(context, dataSnapshot.getChildrenCount() + "", Toast.LENGTH_SHORT).show();
//                            if ((int) dataSnapshot.getChildrenCount() >= 5) {
//                                StudentHomeFragment.addSubject.setVisibility(View.GONE);
//                            }
                            if (SubjectsQueueAdapter.subjectCardModels.size() >= 5) {
                                StudentHomeFragment.addSubject.setVisibility(View.GONE);
                            }
                            dismiss();
                        } else {
                            Toast.makeText(context, subject_names.get(subject_index) + " already exist", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }
                });
    }

}
