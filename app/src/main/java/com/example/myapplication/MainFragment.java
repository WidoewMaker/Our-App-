package com.example.myapplication;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;


public class MainFragment extends Fragment
{
    private ViewPager myViewPager;
    private TabLayout myTabLayout;
    private TabsAccesorAdopter myTabsAccessorAdapter;
    FragmentActivity activityContext;


    @Override
    public void onAttach(@NonNull Activity activity)
    { activityContext = (FragmentActivity) activity;

        super.onAttach(activity);
    }

    public MainFragment()
    {

    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        View v = inflater.inflate(R.layout.fragment_main, container, false);

        myViewPager = (ViewPager) v.findViewById(R.id.main_tabs_pager);
        myTabsAccessorAdapter = new TabsAccesorAdopter(activityContext.getSupportFragmentManager());
        myViewPager.setAdapter(myTabsAccessorAdapter);


        myTabLayout = (TabLayout) v.findViewById(R.id.main_tabs);
        myTabLayout.setupWithViewPager(myViewPager);
        return v;


    }


}
