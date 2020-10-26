package com.introverted;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Application;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.Image;
import android.os.Bundle;
import android.util.SparseBooleanArray;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class RegistroGustosPt7 extends AppCompatActivity {
    private String email, user, contraseña, gender, bday, tipo_persona, aficiones;
    private String generos_musica, generos_movies, food_type, persona_bebe, persona_fuma;
    private SharedPreferences adminSession;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro_gustos_pt7);

        Intent intent = getIntent();
        email = intent.getStringExtra("MAIL");
        user = intent.getStringExtra("USER");
        contraseña = intent.getStringExtra("PASSWORD");
        gender = intent.getStringExtra("GENDER");
        bday = intent.getStringExtra("BDAY");
        tipo_persona = intent.getStringExtra("TYPE_PERSON");
        aficiones = intent.getStringExtra("HOBBIE_LIST");
        generos_musica = intent.getStringExtra("GENRES_LIST");
        generos_movies = intent.getStringExtra("MOVIE_GENRES");
        food_type = intent.getStringExtra("FOOD_TYPE");
        persona_bebe = intent.getStringExtra("PERSON_DRINK");
        persona_fuma = intent.getStringExtra("PERSON_SMOKE");

        adminSession = getSharedPreferences("LOGIN_PREF", MODE_PRIVATE);

        TextView typep_txt = findViewById(R.id.typep_txt);
        Button siguientept7 = findViewById(R.id.siguientept7);
        ImageView typep_pic = findViewById(R.id.typep_pic);

        //SE LE LANZA UNA VISTA DIFERENTE DEPENDIENDO DEL TIPO DE PERSONA QUE ELIGIÓ
        if(tipo_persona.toLowerCase().equals("otaku")){

            typep_pic.setImageResource(R.drawable.ic_naruto);
            typep_txt.setText("Por favor selecciona que tipos de anime te interesan más");

            final ListView listView = findViewById(R.id.typep_listv);
            ArrayAdapter<String> lvAdapter = new ArrayAdapter<String>(RegistroGustosPt7.this, R.layout.genres_style,
                    getResources().getStringArray(R.array.anime));
            listView.setAdapter(lvAdapter);
            listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
            final ArrayList<String> listaAnime = new ArrayList<>();

            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int id, long l) {
                    int cnt_lv = listView.getCount();
                    SparseBooleanArray sba = listView.getCheckedItemPositions();
                    for(int i = 0; i < cnt_lv; i++){
                        if(sba.get(i)){
                            if(!listaAnime.contains(listView.getItemAtPosition(i).toString()))
                                listaAnime.add(listView.getItemAtPosition(i).toString());
                        }
                        else listaAnime.remove(listView.getItemAtPosition(i).toString());
                    }
                }
            });

            siguientept7.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(!listaAnime.isEmpty()){
                    Intent in = new Intent(RegistroGustosPt7.this, Dashboard.class);
                    in.putExtra("MAIL", email);
                    in.putExtra("USER", user);
                    in.putExtra("PASSWORD", contraseña);
                    in.putExtra("GENDER", gender);
                    in.putExtra("BDAY", bday);
                    in.putExtra("TYPE_PERSON", tipo_persona);
                    in.putExtra("HOBBIE_LIST", aficiones);
                    in.putExtra("GENRES_LIST", generos_musica);
                    in.putExtra("MOVIE_GENRES", generos_movies);
                    in.putExtra("FOOD_TYPE", food_type);
                    in.putExtra("PERSON_DRINK", persona_bebe);
                    in.putExtra("PERSON_SMOKE", persona_fuma);

                    String anime = "";
                    int cont = 0;
                    Iterator it = listaAnime.iterator();
                    while (it.hasNext()) {
                        if(cont == 0)
                            anime = (String) it.next();
                        else
                            anime += "/" + it.next();
                        cont++;
                    }
                    in.putExtra("ANIME_GENRES", anime);
                    executeService(user,email,contraseña,bday,gender, persona_bebe, persona_fuma, tipo_persona,
                            aficiones, generos_musica, generos_movies, food_type, anime, in);
                    }
                    else{
                        Toast.makeText(RegistroGustosPt7.this, "Seleccione una opción por favor",
                                Toast.LENGTH_LONG).show();
                    }
                }
            });

        }

        else if(tipo_persona.toLowerCase().equals("gamer")){

            typep_pic.setImageResource(R.drawable.ic_palanca_de_mando);
            typep_txt.setText("Por favor selecciona que tipo de videojuegos te interesan más");

            final ListView listView = findViewById(R.id.typep_listv);
            ArrayAdapter<String> lvAdapter = new ArrayAdapter<String>(RegistroGustosPt7.this, R.layout.genres_style,
                    getResources().getStringArray(R.array.videogames_genres));
            listView.setAdapter(lvAdapter);
            listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
            final ArrayList<String> listaVG = new ArrayList<>();

            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int id, long l) {
                    int cnt_lv = listView.getCount();
                    SparseBooleanArray sba = listView.getCheckedItemPositions();
                    for(int i = 0; i < cnt_lv; i++){
                        if(sba.get(i)){
                            if(!listaVG.contains(listView.getItemAtPosition(i).toString()))
                                listaVG.add(listView.getItemAtPosition(i).toString());
                        }
                        else listaVG.remove(listView.getItemAtPosition(i).toString());
                    }
                }
            });

            siguientept7.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(!listaVG.isEmpty()){
                    Intent in = new Intent(RegistroGustosPt7.this, Dashboard.class);
                    in.putExtra("MAIL", email);
                    in.putExtra("USER", user);
                    in.putExtra("PASSWORD", contraseña);
                    in.putExtra("GENDER", gender);
                    in.putExtra("BDAY", bday);
                    in.putExtra("TYPE_PERSON", tipo_persona);
                    in.putExtra("HOBBIE_LIST", aficiones);
                    in.putExtra("GENRES_LIST", generos_musica);
                    in.putExtra("MOVIE_GENRES", generos_movies);
                    in.putExtra("FOOD_TYPE", food_type);
                    in.putExtra("PERSON_DRINK", persona_bebe);
                    in.putExtra("PERSON_SMOKE", persona_fuma);

                    String vg = "";
                    int cont = 0;
                    Iterator it = listaVG.iterator();
                    while (it.hasNext()) {
                        if(cont == 0)
                            vg = (String) it.next();
                        else
                            vg += "/" + it.next();
                        cont++;
                    }
                    in.putExtra("VG_GENRES", vg);
                    executeService(user,email,contraseña,bday,gender, persona_bebe, persona_fuma, tipo_persona,
                            aficiones, generos_musica, generos_movies, food_type, vg, in);
                    }
                    else{
                        Toast.makeText(RegistroGustosPt7.this, "Seleccione una opción por favor",
                                Toast.LENGTH_LONG).show();
                    }
                }
            });

        }

        else if(tipo_persona.toLowerCase().equals("bibliófilo")){

            typep_pic.setImageResource(R.drawable.ic_libro);
            typep_txt.setText("Por favor selecciona que tipo de libros te interesan más");

            final ListView listView = findViewById(R.id.typep_listv);
            ArrayAdapter<String> lvAdapter = new ArrayAdapter<String>(RegistroGustosPt7.this, R.layout.genres_style,
                    getResources().getStringArray(R.array.biblio));
            listView.setAdapter(lvAdapter);
            listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
            final ArrayList<String> listaLibros = new ArrayList<>();

            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int id, long l) {
                    int cnt_lv = listView.getCount();
                    SparseBooleanArray sba = listView.getCheckedItemPositions();
                    for(int i = 0; i < cnt_lv; i++){
                        if(sba.get(i)){
                            if(!listaLibros.contains(listView.getItemAtPosition(i).toString()))
                                listaLibros.add(listView.getItemAtPosition(i).toString());
                        }
                        else listaLibros.remove(listView.getItemAtPosition(i).toString());
                    }
                }
            });

            siguientept7.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(!listaLibros.isEmpty()){
                    Intent in = new Intent(RegistroGustosPt7.this, Dashboard.class);
                    in.putExtra("MAIL", email);
                    in.putExtra("USER", user);
                    in.putExtra("PASSWORD", contraseña);
                    in.putExtra("GENDER", gender);
                    in.putExtra("BDAY", bday);
                    in.putExtra("TYPE_PERSON", tipo_persona);
                    in.putExtra("HOBBIE_LIST", aficiones);
                    in.putExtra("GENRES_LIST", generos_musica);
                    in.putExtra("MOVIE_GENRES", generos_movies);
                    in.putExtra("FOOD_TYPE", food_type);
                    in.putExtra("PERSON_DRINK", persona_bebe);
                    in.putExtra("PERSON_SMOKE", persona_fuma);

                    String libros = "";
                    int cont = 0;
                    Iterator it = listaLibros.iterator();
                    while (it.hasNext()) {
                        if(cont == 0)
                            libros = (String) it.next();
                        else
                            libros += "/" + it.next();
                        cont++;
                    }
                    in.putExtra("BOOK_GENRES", libros);
                    executeService(user,email,contraseña,bday,gender, persona_bebe, persona_fuma, tipo_persona,
                            aficiones, generos_musica, generos_movies, food_type, libros, in);
                }
                else{
                    Toast.makeText(RegistroGustosPt7.this, "Seleccione una opción por favor",
                            Toast.LENGTH_LONG).show();
                }
                }
            });

        }
        else if(tipo_persona.toLowerCase().equals("melómano")){
            typep_pic.setImageResource(R.drawable.ic_concert);
            typep_txt.setText("¿Cúal de las siguientes opciones sería algo que harías en tu tiempo libre?");

            final ListView listView = findViewById(R.id.typep_listv);
            ArrayAdapter<String> lvAdapter = new ArrayAdapter<String>(RegistroGustosPt7.this, R.layout.genres_style,
                    getResources().getStringArray(R.array.music_activities));
            listView.setAdapter(lvAdapter);
            listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
            final ArrayList<String> listaMusicActivities = new ArrayList<>();

            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int id, long l) {
                    int cnt_lv = listView.getCount();
                    SparseBooleanArray sba = listView.getCheckedItemPositions();
                    for(int i = 0; i < cnt_lv; i++){
                        if(sba.get(i)){
                            if(!listaMusicActivities.contains(listView.getItemAtPosition(i).toString()))
                                listaMusicActivities.add(listView.getItemAtPosition(i).toString());
                        }
                        else listaMusicActivities.remove(listView.getItemAtPosition(i).toString());
                    }
                }
            });

            siguientept7.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(!listaMusicActivities.isEmpty()){
                    Intent in = new Intent(RegistroGustosPt7.this, Dashboard.class);
                    in.putExtra("MAIL", email);
                    in.putExtra("USER", user);
                    in.putExtra("PASSWORD", contraseña);
                    in.putExtra("GENDER", gender);
                    in.putExtra("BDAY", bday);
                    in.putExtra("TYPE_PERSON", tipo_persona);
                    in.putExtra("HOBBIE_LIST", aficiones);
                    in.putExtra("GENRES_LIST", generos_musica);
                    in.putExtra("MOVIE_GENRES", generos_movies);
                    in.putExtra("FOOD_TYPE", food_type);
                    in.putExtra("PERSON_DRINK", persona_bebe);
                    in.putExtra("PERSON_SMOKE", persona_fuma);

                    String musicActivities = "";
                    int cont = 0;
                    Iterator it = listaMusicActivities.iterator();
                    while (it.hasNext()) {
                        if(cont == 0)
                            musicActivities = (String) it.next();
                        else
                            musicActivities += "/" + it.next();
                        cont++;
                    }
                    in.putExtra("MUSIC_ACTIVITIES", musicActivities);
                    executeService(user,email,contraseña,bday,gender, persona_bebe, persona_fuma, tipo_persona,
                            aficiones, generos_musica, generos_movies, food_type, musicActivities, in);
                    }
                    else{
                        Toast.makeText(RegistroGustosPt7.this, "Seleccione una opción por favor",
                                Toast.LENGTH_LONG).show();
                    }
                }
            });
        }
        //SI NO ES ESO, ENTONCES ES ARTISTA
        else{

            typep_pic.setImageResource(R.drawable.ic_artista);
            typep_txt.setText("Por favor selecciona que tipos de arte te interesan más");

            final ListView listView = findViewById(R.id.typep_listv);
            ArrayAdapter<String> lvAdapter = new ArrayAdapter<String>(RegistroGustosPt7.this, R.layout.genres_style,
                    getResources().getStringArray(R.array.art_type));
            listView.setAdapter(lvAdapter);
            listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
            final ArrayList<String> listaArte = new ArrayList<>();

            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int id, long l) {
                    int cnt_lv = listView.getCount();
                    SparseBooleanArray sba = listView.getCheckedItemPositions();
                    for(int i = 0; i < cnt_lv; i++){
                        if(sba.get(i)){
                            if(!listaArte.contains(listView.getItemAtPosition(i).toString()))
                                listaArte.add(listView.getItemAtPosition(i).toString());
                        }
                        else listaArte.remove(listView.getItemAtPosition(i).toString());
                    }
                }
            });

            siguientept7.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent in = new Intent(RegistroGustosPt7.this, Dashboard.class);
                    in.putExtra("MAIL", email);
                    in.putExtra("USER", user);
                    in.putExtra("PASSWORD", contraseña);
                    in.putExtra("GENDER", gender);
                    in.putExtra("BDAY", bday);
                    in.putExtra("TYPE_PERSON", tipo_persona);
                    in.putExtra("HOBBIE_LIST", aficiones);
                    in.putExtra("GENRES_LIST", generos_musica);
                    in.putExtra("MOVIE_GENRES", generos_movies);
                    in.putExtra("FOOD_TYPE", food_type);
                    in.putExtra("PERSON_DRINK", persona_bebe);
                    in.putExtra("PERSON_SMOKE", persona_fuma);

                    String art = "";
                    int cont = 0;
                    Iterator it = listaArte.iterator();
                    while (it.hasNext()) {
                        if(cont == 0)
                            art = (String) it.next();
                        else
                            art += "/" + it.next();
                        cont++;
                    }
                    in.putExtra("ART_GENRES", art);
                    executeService(user,email,contraseña,bday,gender, persona_bebe, persona_fuma, tipo_persona,
                                    aficiones, generos_musica, generos_movies, food_type, art, in);
                }
            });

        }

    }


    private void executeService (final String p1, final String p2, final String p3, final String p4, final String p5,
                                 final String p6, final String p7, final String p8, final String p9, final String p10,
                                         final String p11, final String p12, final String p13, final Intent in){
        StringRequest sr = new StringRequest(Request.Method.POST, "http://192.168.0.6/WebService-Introverted/insert-user.php",
                response -> {
                    if (!response.equals("0")) {
                        SharedPreferences.Editor editor = adminSession.edit();
                        editor.putInt("USER_ID", Integer.parseInt(response));
                        editor.putString("EMAIL_OR_USER_PREF", user);
                        editor.putString("PERSON_TYPE_PREF", p8);
                        editor.commit();
                        Toast.makeText(RegistroGustosPt7.this, "Registro existoso",
                                Toast.LENGTH_LONG).show();
                        startActivity(in);
                    } else {
                        Toast.makeText(RegistroGustosPt7.this, "Registro fallido",
                                Toast.LENGTH_LONG).show();

                    }
                }, error -> Toast.makeText(RegistroGustosPt7.this, error.toString(),
                        Toast.LENGTH_LONG).show()){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<String, String>();
                params.put("user", p1);
                params.put("email", p2);
                params.put("pass", p3);
                params.put("bday", p4);
                params.put("gender",p5);
                params.put("typePerson", p8);
                params.put("drink",p6);
                params.put("smoke", p7);
                params.put("hobbies", p9);
                params.put("music_gender",p10);
                params.put("movies_gender", p11);
                params.put("food_type",p12);
                params.put("personal_list", p13);
                return params;
            }
        };
        //Para procesar las peticiones hechas por la aplicación
        Volley.newRequestQueue(RegistroGustosPt7.this).add(sr);
    }

}