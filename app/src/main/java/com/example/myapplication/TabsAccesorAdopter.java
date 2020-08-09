package com.example.myapplication;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class TabsAccesorAdopter extends FragmentPagerAdapter {
    public TabsAccesorAdopter(@NonNull FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int i) {

        switch (i) {
            case 0:
                Log.d("jagadesssh", "getItem: Chat");
                return new Chats_Fragment();

            case 1:
                Log.d("jagadesssh", "getItem: Group");

                Groups_Fragment groups_fragment = new Groups_Fragment();
                return groups_fragment;

            case 2:
                Log.d("jagadesssh", "getItem: Contact");

                Contacts_Fragment contacts_fragment = new Contacts_Fragment();
                return contacts_fragment;


        }

        return null;
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {

        switch (position) {
            case 0:

                return "Chats";

            case 1:


                return "Groups";

            case 2:

                return "Stories";


            default:

                return null;


        }
    }
}
