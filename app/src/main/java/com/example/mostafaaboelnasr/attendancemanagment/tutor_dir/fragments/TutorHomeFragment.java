package com.example.mostafaaboelnasr.attendancemanagment.tutor_dir.fragments;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.mostafaaboelnasr.attendancemanagment.R;
import com.example.mostafaaboelnasr.attendancemanagment.tutor_dir.TutorActivity;
import com.example.mostafaaboelnasr.attendancemanagment.tutor_dir.adapters.SessionsAdapter;
import com.example.mostafaaboelnasr.attendancemanagment.tutor_dir.diaogs.DialogAddSession;
import com.example.mostafaaboelnasr.attendancemanagment.tutor_dir.diaogs.DialogLiveSession;
import com.example.mostafaaboelnasr.attendancemanagment.tutor_dir.models.SessionModel;
import com.example.mostafaaboelnasr.attendancemanagment.utils.SpeedyLinearLayoutManager;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class TutorHomeFragment extends Fragment {
    public static LinearLayoutManager layoutManager;
    public static SessionsAdapter sessionsAdapter;
    public static RecyclerView recyclerView;
    public static boolean hadLiveSession = false;
    public static String liveSessionID = null;
    public static LinearLayout create_seasionLinearLayout, live_seasionLinearLayout;

    View view;
    Context context;
    SwipeRefreshLayout sesionsSwipeRefreshLayout;
    ImageView searchImageView, delete_searchImageView;
    EditText search_EditText;
    View toggleView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_tutor_home, container, false);
        context = getContext();
        create_seasionLinearLayout = view.findViewById(R.id.create_seasionLinearLayout);
        live_seasionLinearLayout = view.findViewById(R.id.live_seasionLinearLayout);
        sesionsSwipeRefreshLayout = view.findViewById(R.id.sesionsSwipeRefreshLayout);
        search_EditText = view.findViewById(R.id.search_EditText);
        searchImageView = view.findViewById(R.id.search);
        delete_searchImageView = view.findViewById(R.id.delete_search);
        toggleView = view.findViewById(R.id.toggleView);
        liveSession();
        sesionsSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                searchByDate("");
                sesionsSwipeRefreshLayout.setRefreshing(false);
            }
        });
        searchImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchByDate(search_EditText.getText().toString());
            }
        });
        delete_searchImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                search_EditText.setText("");
                searchByDate(search_EditText.getText().toString());
            }
        });
        create_seasionLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogAddSession dialogAddSession = new DialogAddSession(context);
                dialogAddSession.show();
            }
        });
        live_seasionLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                liveSessionID
//                liveSessionID = "";
                DialogLiveSession session = new DialogLiveSession(context);
                session.show();
            }
        });
        searchByDate("");
//        swipe_refresh = view.findViewById(R.id.swipe_refresh);

        toggleView();
        return view;
    }

    private void liveSession() {
        FirebaseDatabase.getInstance().getReference()
                .child("University")
                .child("Faculty")
                .child("LiveSessions")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            SessionModel sessionModel = snapshot.getValue(SessionModel.class);
                            if (sessionModel.getTutorID().contains(getTutorId())) {
                                hadLiveSession = true;
                                liveSessionID = sessionModel.getId();
                            }
                        }
                        if (hadLiveSession) {
                            live_seasionLinearLayout.setVisibility(View.VISIBLE);
                            create_seasionLinearLayout.setVisibility(View.GONE);
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Toast.makeText(context, "Check internet Connection! or message support team.", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void searchByDate(final String date) {

        recyclerView = (RecyclerView) view.findViewById(R.id.sessions_Recycler_view);
        layoutManager = new SpeedyLinearLayoutManager(context, SpeedyLinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);

        sessionsAdapter = new SessionsAdapter(context);
        recyclerView.setAdapter(sessionsAdapter);
        SessionsAdapter.ids = new ArrayList<>();
        SessionsAdapter.names = new ArrayList<>();
        SessionsAdapter.dates = new ArrayList<>();

        FirebaseDatabase.getInstance().getReference()
                .child("University")
                .child("Faculty")
//                .child("Tutors")
//                .child("-L9r3R2A4-mc_t6tzmzO")
                .child("Sessions")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            SessionModel sessionModel = snapshot.getValue(SessionModel.class);
                            if (sessionModel.getDate().contains(date) && sessionModel.getTutorID().equals(TutorActivity.mySharedPreferences.getString("id", ""))) {
                                SessionsAdapter.ids.add(sessionModel.getId());
                                SessionsAdapter.names.add("");
                                SessionsAdapter.dates.add(sessionModel.getDate());

                                sessionsAdapter.notifyItemInserted(SessionsAdapter.ids.size() - 1);
                            }
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Toast.makeText(context, "Check internet Connection! or message support team.", Toast.LENGTH_SHORT).show();
                    }
                });

    }

    private String getTutorId() {
        return TutorActivity.id;
    }

    boolean toggle = true;

    private void toggleView() {
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                //Do something after 100ms
                if (toggle) {
                    toggleView.setBackground(context.getResources().getDrawable(R.drawable.circle));
                    toggle = false;
                } else if (!toggle) {
                    toggleView.setBackground(context.getResources().getDrawable(R.drawable.circle_solid));
                    toggle = true;
                }
                handler.postDelayed(this, 1000);
            }
        }, 1000);
    }
}
