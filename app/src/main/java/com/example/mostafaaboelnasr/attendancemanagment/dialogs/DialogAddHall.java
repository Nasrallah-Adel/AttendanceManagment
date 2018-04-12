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

import com.example.mostafaaboelnasr.attendancemanagment.Adapters.HallsAdapter;
import com.example.mostafaaboelnasr.attendancemanagment.utils.CheckNetworkState;
import com.example.mostafaaboelnasr.attendancemanagment.R;
import com.example.mostafaaboelnasr.attendancemanagment.fragments.SearchFragment;
import com.example.mostafaaboelnasr.attendancemanagment.models.adaptersModels.HallCardModel;
import com.example.mostafaaboelnasr.attendancemanagment.models.firebaseModels.HallModel;
import com.example.mostafaaboelnasr.attendancemanagment.models.firebaseModels.PointModel;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

/**
 * Created by Mostafa Aboelnasr on 4/11/2018.
 */

public class DialogAddHall extends Dialog {
    DatabaseReference reference;
    private EditText name;
    private EditText description;
    private EditText high;
    private RelativeLayout add;
    private ArrayList<PointModel> points;

    private ProgressBar progressBar;
    private ImageView ok, not_ok;
    TextView textView;

    public DialogAddHall(@NonNull Context context) {
        super(context);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_add_hall);
        getWindow().getAttributes().windowAnimations = R.style.MyCustomTheme;
        getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        name = (EditText) findViewById(R.id.hall_name);
        description = (EditText) findViewById(R.id.hall_des);
        high = (EditText) findViewById(R.id.hall_high);
        add = (RelativeLayout) findViewById(R.id.CreateButton);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        ok = (ImageView) findViewById(R.id.ok);
        not_ok = (ImageView) findViewById(R.id.not_ok);
        textView = (TextView) findViewById(R.id.textView);
        points = new ArrayList();
        points.add(new PointModel("1", "2", "3"));
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
                                .child("Halls").push();
                        HallModel user_model = new HallModel(reference.getKey().toString()
                                , name.getText().toString() + ""
                                , description.getText().toString() + ""
                                , high.getText().toString() + ""
                                , points);

                        reference.setValue(user_model);

                        new android.os.Handler().postDelayed(
                                new Runnable() {
                                    public void run() {
                                        add.setEnabled(true);
                                        ok.setVisibility(View.VISIBLE);
                                        not_ok.setVisibility(View.GONE);
                                        textView.setVisibility(View.VISIBLE);
                                        progressBar.setVisibility(View.GONE);

                                        HallsAdapter.hallCardModels.add(
                                                new HallCardModel(reference.getKey().toString() + ""
                                                        , name.getText().toString() + ""
                                                        , description.getText().toString() + ""));
                                        SearchFragment.hallsAdapter.notifyItemInserted(HallsAdapter.hallCardModels.size() - 1);

                                        setDefaultBorder(name);
                                        setDefaultBorder(description);
                                        setDefaultBorder(high);

                                        name.setText("");
                                        description.setText("");
                                        high.setText("");

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

    private void setDefaultBorder(EditText editText) {
        ViewGroup parent;
        parent = (ViewGroup) editText.getParent();
        editText.setBackgroundResource(R.drawable.rounded_corners);
        ((ImageView) parent.getChildAt(parent.indexOfChild(editText) + 1)).setColorFilter(Color.parseColor("#727272"));
    }

    private boolean validate() {
        boolean valid = true;
        if (setBorderIfEmpty(name)) {
            valid = false;
        } else if (setBorderIfEmpty(description)) {
            valid = false;
        } else if (setBorderIfEmpty(high)) {
            valid = false;
        }
        return valid;

    }

}
