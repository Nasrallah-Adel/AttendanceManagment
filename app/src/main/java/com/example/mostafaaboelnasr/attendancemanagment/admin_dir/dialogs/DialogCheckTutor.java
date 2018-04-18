package com.example.mostafaaboelnasr.attendancemanagment.admin_dir.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.NonNull;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.mostafaaboelnasr.attendancemanagment.admin_dir.Adapters.TutorAdapter;
import com.example.mostafaaboelnasr.attendancemanagment.R;
import com.example.mostafaaboelnasr.attendancemanagment.admin_dir.fragments.SearchFragment;
import com.example.mostafaaboelnasr.attendancemanagment.admin_dir.models.firebaseModels.TutorModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * Created by Mostafa Aboelnasr on 4/16/2018.
 */

public class DialogCheckTutor extends Dialog {
    DatabaseReference reference;
    SwipeRefreshLayout swipe_view;
    TextView person_name, person_email, person_phone, person_pass;
    EditText person_nameEditText, person_emailEditText, person_phoneEditText, person_passEditText;
    ImageView delete, edit, done;
    LinearLayout normal_mode, edit_mode;
    String id;

    public DialogCheckTutor(@NonNull Context context, final String id, final int i) {
        super(context);
        this.id = id;
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_check_tutor);
        getWindow().getAttributes().windowAnimations = R.style.MyAnimation_Window;
        getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        normal_mode = (LinearLayout) findViewById(R.id.normal_mode);
        edit_mode = (LinearLayout) findViewById(R.id.edit_mode);
        swipe_view = (SwipeRefreshLayout) findViewById(R.id.swipe_view);
        person_name = (TextView) findViewById(R.id.person_name);
        person_email = (TextView) findViewById(R.id.person_email);
        person_phone = (TextView) findViewById(R.id.person_phone);
        person_pass = (TextView) findViewById(R.id.person_pass);

        person_nameEditText = (EditText) findViewById(R.id.person_nameEditText);
        person_emailEditText = (EditText) findViewById(R.id.person_emailEditText);
        person_phoneEditText = (EditText) findViewById(R.id.person_phoneEditText);
        person_passEditText = (EditText) findViewById(R.id.person_passEditText);

        delete = (ImageView) findViewById(R.id.delete);
        edit = (ImageView) findViewById(R.id.edit);
        done = (ImageView) findViewById(R.id.done);

        setTutorData();
        swipe_view.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                setTutorData();
                swipe_view.setRefreshing(false);
            }
        });
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseDatabase.getInstance().getReference()
                        .child("University")
                        .child("Faculty")
                        .child("Tutors")
                        .child(id).removeValue();

                TutorAdapter.tutorCardModels.remove(i);
                SearchFragment.tutorAdapter.notifyItemRemoved(i);

                dismiss();
            }
        });
        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (done.getVisibility() == View.VISIBLE) {
                    normal_mode.setVisibility(View.VISIBLE);
                    edit_mode.setVisibility(View.GONE);
                    done.setVisibility(View.GONE);
                } else {
                    normal_mode.setVisibility(View.GONE);
                    edit_mode.setVisibility(View.VISIBLE);
                    done.setVisibility(View.VISIBLE);
                }
            }
        });
        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                normal_mode.setVisibility(View.VISIBLE);
                edit_mode.setVisibility(View.GONE);
                done.setVisibility(View.GONE);
                reference = FirebaseDatabase.getInstance().getReference()

                        .child("University")
                        .child("Faculty")
                        .child("Tutors")
                        .child(id);

                reference.setValue(new TutorModel(reference.getKey().toString() + ""
                        , person_nameEditText.getText().toString().trim() + ""
                        , person_emailEditText.getText().toString().trim() + ""
                        , person_phoneEditText.getText().toString().trim() + ""
                        , person_passEditText.getText().toString().trim() + ""));
            }
        });
        show();
    }

    private void setTutorData() {
        FirebaseDatabase.getInstance().getReference()
                .child("University")
                .child("Faculty")
                .child("Tutors")
                .child(id)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        try {
                            TutorModel personModel = dataSnapshot.getValue(TutorModel.class);
                            person_name.setText(personModel.getName());
                            person_email.setText(personModel.getEmail());
                            person_phone.setText(personModel.getPhone());
                            person_pass.setText(personModel.getPass());
                            person_nameEditText.setText(personModel.getName());
                            person_emailEditText.setText(personModel.getEmail());
                            person_phoneEditText.setText(personModel.getPhone());
                            person_passEditText.setText(personModel.getPass());
                        } catch (com.google.firebase.database.DatabaseException e) {
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }
                });
    }
}
