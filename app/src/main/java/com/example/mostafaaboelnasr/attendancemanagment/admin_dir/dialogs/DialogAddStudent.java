package com.example.mostafaaboelnasr.attendancemanagment.admin_dir.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.mostafaaboelnasr.attendancemanagment.admin_dir.Adapters.StudentsAdapter;
import com.example.mostafaaboelnasr.attendancemanagment.admin_dir.fragments.SearchFragment;
import com.example.mostafaaboelnasr.attendancemanagment.admin_dir.models.adaptersModels.StudentCardModel;
import com.example.mostafaaboelnasr.attendancemanagment.utils.CheckNetworkState;
import com.example.mostafaaboelnasr.attendancemanagment.R;
import com.example.mostafaaboelnasr.attendancemanagment.admin_dir.models.firebaseModels.PointModel;
import com.example.mostafaaboelnasr.attendancemanagment.admin_dir.models.firebaseModels.StudentModel;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

/**
 * Created by Mostafa Aboelnasr on 4/11/2018.
 */

public class DialogAddStudent extends Dialog {
    DatabaseReference reference;
    private EditText name;
    private EditText email;
    private EditText phone;
    private EditText age;
    private EditText level;
    private EditText specialization;
    private EditText section;
    private EditText password;

    private RelativeLayout add;
    private ArrayList<PointModel> points;

    private ProgressBar progressBar;
    private ImageView ok, not_ok;
    TextView textView;

    public DialogAddStudent(@NonNull Context context) {
        super(context);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_add_student);
        getWindow().getAttributes().windowAnimations = R.style.MyAnimation_Window;
        getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        name = (EditText) findViewById(R.id.student_name);
        email = (EditText) findViewById(R.id.student_email);
        phone = (EditText) findViewById(R.id.student_phone);
        age = (EditText) findViewById(R.id.student_age);
        level = (EditText) findViewById(R.id.student_level);
        specialization = (EditText) findViewById(R.id.student_specialization);
        section = (EditText) findViewById(R.id.student_section);
        password = (EditText) findViewById(R.id.student_pass);

        add = (RelativeLayout) findViewById(R.id.CreateButton);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        ok = (ImageView) findViewById(R.id.ok);
        not_ok = (ImageView) findViewById(R.id.not_ok);
        textView = (TextView) findViewById(R.id.textView);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (CheckNetworkState.isConnect(getContext())) {
                    if (validate()) {
                        add.setEnabled(false);
                        ok.setVisibility(View.GONE);
                        not_ok.setVisibility(View.GONE);
                        textView.setVisibility(View.GONE);
                        progressBar.setVisibility(View.VISIBLE);
                        reference = FirebaseDatabase.getInstance().getReference()
                                .child("University")
                                .child("Faculty")
                                .child("Students").push();
                        StudentModel user_model = new StudentModel(
                                reference.getKey().toString()
                                , name.getText().toString() + ""
                                , phone.getText().toString() + ""
                                , email.getText().toString() + ""
                                , password.getText().toString() + ""
                                , age.getText().toString() + ""
                                , level.getText().toString() + ""
                                , specialization.getText().toString() + ""
                                , section.getText().toString() + ""
                                , "mob DNA");

                        reference.setValue(user_model);
                        reference.child("FirstOpen").setValue("0");

                        new android.os.Handler().postDelayed(
                                new Runnable() {
                                    public void run() {
                                        add.setEnabled(true);
                                        ok.setVisibility(View.VISIBLE);
                                        not_ok.setVisibility(View.GONE);
                                        textView.setVisibility(View.VISIBLE);
                                        progressBar.setVisibility(View.GONE);

                                        StudentsAdapter.studentCardModels.add(
                                                new StudentCardModel(reference.getKey().toString() + ""
                                                        , name.getText().toString() + ""
                                                        , level.getText().toString() + ""));
                                        SearchFragment.studentsAdapter.notifyItemInserted(StudentsAdapter.studentCardModels.size() - 1);

                                        setDefaultBorder(name);
                                        setDefaultBorder(email);
                                        setDefaultBorder(phone);
                                        setDefaultBorder(password);

                                        name.setText("");
                                        email.setText("");
                                        phone.setText("");
                                        password.setText("");
                                    }
                                }, 1200);
                    }
                } else {
                    add.setEnabled(true);
                    ok.setVisibility(View.GONE);
                    not_ok.setVisibility(View.VISIBLE);
                    textView.setVisibility(View.VISIBLE);
                    progressBar.setVisibility(View.GONE);
                }
            }
        });

    }

    private boolean setBorderIfEmpty(EditText editText) {
        if (editText.getText().toString().equals("")) {
            ViewGroup parent;
            parent = (ViewGroup) editText.getParent();
            editText.setBackgroundResource(R.drawable.edittext_red_border);
            ((ImageView) parent.getChildAt(parent.indexOfChild(editText) + 1)).setColorFilter(Color.parseColor("#F44336"));
            return true;
        }
        return false;
    }

    private boolean validate() {
        boolean valid = true;
        if (setBorderIfEmpty(name)) {
            valid = false;
        } else if (setBorderIfEmpty(email)) {
            valid = false;
        } else if (setBorderIfEmpty(phone)) {
            valid = false;
        } else if (setBorderIfEmpty(age)) {
            valid = false;
        } else if (setBorderIfEmpty(level)) {
            valid = false;
        } else if (setBorderIfEmpty(specialization)) {
            valid = false;
        } else if (setBorderIfEmpty(section)) {
            valid = false;
        } else if (setBorderIfEmpty(password)) {
            valid = false;
        }
        return valid;

    }


    private void setDefaultBorder(EditText editText) {
        ViewGroup parent;
        parent = (ViewGroup) editText.getParent();
        editText.setBackgroundResource(R.drawable.rounded_corners);
        ((ImageView) parent.getChildAt(parent.indexOfChild(editText) + 1)).setColorFilter(Color.parseColor("#727272"));
    }

}
