package com.example.mostafaaboelnasr.attendancemanagment;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.example.mostafaaboelnasr.attendancemanagment.admin_dir.HomeActivity;
import com.example.mostafaaboelnasr.attendancemanagment.student.StudentActivity;
import com.example.mostafaaboelnasr.attendancemanagment.tutor_dir.TutorActivity;

public class MainActivity extends AppCompatActivity {
    //    Intent intent;
    public static SharedPreferences mySharedPreferences;
    public static String id, name, email, phone, pass, state;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        mySharedPreferences = getSharedPreferences("MY_SHARED_Preferences", MODE_PRIVATE);
//        id = mySharedPreferences.getString("id", "");
//        name = mySharedPreferences.getString("name", "");
//        email = mySharedPreferences.getString("email", "");
//        phone = mySharedPreferences.getString("phone", "");
//        pass = mySharedPreferences.getString("password", "");
//        state = mySharedPreferences.getString("state", "");

//        FragmentManager fragmentManager = getSupportFragmentManager();
//        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();


//        SharedPreferences.Editor editor = getSharedPreferences("MY_SHARED_Preferences", MODE_PRIVATE).edit();
//        editor.putString("id", "lm");
//        editor.putString("name", "dlkv");
//        editor.putString("email", "dk");
//        editor.putString("pass", "kd");
//        editor.putString("state", "Tutors");
//        editor.commit();

        mySharedPreferences = getSharedPreferences("MY_SHARED_Preferences", MODE_PRIVATE);
        id = mySharedPreferences.getString("id", "");
        name = mySharedPreferences.getString("name", "");
        email = mySharedPreferences.getString("email", "");
        pass = mySharedPreferences.getString("pass", "");
        state = mySharedPreferences.getString("state", "");

        if (!id.equals("") && !name.equals("") && !email.equals("") && !pass.equals("") && !state.equals("")) {
            if (state.equals("Admins")) {
//                SignInFragment signInFragment = new SignInFragment();
//                signInFragment.adminLogin(email, pass);
                startActivity(new Intent(MainActivity.this, HomeActivity.class));
                finish();
            } else if (state.equals("Tutors")) {
//                SignInFragment signInFragment = new SignInFragment();
//                signInFragment.tutorLogin(email, pass);
                startActivity(new Intent(MainActivity.this, TutorActivity.class));
                finish();
            } else if (state.equals("Students")) {
//                SignInFragment signInFragment = new SignInFragment();
//                signInFragment.studentLogin(email, pass);
                startActivity(new Intent(MainActivity.this, StudentActivity.class));
                finish();
            }
        } else {
            Toast.makeText(this, "notSpecified", Toast.LENGTH_SHORT).show();
            notSpecified();
        }


    }

    private void notSpecified() {
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                SignInFragment newFragment = new SignInFragment();
                Bundle args = new Bundle();
                newFragment.setArguments(args);
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.viewpagerMain, newFragment);
                transaction.commit();
            }
        }, 1000);
    }

}