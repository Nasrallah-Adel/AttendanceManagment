package com.example.mostafaaboelnasr.attendancemanagment.admin_dir;

import android.content.SharedPreferences;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.widget.TextView;

import com.example.mostafaaboelnasr.attendancemanagment.AdminContainerFragment;
import com.example.mostafaaboelnasr.attendancemanagment.NavFragment;
import com.example.mostafaaboelnasr.attendancemanagment.R;
import com.example.mostafaaboelnasr.attendancemanagment.admin_dir.fragments.AdminHomeFragment;
import com.example.mostafaaboelnasr.attendancemanagment.utils.CustomViewPager;

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends AppCompatActivity {
    public static SharedPreferences mySharedPreferences;
    public static String id, name, email, phone, pass, state;

    private TabLayout tabLayout;
    public static CustomViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        mySharedPreferences = getSharedPreferences("MY_SHARED_Preferences", MODE_PRIVATE);
//        AdminHomeFragment newFragment = new AdminHomeFragment();
//        Bundle args = new Bundle();
//        newFragment.setArguments(args);
//        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
//        transaction.replace(R.id.viewpagerHome, newFragment);
//        transaction.addToBackStack(null);
//        transaction.commit();


        viewPager = (CustomViewPager) findViewById(R.id.viewpagerHome);
        viewPager.setPagingEnabled(true);
        setupViewPager(viewPager);

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);

        setupTabIcons();
    }

    private void setupTabIcons() {

        tabLayout.setSelectedTabIndicatorColor(getResources().getColor(R.color.darkGreen));
        tabLayout.animate();
        tabLayout.setFocusableInTouchMode(false);
        TextView Posts = (TextView) LayoutInflater.from(this).inflate(R.layout.custom_tab, null);
//        Posts.setText("Posts");
        Posts.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_public_black_24dp, 0, 0);
        tabLayout.getTabAt(0).setCustomView(Posts);

        TextView Navigation = (TextView) LayoutInflater.from(this).inflate(R.layout.custom_tab, null);
//        Navigation.setText("Posts");
        Navigation.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_menu_black_24dp, 0, 0);
        tabLayout.getTabAt(1).setCustomView(Navigation);
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new AdminContainerFragment(), "Admin");
        adapter.addFragment(new NavFragment(), "Navigation");
        viewPager.setAdapter(adapter);
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }

}
