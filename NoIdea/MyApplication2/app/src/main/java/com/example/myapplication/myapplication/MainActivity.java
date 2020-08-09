package com.example.myapplication.myapplication;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import com.example.myapplication.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity

{

    private Toolbar toolbar;
    private ViewPager viewPager;
    private TabLayout tabLayout;
    private TabsAccesorAdopter tabsAccesorAdopter;
    private FirebaseAuth mAuth;
    private DatabaseReference Rootref;

    private String currentUserId;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mAuth = FirebaseAuth.getInstance();
        Rootref = FirebaseDatabase.getInstance().getReference();

        currentUserId = mAuth.getCurrentUser().getUid();


        toolbar =(Toolbar) findViewById(R.id.main_page_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("No Idea");
        viewPager = (ViewPager) findViewById(R.id.main_tabs_pager);
        tabsAccesorAdopter = new TabsAccesorAdopter(getSupportFragmentManager());
        viewPager.setAdapter(tabsAccesorAdopter);


        tabLayout = (TabLayout) findViewById(R.id.main_tabs);
        tabLayout.setupWithViewPager(viewPager);



    }

    @Override
    protected void onStart()
    {


        super.onStart();

        FirebaseUser currentuser = mAuth.getCurrentUser();


        if (currentuser == null )

        {
            SendUserToLoginActivity();




        }
        else {

            UpdateUserStatus("online");

            VerifyUserExistance();
        }
    }


    @Override
    protected void onStop()
    {
        super.onStop();
        FirebaseUser currentuser = mAuth.getCurrentUser();


        if (currentuser != null ) {

            UpdateUserStatus("offline");
        }

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        FirebaseUser currentuser = mAuth.getCurrentUser();


        if (currentuser != null ) {

            UpdateUserStatus("offline");
        }

    }

    private void VerifyUserExistance()
    {
        String CurrentUserId = mAuth.getCurrentUser().getUid();
        Rootref.child("Users").child(CurrentUserId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {

                if((dataSnapshot.child("name").exists()))

                {

                }
                else
                {
                   SendUerToSettingsActivity();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu)

    {
        menu.add("Notifications").setIcon(R.drawable.notification).setShowAsAction((MenuItem.SHOW_AS_ACTION_ALWAYS));

      super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.options_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item)
    {
         super.onOptionsItemSelected(item);

        switch (item.getTitle().toString()) {
            case "Notifications":
                SendUerToReqsActivity();
        }

         if (item.getItemId() == R.id.logout_op)
         {

             AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this, R.style.AlertDialog);
             builder.setTitle("Are you Sure want to LogOut");
             builder.setPositiveButton("LogOut", new DialogInterface.OnClickListener() {
                 @Override
                 public void onClick(DialogInterface dialog, int which)
                 {
                     UpdateUserStatus("offline");

                     mAuth.signOut();
                     SendUserToLoginActivity();

                 }
             });


             builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                 @Override
                 public void onClick(DialogInterface dialog, int which)
                 {
                     dialog.cancel();

                 }
             });
             builder.show();








         }
        if (item.getItemId() == R.id.settings_op)
        {
            SendUerToSettingsActivity();




        }

        if (item.getItemId() == R.id.main_search_frnds_op)
        {

            SendUerToFindFriendsActivity();




        }

        if (item.getItemId() == R.id.main_createGroip_op)
        {
            CreatingNewGroup();


        }

        return true;
    }


    private void CreatingNewGroup()
    {

        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this, R.style.AlertDialog);
        builder.setTitle("Type your Group Name");
        final EditText groupName = new  EditText(MainActivity.this);
        groupName.setHint("      e.g  Friends Forever");
        builder.setView(groupName);
        builder.setPositiveButton("Create", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {

                String groupname = groupName.getText().toString();
                if(TextUtils.isEmpty(groupname))
                {
                    Toast.makeText(MainActivity.this, "Group required", Toast.LENGTH_SHORT).show();
                    groupName.setError("Type a Name");

                }
                else
                {
                    CreateNewGroup(groupname);

                }
            }
        });


        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                dialog.cancel();


            }
        });

        builder.show();





    }

    private void CreateNewGroup(final String groupname)
    {

        Rootref.child("Groups").child(groupname).setValue("")
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task)
                    {
                        if(task.isSuccessful())
                        {
                            Toast.makeText(MainActivity.this, groupname+"Created", Toast.LENGTH_SHORT).show();
                        }


                    }
                });
    }

    private void SendUserToLoginActivity() {

        Intent loginIntent = new Intent(MainActivity.this, LoginActivity.class);
        loginIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(loginIntent);
        finish();
    }

        private void SendUerToSettingsActivity()

        {

            Intent settingsIntent = new Intent(MainActivity.this,SettingsActivity.class);
            settingsIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(settingsIntent);


        }



    private void SendUerToFindFriendsActivity()
    {
        Intent findFriendsIntent = new Intent(MainActivity.this, FindFirendsAcitvity.class);
        startActivity(findFriendsIntent);
    }

    private void SendUerToReqsActivity()
    {
        Intent ReqIntent = new Intent(MainActivity.this, Req_Activity.class);
        startActivity(ReqIntent);

    }


    private void  UpdateUserStatus(String state)
    {
        String SaveCurrentTime , SaveCurrentDate;

        Calendar calendar = Calendar.getInstance();

        SimpleDateFormat currentDate = new SimpleDateFormat("dd MM,yyy");
        SaveCurrentDate = currentDate.format(calendar.getTime());

        SimpleDateFormat currentTime = new SimpleDateFormat("hh:mm a");
        SaveCurrentTime = currentTime.format(calendar.getTime());

        HashMap<String,Object>  OnlineState = new HashMap<>();

        OnlineState.put("time",SaveCurrentTime);
        OnlineState.put("date",SaveCurrentDate);
        OnlineState.put("state",state);





        Rootref.child("Users").child(currentUserId).child("userState")
                .updateChildren(OnlineState);



    }






}

