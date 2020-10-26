package com.introverted;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import com.introverted.R;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class ChatAndForum extends AppCompatActivity {
    BottomNavigationView my_nav;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_and_forum);

        my_nav = findViewById(R.id.bottomMenu);
        //my_nav.getMenu().findItem(R.id.match_window).setChecked(true);

        setTheme(R.style.DashboardTheme);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);

        my_nav.setSelectedItemId(R.id.forum_chat);
        my_nav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @SuppressLint("NonConstantResourceId")
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch(item.getItemId()){
                    case  R.id.forum_chat:
                        return true;
                    case R.id.match_window:
                        startActivity(new Intent(getApplicationContext(), Dashboard.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.activity_window:
                        startActivity(new Intent(getApplicationContext(), GamesAndActivities.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.profile:
                        startActivity(new Intent(getApplicationContext(), Profile.class));
                        overridePendingTransition(0,0);
                        return true;
                }
                return false;
            }
        });
    }
}