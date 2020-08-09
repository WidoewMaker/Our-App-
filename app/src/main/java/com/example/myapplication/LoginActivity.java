package com.example.myapplication;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;

public class LoginActivity extends AppCompatActivity
{
    private FirebaseAuth mAuth;
    private ProgressDialog loadingbar;
    private Button LoginButton,PhoneLoginButton;
    private EditText UserEmail,UserPassword;
    private TextView NeedNewAcc,ForgPass;
    private DatabaseReference UsrRef;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mAuth = FirebaseAuth.getInstance();
        UsrRef = FirebaseDatabase.getInstance().getReference().child("Users");


        InitializeFields();


        NeedNewAcc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)

            {

                SendUserToSignActivity();

            }
        });

        PhoneLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SendUserToPhoneNumActivity();
            }
        });

        LoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AllowUserToLogin();

            }
        });

    }








    private void AllowUserToLogin()
    {


        String email = UserEmail.getText().toString();
        String password = UserPassword.getText().toString();
        if (TextUtils.isEmpty(email))
        {
            Toast.makeText(this, "Please Enter email", Toast.LENGTH_SHORT).show();
        }
        if (TextUtils.isEmpty(password))
        {
            Toast.makeText(this, "Please Enter password", Toast.LENGTH_SHORT).show();
        }
        else {
            loadingbar.setTitle("Logging In");
            loadingbar.setMessage("Pls wait...Dont Cancel this Action");
            loadingbar.setCanceledOnTouchOutside(false);
            loadingbar.show();
            mAuth.signInWithEmailAndPassword(email,password)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task)
                        {
                            if (task.isSuccessful())
                            {
                                String CurrentUserId =mAuth.getCurrentUser().getUid();
                                String deviceToken = FirebaseInstanceId.getInstance().getToken();

                                UsrRef.child(CurrentUserId).child("device_token")
                                        .setValue(deviceToken)
                                        .addOnCompleteListener(new OnCompleteListener<Void>()
                                        {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task)

                                            {

                                                if (task.isSuccessful())
                                                {
                                                    SendUserToMainActivity();
                                                    Toast.makeText(LoginActivity.this, "LogIn Sucessfull", Toast.LENGTH_SHORT).show();

                                                    loadingbar.dismiss();



                                                }

                                            }
                                        });




                                     }
                            else {
                                String message = task.getException().toString();
                                Toast.makeText(LoginActivity.this, "Error:-"+message, Toast.LENGTH_SHORT).show();

                                loadingbar.dismiss();
                            }

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.d("Login Activity", "onFailure() called with: e = [" + e + "]");
                }
            });
        }
    }


    private void InitializeFields()

    {

        LoginButton = (Button) findViewById(R.id.login_button);
        PhoneLoginButton = (Button) findViewById(R.id.phone_login_button);
        UserEmail =(EditText) findViewById(R.id.login_email);
        UserPassword =(EditText) findViewById(R.id.login_pass);
        NeedNewAcc = (TextView) findViewById(R.id.needNewAccoumt);
       ForgPass = (TextView) findViewById(R.id.forg_pass);
        loadingbar = new ProgressDialog(this);

    }




    private void SendUserToMainActivity()
    {
        Intent mainIntent = new Intent(LoginActivity.this,MainActivity.class);
        mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(mainIntent);
        finish();

    }
    private void SendUserToSignActivity()
    {
        Intent signIntent = new Intent(LoginActivity.this,RegisterActivity.class);
        startActivity(signIntent);

    }


    private void SendUserToPhoneNumActivity()
    {
        Intent signPHIntent = new Intent(LoginActivity.this,PhoneNumLogActivity.class);
        startActivity(signPHIntent);

    }

}
