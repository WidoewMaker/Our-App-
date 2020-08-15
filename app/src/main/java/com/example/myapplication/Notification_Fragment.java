package com.example.myapplication;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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

import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;


public class Notification_Fragment extends Fragment {
    private RecyclerView myRequestsList;
    private FragmentActivity myActivity;

    private DatabaseReference ChatRequestsRef, UsersRef, ContactsRef;
    private String currentUserID;


    @Override
    public void onAttach(@NonNull Activity activity) {
        this.myActivity = (FragmentActivity) activity;
        super.onAttach(activity);
    }

    @Nullable


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_notification_, container, false);

        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        currentUserID = Objects.requireNonNull(mAuth.getCurrentUser()).getUid();

        UsersRef = FirebaseDatabase.getInstance().getReference().child("Users");
        ChatRequestsRef = FirebaseDatabase.getInstance().getReference().child("Chat Requests");
        ContactsRef = FirebaseDatabase.getInstance().getReference().child("Contacts");


        myRequestsList = (RecyclerView) view.findViewById(R.id.Req_Rcy);
        myRequestsList.setLayoutManager(new LinearLayoutManager(myActivity.getApplicationContext()));

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

        FirebaseRecyclerOptions<Contacts> options =
                new FirebaseRecyclerOptions.Builder<Contacts>()
                        .setQuery(ChatRequestsRef.child(currentUserID), Contacts.class)
                        .build();


        FirebaseRecyclerAdapter<Contacts, Notification_Fragment.RequestsViewHolder> adapter =
                new FirebaseRecyclerAdapter<Contacts, Notification_Fragment.RequestsViewHolder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull final RequestsViewHolder holder, int position, @NonNull Contacts contacts) {
                        holder.itemView.findViewById(R.id.AcRejBtnLinr).setVisibility(View.VISIBLE);


                        final String list_user_id = getRef(position).getKey();

                        DatabaseReference getTypeRef = getRef(position).child("request_type").getRef();

                        getTypeRef.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                if (dataSnapshot.exists()) {
                                    String type = Objects.requireNonNull(dataSnapshot.getValue()).toString();

                                    if (type.equals("received")) {
                                        UsersRef.child(list_user_id).addValueEventListener(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(DataSnapshot dataSnapshot) {
                                                if (dataSnapshot.hasChild("image")) {
                                                    final String requestProfileImage = Objects.requireNonNull(dataSnapshot.child("image").getValue()).toString();

                                                    Picasso.get().load(requestProfileImage).into(holder.profileImage);
                                                }

                                                final String requestUserName = Objects.requireNonNull(dataSnapshot.child("name").getValue()).toString();


                                                holder.userName.setText(requestUserName);
                                                holder.userStatus.setText(R.string.wants_to_connect_with_you);

                                                holder.RejectButton.setOnClickListener(new View.OnClickListener() {
                                                    @Override
                                                    public void onClick(View v) {
                                                        ChatRequestsRef.child(currentUserID).child(list_user_id)
                                                                .removeValue()
                                                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                    @Override
                                                                    public void onComplete(@NonNull Task<Void> task) {
                                                                        if (task.isSuccessful()) {
                                                                            ChatRequestsRef.child(list_user_id).child(currentUserID)
                                                                                    .removeValue()
                                                                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                                        @Override
                                                                                        public void onComplete(@NonNull Task<Void> task) {
                                                                                            if (task.isSuccessful()) {
                                                                                                Toast.makeText(myActivity.getApplicationContext(), "Contact Deleted", Toast.LENGTH_SHORT).show();
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
                                                    public void onClick(View view) {


                                                        ContactsRef.child(currentUserID).child(list_user_id).child("Contact")
                                                                .setValue("Saved").addOnCompleteListener(new OnCompleteListener<Void>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<Void> task) {
                                                                if (task.isSuccessful()) {
                                                                    ContactsRef.child(list_user_id).child(currentUserID).child("Contact")
                                                                            .setValue("Saved").addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                        @Override
                                                                        public void onComplete(@NonNull Task<Void> task) {
                                                                            if (task.isSuccessful()) {
                                                                                ChatRequestsRef.child(currentUserID).child(list_user_id)
                                                                                        .removeValue()
                                                                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                                            @Override
                                                                                            public void onComplete(@NonNull Task<Void> task) {
                                                                                                if (task.isSuccessful()) {
                                                                                                    ChatRequestsRef.child(list_user_id).child(currentUserID)
                                                                                                            .removeValue()
                                                                                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                                                                @Override
                                                                                                                public void onComplete(@NonNull Task<Void> task) {
                                                                                                                    if (task.isSuccessful()) {
                                                                                                                        Toast.makeText(myActivity.getApplicationContext(), "New Contact Saved", Toast.LENGTH_SHORT).show();
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
                                    } else if (type.equals("sent")) {
                                        Button request_sent_btn = holder.itemView.findViewById(R.id.AcceptBtn);
                                        request_sent_btn.setText(R.string.Req_Sent);

                                        holder.itemView.findViewById(R.id.AcRejBtnLinr).setVisibility(View.VISIBLE);
                                        holder.itemView.findViewById(R.id.AcceptBtn).setVisibility(View.GONE);


                                        UsersRef.child(list_user_id).addValueEventListener(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(DataSnapshot dataSnapshot) {
                                                if (dataSnapshot.hasChild("image")) {
                                                    final String requestProfileImage = Objects.requireNonNull(dataSnapshot.child("image").getValue()).toString();

                                                    Picasso.get().load(requestProfileImage).into(holder.profileImage);
                                                }

                                                final String requestUserName = Objects.requireNonNull(dataSnapshot.child("name").getValue()).toString();

                                                holder.userName.setText(requestUserName);
                                                holder.userStatus.setText(String.format("%s%s", getString(R.string.you_have_sent_a_request_to), requestUserName));


                                                holder.RejectButton.setOnClickListener(new View.OnClickListener() {
                                                    @Override
                                                    public void onClick(View view) {
                                                        ChatRequestsRef.child(currentUserID).child(list_user_id)
                                                                .removeValue()
                                                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                    @Override
                                                                    public void onComplete(@NonNull Task<Void> task) {
                                                                        if (task.isSuccessful()) {
                                                                            ChatRequestsRef.child(list_user_id).child(currentUserID)
                                                                                    .removeValue()
                                                                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                                        @Override
                                                                                        public void onComplete(@NonNull Task<Void> task) {
                                                                                            if (task.isSuccessful()) {
                                                                                                Toast.makeText(myActivity.getApplicationContext(), "you have cancelled the chat request.", Toast.LENGTH_SHORT).show();
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
                    public RequestsViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
                        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.users_display_layout, viewGroup, false);
                        return new RequestsViewHolder(view);
                    }
                };

        myRequestsList.setAdapter(adapter);
        adapter.startListening();
    }


    public static class RequestsViewHolder extends RecyclerView.ViewHolder {
        TextView userName, userStatus;
        CircleImageView profileImage;
        Button AcceptButton, RejectButton;
        LinearLayout ButtonsLinear;


        public RequestsViewHolder(@NonNull View itemView) {
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



