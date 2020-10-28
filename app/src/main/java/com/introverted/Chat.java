package com.introverted;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class Chat extends AppCompatActivity {

    BottomNavigationView my_nav;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);

        my_nav = findViewById(R.id.bottomMenu);
        //my_nav.getMenu().findItem(R.id.match_window).setChecked(true);
        my_nav.setSelectedItemId(R.id.chat);
        my_nav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.match_window:
                        startActivity(new Intent(getApplicationContext(), Dashboard.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.profile:
                        startActivity(new Intent(getApplicationContext(), Profile.class));
                        overridePendingTransition(0, 0);
                        return true;
                    case R.id.activity_window:
                        startActivity(new Intent(getApplicationContext(), GamesAndActivities.class));
                        overridePendingTransition(0, 0);
                        return true;
                    case R.id.forum_chat:
                        startActivity(new Intent(getApplicationContext(), Forum.class));
                        overridePendingTransition(0, 0);
                        return true;
                    case R.id.chat:
                        return true;
                }
                return false;
            }
        });
    }
}