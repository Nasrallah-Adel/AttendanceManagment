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
import com.example.mostafaaboelnasr.attendancemanagment.models.adaptersModels.HallCardModel;
import com.example.mostafaaboelnasr.attendancemanagment.models.adaptersModels.SubjectCardModel;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

/**
 * Created by Mostafa Aboelnasr on 4/11/2018.
 */

public class SubjectsAdapter extends RecyclerView.Adapter<SubjectsAdapter.ViewHolder> {

    Context context;
    public static ArrayList<SubjectCardModel> subjectCardModels;
//    public static ArrayList<String> ids, names, descriptions;

    public SubjectsAdapter(Context context) {
        subjectCardModels = new ArrayList<>();
//        ids = new ArrayList<>();
//        names = new ArrayList<>();
//        descriptions = new ArrayList<>();
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
                                .child("Subjects")
                                .removeValue();
                        subjectCardModels.remove(i);
//                        ids.remove(i);
//                        names.remove(i);
//                        descriptions.remove(i);

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

        public TextView name;
        public ImageView delete;

        public ViewHolder(View itemView) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.subject_name);
            delete = (ImageView) itemView.findViewById(R.id.deleteButton);
        }
    }

    @Override
    public SubjectsAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.card_subject, viewGroup, false);
        SubjectsAdapter.ViewHolder viewHolder = new SubjectsAdapter.ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final SubjectsAdapter.ViewHolder viewHolder, final int i) {
        viewHolder.name.setText(subjectCardModels.get(i).getName());

        viewHolder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AskOption(i).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return subjectCardModels.size();
    }
}