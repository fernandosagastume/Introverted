package com.introverted;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.introverted.adapters.PostCardAdapter;
import com.miguelcatalan.materialsearchview.MaterialSearchView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Forum extends AppCompatActivity {
    BottomNavigationView my_nav;
    Toolbar toolbar;
    MaterialSearchView materialSearchView;
    RecyclerView cards;
    PostCardAdapter adapter;
    List<PostCardModel> models;
    SharedPreferences adminSession;
    Call<List<PostCardInfo>> call;
    ProgressDialog progressDialog;
    int imageRequests;
    int imageCounter;
    int profilePicRequests;
    int profilePicsCounter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forum);
        init();

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
                    case R.id.chat:
                        startActivity(new Intent(getApplicationContext(), Chat.class));
                        overridePendingTransition(0,0);
                        return true;
                }
                return false;
            }
        });
    }
    private void init(){
        toolbar = findViewById(R.id.toolbarF);
        setSupportActionBar(toolbar);
        toolbar.setTitle("Foro");
        toolbar.setTitleTextColor(Color.WHITE);
        my_nav = findViewById(R.id.bottomMenu);
        materialSearchView = findViewById(R.id.searchView);
        models = new ArrayList<>();
        adapter = new PostCardAdapter(models, this);
        cards = (RecyclerView)findViewById(R.id.postRV);
        cards.setLayoutManager(new LinearLayoutManager(this));
        cards.setItemAnimator(new DefaultItemAnimator());
        cards.setAdapter(adapter);
        adminSession = getSharedPreferences("LOGIN_PREF", MODE_PRIVATE);

        Retrofit retrofit = new Retrofit.Builder().baseUrl(Api.url).addConverterFactory(GsonConverterFactory.create())
                .build();
        Api api = retrofit.create(Api.class);
        call = api.getForumPostedData();

        progressDialog = new ProgressDialog(Forum.this);
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
    }

    private void callInfo(Call<List<PostCardInfo>> call) {
        call.enqueue(new Callback<List<PostCardInfo>>() {
            @Override
            public void onResponse(Call<List<PostCardInfo>> call, Response<List<PostCardInfo>> response) {
                List<PostCardInfo> postCardInfos = response.body();
                if (postCardInfos != null) {
                    if (postCardInfos.size() > 0) {
                        imageRequests = postCardInfos.size();
                        profilePicRequests = postCardInfos.size();
                    } else {
                        progressDialog.dismiss();
                    }

                    for (PostCardInfo pci : postCardInfos) {
                        PostCardModel modelo = null;

                        String time = pci.getTiempo();
                        time = time.replace("/", " ");
                        Date date = null;
                        try {
                            date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(time);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        Date current = new Date();
                        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        String stringDate = formatter.format(current);
                        Date ahora = null;
                        try {
                            ahora = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(stringDate);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }

                        String postTime = "";
                        try {
                            long diff = ahora.getTime() - date.getTime();
                            long mins = diff / (60 * 1000);
                            long horas = diff / (60 * 60 * 1000);
                            long dias = diff / (60 * 60 * 60 * 1000);

                            if(dias > 0){
                                if(dias == 1)
                                    postTime = dias + " día";
                                else
                                    postTime = dias + " días";
                            }
                            else if(horas > 0){
                                if(horas == 1)
                                    postTime = horas + " hora";
                                else
                                    postTime = horas + " horas";
                            }
                            else {
                                if(mins == 1)
                                    postTime = mins + " minuto";
                                else
                                    postTime = mins + " minutos";
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        try {
                            modelo = new PostCardModel(pci.getPostId(), pci.getId(), pci.getLikes(), pci.getDislikes(),
                                    pci.getUsername(), null, "no", pci.getTitulo(), pci.getPublicacion(),
                                    pci.getSubject(), postTime, pci.getCommentCounter(),
                                    BitmapFactory.decodeResource(getResources(), R.drawable.no_image));
                            models.add(modelo);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        String profileImLocation = "";
                        String saveLocation = "";
                        if(pci.getSaveLocation() != null) {
                            saveLocation = pci.getSaveLocation();
                            saveLocation = saveLocation.replace("/wamp64/www/", "");
                            String url = "http://" + getString(R.string.IP) + ":80/" + saveLocation;
                            getPostImage(url, modelo);
                        }else{
                            imageRequests--;
                        }
                        if(pci.getProfilePic() != null) {
                            profileImLocation = pci.getProfilePic();
                            profileImLocation = profileImLocation.replace("www/", "");
                            String url1 = "http://" + getString(R.string.IP) + ":80/" + profileImLocation;
                            getProfilePicture(url1, modelo);
                        }else{
                            profilePicRequests--;
                        }
                    }
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<List<PostCardInfo>> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    private void getProfilePicture(String url1, PostCardModel modelo) {
        ImageRequest imageRequest = new ImageRequest(url1, response -> {
            for (int i = 0; i < models.size(); i++) {
                int id_cm = models.get(i).getId();
                if (id_cm == modelo.getId()) {
                    Log.e("ID = ", String.valueOf(modelo.getId() + ", " + url1));
                    models.get(i).setUserImage(response);
                    int id = models.get(i).getId();
                    for (int j = 0; j < models.size(); j++) {
                        if(models.get(j).getId() == id)
                            models.get(j).setUserImage(response);
                    }
                    adapter.notifyDataSetChanged();
                    break;
                }
            }
            if (profilePicRequests > 0)
                profilePicsCounter++;
            if (profilePicsCounter == profilePicRequests) {
                if (progressDialog.isShowing()) {
                    progressDialog.dismiss();
                }
            }
        }, 0, 0, ImageView.ScaleType.CENTER_CROP, null,
                new com.android.volley.Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                    }
                });
        Volley.newRequestQueue(Forum.this).add(imageRequest);
    }

    private void getPostImage(String url, PostCardModel modelo) {
        ImageRequest imageRequest = new ImageRequest(url, new com.android.volley.Response.Listener<Bitmap>() {
            @Override
            public void onResponse(Bitmap response) {
                for (int i = 0; i < models.size(); i++) {
                    int id_cm = models.get(i).getPostId();
                    if (id_cm == modelo.getPostId()) {
                        Log.e("POST ID = ", String.valueOf(modelo.getPostId() + ", " + url));
                        models.get(i).setImage(response);
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
        Volley.newRequestQueue(Forum.this).add(imageRequest);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.searchbar_menu, menu);
        MenuItem menuItem = menu.findItem(R.id.searchAction);
        materialSearchView.setMenuItem(menuItem);
        return true;
    }

    public void openPostActivity(View view) {
        startActivity(new Intent(this, AddPost.class));
    }
}
