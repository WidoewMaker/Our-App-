package com.example.myapplication;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.theartofdev.edmodo.cropper.CropImage;

import java.util.HashMap;

public class PostActivity extends AppCompatActivity {
    String  myUrl = "";
    private StorageTask uploadTask;
    private Uri fileUri;
    StorageReference storageReference;

    ImageView close, image_added;
    TextView post;
    EditText description;

    ProgressDialog progressDialog;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);

        close = (ImageView) findViewById(R.id.close);
        image_added = (ImageView) findViewById(R.id.image_added);
        post = (TextView) findViewById(R.id.post);
        description = (EditText) findViewById(R.id.Dec_Etx);

        progressDialog = new ProgressDialog(this);

        storageReference = FirebaseStorage.getInstance().getReference("Posts");




        close.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(PostActivity.this, Post_MainActivity.class));
                finish();


            }


        });

        post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                uploadimage();

            }
        });

        CropImage.activity()
                .setAspectRatio(1, 1)
                .start(PostActivity.this);
    }

    private String getFileEx(Uri uri)
    {
        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(contentResolver.getType(uri));
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE)
        {

            if (resultCode == RESULT_OK)
            {
                CropImage.ActivityResult result = CropImage.getActivityResult(data);
                fileUri = result.getUri();

                image_added.setImageURI(fileUri);
            }

            else
                {
                Toast.makeText(this, "Somrthing Went Wrong \n Please Post Again ", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(PostActivity.this, Post_MainActivity.class));
                finish();
            }

           
        }


    }




    private void uploadimage()
    {
        progressDialog.setTitle("Sending Image");
        progressDialog.setMessage("Please wait....");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();

        if (fileUri != null)
        {

            final StorageReference filereference = storageReference.child(System.currentTimeMillis() + "." + getFileEx(fileUri));

            uploadTask =filereference.putFile(fileUri);
            uploadTask.continueWithTask(new Continuation() {
                @Override
                public Object then(@NonNull Task task) throws Exception
                {
                    if (!task.isSuccessful())
                    {
                        throw task.getException();

                    }

                    return filereference.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>()
            {
                @Override
                public void onComplete(@NonNull Task<Uri> task)
                {

                    if (task.isSuccessful())
                    {
                        Uri dounloadUri = task.getResult();
                        myUrl = dounloadUri.toString();


                        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Posts");

                        String postid = reference.push().getKey();
                        HashMap<String,Object> hashMap = new HashMap<>();
                        hashMap.put("postId",postid);
                        hashMap.put("postImage",myUrl);
                        hashMap.put("Description",description.getText().toString());
                        hashMap.put("Publisher", FirebaseAuth.getInstance().getCurrentUser().getUid());
                        reference.child(postid).setValue(hashMap);

                        progressDialog.dismiss();
                        startActivity(new Intent(PostActivity.this, Post_MainActivity.class));
                        finish();

                    }
                    else
                    {
                        Toast.makeText(PostActivity.this, "Failded to Uploaded", Toast.LENGTH_SHORT).show();


                    }

                }





            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e)
                {
                    Toast.makeText(PostActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();


                }
            });




        }

        else
        {
            Toast.makeText(PostActivity.this, "Select a Image", Toast.LENGTH_SHORT).show();

        }

    }
}
