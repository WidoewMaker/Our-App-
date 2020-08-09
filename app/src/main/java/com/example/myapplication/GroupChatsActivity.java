package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.icu.text.Edits;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;

public class GroupChatsActivity extends AppCompatActivity
{

    private Toolbar mtoolbar;
    private ImageButton senddingbutton;
    private EditText msgEditText;
    private TextView name,time,msg;
    private String currentGroupName ,currentUserName,currentUserId,currentd,currentt;
    private FirebaseAuth mauth ;
    private DatabaseReference UaserRef,Rootnameref,Grpmsgkeyref,datref;
    private RecyclerView recyclerView;





        @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_chats);


        currentGroupName = getIntent().getExtras().get("GrpName").toString();

        mauth = FirebaseAuth.getInstance();
        currentUserId = mauth.getCurrentUser().getUid();
        UaserRef = FirebaseDatabase.getInstance().getReference().child("Users");
        Rootnameref = FirebaseDatabase.getInstance().getReference().child("Groups").child(currentGroupName);


            Initializefields();












            GetUserInformation();

        senddingbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                sendinfoTODB();
                msgEditText.setText("");




            }
        });

    }





    @Override
    protected void onStart() {
        super.onStart();

        Rootnameref.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s)
            {
                if (dataSnapshot.exists())
                {
                    Displaymessages(dataSnapshot);

                }

            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s)
            {
                if (dataSnapshot.exists())
                {
                    Displaymessages(dataSnapshot);

                }


            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }



    private void Initializefields() {
        mtoolbar = (Toolbar) findViewById(R.id.group_titlie_bar);
        setSupportActionBar(mtoolbar);
        getSupportActionBar().setTitle(currentGroupName);
        senddingbutton = (ImageButton) findViewById(R.id.sendButton);
        msgEditText = (EditText) findViewById(R.id.msginp_group);
        name = (TextView) findViewById(R.id.Name);









    }


    private void GetUserInformation()

    {
        UaserRef.child(currentUserId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                if(dataSnapshot.exists())
                {
                    currentUserName =dataSnapshot.child("name").getValue().toString();
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });




    }



    private void sendinfoTODB()
    {
        String messages = msgEditText.getText().toString();
        String msgkey = Rootnameref.push().getKey();

        if (TextUtils.isEmpty(messages)) {
            msgEditText.setError("Empty");
        }


        else
        {
            Calendar calfordate = Calendar.getInstance();
            SimpleDateFormat currentDateFormate = new SimpleDateFormat("MM/dd>yyyy");
            currentd = currentDateFormate.format(calfordate.getTime());

            Calendar calfortime = Calendar.getInstance();
            SimpleDateFormat currentTimeFormate = new SimpleDateFormat("hh:mm (a)");
            currentt = currentTimeFormate.format(calfortime.getTime());


            HashMap<String,Object> grpmsgKey = new HashMap<>();

            Rootnameref.updateChildren(grpmsgKey);


            Grpmsgkeyref = Rootnameref.child(msgkey);
            HashMap<String,Object> msginfomap = new HashMap<>();
            msginfomap.put("name",currentUserName);
            msginfomap.put("message",messages);

            msginfomap.put("date",currentd);

            msginfomap.put("time",currentt);
            Grpmsgkeyref.updateChildren(msginfomap);



        }
    }

    private void Displaymessages(DataSnapshot dataSnapshot)
    {
        Iterator iterator = dataSnapshot.getChildren().iterator();

        while (iterator.hasNext())
        {
            String chatdate = (String) ((DataSnapshot)iterator.next()).getValue();
            String chatmsg= (String) ((DataSnapshot)iterator.next()).getValue();
            String chatnm= (String) ((DataSnapshot)iterator.next()).getValue();
            String chattime = (String) ((DataSnapshot)iterator.next()).getValue();
            name.append("\t"+chatnm +"\t\t"+chattime+"\n\n"+"\t"+chatmsg+"\n____________________________________________________________"+"\n\n\n");




        }

    }




}
