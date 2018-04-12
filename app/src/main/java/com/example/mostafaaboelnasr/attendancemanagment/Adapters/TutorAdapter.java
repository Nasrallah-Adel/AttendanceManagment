package com.example.mostafaaboelnasr.attendancemanagment.Adapters;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.mostafaaboelnasr.attendancemanagment.R;
import com.example.mostafaaboelnasr.attendancemanagment.models.adaptersModels.SubjectCardModel;
import com.example.mostafaaboelnasr.attendancemanagment.models.adaptersModels.TutorCardModel;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

/**
 * Created by Mostafa Aboelnasr on 4/11/2018.
 */

public class TutorAdapter extends RecyclerView.Adapter<TutorAdapter.ViewHolder> {

    Context context;
    public static ArrayList<TutorCardModel> tutorCardModels;

    public TutorAdapter(Context context) {
        tutorCardModels = new ArrayList<>();
        this.context = context;
    }


    private AlertDialog AskOption(final int i) {
        AlertDialog myQuittingDialogBox = new AlertDialog.Builder(context)
                //set message, title, and icon
                .setTitle("Delete")
                .setMessage("Do you want to Delete")
                .setIcon(R.drawable.ic_delete_forever_black_24dp)

                .setPositiveButton("Delete", new DialogInterface.OnClickListener() {

                    public void onClick(final DialogInterface dialog, int whichButton) {
                        //your deleting code
                        FirebaseDatabase.getInstance().getReference()
                                .child("University")
                                .child("Faculty")
                                .child("Tutors")
                                .removeValue();
                        tutorCardModels.remove(i);

                        final Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                //Do something after 100ms
                                dialog.dismiss();
                            }
                        }, 500);
                    }

                })

                .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, int which) {

                        final Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                //Do something after 100ms
                                dialog.dismiss();
                            }
                        }, 500);

                    }
                })
                .create();
        return myQuittingDialogBox;

    }

    class ViewHolder extends RecyclerView.ViewHolder {

        public TextView name, phone;
        public ImageView delete;

        public ViewHolder(View itemView) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.tutor_name);
            phone = (TextView) itemView.findViewById(R.id.tutor_phone);
            delete = (ImageView) itemView.findViewById(R.id.deleteButton);
        }
    }

    @Override
    public TutorAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.card_tutor, viewGroup, false);
        TutorAdapter.ViewHolder viewHolder = new TutorAdapter.ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final TutorAdapter.ViewHolder viewHolder, final int i) {
        viewHolder.name.setText(tutorCardModels.get(i).getName());

        viewHolder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AskOption(i).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return tutorCardModels.size();
    }
}