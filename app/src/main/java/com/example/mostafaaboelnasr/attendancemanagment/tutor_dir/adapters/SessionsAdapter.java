package com.example.mostafaaboelnasr.attendancemanagment.tutor_dir.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mostafaaboelnasr.attendancemanagment.R;
import com.example.mostafaaboelnasr.attendancemanagment.tutor_dir.diaogs.DialogLastSession;

import java.util.ArrayList;

/**
 * Created by Mostafa Aboelnasr on 4/15/2018.
 */

public class SessionsAdapter extends RecyclerView.Adapter<SessionsAdapter.ViewHolder> {

    Context context;
    public static ArrayList<String> ids, names, dates;

    public SessionsAdapter(Context context) {
        ids = new ArrayList<>();
        names = new ArrayList<>();
        dates = new ArrayList<>();
        this.context = context;
    }


    class ViewHolder extends RecyclerView.ViewHolder {

        public TextView name, date;

        public ViewHolder(View itemView) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.nameTextView);
            date = (TextView) itemView.findViewById(R.id.dateTextView);
        }
    }

    @Override
    public SessionsAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.card_session, viewGroup, false);
        SessionsAdapter.ViewHolder viewHolder = new SessionsAdapter.ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final SessionsAdapter.ViewHolder viewHolder, final int i) {
        viewHolder.name.setText(names.get(i));
        viewHolder.date.setText(dates.get(i));

        viewHolder.name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, ids.get(i) + "", Toast.LENGTH_SHORT).show();
                new DialogLastSession(context, ids.get(i));
            }
        });
    }

    @Override
    public int getItemCount() {
        return ids.size();
    }
}