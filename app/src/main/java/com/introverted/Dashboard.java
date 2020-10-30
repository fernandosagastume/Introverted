package com.introverted;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DefaultItemAnimator;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Shader;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.LinearInterpolator;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.yuyakaido.android.cardstackview.CardStackLayoutManager;
import com.yuyakaido.android.cardstackview.CardStackListener;
import com.yuyakaido.android.cardstackview.CardStackView;
import com.yuyakaido.android.cardstackview.Direction;
import com.yuyakaido.android.cardstackview.Duration;
import com.yuyakaido.android.cardstackview.StackFrom;
import com.yuyakaido.android.cardstackview.SwipeAnimationSetting;
import com.yuyakaido.android.cardstackview.SwipeableMethod;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;

public class Dashboard extends AppCompatActivity implements LocationListener, MatchPreferencesDialog.MatchPreferenceDialogListener {

    private SharedPreferences adminSession, matchPreferences, rejectedPreferences;
    private FusedLocationProviderClient cliente;
    private CardStackLayoutManager manager;
    private CardStackAdapter adapter;

    Call<List<SwipeCardInfo>> call;
    BottomNavigationView my_nav;
    LocationManager locationManager;
    ProgressDialog progressDialog, progressDialogPreferences;
    SQLITEHelper sqliteHelper;
    TextView title;
    CardStackView view;
    String ip;
    List<cardModel> cardModels;
    TextView nomatch;
    int contador;
    int imageCounter;
    int imageRequests;
    Direction lastDirection;
    Bitmap matchIm;
    Bitmap userIm;
    String userFN;
    String matchFN;

    @RequiresApi(api = Build.VERSION_CODES.R)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        ip = getString(R.string.IP);
        adminSession = getSharedPreferences("LOGIN_PREF", MODE_PRIVATE);
        matchPreferences = getSharedPreferences("MATCH_PREFERENCES", MODE_PRIVATE);
        rejectedPreferences = getSharedPreferences("REJECTED_PEOPLE", MODE_PRIVATE);
        progressDialog = new ProgressDialog(this);
        progressDialogPreferences = new ProgressDialog(this);
        sqliteHelper = new SQLITEHelper(this);
        Intent intent = getIntent();

        findViewById(R.id.likeButton).setVisibility(View.GONE);
        findViewById(R.id.dislikeButton).setVisibility(View.GONE);
        findViewById(R.id.rewind).setVisibility(View.GONE);
        nomatch = findViewById(R.id.nomatches);
        nomatch.setText("No hay más personas para mostrar, por favor cambia tus preferencias");
        contador = 0;
        PPHelper.getInstanceNO().setConter(contador);
        imageCounter = 0;
        imageRequests = 0;
        title = findViewById(R.id.introvertedDashboard);
        Shader textShader = new LinearGradient(0, 0, 60, 140,
                new int[]{Color.parseColor("#39C34A"), Color.parseColor("#1D5480")},
                new float[]{0, 1}, Shader.TileMode.CLAMP);
        title.getPaint().setShader(textShader);

        cardModels = new ArrayList<>();
        view = findViewById(R.id.cardStackView);
        try {
            //progressDialog.dismiss();
            manager = new CardStackLayoutManager(Dashboard.this, new CardStackListener() {

                @Override
                public void onCardDragging(Direction direction, float ratio) {
                }

                @Override
                public void onCardSwiped(Direction direction) {
                    if (direction == Direction.Right) {
                        findViewById(R.id.rewind).setVisibility(View.GONE);
                        int ID = adapter.getItems().get(contador).getId();
                        matchIm = adapter.getItems().get(contador).getImage();
                        matchFN = adapter.getItems().get(contador).getName();
                        BMHelper.getInstance().setBitmap(1, matchIm);
                        insertInterested(ID);
                        lastDirection = Direction.Right;
                        contador++;
                    }
                    if (direction == Direction.Top) {
                        if (findViewById(R.id.rewind).getVisibility() == View.GONE)
                            findViewById(R.id.rewind).setVisibility(View.VISIBLE);
                        lastDirection = Direction.Top;
                    }
                    if (direction == Direction.Left) {
                        if (findViewById(R.id.rewind).getVisibility() == View.GONE)
                            findViewById(R.id.rewind).setVisibility(View.VISIBLE);
                        lastDirection = Direction.Left;
                        //SharedPreferences.Editor editor = adminSession.edit();
                        //editor.putInt("MATCH_MIN_AGE", Integer.parseInt(ageMin.getText().toString()));
                        //editor.putInt("MATCH_MAX_AGE", Integer.parseInt(ageMax.getText().toString()));
                        contador++;
                    }
                    if (direction == Direction.Bottom) {
                        if (findViewById(R.id.rewind).getVisibility() == View.GONE)
                            findViewById(R.id.rewind).setVisibility(View.VISIBLE);
                        lastDirection = Direction.Bottom;
                    }

                    // Paginating
                   /* if (manager.getTopPosition() == adapter.getItemCount() - 5) {
                        paginate();
                    }*/

                }

                @Override
                public void onCardRewound() {
                }

                @Override
                public void onCardCanceled() {
                }

                @Override
                public void onCardAppeared(View view, int position) {
                    if (findViewById(R.id.settings_matches).getVisibility() == View.GONE)
                        findViewById(R.id.settings_matches).setVisibility(View.VISIBLE);
                    if (findViewById(R.id.likeButton).getVisibility() == View.GONE)
                        findViewById(R.id.likeButton).setVisibility(View.VISIBLE);
                    if (findViewById(R.id.dislikeButton).getVisibility() == View.GONE)
                        findViewById(R.id.dislikeButton).setVisibility(View.VISIBLE);
                    if (view.getVisibility() == View.GONE)
                        view.setVisibility(View.VISIBLE);
                }

                @Override
                public void onCardDisappeared(View view, int position) {
                    try {
                        findViewById(R.id.likeButton).setVisibility(View.GONE);
                        findViewById(R.id.dislikeButton).setVisibility(View.GONE);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

            });
            manager.setStackFrom(StackFrom.None);
            manager.setVisibleCount(3);
            manager.setTranslationInterval(8.0f);
            manager.setScaleInterval(0.95f);
            manager.setSwipeThreshold(0.3f);
            manager.setMaxDegree(50.0f);
            manager.setDirections(Direction.FREEDOM);
            manager.setCanScrollVertical(false);
            manager.setCanScrollHorizontal(true);
            manager.setSwipeableMethod(SwipeableMethod.AutomaticAndManual);
            manager.setOverlayInterpolator(new LinearInterpolator());
            view.setLayoutManager(manager);
            adapter = new CardStackAdapter(cardModels, Dashboard.this);
            view.setAdapter(adapter);
            view.setItemAnimator(new DefaultItemAnimator());

        } catch (Exception e) {
            e.printStackTrace();
        }

        findViewById(R.id.likeButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (findViewById(R.id.rewind).getVisibility() == View.GONE)
                    findViewById(R.id.rewind).setVisibility(View.VISIBLE);
                //manager.setSwipeableMethod(SwipeableMethod.AutomaticAndManual);
                SwipeAnimationSetting setting = new SwipeAnimationSetting.Builder()
                        .setDirection(Direction.Right)
                        .setDuration(Duration.Slow.duration)
                        .setInterpolator(new AccelerateInterpolator())
                        .build();
                manager.setSwipeAnimationSetting(setting);
                view.swipe();
                if (contador == (cardModels.size() - 1)) {
                    findViewById(R.id.likeButton).setVisibility(View.GONE);
                    findViewById(R.id.dislikeButton).setVisibility(View.GONE);
                }
            }
        });

        findViewById(R.id.dislikeButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (findViewById(R.id.rewind).getVisibility() == View.GONE)
                    findViewById(R.id.rewind).setVisibility(View.VISIBLE);
                //manager.setSwipeableMethod(SwipeableMethod.AutomaticAndManual);
                SwipeAnimationSetting setting = new SwipeAnimationSetting.Builder()
                        .setDirection(Direction.Left)
                        .setDuration(Duration.Slow.duration)
                        .setInterpolator(new DecelerateInterpolator())
                        .build();
                manager.setSwipeAnimationSetting(setting);
                view.swipe();
                if (contador == (cardModels.size() - 1)) {
                    findViewById(R.id.likeButton).setVisibility(View.GONE);
                    findViewById(R.id.dislikeButton).setVisibility(View.GONE);
                }
            }
        });

        findViewById(R.id.rewind).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //manager.setSwipeableMethod(SwipeableMethod.AutomaticAndManual);
                SwipeAnimationSetting setting = new SwipeAnimationSetting.Builder()
                        .setDirection(Direction.Right)
                        .setDuration(Duration.Slow.duration)
                        .setInterpolator(new DecelerateInterpolator())
                        .build();
                manager.setSwipeAnimationSetting(setting);
                view.rewind();
                contador--;
            }
        });

        getLocation();

        Retrofit retrofit = new Retrofit.Builder().baseUrl(Api.url).addConverterFactory(GsonConverterFactory.create())
                .build();
        Api api = retrofit.create(Api.class);
        call = api.getSwipeInfo(adminSession.getInt("USER_ID", 0), "userQuery");

        if (ContextCompat.checkSelfPermission(Dashboard.this, Manifest.permission.ACCESS_FINE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(Dashboard.this, new String[]{ACCESS_FINE_LOCATION}, 100);
            progressDialog = new ProgressDialog(Dashboard.this);
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
        }
        else{
            progressDialog = new ProgressDialog(Dashboard.this);
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
            callInfo(call, "null");
        }

        setTheme(R.style.DashboardTheme);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);

        my_nav = findViewById(R.id.bottomMenu);
        //my_nav.getMenu().findItem(R.id.match_window).setChecked(true);
        my_nav.setSelectedItemId(R.id.match_window);
        my_nav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
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
                        startActivity(new Intent(getApplicationContext(), Chat.class));
                        overridePendingTransition(0, 0);
                        return true;
                }
                return false;
            }
        });

        ImageButton settings = findViewById(R.id.settings_matches);
        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openMatchPreferences();
            }
        });

    }

    @Override
    public void applyChanges() {
        Call<List<SwipeCardInfo>> callRefresh;
        Retrofit retrofit = new Retrofit.Builder().baseUrl(Api.url).addConverterFactory(GsonConverterFactory.create())
                .build();
        Api api = retrofit.create(Api.class);
        callRefresh = api.getSwipeInfo(adminSession.getInt("USER_ID", 0), "userQuery");
        progressDialogPreferences = new ProgressDialog(Dashboard.this);
        progressDialogPreferences.setMessage("Cargando...");
        progressDialogPreferences.setCancelable(false);
        progressDialogPreferences.setCanceledOnTouchOutside(false);
        progressDialogPreferences.show();

        //Handler para manejar cuando el progress dialog se cierra porque no hubo response
        Runnable progressRunnable = new Runnable() {
            @Override
            public void run() {
                progressDialogPreferences.cancel();
            }
        };
        Handler pdCanceller = new Handler();
        pdCanceller.postDelayed(progressRunnable, 10000);
        callInfo(call, "clone");
    }

    private void openMatchPreferences() {
        MatchPreferencesDialog preferencesDialog = new MatchPreferencesDialog();
        preferencesDialog.show(getSupportFragmentManager(), "Settings de matches");
    }

    private void callInfo(Call<List<SwipeCardInfo>> call, String clone) {
        int min_age  = matchPreferences.getInt("MATCH_MIN_AGE", 0);
        int max_age  = matchPreferences.getInt("MATCH_MAX_AGE", 0);
        int max_dist  = matchPreferences.getInt("MATCH_MAX_DIST", 0);
        String gender_preference  = matchPreferences.getString("MATCH_GENDER_PREFERENCE", "null");
        if(clone.equals("clone")){
            cardModels.clear();
            adapter.notifyDataSetChanged();
            call.clone().enqueue(new Callback<List<SwipeCardInfo>>() {
                @RequiresApi(api = Build.VERSION_CODES.O)
                @Override
                public void onResponse(Call<List<SwipeCardInfo>> call, retrofit2.Response<List<SwipeCardInfo>> response) {
                    List<SwipeCardInfo> swipeCardInfos = response.body();
                    Double userLon = 0.0;
                    Double userLat = 0.0;
                    for (SwipeCardInfo sci : swipeCardInfos) {
                        if (sci.getId() == adminSession.getInt("USER_ID", 0)) {
                            if (!(sci.getLatitude() == null) && !(sci.getLongitude() == null)) {
                                userLon = sci.getLongitude();
                                userLat = sci.getLatitude();
                            }
                            break;
                        }
                    }

                    Geocoder geocoder;
                    List<Address> addresses;
                    int conter = 0;
                    if (swipeCardInfos.size() > 1) {
                        imageRequests = swipeCardInfos.size();
                        findViewById(R.id.likeButton).setVisibility(View.VISIBLE);
                        findViewById(R.id.dislikeButton).setVisibility(View.VISIBLE);
                    } else {
                        findViewById(R.id.likeButton).setVisibility(View.GONE);
                        findViewById(R.id.dislikeButton).setVisibility(View.GONE);
                        progressDialog.dismiss();
                    }
                    for (SwipeCardInfo sci : swipeCardInfos) {
                        if (!(sci.getId() == adminSession.getInt("USER_ID", 0))) {
                            String fullName = "";
                            String ciudad = "";
                            String distance = "";
                            int dist = 0;
                            if (sci.getNombre() != null && sci.getApellido() != null) {
                                fullName = sci.getNombre() + " " + sci.getApellido();
                            } else {
                                fullName = sci.getUsername();
                            }
                            if (sci.getLatitude() == null && sci.getLongitude() == null) {
                                ciudad = "";
                            } else {
                                geocoder = new Geocoder(Dashboard.this, Locale.getDefault());
                                try {
                                    addresses = geocoder.getFromLocation(sci.getLatitude(), sci.getLongitude(), 1);
                                    ciudad = addresses.get(0).getLocality() + ", " + addresses.get(0).getCountryName();
                                    dist = (int) distance(userLat, userLon, sci.getLatitude(), sci.getLongitude());
                                    if (dist == 0)
                                        distance = "A menos de un kilómetro";
                                    else
                                        distance = String.valueOf(dist) + " KM";
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                            //Verificaciones en cuanto a las preferencias del usuario
                            if (max_age > 0 && min_age > 0) {
                                int bday = 0;
                                try {
                                    bday = Integer.parseInt(calculateAge(sci.getBday()));
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }
                                if (bday < min_age || bday > max_age) {
                                    continue;
                                }
                            }
                            if (max_dist > 0) {
                                if (max_dist != 201) {
                                    if (dist > max_dist) {
                                        continue;
                                    }
                                }
                            }
                            if (!(gender_preference.equals("null"))) {
                                if (!(gender_preference.equals("Ambos"))) {
                                    if (!(gender_preference.equals(sci.getSexo()))) {
                                        continue;
                                    }
                                }
                            }

                            cardModel modelo = null;
                            try {
                                modelo = new cardModel(sci.getId(), fullName, calculateAge(sci.getBday()), ciudad);
                                modelo.setImage(BitmapFactory.decodeResource(getResources(), R.drawable.no_image));
                                modelo.setDistance(distance);
                                cardModels.add(modelo);
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }

                            RequestPI requestPI = (RequestPI) new RequestPI(sci.getId(), modelo, "POST_PROF_PIC").execute();
                        } else {
                            String fullName = "";
                            String ciudad = "";
                            String distance = "";
                            if (sci.getNombre() != null && sci.getApellido() != null) {
                                fullName = sci.getNombre() + " " + sci.getApellido();
                            } else {
                                fullName = sci.getUsername();
                            }
                            if (sci.getLatitude() == null && sci.getLongitude() == null) {
                                ciudad = "";
                            } else {
                                geocoder = new Geocoder(Dashboard.this, Locale.getDefault());
                                try {
                                    addresses = geocoder.getFromLocation(sci.getLatitude(), sci.getLongitude(), 1);
                                    ciudad = addresses.get(0).getLocality() + ", " + addresses.get(0).getCountryName();
                                    int dist = (int) distance(userLat, userLon, sci.getLatitude(), sci.getLongitude());
                                    if (dist == 0)
                                        distance = "A menos de un kilómetro";
                                    else
                                        distance = String.valueOf(dist) + " KM";
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                            cardModel modelo = null;
                            try {
                                modelo = new cardModel(sci.getId(), fullName, calculateAge(sci.getBday()), ciudad);
                                modelo.setImage(BitmapFactory.decodeResource(getResources(), R.drawable.no_image));
                                modelo.setDistance(distance);
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                            userFN = fullName;
                            userIm = BitmapFactory.decodeResource(getResources(), R.drawable.no_image);
                            BMHelper.getInstance().setBitmap(0, userIm);
                            RequestPI requestPI = (RequestPI) new RequestPI(sci.getId(), modelo, "POST_PROF_PIC").execute();
                            PPHelper.getInstance().setName(fullName);
                            PPHelper.getInstance().setCity(ciudad);
                            PPHelper.getInstance().setDistance(distance);
                            PPHelper.getInstance().setSex(sci.getSexo());
                        }
                        conter++;
                    }
                    adapter.notifyDataSetChanged();
                }

                @Override
                public void onFailure(Call<List<SwipeCardInfo>> call, Throwable t) {
                    t.printStackTrace();
                }
            });
        }
        else {
            call.enqueue(new Callback<List<SwipeCardInfo>>() {
                @RequiresApi(api = Build.VERSION_CODES.O)
                @Override
                public void onResponse(Call<List<SwipeCardInfo>> call, retrofit2.Response<List<SwipeCardInfo>> response) {
                    List<SwipeCardInfo> swipeCardInfos = response.body();
                    if (swipeCardInfos != null) {
                        Double userLon = 0.0;
                        Double userLat = 0.0;
                        for (SwipeCardInfo sci : swipeCardInfos) {
                            if (sci.getId() == adminSession.getInt("USER_ID", 0)) {
                                if (!(sci.getLatitude() == null) && !(sci.getLongitude() == null)) {
                                    userLon = sci.getLongitude();
                                    userLat = sci.getLatitude();
                                }
                                break;
                            }
                        }

                        Geocoder geocoder;
                        List<Address> addresses;
                        if (swipeCardInfos.size() > 1) {
                            imageRequests = swipeCardInfos.size();
                            findViewById(R.id.likeButton).setVisibility(View.VISIBLE);
                            findViewById(R.id.dislikeButton).setVisibility(View.VISIBLE);
                        } else {
                            findViewById(R.id.likeButton).setVisibility(View.GONE);
                            findViewById(R.id.dislikeButton).setVisibility(View.GONE);
                            progressDialog.dismiss();
                        }
                        for (SwipeCardInfo sci : swipeCardInfos) {
                            if (!(sci.getId() == adminSession.getInt("USER_ID", 0))) {
                                String fullName = "";
                                String ciudad = "";
                                String distance = "";
                                int dist = 0;
                                if (sci.getNombre() != null && sci.getApellido() != null) {
                                    fullName = sci.getNombre() + " " + sci.getApellido();
                                } else {
                                    fullName = sci.getUsername();
                                }
                                if (sci.getLatitude() == null && sci.getLongitude() == null) {
                                    ciudad = "";
                                } else {
                                    geocoder = new Geocoder(Dashboard.this, Locale.getDefault());
                                    try {
                                        addresses = geocoder.getFromLocation(sci.getLatitude(), sci.getLongitude(), 1);
                                        ciudad = addresses.get(0).getLocality() + ", " + addresses.get(0).getCountryName();
                                        dist = (int) distance(userLat, userLon, sci.getLatitude(), sci.getLongitude());
                                        if (dist == 0)
                                            distance = "A menos de un kilómetro";
                                        else
                                            distance = String.valueOf(dist) + " KM";
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                }
                                //Verificaciones en cuanto a las preferencias del usuario
                                if (max_age > 0 && min_age > 0) {
                                    int bday = 0;
                                    try {
                                        bday = Integer.parseInt(calculateAge(sci.getBday()));
                                    } catch (ParseException e) {
                                        e.printStackTrace();
                                    }
                                    if (bday < min_age || bday > max_age) {
                                        continue;
                                    }
                                }
                                if (max_dist > 0) {
                                    if (max_dist != 201) {
                                        if (dist > max_dist) {
                                            continue;
                                        }
                                    }
                                }
                                if (!(gender_preference.equals("null"))) {
                                    if (!(gender_preference.equals("Ambos"))) {
                                        if (!(gender_preference.equals(sci.getSexo()))) {
                                            continue;
                                        }
                                    }
                                }

                                cardModel modelo = null;
                                try {
                                    modelo = new cardModel(sci.getId(), fullName, calculateAge(sci.getBday()), ciudad);
                                    modelo.setImage(BitmapFactory.decodeResource(getResources(), R.drawable.no_image));
                                    modelo.setDistance(distance);
                                    cardModels.add(modelo);
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }

                                RequestPI requestPI = (RequestPI) new RequestPI(sci.getId(), modelo, "POST_PROF_PIC").execute();
                            } else {
                                String fullName = "";
                                String ciudad = "";
                                String distance = "";
                                if (sci.getNombre() != null && sci.getApellido() != null) {
                                    fullName = sci.getNombre() + " " + sci.getApellido();
                                } else {
                                    fullName = sci.getUsername();
                                }
                                if (sci.getLatitude() == null && sci.getLongitude() == null) {
                                    ciudad = "";
                                } else {
                                    geocoder = new Geocoder(Dashboard.this, Locale.getDefault());
                                    try {
                                        addresses = geocoder.getFromLocation(sci.getLatitude(), sci.getLongitude(), 1);
                                        ciudad = addresses.get(0).getLocality() + ", " + addresses.get(0).getCountryName();
                                        int dist = (int) distance(userLat, userLon, sci.getLatitude(), sci.getLongitude());
                                        if (dist == 0)
                                            distance = "A menos de un kilómetro";
                                        else
                                            distance = String.valueOf(dist) + " KM";
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                }
                                cardModel modelo = null;
                                try {
                                    modelo = new cardModel(sci.getId(), fullName, calculateAge(sci.getBday()), ciudad);
                                    modelo.setImage(BitmapFactory.decodeResource(getResources(), R.drawable.no_image));
                                    modelo.setDistance(distance);
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }
                                userFN = fullName;
                                userIm = BitmapFactory.decodeResource(getResources(), R.drawable.no_image);
                                BMHelper.getInstance().setBitmap(0, userIm);
                                RequestPI requestPI = (RequestPI) new RequestPI(sci.getId(), modelo, "POST_PROF_PIC").execute();

                            }
                        }
                        adapter.notifyDataSetChanged();
                    }
                }

                    @Override
                    public void onFailure (Call < List < SwipeCardInfo >> call, Throwable t){
                        t.printStackTrace();
                    }
            });
        }
    }

    class RequestPI extends AsyncTask<Void, Void, Void> {
        int id;
        String saveLocation;
        cardModel cardModel;
        String whichRequest;
        List<cardModel> cM;

        public RequestPI(String whichRequest, List<cardModel> cardModels) {
            this.whichRequest = whichRequest;
            this.cM = cardModels;
        }

        public RequestPI(int id, cardModel cardModel, String whichRequest) {
            this.id = id;
            this.cardModel = cardModel;
            this.whichRequest = whichRequest;
        }

        @RequiresApi(api = Build.VERSION_CODES.O)
        @Override
        protected Void doInBackground(Void... voids) {
            if (whichRequest.equals("POST_PROF_PIC")) {
                //Creamos el objeto Retrofit
                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl(Api.url)//Indicamos la url del servicio
                        .addConverterFactory(GsonConverterFactory.create())//Agregue la fábrica del convertidor para la serialización y la deserialización de objetos.
                        .build();//Cree la instancia de Retrofit utilizando los valores configurados.

                Api service = retrofit.create(Api.class);

                //Recuerda que debemos colocar el modo en como obtenemos esa respuesta,en este caso es una lista de objetos
                //pero puede ser simplemente un objeto.
                Call<List<SwipeCardImages>> response = service.getProfilePics(this.id);//indicamos el metodo que deseamos ejecutar
                try {
                    List<SwipeCardImages> swipeCardImagesList = response.execute().body();
                    if (swipeCardImagesList.size() == 0) {
                        if (progressDialog.isShowing()) {
                            progressDialog.dismiss();
                        }
                        if(progressDialogPreferences.isShowing()){
                            progressDialogPreferences.dismiss();
                        }
                    }
                    //Realizamos la peticion sincrona
                    //Recuerda que en el body se encuentra la respuesta,que en este caso es una lista de objetos
                    for (SwipeCardImages scim : swipeCardImagesList)//realizamos un foreach para recorrer la lista
                        if (!(scim == null)) {
                            saveLocation = scim.getSaveLocation();
                            saveLocation = saveLocation.replace("www/", "");
                            String url1 = "http://" + getString(R.string.IP) + ":80/" + saveLocation;
                            final cardModel cm = this.cardModel;
                            ImageRequest imageRequest = new ImageRequest(url1, new Response.Listener<Bitmap>() {
                                @Override
                                public void onResponse(Bitmap response) {
                                    for (int i = 0; i < cardModels.size(); i++) {
                                        int id_cm = cardModels.get(i).getId();
                                        if (adminSession.getInt("USER_ID", 0) == cm.getId()) {
                                            userIm = response;
                                            BMHelper.getInstance().getBitmap().add(0, userIm);
                                            break;
                                        }
                                        if (id_cm == cm.getId()) {
                                            Log.e("ID = ", String.valueOf(cm.getId() + ", " + saveLocation));
                                            cardModels.get(i).setImage(response);
                                            adapter.notifyDataSetChanged();
                                            break;
                                        }
                                    }
                                    if (imageRequests > 1)
                                        imageCounter++;
                                    if (imageCounter == imageRequests) {
                                        if (progressDialog.isShowing()) {
                                            progressDialog.dismiss();
                                        }
                                        if(progressDialogPreferences.isShowing()){
                                            progressDialogPreferences.dismiss();
                                        }
                                    }
                                }
                            }, 0, 0, ImageView.ScaleType.CENTER_CROP, null,
                                    new Response.ErrorListener() {
                                        @Override
                                        public void onErrorResponse(VolleyError error) {
                                            error.printStackTrace();
                                        }
                                    });
                            Volley.newRequestQueue(Dashboard.this).add(imageRequest);
                        }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public String calculateAge(String fecha) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date datee = sdf.parse(fecha);
        Calendar c = Calendar.getInstance();
        c.setTime(datee);
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH) + 1;
        int date = c.get(Calendar.DATE);
        LocalDate l1 = LocalDate.of(year, month, date);
        LocalDate now1 = LocalDate.now();
        Period diff1 = Period.between(l1, now1);
        return "" + diff1.getYears();
    }

    private double distance(double lat1, double lon1, double lat2, double lon2) {
        double theta = lon1 - lon2;
        double dist = Math.sin(deg2rad(lat1))
                * Math.sin(deg2rad(lat2))
                + Math.cos(deg2rad(lat1))
                * Math.cos(deg2rad(lat2))
                * Math.cos(deg2rad(theta));
        dist = Math.acos(dist);
        dist = rad2deg(dist);
        dist = dist * 60 * 1.1515;
        return (dist);
    }

    private double deg2rad(double deg) {
        return (deg * Math.PI / 180.0);
    }

    private double rad2deg(double rad) {
        return (rad * 180.0 / Math.PI);
    }

    /*private void paginate() {
        List<cardModel> old = adapter.getItems();
        List<cardModel> nuevo = new ArrayList<>(addList());
        CardStackCallback callback = new CardStackCallback(old, nuevo);
        DiffUtil.DiffResult hasil = DiffUtil.calculateDiff(callback);
        adapter.setItems(nuevo);
        hasil.dispatchUpdatesTo(adapter);
    }*/

    @SuppressLint("MissingPermission")
    private void getLocation() {
        try {
            locationManager = (LocationManager) getApplicationContext().getSystemService(LOCATION_SERVICE);
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 5, Dashboard.this);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void onBackPressed(){
        AlertDialog.Builder builder = new AlertDialog.Builder(Dashboard.this);
        builder.setTitle("Salir").
                setMessage("¿Estás seguro que quieres salir de la aplicación?");
        builder.setPositiveButton("Si",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        finishAffinity();
                        finish();
                    }
                });
        builder.setNegativeButton("No",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alertExit = builder.create();
        alertExit.show();
    }

    @Override
    public void onLocationChanged(@NonNull Location location) {

        try{
            Geocoder geocoder = new Geocoder(Dashboard.this, Locale.getDefault());
            List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
            String address = addresses.get(0).getAddressLine(0);
            updateLonLan(location.getLongitude(), location.getLatitude());
            callInfo(call, "null");
            //welcome.setText(address + ", Latitude: " + location.getLatitude() + ", Longitude: " + location.getLongitude());
            //progressDialog.dismiss();
            //Toast.makeText(Dashboard.this, "" + addresses.get(0).getCountryName() + ", " +
              //      addresses.get(0).getLocality() + ", " + addresses.get(0).getFeatureName(), Toast.LENGTH_LONG).show();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(@NonNull String provider) {

    }

    @Override
    public void onProviderDisabled(@NonNull String provider) {

    }

    public void insertInterested(final int id_interesado){
        String url = "http://"+ip+":80/WebService-Introverted/insertInteresados.php";
        StringRequest request = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if(response.contains("1")) {
                            Log.i("INSERT_INTERESTED: ", "La operación fue un éxito, msg: " + response);
                        }
                        else if(response.contains("0")) {
                            Log.i("INSERT_INTERESTED: ", "No se pudo concretar la operación, msg: " + response);
                        }
                        else {
                            Log.i("INSERT_INTERESTED: ", "Hubo match, dos interesados coincidieron, msg: " + response);
                            Intent in = new Intent(Dashboard.this, MatchDisplay.class);
                            //in.putExtra("USER_IM", userIm);
                            //in.putExtra("MATCH_IM", matchIm);
                            in.putExtra("USER_FN", userFN);
                            in.putExtra("MATCH_FN", matchFN);
                            if(BMHelper.getInstance().getBitmap() != null)
                                startActivity(in);
                            else
                                Toast.makeText(Dashboard.this, "Hubo un error", Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<>();
                int ID = adminSession.getInt("USER_ID", 0);
                params.put("id_us", String.valueOf(ID));
                params.put("id_interested", String.valueOf(id_interesado));
                return params;
            }
        };
        Volley.newRequestQueue(Dashboard.this).add(request);
    }

    public void updateLonLan(final Double lon, final Double lat){
        String url = "http://"+ip+":80/WebService-Introverted/updateLonLat.php";
        StringRequest request = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if(response.contains("1")) {
                            Log.i("UPDATE_LONLAT: ", "La operación fue un éxito");
                        }
                        else{
                            Log.i("UPDATE_LONLAT: ", "No se pudo concretar la operación");
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<>();
                int ID = adminSession.getInt("USER_ID", 0);
                params.put("id_us", String.valueOf(ID));
                params.put("lon", String.valueOf(lon));
                params.put("lat", String.valueOf(lat));
                return params;
            }
        };
        Volley.newRequestQueue(Dashboard.this).add(request);
    }



}