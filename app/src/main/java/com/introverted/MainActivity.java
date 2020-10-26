package com.introverted;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;


public class MainActivity extends AppCompatActivity {

    //Animation variables
    Animation topvar, bottomvar;
    //Objects to run animations on
    ImageView logoIcon;
    TextView logoText;
    private static int MS = 1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);

        //Animations
        topvar = AnimationUtils.loadAnimation(this, R.anim.top_animation);
        bottomvar = AnimationUtils.loadAnimation(this, R.anim.bottom_animation);
        //Hook
        logoIcon = findViewById(R.id.logoStart);
        logoText = findViewById(R.id.introvertedStartup);
        //Se setea las animaciones al logo y el texto del logo
        logoIcon.setAnimation(topvar);
        logoText.setAnimation(bottomvar);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent onboard = new Intent(MainActivity.this, Onboarding.class);
                startActivity(onboard);
                finish();
            }
        }, MS);
    }

    public void next(View v){

    }
}