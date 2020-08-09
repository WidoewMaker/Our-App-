package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class FindFirendsAcitvity extends AppCompatActivity
{

    private Toolbar mToolbar;
    private RecyclerView FindFriendsRecyclerList;
    private DatabaseReference UsersRef;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_firends_acitvity);


        UsersRef = FirebaseDatabase.getInstance().getReference().child("Users");





        mToolbar = (Toolbar) findViewById(R.id.findfrnds_titlie_bar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Find Friends");

        FindFriendsRecyclerList = (RecyclerView) findViewById(R.id.findfrnds_RecyView);
        FindFriendsRecyclerList.setLayoutManager(new LinearLayoutManager(FindFirendsAcitvity.this));
    }


    @Override
    protected void onStart()
    {
        super.onStart();

        FirebaseRecyclerOptions<Contacts> options =
                new FirebaseRecyclerOptions.Builder<Contacts>()
                        .setQuery(UsersRef, Contacts.class)
                        .build();

        FirebaseRecyclerAdapter<Contacts, FindFriendViewHolder> adapter =
                new FirebaseRecyclerAdapter<Contacts, FindFriendViewHolder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull FindFriendViewHolder holder, final int position, @NonNull Contacts model)
                    {
                        holder.userName.setText(model.getName());
                        holder.userStatus.setText(model.getStatus());
                        if (holder.userStatus.getText().toString().length() > 30 )

                        {
                            holder.userStatus.setText(model.getStatus().substring((int)(0),(int)(30)).concat("....."));

                        }
                        else
                        {
                            holder.userStatus.setText(model.getStatus());


                        }
                        Picasso.get().load(model.getImage()).placeholder(R.drawable.profilemiga).into(holder.profileImage);


                        holder.itemView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view)
                            {
                                String visit_user_id = getRef(position).getKey();
                                Intent profileIntent = new Intent(FindFirendsAcitvity.this, ProfileActivity.class);
                                profileIntent.putExtra("visit_user_id", visit_user_id);
                                startActivity(profileIntent);


                            }
                        });
                    }

                    @NonNull
                    @Override
                    public FindFriendViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i)
                    {
                            View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.users_display_layout, viewGroup, false);
                        FindFriendViewHolder viewHolder = new FindFriendViewHolder(view);
                        return viewHolder;
                    }
                };

        FindFriendsRecyclerList.setAdapter(adapter);

        adapter.startListening();
    }



    public static class FindFriendViewHolder extends RecyclerView.ViewHolder
    {
        TextView userName, userStatus;
        CircleImageView profileImage;




        public FindFriendViewHolder(@NonNull View itemView)
        {
            super(itemView);

            userName = itemView.findViewById(R.id.FiFrUsNmTxVw);
            userStatus = itemView.findViewById(R.id.FiFrUsStTxVw);
            profileImage = itemView.findViewById(R.id.FiFrndsProImg);
        }
    }


}
