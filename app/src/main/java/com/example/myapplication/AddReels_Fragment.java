package com.example.myapplication;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.util.ArrayList;
import java.util.List;

import static android.app.Activity.RESULT_OK;


public class AddReels_Fragment extends Fragment  {
    private List<MediaObject> mediaObjectList = new ArrayList<>();
    private DemoAdapter demoAdapter;
    private RecyclerView recyclerView;
    private ImageButton ReelsAdd;
    private static final int GallleryPick = 1;
    private String currentUserId;
    private FirebaseAuth mAuth;
    private DatabaseReference Reelreef;
    private Uri ImgUri;
    private StorageReference ReelsProStImg;
    TextView CancelBoSh, GalaryBoSh;
    BottomSheetDialog bottomSheetDialog;
    private int requestCode;
    private int resultCode;
    @Nullable
    private Intent data;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_add_reels_, container, false);
        mAuth = FirebaseAuth.getInstance();
        currentUserId = mAuth.getCurrentUser().getUid();
        Reelreef = FirebaseDatabase.getInstance().getReference();
        ReelsProStImg = FirebaseStorage.getInstance().getReference().child("Reels");

        recyclerView = (RecyclerView) v.findViewById(R.id.reelsRecycler_View);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);

        ReelsAdd = (ImageButton) v.findViewById(R.id.reelsAdd_imgBut);


        SnapHelper snapHelper = new PagerSnapHelper();
        snapHelper.attachToRecyclerView(recyclerView);


        mediaObjectList.add(new MediaObject("", "", "", "", "", "", "", "", ""));

        mediaObjectList.add(new MediaObject("", "", "", "", "", "", "", "", ""));

        mediaObjectList.add(new MediaObject("", "", "", "", "", "", "", "", ""));

        mediaObjectList.add(new MediaObject("", "", "", "", "", "", "", "", ""));

        mediaObjectList.add(new MediaObject("", "", "", "", "", "", "", "", ""));

        demoAdapter = new DemoAdapter(mediaObjectList, getActivity().getApplicationContext());
        recyclerView.setAdapter(demoAdapter);
        demoAdapter.notifyDataSetChanged();


        CreateBottomSheetDialog();

        ReelsAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomSheetDialog.show();


            }
        });


        return v;


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
                    startActivityForResult(galleryIntent, GallleryPick);
                    bottomSheetDialog.dismiss();
                }
            });

            CancelBoSh.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v)
                {
                    bottomSheetDialog.dismiss();

                }
            });


            bottomSheetDialog = new BottomSheetDialog(getContext());
            bottomSheetDialog.setContentView(view);
        }

    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        this.requestCode = requestCode;
        this.resultCode = resultCode;
        this.data = data;
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == GallleryPick && resultCode == RESULT_OK && data != null) {
            Uri ImageUri = data.getData();

            CropImage.activity(ImageUri)
                    .setGuidelines(CropImageView.Guidelines.ON)
                    .setAspectRatio(1, 1)
                    .start(getActivity());
        }

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            Uri resultUri = result.getUri();
            StorageReference filePath = ReelsProStImg.child(currentUserId + ".jpg");
            final UploadTask uploadTask = filePath.putFile(resultUri);

            uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    taskSnapshot.getStorage().getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            String downloadUrl = uri.toString();
                            Reelreef.child("Posts").setValue(downloadUrl)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            Toast.makeText(getContext(), "Op", Toast.LENGTH_SHORT).show();

                                        }
                                    });
                        }
                    });
                }
            });
            }



    }


}
