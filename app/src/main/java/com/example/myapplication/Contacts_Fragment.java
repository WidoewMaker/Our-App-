package com.example.myapplication;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.content.Context.INPUT_METHOD_SERVICE;


/**
 * A simple {@link Fragment} subclass.
 */
public class Contacts_Fragment extends Fragment
{
    private View ContactsView;
    private RecyclerView myContactsList;

    private DatabaseReference ContacsRef, UsersRef;
    private FirebaseAuth mAuth;
    private String currentUserID;
    private int countu= -1;


    public Contacts_Fragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        ContactsView = inflater.inflate(R.layout.fragment_contacts_, container, false);


        myContactsList = (RecyclerView) ContactsView.findViewById(R.id.Contacts_RecyView);
        myContactsList.setLayoutManager(new LinearLayoutManager(getContext()));


        mAuth = FirebaseAuth.getInstance();
        currentUserID = mAuth.getCurrentUser().getUid();


        ContacsRef = FirebaseDatabase.getInstance().getReference().child("Contacts").child(currentUserID);
        UsersRef = FirebaseDatabase.getInstance().getReference().child("Users");



        return ContactsView;
    }




    @Override
    public void onStart()
    {
        super.onStart();

        FirebaseRecyclerOptions options =
                new FirebaseRecyclerOptions.Builder<Contacts>()
                        .setQuery(ContacsRef, Contacts.class)
                        .build();


        final FirebaseRecyclerAdapter<Contacts, ContactsViewHolder> adapter
                = new FirebaseRecyclerAdapter<Contacts, ContactsViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull final ContactsViewHolder holder, final int position, @NonNull Contacts model)
            {
                final String userIDs = getRef(position).getKey();

                UsersRef.child(userIDs).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot)
                    {
                        if (countu == position)
                            holder.NewsFeed.setVisibility(View.VISIBLE);
                        else
                            holder.NewsFeed.setVisibility(View.GONE);





                        if (dataSnapshot.exists())
                        {

                            if (dataSnapshot.child("userState").hasChild("state"))
                            {
                                String state = dataSnapshot.child("userState").child("state").getValue().toString();
                                String date = dataSnapshot.child("userState").child("date").getValue().toString();
                                String time = dataSnapshot.child("userState").child("time").getValue().toString();

                                if (state.equals("online"))
                                {
                                    holder.OnlineIcon.setVisibility(View.VISIBLE);
                                }
                                else if (state.equals("offline"))
                                {
                                    holder.OnlineIcon.setVisibility(View.INVISIBLE);
                                }
                            }
                            else
                            {
                                holder.OnlineIcon.setVisibility(View.INVISIBLE);
                            }



                            if (dataSnapshot.hasChild("image"))
                            {
                                String userImage = dataSnapshot.child("image").getValue().toString();
                                String profileName = dataSnapshot.child("name").getValue().toString();
                                String profileStatus = dataSnapshot.child("status").getValue().toString();

                                holder.userName.setText(profileName);
                                holder.userStatus.setText(profileStatus);
                                if (holder.userStatus.getText().toString().length() > 30 )

                                {
                                    holder.userStatus.setText(profileStatus);
                                    holder.userStatus.setText(profileStatus.substring((int)(0),(int)(30)).concat("....."));
                                }
                                Picasso.get().load(userImage).placeholder(R.drawable.profilemiga).into(holder.profileImage);
                            }
                            else
                            {
                                String profileName = dataSnapshot.child("name").getValue().toString();
                                String profileStatus = dataSnapshot.child("status").getValue().toString();

                                holder.userName.setText(profileName);
                                holder.userStatus.setText(profileStatus);
                                if (holder.userStatus.getText().toString().length() > 30 )

                                {
                                    holder.userStatus.setText(profileStatus);
                                    holder.userStatus.setText(profileStatus.substring((int)(0),(int)(30)).concat("....."));
                                }
                            }

                            holder.itemView.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v)
                                {
                                    if (countu == position)

                                        countu = -1;
                                    else
                                        countu = position;
                                   notifyDataSetChanged();



                                }
                            });




                            holder.Up.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v)
                                {
                                    holder.NewsFeed.setVisibility(View.GONE);

                                }
                            });
                            holder.X.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v)
                                {
                                    holder.LCSlnr.setVisibility(View.VISIBLE);
                                    holder.CmEtLnr.setVisibility(View.GONE);


                                            InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(INPUT_METHOD_SERVICE);
                                            imm.hideSoftInputFromWindow(holder.CmtEdtx.getWindowToken(), 0);

                                            holder.CmtEdtx.setText("");






                                }
                            });
                            holder.ComBt.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v)
                                {
                                    holder.CmEtLnr.setVisibility(View.VISIBLE);
                                    holder.LCSlnr.setVisibility(View.GONE);

                                }
                            });

                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }

            @NonNull
            @Override
            public ContactsViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i)
            {
                View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.coustom_newsfeed, viewGroup, false);
                ContactsViewHolder viewHolder = new ContactsViewHolder(view);
                return viewHolder;
            }
        };

        myContactsList.setAdapter(adapter);
        adapter.startListening();
    }




    public static class ContactsViewHolder extends RecyclerView.ViewHolder
    {
        TextView userName, userStatus;
        CircleImageView profileImage;
        ImageView OnlineIcon;
        LinearLayout NewsFeed;
        ImageView Up;
        ImageView ComBt;
        ImageView X;
        LinearLayout LCSlnr;
        LinearLayout CmEtLnr;
        EditText CmtEdtx;




        public ContactsViewHolder(@NonNull View itemView)
        {
            super(itemView);

            userName = itemView.findViewById(R.id.NsFdUsNmTxVw);
            userStatus = itemView.findViewById(R.id.NsfdUsStTxVw);
            profileImage = itemView.findViewById(R.id.NsFdProImg);
            OnlineIcon = itemView.findViewById(R.id.NsFdUserIsOnlineImg);
            NewsFeed = itemView.findViewById(R.id.newsfeedlinr);
            Up = itemView.findViewById(R.id.up);
            ComBt = itemView.findViewById(R.id.commentBut);
            X = itemView.findViewById(R.id.x);
            LCSlnr = itemView.findViewById(R.id.NsFdLikeCmtShare);
            CmEtLnr = itemView.findViewById(R.id.ComtEtLnr);
            CmtEdtx = itemView.findViewById(R.id.CmtEtx);










        }

    }

}
