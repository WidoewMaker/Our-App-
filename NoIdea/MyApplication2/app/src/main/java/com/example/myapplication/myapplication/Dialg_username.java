package com.example.myapplication.myapplication;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.example.myapplication.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Dialg_username extends AppCompatDialogFragment {

    private EditText editTextUsnm,editTextUst;
    private  DialoagUsernameLisner lisner;
    private DatabaseReference Rootreef;
    private FirebaseAuth mAuth;
    private String currentUserId;





    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable final Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_layout, null);
        mAuth =FirebaseAuth.getInstance();

        currentUserId = mAuth.getCurrentUser().getUid();
        Rootreef = FirebaseDatabase.getInstance().getReference();

        builder.setView(view)
                .setTitle("UserName")
                .setNegativeButton("Cancle", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })

                .setPositiveButton("Save", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {
                        if((editTextUsnm.getText().toString().length()<5) || (editTextUsnm.getText().toString().length()>20))

                        {


                        }
                        else if((editTextUst.getText().toString().length()<1) || (editTextUst.getText().toString().length()>50))
                        {

                        }

                        else {
                            String UserName = editTextUsnm.getText().toString();
                            String UserStatus = editTextUst.getText().toString();
                            lisner.applyTexts(UserName,UserStatus);
                        }


                    }
                });
        editTextUsnm = view.findViewById(R.id.dialogedtx);
        editTextUst = view.findViewById(R.id.dialogedst);
        RetriveUserInfo();

        return builder.create();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            lisner = (DialoagUsernameLisner) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()+"Must Implement DialgUsernamelisner");
        }
    }







    public interface DialoagUsernameLisner
    {
        void applyTexts(String username, String userstatus);
    }


    private void RetriveUserInfo()

    {

        Rootreef.child("Users").child(currentUserId)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot)
                    {

                        if((dataSnapshot.exists()) && (dataSnapshot.hasChild("name") && (dataSnapshot.hasChild("image"))))
                        {
                            String retriveUserName = dataSnapshot.child("name").getValue().toString();
                            String retriveStatus = dataSnapshot.child("status").getValue().toString();


                            editTextUsnm.setText(retriveUserName);
                            editTextUst.setText(retriveStatus);





                        }
                        else if((dataSnapshot.exists()) && (dataSnapshot.hasChild("name")))
                        {

                            String retriveUserName = dataSnapshot.child("name").getValue().toString();
                            String retriveStatus = dataSnapshot.child("status").getValue().toString();

                            editTextUsnm.setText(retriveUserName);
                            editTextUst.setText(retriveStatus);

                        }

                        else {

                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });


                }

    }
