package com.example.myapplication;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;
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
import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private DatabaseReference Rootref;
    private String currentUserId;

    BottomNavigationView bottomNavigationView;
    Fragment selectedFragment = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mAuth = FirebaseAuth.getInstance();
        Rootref = FirebaseDatabase.getInstance().getReference();


        Toolbar toolbar =  findViewById(R.id.main_page_toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setTitle("No Idea");


        bottomNavigationView = findViewById(R.id.main_act_Bott_Nav);
        bottomNavigationView.setOnNavigationItemSelectedListener(navigationItemSelectedListener);


    }

    private BottomNavigationView.OnNavigationItemSelectedListener navigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
            switch (menuItem.getItemId()) {
                case R.id.Nav_home:

                    selectedFragment = new MainFragment();
                    //Toast.makeText(MainActivity.this, "hame", Toast.LENGTH_SHORT).show();

                    break;


                case R.id.Nav_Add_post:

                    selectedFragment = new AddPost_Fragment();
                    // Toast.makeText(MainActivity.this, "Addpost", Toast.LENGTH_SHORT).show();

                    break;

                case R.id.Nav_Add_post1:

                    selectedFragment = new AddReels_Fragment();
                    //Toast.makeText(MainActivity.this, "Addpost1", Toast.LENGTH_SHORT).show();


                    break;


                case R.id.Nav_Search:

                    selectedFragment = new Search_Fragment();
                    //Toast.makeText(MainActivity.this, "Search", Toast.LENGTH_SHORT).show();
//

                    break;

                case R.id.Nav_settings:

                    // selectedFragment = new AddPost_Fragment();
                    //Toast.makeText(MainActivity.this, "Settings", Toast.LENGTH_SHORT).show();
//


                    break;

            }

            if (selectedFragment != null) {
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        selectedFragment).commit();

            }


            return true;
        }
    };

    @Override
    protected void onStart() {


        super.onStart();


        FirebaseUser currentuser = mAuth.getCurrentUser();


        if (currentuser == null) {
            SendUserToLoginActivity();


        } else {

            currentUserId = mAuth.getCurrentUser().getUid();
            selectedFragment = new MainFragment();
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                    selectedFragment).commit();

            UpdateUserStatus("online");
            VerifyUserExistance();
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        FirebaseUser currentuser = mAuth.getCurrentUser();


        if (currentuser != null) {

            UpdateUserStatus("offline");
        }

    }

    private void VerifyUserExistance() {
        String CurrentUserId = Objects.requireNonNull(mAuth.getCurrentUser()).getUid();
        Rootref.child("Users").child(CurrentUserId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (!(dataSnapshot.child("name").exists())) {

                    sendUserToActivity(SettingsActivity.class);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.options_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        super.onOptionsItemSelected(item);

        switch (item.getItemId()) {
            case R.id.main_notification:

                    startActivity(new Intent(this, Req_Activity.class));



                break;

            case R.id.logout_op:

                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this, R.style.AlertDialog);
                builder.setTitle("Are you Sure want to LogOut");
                builder.setPositiveButton("LogOut", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        UpdateUserStatus("offline");

                        mAuth.signOut();
                        SendUserToLoginActivity();

                    }
                });


                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();

                    }
                });
                builder.show();
                break;

            case R.id.settings_op:
                sendUserToActivity(SettingsActivity.class);
                break;


            case R.id.main_search_frnds_op:
                sendUserToActivity(FindFirendsAcitvity.class);
                break;


            case R.id.main_createGroip_op:
                //CreatingNewGroup();
        }


        return true;
    }


//    private void CreatingNewGroup() {
//
//        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this, R.style.AlertDialog);
//        builder.setTitle("Type your Group Name");
//        final EditText groupName = new EditText(MainActivity.this);
//        groupName.setHint("      e.g  Friends Forever");
//        builder.setView(groupName);
//        builder.setPositiveButton("Create", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//
//                String groupname = groupName.getText().toString();
//                if (TextUtils.isEmpty(groupname)) {
//                    Toast.makeText(MainActivity.this, "Group required", Toast.LENGTH_SHORT).show();
//                    groupName.setError("Type a Name");
//
//                } else {
//                    CreateNewGroup(groupname);
//
//                }
//            }
//        });
//
//
//        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                dialog.cancel();
//
//
//            }
//        });
//
//        builder.show();
//
//
//    }

   /* private void CreateNewGroup(final String groupname) {

        Rootref.child("Groups").child(groupname).setValue("")
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(MainActivity.this, groupname + "Created", Toast.LENGTH_SHORT).show();
                        }


                    }
                });
    }*/

    private void SendUserToLoginActivity() {

        Intent loginIntent = new Intent(MainActivity.this, LoginActivity.class);
        loginIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(loginIntent);
        finish();
    }

    private void sendUserToActivity(Class<?> activity) {
        Intent activityIntent = new Intent(this, activity);
        startActivity(activityIntent);
    }

    private void UpdateUserStatus(String state) {
        String SaveCurrentTime, SaveCurrentDate;

        Calendar calendar = Calendar.getInstance();

        @SuppressLint("SimpleDateFormat") SimpleDateFormat currentDate = new SimpleDateFormat("dd MM,yyy");
        SaveCurrentDate = currentDate.format(calendar.getTime());

        @SuppressLint("SimpleDateFormat") SimpleDateFormat currentTime = new SimpleDateFormat("hh:mm a");
        SaveCurrentTime = currentTime.format(calendar.getTime());

        HashMap<String, Object> OnlineState = new HashMap<>();

        OnlineState.put("time", SaveCurrentTime);
        OnlineState.put("date", SaveCurrentDate);
        OnlineState.put("state", state);


        Rootref.child("Users").child(currentUserId).child("userState")
                .updateChildren(OnlineState);


    }


}