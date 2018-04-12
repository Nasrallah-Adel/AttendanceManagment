package com.example.mostafaaboelnasr.attendancemanagment.fragments;

import android.content.Context;
import android.os.Bundle;
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
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.mostafaaboelnasr.attendancemanagment.Adapters.HallsAdapter;
import com.example.mostafaaboelnasr.attendancemanagment.Adapters.StudentsAdapter;
import com.example.mostafaaboelnasr.attendancemanagment.Adapters.SubjectsAdapter;
import com.example.mostafaaboelnasr.attendancemanagment.Adapters.TutorAdapter;
import com.example.mostafaaboelnasr.attendancemanagment.utils.CheckNetworkState;
import com.example.mostafaaboelnasr.attendancemanagment.R;
import com.example.mostafaaboelnasr.attendancemanagment.utils.SpeedyLinearLayoutManager;
import com.example.mostafaaboelnasr.attendancemanagment.dialogs.DialogAddHall;
import com.example.mostafaaboelnasr.attendancemanagment.dialogs.DialogAddStudent;
import com.example.mostafaaboelnasr.attendancemanagment.dialogs.DialogAddSubject;
import com.example.mostafaaboelnasr.attendancemanagment.dialogs.DialogAddTutor;
import com.example.mostafaaboelnasr.attendancemanagment.models.adaptersModels.HallCardModel;
import com.example.mostafaaboelnasr.attendancemanagment.models.adaptersModels.StudentCardModel;
import com.example.mostafaaboelnasr.attendancemanagment.models.adaptersModels.SubjectCardModel;
import com.example.mostafaaboelnasr.attendancemanagment.models.adaptersModels.TutorCardModel;
import com.example.mostafaaboelnasr.attendancemanagment.models.firebaseModels.HallModel;
import com.example.mostafaaboelnasr.attendancemanagment.models.firebaseModels.StudentModel;
import com.example.mostafaaboelnasr.attendancemanagment.models.firebaseModels.SubjectModel;
import com.example.mostafaaboelnasr.attendancemanagment.models.firebaseModels.TutorModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class SearchFragment extends Fragment {
    View view;
    Context context;
    String key;
    ImageView add_newImageView, searchImageView, delete_searchImageView;
    EditText search_EditText;
    SwipeRefreshLayout swipe_refresh;
    public static RecyclerView recyclerView;
    public static LinearLayoutManager layoutManager;
    public static HallsAdapter hallsAdapter;
    public static StudentsAdapter studentsAdapter;
    public static SubjectsAdapter subjectsAdapter;
    public static TutorAdapter tutorAdapter;
    LinearLayout no_items, no_internet;
    ProgressBar progressBar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_search, container, false);
        init();
        add_newImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                add(key);
            }
        });
        searchImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                search(key, search_EditText.getText().toString().trim());
            }
        });
        delete_searchImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                search_EditText.setText("");
                search(key, "");
            }
        });
        swipe_refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                search(key, "");
                swipe_refresh.setRefreshing(false);
            }
        });
        search(key, "");
        return view;
    }

    private void add(String key) {
        if (key.equals("Halls")) {
            DialogAddHall dialog = new DialogAddHall(getContext());
            dialog.show();
        } else if (key.equals("Tutors")) {
            DialogAddTutor dialog = new DialogAddTutor(getContext());
            dialog.show();
        } else if (key.equals("Students")) {
            DialogAddStudent dialog = new DialogAddStudent(getContext());
            dialog.show();
        } else if (key.equals("Subjects")) {
            DialogAddSubject dialog = new DialogAddSubject(getContext());
            dialog.show();
        }
    }

    private void search(String key, String content) {
        recyclerView = (RecyclerView) view.findViewById(R.id.Recycler_view);
        layoutManager = new SpeedyLinearLayoutManager(context, SpeedyLinearLayoutManager.VERTICAL, true);
        recyclerView.setLayoutManager(layoutManager);

        if (key.equals("Halls")) {
            hallsAdapter = new HallsAdapter(context);
            recyclerView.setAdapter(hallsAdapter);
            HallsAdapter.hallCardModels = new ArrayList<>();
            searchHallsByContent(key, content);
        } else if (key.equals("Tutors")) {
            tutorAdapter = new TutorAdapter(context);
            recyclerView.setAdapter(tutorAdapter);
            TutorAdapter.tutorCardModels = new ArrayList<>();
            searchHallsByContent(key, content);
        } else if (key.equals("Students")) {
            studentsAdapter = new StudentsAdapter(context);
            recyclerView.setAdapter(studentsAdapter);
            StudentsAdapter.studentCardModels = new ArrayList<>();
            searchHallsByContent(key, content);
        } else if (key.equals("Subjects")) {
            subjectsAdapter = new SubjectsAdapter(context);
            recyclerView.setAdapter(subjectsAdapter);
            SubjectsAdapter.subjectCardModels = new ArrayList<>();
            searchHallsByContent(key, content);
        }
    }

    private void searchHallsByContent(final String key, final String content) {
        if (CheckNetworkState.isConnect(context)) {
            FirebaseDatabase.getInstance().getReference()
                    .child("University")
                    .child("Faculty")
                    .child(key).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        addModel(snapshot, key, content);
                    }
                    if (checkEmpty(key)) {
                        no_internet.setVisibility(View.GONE);
                        no_items.setVisibility(View.VISIBLE);
                        progressBar.setVisibility(View.GONE);
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Toast.makeText(context, "Check internet Connection! or message support team.", Toast.LENGTH_SHORT).show();
                    no_internet.setVisibility(View.VISIBLE);
                    no_items.setVisibility(View.GONE);
                    progressBar.setVisibility(View.GONE);
                }
            });
        } else {
            no_internet.setVisibility(View.VISIBLE);
            no_items.setVisibility(View.GONE);
            progressBar.setVisibility(View.GONE);
        }
    }

    private boolean checkEmpty(String key) {
        if (key.equals("Halls")) {
            if (HallsAdapter.hallCardModels.isEmpty()) {
                return true;
            }
        } else if (key.equals("Tutors")) {
            if (TutorAdapter.tutorCardModels.isEmpty()) {
                return true;
            }
        } else if (key.equals("Students")) {
            if (StudentsAdapter.studentCardModels.isEmpty()) {
                return true;
            }
        } else if (key.equals("Subjects")) {
            if (SubjectsAdapter.subjectCardModels.isEmpty()) {
                return true;
            }
        }
        return false;
    }

    private void addModel(DataSnapshot snapshot, String key, String content) {
        if (key.equals("Halls")) {
            HallModel moduel = snapshot.getValue(HallModel.class);
            if (moduel.getName().toString().contains(content + "")) {
                HallsAdapter.hallCardModels.add(new HallCardModel(moduel.getId(), moduel.getName(), moduel.getDescription()));
                hallsAdapter.notifyItemInserted(HallsAdapter.hallCardModels.size() - 1);
            }
        } else if (key.equals("Tutors")) {
            TutorModel moduel = snapshot.getValue(TutorModel.class);
            if (moduel.getName().toString().contains(content + "")) {
                TutorAdapter.tutorCardModels.add(new TutorCardModel(moduel.getId(), moduel.getName(), moduel.getPhone()));
                tutorAdapter.notifyItemInserted(TutorAdapter.tutorCardModels.size() - 1);
            }
        } else if (key.equals("Students")) {
            StudentModel moduel = snapshot.getValue(StudentModel.class);
            if (moduel.getName().toString().contains(content + "")) {
                StudentsAdapter.studentCardModels.add(new StudentCardModel(moduel.getId(), moduel.getName(), moduel.getLevel()));
                studentsAdapter.notifyItemInserted(StudentsAdapter.studentCardModels.size() - 1);
            }
        } else if (key.equals("Subjects")) {
            SubjectModel moduel = snapshot.getValue(SubjectModel.class);
            if (moduel.getName().toString().contains(content + "")) {
                SubjectsAdapter.subjectCardModels.add(new SubjectCardModel(moduel.getId(), moduel.getName()));
                subjectsAdapter.notifyItemInserted(SubjectsAdapter.subjectCardModels.size() - 1);
            }
        }
    }

    private void init() {
        Bundle args = getArguments();
        key = args.getString("key");
        context = getContext();
        progressBar = view.findViewById(R.id.progressBar);
        swipe_refresh = view.findViewById(R.id.swipe_refresh);
        search_EditText = view.findViewById(R.id.search_EditText);
        add_newImageView = view.findViewById(R.id.add_newImageView);
        delete_searchImageView = view.findViewById(R.id.delete_searchImageView);
        searchImageView = view.findViewById(R.id.searchImageView);
        no_internet = view.findViewById(R.id.no_internet);
        no_items = view.findViewById(R.id.no_items);
    }


}
