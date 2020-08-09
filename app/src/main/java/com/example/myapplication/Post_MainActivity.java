package com.example.myapplication;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

public class Post_MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post__main);




        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                new Home_Fragment()).commit();

    }



}
