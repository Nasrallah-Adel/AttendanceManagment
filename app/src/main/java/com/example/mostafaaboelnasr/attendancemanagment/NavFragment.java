package com.example.mostafaaboelnasr.attendancemanagment;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.mostafaaboelnasr.attendancemanagment.admin_dir.dialogs.SendProblemDialog;

public class NavFragment extends Fragment {
    View view;
    Context context;
    LinearLayout setting, send_report, about_us, log_out;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_nav, container, false);
        context = getContext();

        send_report = (LinearLayout) view.findViewById(R.id.send_report);
        about_us = (LinearLayout) view.findViewById(R.id.about_us);
        log_out = (LinearLayout) view.findViewById(R.id.log_out);

        send_report.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new SendProblemDialog(context);
            }
        });
        about_us.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        log_out.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final Dialog dialog = new Dialog(context);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.dialog_log_out);
                dialog.getWindow().getAttributes().windowAnimations = R.style.MyCustomTheme;
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
//                dialog.setTitle("Comments");

                TextView logout = (TextView) dialog.findViewById(R.id.log_out);
                LinearLayout cancel = (LinearLayout) dialog.findViewById(R.id.cancel);
                TextView report = (TextView) dialog.findViewById(R.id.report);
                logout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        SharedPreferences.Editor editor = context.getSharedPreferences("MY_SHARED_Preferences", context.MODE_PRIVATE).edit();
                        editor.remove("id");
                        editor.remove("password");
                        editor.remove("email");
                        editor.remove("name");
                        editor.remove("username");
                        editor.clear();
                        editor.commit();

                        Intent intent = new Intent(context, MainActivity.class);
                        startActivity(intent);
                        getActivity().finish();
                    }
                });
                cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                report.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        new SendProblemDialog(context);
                    }
                });

                dialog.show();
            }
        });

        return view;
    }
}
