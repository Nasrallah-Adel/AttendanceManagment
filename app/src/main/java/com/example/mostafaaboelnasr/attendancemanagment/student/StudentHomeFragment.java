package com.example.mostafaaboelnasr.attendancemanagment.student;

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
import android.widget.TextView;
import android.widget.Toast;

import com.example.mostafaaboelnasr.attendancemanagment.R;
import com.example.mostafaaboelnasr.attendancemanagment.admin_dir.models.adaptersModels.SubjectCardModel;
import com.example.mostafaaboelnasr.attendancemanagment.admin_dir.models.firebaseModels.SubjectModel;
import com.example.mostafaaboelnasr.attendancemanagment.student.adapters.SubjectsQueueAdapter;
import com.example.mostafaaboelnasr.attendancemanagment.student.dialogs.DialogAddSubjectQueue;
import com.example.mostafaaboelnasr.attendancemanagment.utils.SpeedyLinearLayoutManager;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class StudentHomeFragment extends Fragment {
    View view;
    Context context;
    SwipeRefreshLayout swipe_refresh;
    public static RecyclerView recyclerView;
    public static LinearLayoutManager layoutManager;
    public static SubjectsQueueAdapter subjectsQueueAdapter;
    ArrayList<String> queueSubjectsIDs, queueSubjectsNames;

    public static TextView addSubject;
    View toggleView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_student_home, container, false);
        context = getContext();
        queueSubjectsIDs = new ArrayList<>();
        queueSubjectsNames = new ArrayList<>();
        addSubject = view.findViewById(R.id.addSubject);
        recyclerView = (RecyclerView) view.findViewById(R.id.subjects_Recycler_view);
        layoutManager = new SpeedyLinearLayoutManager(context, SpeedyLinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        toggleView = view.findViewById(R.id.toggleView);

//        swipe_refresh = view.findViewById(R.id.swipe_refresh);
//        swipe_refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
//            @Override
//            public void onRefresh() {
//                loadSubjects();
//                swipe_refresh.setRefreshing(false);
//            }
//        });

        subjectsQueueAdapter = new SubjectsQueueAdapter(context);
        recyclerView.setAdapter(subjectsQueueAdapter);
        SubjectsQueueAdapter.subjectCardModels = new ArrayList<>();
        addSubject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DialogAddSubjectQueue(getContext());
            }
        });
        loadSubjects();
        toggleView();
        return view;
    }


    private void loadSubjects() {
        SubjectsQueueAdapter.subjectCardModels = new ArrayList<>();
        queueSubjectsIDs = new ArrayList<>();
        queueSubjectsNames = new ArrayList<>();
        FirebaseDatabase.getInstance().getReference()
                .child("University")
                .child("Faculty")
                .child("Students")
                .child(StudentActivity.mySharedPreferences.getString("id", ""))
                .child("Subjects")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            try {
                                SubjectModel model = snapshot.getValue(SubjectModel.class);
                                SubjectsQueueAdapter.subjectCardModels.add(new SubjectCardModel(model.getId() + "", model.getName() + ""));
                                subjectsQueueAdapter.notifyItemInserted(SubjectsQueueAdapter.subjectCardModels.size() - 1);
                                queueSubjectsIDs.add(model.getId() + "");
                                queueSubjectsNames.add(model.getName() + "");
                            } catch (com.google.firebase.database.DatabaseException e) {
                            }
                        }
//                        if (SubjectsQueueAdapter.subjectCardModels.size() >= 5) {
//                            StudentHomeFragment.addSubject.setVisibility(View.GONE);
//                        }
//                        Toast.makeText(context, dataSnapshot.getChildrenCount() + "", Toast.LENGTH_SHORT).show();
                        if ((int) dataSnapshot.getChildrenCount() >= 5) {
                            StudentHomeFragment.addSubject.setVisibility(View.GONE);
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }
                });
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