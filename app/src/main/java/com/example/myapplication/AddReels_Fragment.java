package com.example.myapplication;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import static android.app.Activity.RESULT_OK;


public class AddReels_Fragment extends Fragment {
    private List<MediaObject> mediaObjectList = new ArrayList<>();
    private DemoAdapter demoAdapter;
    private RecyclerView recyclerView;
    private ImageButton ReelsAdd;
    private static final int GallleryPick = 1;
    private String currentUserId;
    private FirebaseAuth mAuth;
    private DatabaseReference Reelref,Private_Reelref;
    private Uri ImgUri;
    private StorageReference ReelsPostImg;
    TextView CancelBoSh, GalaryBoSh;
    BottomSheetDialog bottomSheetDialog, postbottomSheetDialog;
    ImageView Video;
    CheckBox PublicCkBx, PrivateCkBx;
    TextView ReelsWsr_TxVw;
    ImageButton PostBtn;
    EditText ReelsDescriptionEdtx;
    private FragmentActivity myActivity;


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
        View v = inflater.inflate(R.layout.fragment_add_reels_, container, false);
        CreateBottomSheetDialog();
        CreateReelsPostBottomSheetDialog();



        mAuth = FirebaseAuth.getInstance();
        currentUserId = mAuth.getCurrentUser().getUid();
        Private_Reelref = FirebaseDatabase.getInstance().getReference().child("Users").child(currentUserId).child("ReelsPostPrivate");
        Reelref = FirebaseDatabase.getInstance().getReference().child("ReelsPosts");
        ReelsPostImg = FirebaseStorage.getInstance().getReference("ReelsPosts");

        recyclerView = (RecyclerView) v.findViewById(R.id.reelsRecycler_View);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        SnapHelper snapHelper = new PagerSnapHelper();
        snapHelper.attachToRecyclerView(recyclerView);



            Reelref.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                        MediaObject UmediaObject = postSnapshot.getValue(MediaObject.class);
                        mediaObjectList.add(UmediaObject);


                    }
                    demoAdapter = new DemoAdapter(mediaObjectList, myActivity.getApplicationContext());
                    recyclerView.setAdapter(demoAdapter);


                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Toast.makeText(myActivity.getApplicationContext(), databaseError.getMessage(), Toast.LENGTH_SHORT).show();

                }
            });


        ReelsAdd = (ImageButton) v.findViewById(R.id.reelsAdd_imgBut);







        ReelsAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomSheetDialog.show();


            }
        });


        return v;


    }

    private void CreateReelsPostBottomSheetDialog() {
        if (postbottomSheetDialog == null) {
            final View view = LayoutInflater.from(getContext()).inflate(R.layout.reels_post_bottom_screen, null);
            Video = view.findViewById(R.id.VideoShow);
            PublicCkBx = view.findViewById(R.id.reelsPublic_checkBox);
            PostBtn = view.findViewById(R.id.reelsPost_imgBtn);
            PrivateCkBx = view.findViewById(R.id.reelsPrivate_checkBox);
            ReelsDescriptionEdtx = view.findViewById(R.id.reelsDescriptionEdTx);
            ReelsWsr_TxVw = view.findViewById(R.id.reelsWarning_TxVw);


            PostBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    uploadReels();

                }
            });

            PublicCkBx.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (PublicCkBx.isChecked()) {
                        ReelsWsr_TxVw.setVisibility(View.VISIBLE);
                        PrivateCkBx.setChecked(false);
                    } else {
                        PrivateCkBx.setChecked(true);
                        ReelsWsr_TxVw.setVisibility(View.GONE);
                    }


                }
            });

            PrivateCkBx.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (PrivateCkBx.isChecked()) {
                        PublicCkBx.setChecked(false);
                        ReelsWsr_TxVw.setVisibility(View.GONE);

                    } else {
                        PublicCkBx.setChecked(true);
                        ReelsWsr_TxVw.setVisibility(View.VISIBLE);
                    }


                }
            });


            postbottomSheetDialog = new BottomSheetDialog(getContext());
            postbottomSheetDialog.setContentView(view);
        }


    }

    private void CreateBottomSheetDialog() {
        if (bottomSheetDialog == null) {
            View view = LayoutInflater.from(getContext()).inflate(R.layout.bottom_screen_ly, null);
            GalaryBoSh = view.findViewById(R.id.BotGal);
            CancelBoSh = view.findViewById(R.id.BotCan);

            GalaryBoSh.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent galleryIntent = new Intent();
                    galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
                    galleryIntent.setType("image/*");
                    startActivityForResult(Intent.createChooser(galleryIntent, "Select"), GallleryPick);


                    bottomSheetDialog.dismiss();
                }
            });

            CancelBoSh.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    bottomSheetDialog.dismiss();


                }
            });


            bottomSheetDialog = new BottomSheetDialog(getContext());
            bottomSheetDialog.setContentView(view);
        }

    }

    private String getFileExtension(Uri uri) {
        ContentResolver cR = this.myActivity.getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));

    }

    private void uploadReels() {
        if (ImgUri != null) {
            StorageReference fileRef = ReelsPostImg.child(System.currentTimeMillis() + "." + getFileExtension(ImgUri));
            fileRef.putFile(ImgUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                            Toast.makeText(myActivity.getApplicationContext(), "Yovvv Done", Toast.LENGTH_SHORT).show();
                            MediaObject mediaObject = new MediaObject(ReelsDescriptionEdtx.getText().toString().trim(),
                                    taskSnapshot.getDownloadUrl().toString());
                            String uploadId = Reelref.push().getKey();
                            Reelref.child(uploadId).setValue(mediaObject);

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    postbottomSheetDialog.dismiss();
                    Toast.makeText(myActivity.getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();

                }
            }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                    postbottomSheetDialog.dismiss();
                    Toast.makeText(myActivity.getApplicationContext(), "Uploading yea", Toast.LENGTH_SHORT).show();
                }
            });


        } else {
            postbottomSheetDialog.show();
            Toast.makeText(getContext(), "No File Selected", Toast.LENGTH_SHORT).show();
        }

    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {


        if (requestCode == GallleryPick && resultCode == RESULT_OK) {
            ImgUri = data.getData();
            postbottomSheetDialog.show();
            Picasso.get().load(ImgUri).into(Video);

        }


    }
}
