package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.TimeUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.FirebaseDatabase;

import java.util.concurrent.TimeUnit;

public class PhoneNumLogActivity extends AppCompatActivity
{


    private ImageButton Bt_ph_nm_sd,Bt_otp_sd;
    private EditText Ed_ph_nm,Ed_otp;
    private LinearLayout Lnr_otp,Lnr_pn;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks callbacks;
    private  String mVerificationId;
    private PhoneAuthProvider.ForceResendingToken mResendToken;
    private FirebaseAuth mAuth;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_num_log);

        mAuth = FirebaseAuth.getInstance();

        Bt_ph_nm_sd = (ImageButton) findViewById(R.id.Phnum_sendButton);
        Bt_otp_sd = (ImageButton) findViewById(R.id.Otp_sendButton);
        Ed_ph_nm = (EditText) findViewById(R.id.Phnum_Ed);
        Ed_otp = (EditText) findViewById(R.id.Opt_ed);
        Lnr_otp = (LinearLayout) findViewById(R.id.OtpLnr_linerlayout);
        Lnr_pn =(LinearLayout) findViewById(R.id.Phnum_linerlayout);
        progressDialog =new ProgressDialog(this);



        Bt_ph_nm_sd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String Phnum = Ed_ph_nm.getText().toString();

                if (TextUtils.isEmpty(Phnum))
                {
                    Ed_ph_nm.setError("Empty");
                }
                else
                    {
                        progressDialog.setTitle("Verifying Phone Number");
                        progressDialog.setMessage("Don't Cancel this Action");
                        progressDialog.setCanceledOnTouchOutside(false);
                        progressDialog.show();


                        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                                Phnum,        // Phone number to verify

                                60,                 // Timeout duration
                                TimeUnit.SECONDS,   // Unit of timeout
                                PhoneNumLogActivity.this,               // Activity (for callback binding)
                                callbacks);        // OnVerificationStateChangedCallbacks





                    }
            }
        });

        Bt_otp_sd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {

                Lnr_pn.setVisibility(View.INVISIBLE);

                String Vericode = Ed_otp.getText().toString();
                if (TextUtils.isEmpty(Vericode))
                {
                    Ed_otp.setError("Empty");
                    Toast.makeText(PhoneNumLogActivity.this, "Type Correct Verification Code", Toast.LENGTH_SHORT).show();
                }


                else
                    {
                        progressDialog.setTitle("Verifying Code");
                        progressDialog.setMessage("Don't Cancel this Action");
                        progressDialog.setCanceledOnTouchOutside(false);
                        progressDialog.show();

                        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(mVerificationId, Vericode);

                        signInWithPhoneAuthCredential(credential);

                }




            }
        });




        callbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential)

            {

                signInWithPhoneAuthCredential(phoneAuthCredential);
                Lnr_otp.setVisibility(View.VISIBLE);
                Lnr_pn.setVisibility(View.INVISIBLE);



            }
            public void onCodeSent( String verificationId,
                                   PhoneAuthProvider.ForceResendingToken token) {


                // Save verification ID and resending token so we can use them later
                mVerificationId = verificationId;
                mResendToken = token;


                Toast.makeText(PhoneNumLogActivity.this, "Code has been sended", Toast.LENGTH_SHORT).show();
                Lnr_otp.setVisibility(View.VISIBLE);
                Lnr_pn.setVisibility(View.INVISIBLE);
                progressDialog.dismiss();



            }

            @Override
            public void onVerificationFailed(FirebaseException e)
            {
                progressDialog.dismiss();
                Ed_ph_nm.setError("Invalid Number");
                Toast.makeText(PhoneNumLogActivity.this, "Invalid Phone Number \nEnter Your Ph Number with Contry Code", Toast.LENGTH_SHORT).show();


            }
        };





    }



    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task)
            {

                if (task.isSuccessful())
                {

                    progressDialog.dismiss();
                    Toast.makeText(PhoneNumLogActivity.this, "Your Code is Verified", Toast.LENGTH_SHORT).show();
                    SendUserToMainActivity();


                }


                else
                {
                    String message = task.getException().toString();
                    Toast.makeText(PhoneNumLogActivity.this, "Error:\n" + message, Toast.LENGTH_SHORT).show();

                }

            }
        });
    }

    private void SendUserToMainActivity()
    {
        Intent mainIntent = new Intent(PhoneNumLogActivity.this,MainActivity.class);
        startActivity(mainIntent);
        finish();

    }

}
