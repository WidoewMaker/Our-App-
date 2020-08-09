package com.example.myapplication.myapplication;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class Req_Activity extends AppCompatActivity {

    private RecyclerView myRequestsList;

    private Toolbar mToolbar;


    private DatabaseReference ChatRequestsRef, UsersRef, ContactsRef;
    private FirebaseAuth mAuth;
    private String currentUserID;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_req_);



        mAuth = FirebaseAuth.getInstance();
        currentUserID = mAuth.getCurrentUser().getUid();

        UsersRef = FirebaseDatabase.getInstance().getReference().child("Users");
        ChatRequestsRef = FirebaseDatabase.getInstance().getReference().child("Chat Requests");
        ContactsRef = FirebaseDatabase.getInstance().getReference().child("Contacts");


        myRequestsList = (RecyclerView) findViewById(R.id.Req_Rcy);
        myRequestsList.setLayoutManager(new LinearLayoutManager(this));
        mToolbar = (Toolbar) findViewById(R.id.req_titlie_bar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Notification");



    }



    @Override
    public void onStart()
    {
        super.onStart();

        FirebaseRecyclerOptions<Contacts> options =
                new FirebaseRecyclerOptions.Builder<Contacts>()
                        .setQuery(ChatRequestsRef.child(currentUserID), Contacts.class)
                        .build();


        FirebaseRecyclerAdapter<Contacts, RequestsViewHolder> adapter =
                new FirebaseRecyclerAdapter<Contacts, RequestsViewHolder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull final RequestsViewHolder holder, int position, @NonNull Contacts model)
                    {
                        holder.itemView.findViewById(R.id.AcRejBtnLinr).setVisibility(View.VISIBLE);



                        final String list_user_id = getRef(position).getKey();

                        DatabaseReference getTypeRef = getRef(position).child("request_type").getRef();

                        getTypeRef.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot)
                            {
                                if (dataSnapshot.exists())
                                {
                                    String type = dataSnapshot.getValue().toString();

                                    if (type.equals("received"))
                                    {
                                        UsersRef.child(list_user_id).addValueEventListener(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(DataSnapshot dataSnapshot)
                                            {
                                                if (dataSnapshot.hasChild("image"))
                                                {
                                                    final String requestProfileImage = dataSnapshot.child("image").getValue().toString();

                                                    Picasso.get().load(requestProfileImage).into(holder.profileImage);
                                                }

                                                final String requestUserName = dataSnapshot.child("name").getValue().toString();
                                                final String requestUserStatus = dataSnapshot.child("status").getValue().toString();

                                                holder.userName.setText(requestUserName);
                                                holder.userStatus.setText("wants to connect with you.");

                                                holder.RejectButton.setOnClickListener(new View.OnClickListener() {
                                                    @Override
                                                    public void onClick(View v)
                                                    {
                                                        ChatRequestsRef.child(currentUserID).child(list_user_id)
                                                                .removeValue()
                                                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                    @Override
                                                                    public void onComplete(@NonNull Task<Void> task)
                                                                    {
                                                                        if (task.isSuccessful())
                                                                        {
                                                                            ChatRequestsRef.child(list_user_id).child(currentUserID)
                                                                                    .removeValue()
                                                                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                                        @Override
                                                                                        public void onComplete(@NonNull Task<Void> task)
                                                                                        {
                                                                                            if (task.isSuccessful())
                                                                                            {
                                                                                                Toast.makeText(Req_Activity.this, "Contact Deleted", Toast.LENGTH_SHORT).show();
                                                                                            }
                                                                                        }
                                                                                    });
                                                                        }
                                                                    }
                                                                });

                                                    }
                                                });


                                                holder.AcceptButton.setOnClickListener(new View.OnClickListener() {
                                                    @Override
                                                    public void onClick(View view)
                                                    {


                                                        ContactsRef.child(currentUserID).child(list_user_id).child("Contact")
                                                                .setValue("Saved").addOnCompleteListener(new OnCompleteListener<Void>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<Void> task)
                                                            {
                                                                if (task.isSuccessful())
                                                                {
                                                                    ContactsRef.child(list_user_id).child(currentUserID).child("Contact")
                                                                            .setValue("Saved").addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                        @Override
                                                                        public void onComplete(@NonNull Task<Void> task)
                                                                        {
                                                                            if (task.isSuccessful())
                                                                            {
                                                                                ChatRequestsRef.child(currentUserID).child(list_user_id)
                                                                                        .removeValue()
                                                                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                                            @Override
                                                                                            public void onComplete(@NonNull Task<Void> task)
                                                                                            {
                                                                                                if (task.isSuccessful())
                                                                                                {
                                                                                                    ChatRequestsRef.child(list_user_id).child(currentUserID)
                                                                                                            .removeValue()
                                                                                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                                                                @Override
                                                                                                                public void onComplete(@NonNull Task<Void> task)
                                                                                                                {
                                                                                                                    if (task.isSuccessful())
                                                                                                                    {
                                                                                                                        Toast.makeText(Req_Activity.this, "New Contact Saved", Toast.LENGTH_SHORT).show();
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
                                                            }
                                                        });


                                                    }
                                                });

                                            }

                                            @Override
                                            public void onCancelled(DatabaseError databaseError) {

                                            }
                                        });
                                    }
                                    else if (type.equals("sent"))
                                    {
                                        Button request_sent_btn = holder.itemView.findViewById(R.id.AcceptBtn);
                                        request_sent_btn.setText("Req Sent");

                                        holder.itemView.findViewById(R.id.AcRejBtnLinr).setVisibility(View.VISIBLE);
                                        holder.itemView.findViewById(R.id.AcceptBtn).setVisibility(View.GONE);


                                        UsersRef.child(list_user_id).addValueEventListener(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(DataSnapshot dataSnapshot)
                                            {
                                                if (dataSnapshot.hasChild("image"))
                                                {
                                                    final String requestProfileImage = dataSnapshot.child("image").getValue().toString();

                                                    Picasso.get().load(requestProfileImage).into(holder.profileImage);
                                                }

                                                final String requestUserName = dataSnapshot.child("name").getValue().toString();
                                                final String requestUserStatus = dataSnapshot.child("status").getValue().toString();

                                                holder.userName.setText(requestUserName);
                                                holder.userStatus.setText("you have sent a request to " + requestUserName);


                                                holder.RejectButton.setOnClickListener(new View.OnClickListener() {
                                                    @Override
                                                    public void onClick(View view)
                                                    {
                                                        ChatRequestsRef.child(currentUserID).child(list_user_id)
                                                                .removeValue()
                                                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                    @Override
                                                                    public void onComplete(@NonNull Task<Void> task)
                                                                    {
                                                                        if (task.isSuccessful())
                                                                        {
                                                                            ChatRequestsRef.child(list_user_id).child(currentUserID)
                                                                                    .removeValue()
                                                                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                                        @Override
                                                                                        public void onComplete(@NonNull Task<Void> task)
                                                                                        {
                                                                                            if (task.isSuccessful())
                                                                                            {
                                                                                                Toast.makeText(Req_Activity.this, "you have cancelled the chat request.", Toast.LENGTH_SHORT).show();
                                                                                            }
                                                                                        }
                                                                                    });
                                                                        }
                                                                    }
                                                                });
                                                    }
                                                });

                                            }

                                            @Override
                                            public void onCancelled(DatabaseError databaseError) {

                                            }
                                        });
                                    }
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });
                    }

                    @NonNull
                    @Override
                    public RequestsViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i)
                    {
                        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.users_display_layout, viewGroup, false);
                        RequestsViewHolder holder = new RequestsViewHolder(view);
                        return holder;
                    }
                };

        myRequestsList.setAdapter(adapter);
        adapter.startListening();
    }




    public static class RequestsViewHolder extends RecyclerView.ViewHolder
    {
        TextView userName, userStatus;
        CircleImageView profileImage;
        Button AcceptButton, RejectButton;
        LinearLayout ButtonsLinear;


        public RequestsViewHolder(@NonNull View itemView)
        {
            super(itemView);


            userName = itemView.findViewById(R.id.FiFrUsNmTxVw);
            userStatus = itemView.findViewById(R.id.FiFrUsStTxVw);
            profileImage = itemView.findViewById(R.id.FiFrndsProImg);
            AcceptButton = itemView.findViewById(R.id.AcceptBtn);
            RejectButton = itemView.findViewById(R.id.RejectBtn);
            ButtonsLinear = itemView.findViewById(R.id.AcRejBtnLinr);
        }
    }
}