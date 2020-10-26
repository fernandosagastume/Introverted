package com.introverted;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.facebook.drawee.backends.pipeline.Fresco;

public class UserImages extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_images);
        Fresco.initialize(this);
    }
}