package com.example.myapplication;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileActivity extends AppCompatActivity
{
    private String receiverUserID, senderUserID, Current_State;

    private Toolbar mtoolbar;


    private CircleImageView ShowUsProImg;
    private TextView ShowUsProNm, ShowUsProSt;
    private Button FrndReqBt, DclnReqBt;

    private DatabaseReference UserRef, ChatRequestRef, ContactsRef, NotificationRef;
    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        mtoolbar = (Toolbar) findViewById(R.id.UserPro_titlie_bar);
        setSupportActionBar(mtoolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);


        mAuth = FirebaseAuth.getInstance();
        UserRef = FirebaseDatabase.getInstance().getReference().child("Users");
        ChatRequestRef = FirebaseDatabase.getInstance().getReference().child("Chat Requests");
        ContactsRef = FirebaseDatabase.getInstance().getReference().child("Contacts");
        NotificationRef = FirebaseDatabase.getInstance().getReference().child("Notifications");


        receiverUserID = getIntent().getExtras().get("visit_user_id").toString();
        senderUserID = mAuth.getCurrentUser().getUid();


        ShowUsProImg = (CircleImageView) findViewById(R.id.ShowProfile_image);
        ShowUsProNm = (TextView) findViewById(R.id.ShowUsNm);
        ShowUsProSt = (TextView) findViewById(R.id.ShowStNm);
        FrndReqBt = (Button) findViewById(R.id.FrndReqBt);
        DclnReqBt = (Button) findViewById(R.id.FrndReqDclnBt);
        Current_State = "new";



        RetriveUserInfo();


    }

    private void RetriveUserInfo()
    {
        UserRef.child(receiverUserID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot)
            {
                if ((dataSnapshot.exists())  &&  (dataSnapshot.hasChild("image")))
                {
                    String userImage = dataSnapshot.child("image").getValue().toString();
                    String userName = dataSnapshot.child("name").getValue().toString();
                    String userstatus = dataSnapshot.child("status").getValue().toString();

                    Picasso.get().load(userImage).placeholder(R.drawable.profilemiga).into(ShowUsProImg);
                    ShowUsProNm.setText(userName);
                    ShowUsProSt.setText(userstatus);


                    ChatRequest();


                }
                else
                {
                    String userName = dataSnapshot.child("name").getValue().toString();
                    String userstatus = dataSnapshot.child("status").getValue().toString();

                    ShowUsProNm.setText(userName);
                    ShowUsProSt.setText(userstatus);

                    ChatRequest();



                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }

    private void ChatRequest()
    {
        ChatRequestRef.child(senderUserID)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot)
                    {
                        if (dataSnapshot.hasChild(receiverUserID))
                        {
                            String request_type = dataSnapshot.child(receiverUserID).child("request_type").getValue().toString();

                            if (request_type.equals("sent"))
                            {
                                Current_State = "request_sent";
                                FrndReqBt.setText("Cancel Friend Request");
                            }
                            else if (request_type.equals("received"))
                            {
                                Current_State = "request_received";
                                FrndReqBt.setText("Accept Request");

                                DclnReqBt.setVisibility(View.VISIBLE);
                                DclnReqBt.setEnabled(true);

                                DclnReqBt.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view)
                                    {
                                        CancelChatRequest();
                                    }
                                });
                            }
                        }
                        else
                        {
                            ContactsRef.child(senderUserID)
                                    .addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(DataSnapshot dataSnapshot)
                                        {
                                            if (dataSnapshot.hasChild(receiverUserID))
                                            {
                                                Current_State = "friends";
                                                FrndReqBt.setText("UnFriend");
                                            }
                                        }

                                        @Override
                                        public void onCancelled(DatabaseError databaseError) {

                                        }
                                    });
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });



        if (!senderUserID.equals(receiverUserID))
        {
            FrndReqBt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view)
                {
                    FrndReqBt.setEnabled(false);

                    if (Current_State.equals("new"))
                    {
                        SendChatRequest();
                    }
                    if (Current_State.equals("request_sent"))
                    {
                        CancelChatRequest();
                    }
                    if (Current_State.equals("request_received"))
                    {
                        AcceptChatRequest();
                    }
                    if (Current_State.equals("friends"))
                    {
                        RemoveSpecificContact();
                    }
                }
            });
        }
        else
        {
            FrndReqBt.setVisibility(View.INVISIBLE);
        }
    }

    private void RemoveSpecificContact()

    {
        ContactsRef.child(senderUserID).child(receiverUserID)
                .removeValue()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task)
                    {
                        if (task.isSuccessful())
                        {
                            ContactsRef.child(receiverUserID).child(senderUserID)
                                    .removeValue()
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task)
                                        {
                                            if (task.isSuccessful())
                                            {
                                                FrndReqBt.setEnabled(true);
                                                Current_State = "new";
                                                FrndReqBt.setText("Send Message");

                                                DclnReqBt.setVisibility(View.GONE);
                                                DclnReqBt.setEnabled(false);
                                            }
                                        }
                                    });
                        }
                    }
                });

    }

    private void AcceptChatRequest()
    {


        ContactsRef.child(senderUserID).child(receiverUserID)
                .child("Contacts").setValue("Saved")
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task)
                    {
                        if (task.isSuccessful())
                        {
                            ContactsRef.child(receiverUserID).child(senderUserID)
                                    .child("Contacts").setValue("Saved")
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task)
                                        {
                                            if (task.isSuccessful())
                                            {
                                                ChatRequestRef.child(senderUserID).child(receiverUserID)
                                                        .removeValue()
                                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<Void> task)
                                                            {
                                                                if (task.isSuccessful())
                                                                {
                                                                    ChatRequestRef.child(receiverUserID).child(senderUserID)
                                                                            .removeValue()
                                                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                                @Override
                                                                                public void onComplete(@NonNull Task<Void> task)
                                                                                {
                                                                                    FrndReqBt.setEnabled(true);
                                                                                    Current_State = "friends";
                                                                                    FrndReqBt.setText("UnFriend");

                                                                                    DclnReqBt.setVisibility(View.GONE);
                                                                                    DclnReqBt.setEnabled(false);
                                                                                }
                                                                            });
                                                                }
                                                            }
                                                        });
                                            }
                                        }
                                    });
                        }
                    }
                });


    }

    private void SendChatRequest()
    {
        ChatRequestRef.child(senderUserID).child(receiverUserID)
                .child("request_type").setValue("sent")
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task)
                    {
                        if (task.isSuccessful())
                        {
                            ChatRequestRef.child(receiverUserID).child(senderUserID)
                                    .child("request_type").setValue("received")
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task)
                                        {
                                            if (task.isSuccessful())
                                            {
                                                HashMap<String, String> chatNotificationMap = new HashMap<>();
                                                chatNotificationMap.put("from", senderUserID);
                                                chatNotificationMap.put("type", "request");

                                                NotificationRef.child(receiverUserID).push()
                                                        .setValue(chatNotificationMap)
                                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<Void> task)
                                                            {
                                                                if (task.isSuccessful())
                                                                {
                                                                    FrndReqBt.setEnabled(true);
                                                                    Current_State = "request_sent";
                                                                    FrndReqBt.setText("Cancel Friend Request");
                                                                }
                                                            }
                                                        });
                                            }
                                        }
                                    });
                        }
                    }
                });
    }

    private void CancelChatRequest()
    {
        ChatRequestRef.child(senderUserID).child(receiverUserID)
                .removeValue()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task)
                    {
                        if (task.isSuccessful())
                        {
                            ChatRequestRef.child(receiverUserID).child(senderUserID)
                                    .removeValue()
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task)
                                        {
                                            if (task.isSuccessful())
                                            {
                                                FrndReqBt.setEnabled(true);
                                                Current_State = "new";
                                                FrndReqBt.setText("Add friend");

                                                DclnReqBt.setVisibility(View.GONE);
                                                DclnReqBt.setEnabled(false);
                                            }
                                        }
                                    });
                        }
                    }
                });

    }


}

