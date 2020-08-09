package com.example.myapplication.myapplication;


import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.myapplication.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;


/**
 * A simple {@link Fragment} subclass.
 */
public class Groups_Fragment extends Fragment

{
    private View groupsfragmentView;
    private ListView listView;
    private ArrayAdapter<String> arrayAdapter;
    private ArrayList<String> list_of_groups = new ArrayList<>();

    private DatabaseReference Groupreff;


    public Groups_Fragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)


    {
        groupsfragmentView = inflater.inflate(R.layout.fragment_groups_, container, false);

        Groupreff = FirebaseDatabase.getInstance().getReference().child("Groups");
        IntializeFields();



        RetvNDispGrps();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)

            {
                String currentGroupName = parent.getItemAtPosition(position).toString();
                Intent groupChatIntent = new Intent(getContext(),GroupChatsActivity.class);
                groupChatIntent.putExtra("GrpName",currentGroupName);
                startActivity(groupChatIntent);


            }
        });






    return groupsfragmentView;

    }




    private void IntializeFields()
    {

        listView = (ListView) groupsfragmentView.findViewById(R.id.list_layout);
        arrayAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_expandable_list_item_1,list_of_groups);
        listView.setAdapter(arrayAdapter);
    }

    private void RetvNDispGrps()
    {
        Groupreff.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                Set<String> set = new HashSet<>();




                Iterator iterator = dataSnapshot.getChildren().iterator();
                while (iterator.hasNext())
                {
                    set.add(((DataSnapshot)iterator.next()).getKey());

                }




                list_of_groups.clear();
                list_of_groups.addAll(set);
                arrayAdapter.notifyDataSetChanged();



            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }




}
