package com.example.mostafaaboelnasr.attendancemanagment.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.mostafaaboelnasr.attendancemanagment.R;

public class HomeFragment extends Fragment {
    View view;
    RelativeLayout studentsLayout;
    ProgressBar studentsProgressBar;
    TextView studentsTextView;

    RelativeLayout tutorsLayout;
    ProgressBar tutorsProgressBar;
    TextView tutorsTextView;

    RelativeLayout hallsLayout;
    ProgressBar hallsProgressBar;
    TextView hallsTextView;

    RelativeLayout subjectsLayout;
    ProgressBar subjectsProgressBar;
    TextView subjectsTextView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_home, container, false);
        studentsLayout = (RelativeLayout) view.findViewById(R.id.studentsLayout);
        studentsProgressBar = (ProgressBar) view.findViewById(R.id.studentsProgressBar);
        studentsTextView = (TextView) view.findViewById(R.id.studentsTextView);

        tutorsLayout = (RelativeLayout) view.findViewById(R.id.tutorsLayout);
        tutorsProgressBar = (ProgressBar) view.findViewById(R.id.tutorsProgressBar);
        tutorsTextView = (TextView) view.findViewById(R.id.tutorsTextView);

        hallsLayout = (RelativeLayout) view.findViewById(R.id.hallsLayout);
        hallsProgressBar = (ProgressBar) view.findViewById(R.id.hallsProgressBar);
        hallsTextView = (TextView) view.findViewById(R.id.hallsTextView);

        subjectsLayout = (RelativeLayout) view.findViewById(R.id.subjectsLayout);
        subjectsProgressBar = (ProgressBar) view.findViewById(R.id.subjectsProgressBar);
        subjectsTextView = (TextView) view.findViewById(R.id.subjectsTextView);

        studentsLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                studentsLayout.setEnabled(false);
                studentsProgressBar.setVisibility(View.VISIBLE);
                new android.os.Handler().postDelayed(
                        new Runnable() {
                            public void run() {
                                SearchFragment newFragment = new SearchFragment();
                                Bundle args = new Bundle();
                                args.putString("key", "Students");
                                newFragment.setArguments(args);
                                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                                transaction.replace(R.id.viewpagerHome, newFragment);
                                transaction.addToBackStack(null);
                                transaction.commit();
                            }
                        }, 600);

            }
        });
        tutorsLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tutorsLayout.setEnabled(false);
                tutorsProgressBar.setVisibility(View.VISIBLE);
                new android.os.Handler().postDelayed(
                        new Runnable() {
                            public void run() {
                                SearchFragment newFragment = new SearchFragment();
                                Bundle args = new Bundle();
                                args.putString("key", "Tutors");
                                newFragment.setArguments(args);
                                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                                transaction.replace(R.id.viewpagerHome, newFragment);
                                transaction.addToBackStack(null);
                                transaction.commit();
                            }
                        }, 600);

            }
        });
        hallsLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hallsLayout.setEnabled(false);
                hallsProgressBar.setVisibility(View.VISIBLE);
                new android.os.Handler().postDelayed(
                        new Runnable() {
                            public void run() {
                                SearchFragment newFragment = new SearchFragment();
                                Bundle args = new Bundle();
                                args.putString("key", "Halls");
                                newFragment.setArguments(args);
                                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                                transaction.replace(R.id.viewpagerHome, newFragment);
                                transaction.addToBackStack(null);
                                transaction.commit();
                            }
                        }, 600);

            }
        });
        subjectsLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                subjectsLayout.setEnabled(false);
                subjectsProgressBar.setVisibility(View.VISIBLE);
                new android.os.Handler().postDelayed(
                        new Runnable() {
                            public void run() {
                                SearchFragment newFragment = new SearchFragment();
                                Bundle args = new Bundle();
                                args.putString("key", "Subjects");
                                newFragment.setArguments(args);
                                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                                transaction.replace(R.id.viewpagerHome, newFragment);
                                transaction.addToBackStack(null);
                                transaction.commit();
                            }
                        }, 600);

            }
        });
        return view;
    }
}
