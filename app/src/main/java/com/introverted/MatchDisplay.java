package com.introverted;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import de.hdodenhof.circleimageview.CircleImageView;

public class MatchDisplay extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_match_display);
        findViewById(R.id.cancel_mv).setOnClickListener(this);
        findViewById(R.id.keepSwiping).setOnClickListener(this);

        Intent intent = getIntent();
        String userFN = intent.getStringExtra("USER_FN");
        String matchFN = intent.getStringExtra("MATCH_FN");

        TextView matchText = findViewById(R.id.namesMatch);
        matchText.setText(matchText.getText().toString().replace("x", matchFN));
        CircleImageView useriv = findViewById(R.id.userMIM);
        CircleImageView matchiv = findViewById(R.id.matchMIM);
        useriv.setImageBitmap(BMHelper.getInstance().getBitmap().get(0));
        matchiv.setImageBitmap(BMHelper.getInstance().getBitmap().get(1));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.cancel_mv:
                finish();
            case R.id.keepSwiping:
                finish();
        }
    }
}