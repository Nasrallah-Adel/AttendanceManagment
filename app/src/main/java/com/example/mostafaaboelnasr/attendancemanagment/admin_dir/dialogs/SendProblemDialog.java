package com.example.mostafaaboelnasr.attendancemanagment.admin_dir.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.mostafaaboelnasr.attendancemanagment.R;
import com.example.mostafaaboelnasr.attendancemanagment.admin_dir.HomeActivity;
import com.example.mostafaaboelnasr.attendancemanagment.admin_dir.models.sendProblemModel;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;

/**
 * Created by Mostafa Aboelnasr on 3/29/2018.
 */

public class SendProblemDialog {

    public SendProblemDialog(Context context) {
        final Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.send_problem_with_app);
        dialog.setTitle("Send Your Problem");

        Button problem_Button_send_problem_dialog = (Button) dialog.findViewById(R.id.problem_Button_send_problem_dialog);

        problem_Button_send_problem_dialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar c = Calendar.getInstance();
                DatabaseReference referenceProbem = FirebaseDatabase.getInstance().getReference()
                        .child("System").child("Problems").push();
                EditText problem_EditText_send_problem_dialog = (EditText) dialog.findViewById(R.id.problem_EditText_send_problem_dialog);
                EditText advise_EditText_send_problem_dialog = (EditText) dialog.findViewById(R.id.advise_EditText_send_problem_dialog);
                referenceProbem.setValue(new sendProblemModel(
                        referenceProbem.getKey().toString()
                        , HomeActivity.mySharedPreferences.getString("id", "") + ""
                        , HomeActivity.mySharedPreferences.getString("name", "") + ""
                        , c.get(Calendar.YEAR) + "/" + c.get(Calendar.MONTH) + "/" + c.get(Calendar.DAY_OF_MONTH) + ""
                        , problem_EditText_send_problem_dialog.getText().toString() + ""
                        , advise_EditText_send_problem_dialog.getText().toString() + ""
                        , "0"
                ));
                dialog.cancel();
            }
        });

        dialog.show();
    }
}
