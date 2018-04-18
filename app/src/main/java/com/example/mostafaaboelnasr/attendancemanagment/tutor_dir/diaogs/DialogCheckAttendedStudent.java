package com.example.mostafaaboelnasr.attendancemanagment.tutor_dir.diaogs;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.widget.SwipeRefreshLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.mostafaaboelnasr.attendancemanagment.R;
import com.example.mostafaaboelnasr.attendancemanagment.admin_dir.models.firebaseModels.StudentModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * Created by Mostafa Aboelnasr on 4/16/2018.
 */

public class DialogCheckAttendedStudent extends Dialog {
    SwipeRefreshLayout swipe_view;
    TextView person_name, person_email, person_phone, person_pass, person_age, person_level, person_specialization, person_section;
    LinearLayout normal_mode, edit_mode;
    String id;

    public DialogCheckAttendedStudent(@NonNull Context context, String id) {
        super(context);
        setContentView(R.layout.dialog_check_attended_student);
        normal_mode = (LinearLayout) findViewById(R.id.normal_mode);
        edit_mode = (LinearLayout) findViewById(R.id.edit_mode);
        swipe_view = (SwipeRefreshLayout) findViewById(R.id.swipe_view);

        person_name = (TextView) findViewById(R.id.person_name);
        person_email = (TextView) findViewById(R.id.person_email);
        person_phone = (TextView) findViewById(R.id.person_phone);
        person_pass = (TextView) findViewById(R.id.person_pass);
        person_age = (TextView) findViewById(R.id.person_age);
        person_level = (TextView) findViewById(R.id.person_level);
        person_specialization = (TextView) findViewById(R.id.person_specialization);
        person_section = (TextView) findViewById(R.id.person_section);
        this.id = id;
        setTutorData();
        swipe_view.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                setTutorData();
                swipe_view.setRefreshing(false);
            }
        });
        show();
    }

    private void setTutorData() {
        FirebaseDatabase.getInstance().getReference()
                .child("University")
                .child("Faculty")
                .child("Students")
                .child(id)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        try {
                            StudentModel personModel = dataSnapshot.getValue(StudentModel.class);
                            person_name.setText(personModel.getName());
                            person_email.setText(personModel.getEmail());
                            person_phone.setText(personModel.getPhone());
                            person_pass.setText(personModel.getPass());
                            person_age.setText(personModel.getAge());
                            person_level.setText(personModel.getLevel());
                            person_specialization.setText(personModel.getSpecialization());
                            person_section.setText(personModel.getSection());

                        } catch (com.google.firebase.database.DatabaseException e) {
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }
                });
    }
}
