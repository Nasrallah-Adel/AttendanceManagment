package com.example.mostafaaboelnasr.attendancemanagment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.LightingColorFilter;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mostafaaboelnasr.attendancemanagment.admin_dir.HomeActivity;
import com.example.mostafaaboelnasr.attendancemanagment.admin_dir.models.firebaseModels.AdminModel;
import com.example.mostafaaboelnasr.attendancemanagment.admin_dir.models.firebaseModels.StudentModel;
import com.example.mostafaaboelnasr.attendancemanagment.admin_dir.models.firebaseModels.TutorModel;
import com.example.mostafaaboelnasr.attendancemanagment.student.StudentActivity;
import com.example.mostafaaboelnasr.attendancemanagment.tutor_dir.TutorActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SignInFragment extends Fragment {

    Context context;
    View view;
    private EditText username, password;
    private RelativeLayout loginButton;
    private CoordinatorLayout coordinatorLayout;
    private ProgressBar spinner;
    private ImageView login_ok, login_not_ok;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_sign_in, container, false);
        context = getContext();
//        Toast.makeText(context, "SignInFragment", Toast.LENGTH_SHORT).show();
        spinner = (ProgressBar) view.findViewById(R.id.progressBar);
        spinner.getIndeterminateDrawable().setColorFilter(
                new LightingColorFilter(0xFF000000, Color.WHITE));
        login_ok = (ImageView) view.findViewById(R.id.login_ok);
        login_not_ok = (ImageView) view.findViewById(R.id.login_not_ok);
        coordinatorLayout = (CoordinatorLayout) view.findViewById(R.id.coordinator_layout);
        username = (EditText) view.findViewById(R.id.username);
        password = (EditText) view.findViewById(R.id.password);
        loginButton = (RelativeLayout) view.findViewById(R.id.loginButton);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                // Check if any input is empty
//                login_not_ok.setVisibility(View.GONE);
//                login_ok.setVisibility(View.GONE);
//                boolean shouldReturn = setBorderIfEmpty(username);
//                shouldReturn = setBorderIfEmpty(password) || shouldReturn;
//                // Return if some input is empty
//                if (shouldReturn)
//                    return;
//
//                setEnabled(false);

                get_Multi_Data();
            }
        });

        username.addTextChangedListener(new GenericTextWatcher(username));
        password.addTextChangedListener(new GenericTextWatcher(password));

        EditText.OnEditorActionListener editorActionListener = new EditText.OnEditorActionListener() {

            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (v.getId() == R.id.password)
                    loginButton.performClick();
                return false;
            }
        };
        password.setOnEditorActionListener(editorActionListener);

//        view.findViewById(R.id.registerTextView).setOnClickListener(
//                new View.OnClickListener() {
//
//                    @Override
//                    public void onClick(View v) {
//                        RegisterFragment newFragment = new RegisterFragment();
//                        Bundle args = new Bundle();
//                        newFragment.setArguments(args);
//                        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
//                        transaction.replace(R.id.viewpagerMain, newFragment);
//                        transaction.addToBackStack(null);
//
//                        transaction.commit();
//                    }
//                }
//        );

        setEnabled(true);
        return view;
    }

    /**
     * Enables or disables login button clicks and shows or hides a loading spinner.
     *
     * @param enabled enable or disable
     */
    public void setEnabled(boolean enabled) {
        if (enabled) {
            loginButton.setEnabled(true);
            loginButton.setBackgroundResource(R.drawable.button);
            spinner.setVisibility(View.INVISIBLE);
        } else {
            loginButton.setEnabled(false);
            loginButton.setBackgroundResource(R.drawable.button_pressed);
            spinner.setVisibility(View.VISIBLE);
        }
    }

    /**
     * Sets the border color of an edittext to red if it is empty.
     *
     * @param editText the edittext to be checked
     * @return true if the edittext is empty
     */
    public boolean setBorderIfEmpty(EditText editText) {
        if (editText.getText().toString().equals("")) {
            ViewGroup parent;
            parent = (ViewGroup) editText.getParent();
            editText.setBackgroundResource(R.drawable.edittext_red_border);
            ((ImageView) parent.getChildAt(parent.indexOfChild(editText) + 1)).setColorFilter(Color.parseColor("#F44336"));
            return true;
        }
        return false;
    }

    public boolean setBorder(EditText editText) {
        ViewGroup parent;
        parent = (ViewGroup) editText.getParent();
        editText.setBackgroundResource(R.drawable.edittext_red_border);
        ((ImageView) parent.getChildAt(parent.indexOfChild(editText) + 1)).setColorFilter(Color.parseColor("#F44336"));
        return true;
    }

    /**
     * Custom TextWatcher for resetting edittext borde
     */
    public static class GenericTextWatcher implements TextWatcher {

        public EditText editText;

        public GenericTextWatcher(EditText editText) {
            this.editText = editText;
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void afterTextChanged(Editable editable) {
            editText.setBackgroundResource(R.drawable.rounded_corners);
            ViewGroup parent = (ViewGroup) editText.getParent();
            ((ImageView) parent.getChildAt(parent.indexOfChild(editText) + 1)).setColorFilter(null);
        }
    }

    DatabaseReference reference2;

    private void get_Multi_Data() {

        final String email_ = username.getText().toString().trim();
        final String password_ = password.getText().toString().trim();

//        reference2 = FirebaseDatabase.getInstance().getReference()
//                .child("University")
//                .child("Faculty")
//                .child("Admins").push();
//        reference2.setValue(new AdminModel(reference2.getKey(), "7", "7", "7", "7"));

        if (!adminLogin(email_, password_)) {
            if (!tutorLogin(email_, password_)) {
                if (!studentLogin(email_, password_)) {
                    Toast.makeText(context, "Not", Toast.LENGTH_SHORT).show();
                }
            }
        }
//        tutorLogin(email_, password_);


    }

    public boolean tutorLogin(final String email_, final String password_) {
        Toast.makeText(context, "Tutor", Toast.LENGTH_SHORT).show();
        FirebaseDatabase.getInstance().getReference()
                .child("University")
                .child("Faculty")
                .child("Tutors")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            try {
                                TutorModel tutorModel = snapshot.getValue(TutorModel.class);
                                if (tutorModel.getEmail().equals(email_) && tutorModel.getPass().equals(password_)) {
                                    final Intent intent = new Intent(context, TutorActivity.class);
                                    SharedPreferences.Editor editor = context.getSharedPreferences("MY_SHARED_Preferences", context.MODE_PRIVATE).edit();
                                    editor.putString("id", tutorModel.getId());
                                    editor.putString("name", tutorModel.getName());
                                    editor.putString("email", tutorModel.getEmail());
                                    editor.putString("pass", tutorModel.getPass());
                                    editor.putString("state", "Tutors");
                                    editor.commit();

                                    intent.putExtra("email", tutorModel.getEmail());
                                    intent.putExtra("id", tutorModel.getId());
                                    startActivity(intent);
                                    getActivity().finish();
                                }
                            } catch (com.google.firebase.database.DatabaseException e) {

                            }

                            login_not_ok.setVisibility(View.VISIBLE);
                            login_ok.setVisibility(View.GONE);
                            setBorder(password);
                            setBorder(username);
                            setEnabled(true);
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
        return false;
    }

    public boolean adminLogin(final String email_, final String password_) {
        FirebaseDatabase.getInstance().getReference()
                .child("University")
                .child("Faculty")
                .child("Admins")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            try {
                                AdminModel tutorModel = snapshot.getValue(AdminModel.class);
                                Toast.makeText(context, tutorModel.getName(), Toast.LENGTH_SHORT).show();
                                if (tutorModel.getEmail().equals(email_) && tutorModel.getPass().equals(password_)) {
                                    Intent intent = new Intent(context, HomeActivity.class);
                                    SharedPreferences.Editor editor = context.getSharedPreferences("MY_SHARED_Preferences", context.MODE_PRIVATE).edit();
                                    editor.putString("id", tutorModel.getId());
                                    editor.putString("name", tutorModel.getName());
                                    editor.putString("email", tutorModel.getEmail());
                                    editor.putString("phone", tutorModel.getPhone());
                                    editor.putString("pass", tutorModel.getPass());
                                    editor.putString("state", "Admins");
                                    editor.commit();

                                    intent.putExtra("email", tutorModel.getEmail());
                                    intent.putExtra("id", tutorModel.getId());
                                    startActivity(intent);
                                    getActivity().finish();

                                }
                            } catch (com.google.firebase.database.DatabaseException e) {

                            }

                            login_not_ok.setVisibility(View.VISIBLE);
                            login_ok.setVisibility(View.GONE);
                            setBorder(password);
                            setBorder(username);
                            setEnabled(true);
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
        return false;
    }

    public boolean studentLogin(final String email_, final String password_) {
        FirebaseDatabase.getInstance().getReference()
                .child("University")
                .child("Faculty")
                .child("Students")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            try {
                                StudentModel tutorModel = snapshot.getValue(StudentModel.class);
                                if (tutorModel.getEmail().equals(email_) && tutorModel.getPass().equals(password_)) {
                                    final Intent intent = new Intent(context, StudentActivity.class);
                                    SharedPreferences.Editor editor = context.getSharedPreferences("MY_SHARED_Preferences", context.MODE_PRIVATE).edit();
                                    editor.putString("id", tutorModel.getId());
                                    editor.putString("name", tutorModel.getName());
                                    editor.putString("email", tutorModel.getEmail());
                                    editor.putString("phone", tutorModel.getPhone());
                                    editor.putString("pass", tutorModel.getPass());
                                    editor.putString("state", "Students");
                                    editor.commit();

                                    intent.putExtra("email", tutorModel.getEmail());
                                    intent.putExtra("id", tutorModel.getId());
                                    startActivity(intent);
                                    getActivity().finish();
                                }
                            } catch (com.google.firebase.database.DatabaseException e) {

                            }

                            login_not_ok.setVisibility(View.VISIBLE);
                            login_ok.setVisibility(View.GONE);
                            setBorder(password);
                            setBorder(username);
                            setEnabled(true);
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
        return false;
    }
}