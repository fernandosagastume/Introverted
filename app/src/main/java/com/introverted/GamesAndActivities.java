package com.introverted;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Shader;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.introverted.adapters.HorizontalMatchesAdapter;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class GamesAndActivities extends AppCompatActivity {
    private SharedPreferences adminSession;
    private HorizontalMatchesAdapter adapter;

    BottomNavigationView my_nav;
    TextView tv;
    ImageButton spotify;
    List<MatchModel> matchModels;
    Call<List<MatchCarrouselInfo>> call;
    LinearLayoutManager manager;
    RecyclerView matchesRV;
    ProgressDialog progressDialog;
    int imageRequests;
    int imageCounter;
    Bitmap userIm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_games_and_activities);

        setTheme(R.style.DashboardTheme);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        adminSession = getSharedPreferences("LOGIN_PREF", MODE_PRIVATE);
        //--------------------------------------------------------------------------------------------------
        //Para popular los matches del usuario
        matchModels = new ArrayList<>();
        MatchModel mm = new MatchModel(0, "Likes");
        matchModels.add(mm);

        imageCounter = 0;
        Retrofit retrofit = new Retrofit.Builder().baseUrl(Api.url).addConverterFactory(GsonConverterFactory.create())
                .build();
        Api api = retrofit.create(Api.class);
        call = api.getMatchesInfo(adminSession.getInt("USER_ID", 0));
        progressDialog = new ProgressDialog(GamesAndActivities.this);
        progressDialog.setMessage("Cargando...");
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();

        //Handler para manejar cuando el progress dialog se cierra porque no hubo response
        Runnable progressRunnable = new Runnable() {
            @Override
            public void run() {
                progressDialog.cancel();
            }
        };
        Handler pdCanceller = new Handler();
        pdCanceller.postDelayed(progressRunnable, 10000);
        callInfo(call);
        //--------------------------------------------------------------------------------------------------

        manager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        matchesRV = findViewById(R.id.matchesRV);
        matchesRV.setLayoutManager(manager);
        adapter = new HorizontalMatchesAdapter(matchModels,GamesAndActivities.this);
        matchesRV.setAdapter(adapter);

        tv = findViewById(R.id.introvertedGA);
        Shader textShader = new LinearGradient(0, 0, 60, 140,
                new int[]{Color.parseColor("#39C34A"), Color.parseColor("#1D5480")},
                new float[]{0, 1}, Shader.TileMode.CLAMP);
        tv.getPaint().setShader(textShader);

        spotify = findViewById(R.id.spotifyBanner);
        spotify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(GamesAndActivities.this, SpotifyApi.class));
            }
        });

        my_nav = findViewById(R.id.bottomMenu);
        //my_nav.getMenu().findItem(R.id.match_window).setChecked(true);
        my_nav.setSelectedItemId(R.id.activity_window);
        my_nav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.activity_window:
                        return true;
                    case R.id.match_window:
                        startActivity(new Intent(getApplicationContext(), Dashboard.class));
                        overridePendingTransition(0, 0);
                        return true;
                    case R.id.profile:
                        startActivity(new Intent(getApplicationContext(), Profile.class));
                        overridePendingTransition(0, 0);
                        return true;
                    case R.id.forum_chat:
                        startActivity(new Intent(getApplicationContext(), Forum.class));
                        overridePendingTransition(0, 0);
                        return true;
                    case R.id.chat:
                        startActivity(new Intent(getApplicationContext(), Chat.class));
                        overridePendingTransition(0, 0);
                        return true;
                }
                return false;
            }
        });
    }

    private void callInfo(Call<List<MatchCarrouselInfo>> call) {
        call.enqueue(new Callback<List<MatchCarrouselInfo>>() {
            @Override
            public void onResponse(Call<List<MatchCarrouselInfo>> call, Response<List<MatchCarrouselInfo>> response) {
                List<MatchCarrouselInfo> matchCarrouselInfos = response.body();
                if (matchCarrouselInfos != null) {
                    if (matchCarrouselInfos.size() > 0) {
                        imageRequests = matchCarrouselInfos.size();
                    } else {
                        progressDialog.dismiss();
                    }

                    for (MatchCarrouselInfo mci : matchCarrouselInfos) {
                        String fullName = "";
                        int dist = 0;
                        if (mci.getNombre() != null && mci.getApellido() != null) {
                            fullName = mci.getNombre() + " " + mci.getApellido();
                        } else {
                            fullName = mci.getUsername();
                        }

                        MatchModel modelo = null;
                        try {
                            modelo = new MatchModel(mci.getId(), fullName);
                            modelo.setImage(BitmapFactory.decodeResource(getResources(), R.drawable.no_image));
                            matchModels.add(modelo);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        String saveLocation = mci.getSaveLocation();
                        saveLocation = saveLocation.replace("www/", "");
                        String url = "http://" + getString(R.string.IP) + ":80/" + saveLocation;
                        getMatchImage(url, modelo);
                    }
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<List<MatchCarrouselInfo>> call, Throwable t) {
                t.printStackTrace();
            }
        });

    }

    private void getMatchImage(final String url, final MatchModel model){
        ImageRequest imageRequest = new ImageRequest(url, new com.android.volley.Response.Listener<Bitmap>() {
            @Override
            public void onResponse(Bitmap response) {
                for (int i = 0; i < matchModels.size(); i++) {
                    int id_cm = matchModels.get(i).getId();
                    if (adminSession.getInt("USER_ID", 0) == model.getId()) {
                        userIm = response;
                        //BMHelper.getInstance().getBitmap().add(0, userIm);
                        break;
                    }
                    if (id_cm == model.getId()) {
                        Log.e("ID = ", String.valueOf(model.getId() + ", " + url));
                        matchModels.get(i).setImage(response);
                        adapter.notifyDataSetChanged();
                        break;
                    }
                }
                if (imageRequests > 0)
                    imageCounter++;
                if (imageCounter == imageRequests) {
                    if (progressDialog.isShowing()) {
                        progressDialog.dismiss();
                    }
                }
            }
        }, 0, 0, ImageView.ScaleType.CENTER_CROP, null,
                new com.android.volley.Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                    }
                });
        Volley.newRequestQueue(GamesAndActivities.this).add(imageRequest);
    }

}