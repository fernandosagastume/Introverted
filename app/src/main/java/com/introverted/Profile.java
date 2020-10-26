package com.introverted;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.webkit.ServiceWorkerClient;
import android.widget.GridLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Period;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class Profile extends AppCompatActivity {
    BottomNavigationView my_nav;
    ImageButton logout, selecCat;
    CardView variable;
    TextView nameprof, categoryProf, catText;
    GridLayout grid_prof;
    CircleImageView profIm;
    ImageButton musicButton, moviesButton, foodButton, hobbiesButton, editProf;
    private SharedPreferences adminSession;
    private SharedPreferences matchPreferences;
    String ip;
    ProgressBar progressBar;
    ProgressDialog progressDialog;
    SwipeRefreshLayout swipeRefreshLayout;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        ip = getString(R.string.IP);
        setTheme(R.style.DashboardTheme);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        progressDialog = new ProgressDialog(Profile.this);
        //progressBar = findViewById(R.id.progressFetchingData);
        adminSession = getSharedPreferences("LOGIN_PREF", MODE_PRIVATE);
        matchPreferences = getSharedPreferences("MATCH_PREFERENCES", MODE_PRIVATE);
        logout = findViewById(R.id.logout_button);
        variable = findViewById(R.id.cardview_variable);
        my_nav = findViewById(R.id.bottomMenu);
        nameprof = findViewById(R.id.name_prof);
        categoryProf = findViewById(R.id.categoria_prof);
        grid_prof = findViewById(R.id.grid_prof);
        profIm = findViewById(R.id.profIm);
        //Image button y text view para mostrar pregunta por categoria
        selecCat = findViewById(R.id.categorySelect_profile);
        catText = findViewById(R.id.catselec_text);

        getMYSQLData("user");
        getMYSQLData("categoria");
        String category = adminSession.getString("PERSON_TYPE_PREF", "null");
        if(!category.equals("null")){
            category = category.substring(0,1)+ category.substring(1).toLowerCase();
            categoryProf.setText(category);
        }

        switch(category){
            //Caso melómano
            case "Melómano":
                catText.setText("Actividades");
                selecCat.setImageResource(R.drawable.ic_concert_profile);
                break;
            //Caso gamer
            case "Gamer":
                catText.setText("Videojuegos");
                selecCat.setImageResource(R.drawable.ic_vg_profile);
                break;
            //Caso bibliófilo
            case "Bibliófilo":
                catText.setText("Libros");
                selecCat.setImageResource(R.drawable.ic_libro_profile);
                break;
            //Caso artista
            case "Artista":
                catText.setText("Arte");
                selecCat.setImageResource(R.drawable.ic_artista_profile);
                break;
            default:
                break;
        }

        musicButton = findViewById(R.id.musicaFav_profile);
        moviesButton = findViewById(R.id.movies_profile);
        foodButton = findViewById(R.id.food_profile);
        hobbiesButton = findViewById(R.id.intereses_profile);
        editProf = findViewById(R.id.edit_profile);

        musicButton.setOnClickListener(view -> getMYSQLData("music"));

        moviesButton.setOnClickListener(view -> getMYSQLData("movies"));

        foodButton.setOnClickListener(view -> getMYSQLData("food"));

        hobbiesButton.setOnClickListener(view -> getMYSQLData("hobbies"));

        selecCat.setOnClickListener(view -> getMYSQLData("categoria_personal"));

        editProf.setOnClickListener(view -> startActivity(new Intent(Profile.this, EditProfile.class)));

        //my_nav.getMenu().findItem(R.id.match_window).setChecked(true);
        my_nav.setSelectedItemId(R.id.profile);
        my_nav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch(item.getItemId()){
                    case  R.id.profile:
                        return true;
                    case R.id.match_window:
                        startActivity(new Intent(getApplicationContext(), Dashboard.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.activity_window:
                        startActivity(new Intent(getApplicationContext(), GamesAndActivities.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.forum_chat:
                        startActivity(new Intent(getApplicationContext(), ChatAndForum.class));
                        overridePendingTransition(0,0);
                        return true;
                }
                return false;
            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(Profile.this);
                builder.setTitle("Cerrar sesión").
                        setMessage("¿Estás seguro que quieres cerrar sesión?");
                builder.setPositiveButton("Si",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                logoutSession();
                                Intent in = new Intent(Profile.this, Login.class);
                                in.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(in);
                            }
                        });
                builder.setNegativeButton("No",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });
                AlertDialog alertLogout = builder.create();
                alertLogout.show();
            }
        });
    }
    private void logoutSession(){
        matchPreferences.edit().clear().apply();
        adminSession.edit().clear().apply();
    }

    private void getMYSQLData(final String data){
        String url = null;
        if(data.equals("user")) {
            url = "http://"+ ip +":80/WebService-Introverted/getProfileData.php";
        }
        else {
            url = "http://"+ ip +":80/WebService-Introverted/getCardsData.php";
        }

        StringRequest request = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onResponse(String response) {
                try{
                    if(data.equals("user")) {
                        if (!response.equals("0")) {
                            JSONArray userData = new JSONArray(response);
                            for (int i = 0; i < userData.length(); i++) {
                                JSONObject jobj = userData.getJSONObject(i);
                                if(i == 0) {
                                    String user = jobj.getString("username");
                                    //Se llama a la función calcular edad a base del cumpleaños de la persona
                                    String age = calculateAge(jobj.getString("bday"));
                                    if (!jobj.isNull("nombre") && !jobj.isNull("apellido")) {
                                        String nombre = jobj.getString("nombre");
                                        String apellido = jobj.getString("apellido");
                                        nameprof.setText(nombre + " " + apellido + ", " + age);
                                    } else {
                                        nameprof.setText(user + ", " + age);
                                    }
                                    if (jobj.getString("sexo").toLowerCase().equals("femenino")) {
                                        profIm.setImageResource(R.drawable.ic_woman_user);
                                    }
                                }
                                else{
                                    if(!jobj.isNull("saveLocation")) {
                                        String location = jobj.get("saveLocation").toString();
                                        location = location.replace("www/", "");
                                        String url1 = "http://" + ip + ":80/" + location;
                                        ImageRequest imageRequest = new ImageRequest(url1, new Response.Listener<Bitmap>() {
                                            @Override
                                            public void onResponse(Bitmap response) {
                                                profIm.setImageBitmap(response);
                                            }
                                        }, 0, 0, ImageView.ScaleType.CENTER_CROP, null,
                                                new Response.ErrorListener() {
                                            @Override
                                            public void onErrorResponse(VolleyError error) {
                                                Toast.makeText(Profile.this, "Algo salió mal: " + error,
                                                        Toast.LENGTH_LONG).show();
                                            }
                                        });
                                        Volley.newRequestQueue(Profile.this).add(imageRequest);

                                    }
                                }//else

                            }
                        } else {
                            Toast.makeText(Profile.this, "Error recuperando datos",
                                    Toast.LENGTH_LONG).show();
                        }
                    }
                    else if (data.equals("music")){
                        if(!response.equals("0")){
                            Intent in = new Intent(Profile.this, EditMusicGenres.class);
                            in.putExtra("MUSIC_GENRES", response);
                            startActivity(in);
                        }
                        else {
                            Toast.makeText(Profile.this, "Error recuperando datos",
                                    Toast.LENGTH_LONG).show();
                        }
                    }
                    else if(data.equals("movies")){
                        if(!response.equals("0")){
                            Intent in = new Intent(Profile.this, EditMovieGenres.class);
                            in.putExtra("MOVIE_GENRES", response);
                            startActivity(in);
                        }
                        else {
                            Toast.makeText(Profile.this, "Error recuperando datos",
                                    Toast.LENGTH_LONG).show();
                        }
                    }
                    else if(data.equals("hobbies")){
                        if(!response.equals("0")){
                            Intent in = new Intent(Profile.this, EditHobbies.class);
                            in.putExtra("HOBBIES", response);
                            startActivity(in);
                        }
                        else {
                            Toast.makeText(Profile.this, "Error recuperando datos",
                                    Toast.LENGTH_LONG).show();
                        }
                    }
                    else if(data.equals("food")){
                        if(!response.equals("0")){
                            Intent in = new Intent(Profile.this, EditFoodType.class);
                            in.putExtra("FOOD", response);
                            startActivity(in);
                        }
                        else {
                            Toast.makeText(Profile.this, "Error recuperando datos",
                                    Toast.LENGTH_LONG).show();
                        }
                    }

                    else if(data.equals("categoria_personal")){
                        if(!response.equals("0")){
                            Intent in = new Intent(Profile.this, EditPersonalCategory.class);
                            in.putExtra("PERSONAL", response);
                            startActivity(in);
                        }
                        else {
                            Toast.makeText(Profile.this, "Error recuperando datos",
                                    Toast.LENGTH_LONG).show();
                        }
                    }

                }catch(JSONException | ParseException js){
                    js.printStackTrace();
                }
                progressDialog.dismiss();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(Profile.this, error.toString(),
                        Toast.LENGTH_LONG).show();
                progressDialog.dismiss();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                int ID = adminSession.getInt("USER_ID", 0);
                params.put("id_us", String.valueOf(ID));
                params.put("getList", data);
                return params;
            }
        };
        Volley.newRequestQueue(Profile.this).add(request);
        progressDialog.setMessage("Cargando...");
        progressDialog.show();
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
        return ""+diff1.getYears();
    }
}