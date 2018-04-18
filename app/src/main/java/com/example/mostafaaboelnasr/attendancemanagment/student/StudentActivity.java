package com.example.mostafaaboelnasr.attendancemanagment.student;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.mostafaaboelnasr.attendancemanagment.R;
import com.example.mostafaaboelnasr.attendancemanagment.admin_dir.models.firebaseModels.StudentModel;
import com.example.mostafaaboelnasr.attendancemanagment.tutor_dir.fragments.TutorHomeFragment;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class StudentActivity extends AppCompatActivity {


    public static SharedPreferences mySharedPreferences;
    public static String id, name, email, phone, pass, state;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student);
        mySharedPreferences = getSharedPreferences("MY_SHARED_Preferences", MODE_PRIVATE);
        id = mySharedPreferences.getString("id", "");
        name = mySharedPreferences.getString("name", "");
        email = mySharedPreferences.getString("email", "");
        phone = mySharedPreferences.getString("phone", "");
        pass = mySharedPreferences.getString("password", "");


    }

    private void testFirst() {
        FirebaseDatabase.getInstance().getReference()
                .child("University")
                .child("Faculty")
                .child("Students")
                .child(id)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        try {
                            if (dataSnapshot.getValue().toString().equals("1")) {
                                StudentHomeFragment newFragment = new StudentHomeFragment();
                                Bundle args = new Bundle();
                                newFragment.setArguments(args);
                                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                                transaction.replace(R.id.viewpagerStudent, newFragment);
                                transaction.addToBackStack(null);
                                transaction.commit();
                            }else if (dataSnapshot.getValue().toString().equals("0")) {
                                Intent intent = new Intent(StudentActivity.this, RecognizeActivity.class);
                                startActivity(intent);
                            }
                        } catch (com.google.firebase.database.DatabaseException e) {
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }
                });
    }
}
