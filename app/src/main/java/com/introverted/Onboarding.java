package com.introverted;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

public class Onboarding extends AppCompatActivity {
    public static ViewPager viewpage;
    SlideViewPagerAdapter spa;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_onboarding);
        viewpage = findViewById(R.id.viewp);
        spa = new SlideViewPagerAdapter(this);
        viewpage.setAdapter(spa);
        if(isNotFirstTimeRunning()){
            Intent in = new Intent(Onboarding.this, Login.class);
            in.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(in);
        }
        else {
            SharedPreferences.Editor edit = getSharedPreferences("Onboard", MODE_PRIVATE).edit();
            edit.putBoolean("Onboard", true);
            edit.commit();
        }
    }

    private boolean isNotFirstTimeRunning() {
        SharedPreferences shp = getSharedPreferences("Onboard", MODE_PRIVATE);
        return shp.getBoolean("Onboard", false);
    }
}