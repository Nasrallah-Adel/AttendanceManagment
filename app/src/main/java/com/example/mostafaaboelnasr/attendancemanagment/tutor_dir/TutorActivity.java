package com.example.mostafaaboelnasr.attendancemanagment.tutor_dir;

import android.content.SharedPreferences;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.mostafaaboelnasr.attendancemanagment.R;
import com.example.mostafaaboelnasr.attendancemanagment.tutor_dir.fragments.TutorHomeFragment;

public class TutorActivity extends AppCompatActivity {

    public static SharedPreferences mySharedPreferences;
    public static String id, name, email, phone, pass, state;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tutor);
        mySharedPreferences = getSharedPreferences("MY_SHARED_Preferences", MODE_PRIVATE);
        id = mySharedPreferences.getString("id", "");
        name = mySharedPreferences.getString("name", "");
        email = mySharedPreferences.getString("email", "");
        phone = mySharedPreferences.getString("phone", "");
        pass = mySharedPreferences.getString("password", "");

        TutorHomeFragment newFragment = new TutorHomeFragment();
        Bundle args = new Bundle();
        newFragment.setArguments(args);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.viewpagerTutor, newFragment);
        transaction.addToBackStack(null);
        transaction.commit();

    }
}
