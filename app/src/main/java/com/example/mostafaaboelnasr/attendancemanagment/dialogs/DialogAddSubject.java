package com.example.mostafaaboelnasr.attendancemanagment.dialogs;

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

import com.example.mostafaaboelnasr.attendancemanagment.Adapters.StudentsAdapter;
import com.example.mostafaaboelnasr.attendancemanagment.Adapters.SubjectsAdapter;
import com.example.mostafaaboelnasr.attendancemanagment.fragments.SearchFragment;
import com.example.mostafaaboelnasr.attendancemanagment.models.adaptersModels.StudentCardModel;
import com.example.mostafaaboelnasr.attendancemanagment.models.adaptersModels.SubjectCardModel;
import com.example.mostafaaboelnasr.attendancemanagment.utils.CheckNetworkState;
import com.example.mostafaaboelnasr.attendancemanagment.R;
import com.example.mostafaaboelnasr.attendancemanagment.models.firebaseModels.SubjectModel;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by Mostafa Aboelnasr on 4/11/2018.
 */

public class DialogAddSubject extends Dialog {
    DatabaseReference reference;
    private EditText name;
    private RelativeLayout add;

    private ProgressBar progressBar;
    private ImageView ok, not_ok;
    TextView textView;

    public DialogAddSubject(@NonNull Context context) {
        super(context);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_add_subject);
        getWindow().getAttributes().windowAnimations = R.style.MyCustomTheme;
        getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        name = (EditText) findViewById(R.id.subject_name);
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
                                .child("Subjects").push();
                        SubjectModel user_model = new SubjectModel(
                                reference.getKey().toString()
                                , name.getText().toString() + "");

                        reference.setValue(user_model);

                        new android.os.Handler().postDelayed(
                                new Runnable() {
                                    public void run() {
                                        add.setEnabled(true);
                                        ok.setVisibility(View.VISIBLE);
                                        not_ok.setVisibility(View.GONE);
                                        textView.setVisibility(View.VISIBLE);
                                        progressBar.setVisibility(View.GONE);

                                        SubjectsAdapter.subjectCardModels.add(
                                                new SubjectCardModel(reference.getKey().toString() + ""
                                                        , name.getText().toString() + ""));
                                        SearchFragment.subjectsAdapter.notifyItemInserted(SubjectsAdapter.subjectCardModels.size() - 1);

                                        setDefaultBorder(name);
                                        name.setText("");
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
