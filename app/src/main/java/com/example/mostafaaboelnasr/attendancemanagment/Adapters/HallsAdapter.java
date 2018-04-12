package com.example.mostafaaboelnasr.attendancemanagment.Adapters;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
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
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

/**
 * Created by Mostafa Aboelnasr on 4/11/2018.
 */

public class HallsAdapter extends RecyclerView.Adapter<HallsAdapter.ViewHolder> {

    Context context;
    public static ArrayList<HallCardModel> hallCardModels;

    public HallsAdapter(Context context) {
        hallCardModels = new ArrayList<>();
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
                                .child("Halls")
                                .removeValue();
                        hallCardModels.remove(i);

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

        public TextView name, des;
        public ImageView delete;

        public ViewHolder(View itemView) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.hall_name_TextViewCard);
            des = (TextView) itemView.findViewById(R.id.hall_des_TextViewCard);
            delete = (ImageView) itemView.findViewById(R.id.deleteButton);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.card_hall, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final int i) {
        viewHolder.name.setText(hallCardModels.get(i).getName());
        viewHolder.des.setText(hallCardModels.get(i).getDes());

        viewHolder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AskOption(i).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return hallCardModels.size();
    }
}