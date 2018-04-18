package com.example.mostafaaboelnasr.attendancemanagment;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }
    public void rec (View v){
        startActivity(new Intent(MainActivity.this,RecognizeActivity.class));

    }
    public void insert (View v){
        startActivity(new Intent(MainActivity.this,First_time_student.class));

    }
}
