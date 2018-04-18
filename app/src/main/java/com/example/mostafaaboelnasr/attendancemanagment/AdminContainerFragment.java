package com.example.mostafaaboelnasr.attendancemanagment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.mostafaaboelnasr.attendancemanagment.admin_dir.fragments.AdminHomeFragment;


public class AdminContainerFragment extends Fragment {
    View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_admin_container, container, false);
        Toast.makeText(getContext(), "AdminContainerFragment", Toast.LENGTH_SHORT).show();

        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        AdminHomeFragment fragment = new AdminHomeFragment();
        fragmentTransaction.add(R.id.viewpager_adminContainer, fragment);
        fragmentTransaction.addToBackStack("home");
        fragmentTransaction.commit();
        return view;
    }

}
