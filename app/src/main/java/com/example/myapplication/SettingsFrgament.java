package com.example.myapplication;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.app.Activity.RESULT_OK;


public class SettingsFrgament extends Fragment implements Dialg_username.DialoagUsernameLisner {

    private Button UpdateButton, Done1;
    private CircleImageView UsprofoImg;
    private String currentUserId;
    private FirebaseAuth mAuth;

    private DatabaseReference Rootreef;
    private TextView UsNmEd, UsStEd;
    private static final int GallleryPick = 1;
    private StorageReference UsProStImg;

    private ListView listView;
    private ProgressDialog progressDialog;


    private FragmentActivity myActivity;


    @Override
    public void onAttach(@NonNull Activity activity) {
        this.myActivity = (FragmentActivity) activity;
        super.onAttach(activity);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_sttings_frgament, container, false);

        mAuth = FirebaseAuth.getInstance();
        currentUserId = mAuth.getCurrentUser().getUid();
        Rootreef = FirebaseDatabase.getInstance().getReference();

        UsProStImg = FirebaseStorage.getInstance().getReference().child("Contacts Images");

        listView = view.findViewById(R.id.SettingsListview);


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(myActivity.getApplicationContext(), "Its Working !", Toast.LENGTH_SHORT).show();
            }
        });


        UpdateButton = (Button) view.findViewById(R.id.set_updatebut);
        ;
        Done1 = (Button) view.findViewById(R.id.Done1);
        UsNmEd = (TextView) view.findViewById(R.id.set_username);
        UsStEd = (TextView) view.findViewById(R.id.set_status);
        UsprofoImg = (CircleImageView) view.findViewById(R.id.setprofile_image);
        progressDialog = new ProgressDialog(myActivity.getApplicationContext());


        UpdateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UpdateSettings();
            }
        });

        Done1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDialog1();
                Toast.makeText(myActivity.getApplicationContext(), "Click Update Button to Save", Toast.LENGTH_SHORT).show();


            }
        });


        RetrieveUserInfo();


        UsprofoImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent galleryIntent = new Intent();
                galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
                galleryIntent.setType("image/*");
                startActivityForResult(galleryIntent, GallleryPick);
            }
        });

        return view;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GallleryPick && resultCode == RESULT_OK && data != null) {
            Uri ImageUri = data.getData();

            CropImage.activity(ImageUri)
                    .setGuidelines(CropImageView.Guidelines.ON)
                    .setAspectRatio(1, 1)
                    .start(myActivity);
        }

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);

            if (resultCode == RESULT_OK) {
                progressDialog.setTitle("Set Contacts Image");
                progressDialog.setMessage("Please wait, your profile image is updating...");
                progressDialog.setCanceledOnTouchOutside(false);
                progressDialog.show();

                Uri resultUri = result.getUri();

                StorageReference filePath = UsProStImg.child(currentUserId + ".jpg");


                final UploadTask uploadTask = filePath.putFile(resultUri);

                uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        taskSnapshot.getStorage().getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                String downloadUrl = uri.toString();
                                Rootreef.child("Users").child(currentUserId).child("image").setValue(downloadUrl)
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                progressDialog.dismiss();
                                                Toast.makeText(myActivity.getApplicationContext(), "Contacts Image Updated Successfully", Toast.LENGTH_LONG).show();
                                            }
                                        });
                            }
                        });
                    }
                });
            }
        }
    }


    public void openDialog1() {

        Dialg_username dialg_username = new Dialg_username();
        dialg_username.show(myActivity.getSupportFragmentManager(), "dialg-username");

    }


    private void UpdateSettings() {
        String setUserName = UsNmEd.getText().toString();
        String setUserStatus = UsStEd.getText().toString();

        if (TextUtils.isEmpty(setUserName)) {
            Toast.makeText(myActivity.getApplicationContext(), "User Name Required", Toast.LENGTH_SHORT).show();
            progressDialog.dismiss();
        } else {
            HashMap<String, String> ProfileMap = new HashMap<>();
            ProfileMap.put("uid", currentUserId);
            ProfileMap.put("name", setUserName);
            ProfileMap.put("status", setUserStatus);

            Rootreef.child("Users").child(currentUserId).setValue(ProfileMap)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {

                            if (task.isSuccessful()) {

                                Toast.makeText(myActivity.getApplicationContext(), "Updated Successfully", Toast.LENGTH_SHORT).show();
                            } else {
                                String message = task.getException().toString();
                                Toast.makeText(myActivity.getApplicationContext(), "Error:" + message, Toast.LENGTH_SHORT).show();
                            }
                        }
                    });


        }


    }


    private void RetrieveUserInfo() {
        Rootreef.child("Users").child(currentUserId)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if ((dataSnapshot.exists()) && (dataSnapshot.hasChild("name") && (dataSnapshot.hasChild("image")))) {
                            String retrieveUserName = dataSnapshot.child("name").getValue().toString();
                            String retrievesStatus = dataSnapshot.child("status").getValue().toString();
                            String retrieveProfileImage = dataSnapshot.child("image").getValue().toString();

                            UsNmEd.setText(retrieveUserName);
                            UsStEd.setText(retrievesStatus);
                            Picasso.get().load(retrieveProfileImage).into(UsprofoImg);
                        } else if ((dataSnapshot.exists()) && (dataSnapshot.hasChild("name"))) {
                            String retrieveUserName = dataSnapshot.child("name").getValue().toString();
                            String retrievesStatus = dataSnapshot.child("status").getValue().toString();

                            UsNmEd.setText(retrieveUserName);
                            UsStEd.setText(retrievesStatus);
                        } else {
                            Toast.makeText(myActivity.getApplicationContext(), "Please set & update your profile information...", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
    }


    @Override
    public void applyTexts(String username, String userstatus) {
        UsNmEd.setText(username);
        UsStEd.setText(userstatus);

    }


}