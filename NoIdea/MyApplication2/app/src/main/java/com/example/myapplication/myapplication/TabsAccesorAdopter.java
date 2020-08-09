package com.example.myapplication.myapplication;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class TabsAccesorAdopter extends FragmentPagerAdapter {
    public TabsAccesorAdopter(@NonNull FragmentManager fm) {
        super(fm);
    }

    @NonNull
    @Override
    public Fragment getItem(int i)

    {

        switch (i)
        {
            case 0:
                Chats_Fragment chats_fragment = new Chats_Fragment();
                return chats_fragment;

            case  1:

            Groups_Fragment groups_fragment = new Groups_Fragment();
            return groups_fragment;

            case 2:
            Contacts_Fragment contacts_fragment = new Contacts_Fragment();
            return contacts_fragment;


            default:

                return null;





        }

    }

    @Override
    public int getCount()
    {
        return 3;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {

        switch (position)
        {
            case 0:

                return "Chats";

            case  1:


                return "Grouops";

            case 2:

                return "Contacts";


            default:

                return null;





        }
    }
}
