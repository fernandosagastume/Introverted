package com.introverted;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.SparseBooleanArray;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Objects;

public class EditMovieGenres extends AppCompatActivity {
    ListView movie;
    Toolbar toolbar;
    int[] icons = {R.drawable.ic_pop, R.drawable.ic_jazz, R.drawable.ic_blues,
            R.drawable.ic_indie, R.drawable.ic_rock_alternativo,R.drawable.ic_country,
            R.drawable.ic_reggae, R.drawable.ic_rock_clasico,R.drawable.ic_rock,
            R.drawable.ic_soul,R.drawable.ic_heavy_metal, R.drawable.ic_heavy_metal};
    String[] genres = {"Horror",
        "Fantasía",
        "Ciencia Ficción",
        "Drama",
        "Misterio" ,
        "Documentales",
        "Romance",
        "Comedia",
        "Cartoon",
        "Crimen",
        "Acción",
        "Aventura"};
    private SharedPreferences adminSession;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_movie_genres);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        adminSession = getSharedPreferences("LOGIN_PREF", MODE_PRIVATE);
        movie = findViewById(R.id.edit_movies);
        movie.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        ProgramAdapter programAdapter = new ProgramAdapter(EditMovieGenres.this, icons, genres);
        movie.setAdapter(programAdapter);
        // Attaching the layout to the toolbar object
        toolbar = findViewById(R.id.toolbar_mg_movie);
        // Setting toolbar as the ActionBar with setSupportActionBar() call
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setTitle(null);
        ImageButton back = findViewById(R.id.cancel_musicG);
        ImageButton save = findViewById(R.id.save_musicG);
        Intent intent = getIntent();
        String gender = intent.getStringExtra("MOVIE_GENRES");
        String[] gender_splt = gender.split("/");

        int cnt_lv = movie.getCount();
        final ArrayList<String> lista_selec = new ArrayList<>();
        int cont = 0;
        //Se iteran los generos obtenidos de la base de datos y se seleccionan en el listview
        while(cont < gender_splt.length) {
            for (int i = 0; i < cnt_lv; i++) {
                String selectedFromList = (movie.getItemAtPosition(i).toString());
                if (selectedFromList.equals(gender_splt[cont])){
                    movie.setItemChecked(i, true);
                    lista_selec.add(selectedFromList);
                }
            }
            cont++;
        }

        movie.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int id, long l) {
                int cnt_lv = movie.getCount();
                SparseBooleanArray sba = movie.getCheckedItemPositions();
                for(int i = 0; i < cnt_lv; i++){
                    if(sba.get(i)){
                        if(!lista_selec.contains(movie.getItemAtPosition(i).toString()))
                            lista_selec.add(movie.getItemAtPosition(i).toString());
                    }
                    else lista_selec.remove(movie.getItemAtPosition(i).toString());
                }
            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!lista_selec.isEmpty()){
                    String peliculas = "";
                    int cont = 0;
                    Iterator it = lista_selec.iterator();
                    while (it.hasNext()) {
                        if(cont == 0)
                            peliculas = (String) it.next();
                        else
                            peliculas += "/" + it.next();
                        cont++;
                    }
                    updateMovies(peliculas);
                }
                else{
                    Toast.makeText(EditMovieGenres.this, "Seleccione una opción por favor",
                            Toast.LENGTH_LONG).show();
                }
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    private void updateMovies(final String changeList){
        StringRequest sr = new StringRequest(Request.Method.POST, "http://"+getString(R.string.IP)+"/WebService-Introverted/updateGustos.php",
                response -> {
                    if (response.equals("1")) {
                        Toast.makeText(EditMovieGenres.this, "Actualización de datos existoso",
                                Toast.LENGTH_LONG).show();
                        finish();
                    } else {
                        Toast.makeText(EditMovieGenres.this, "Actualización de datos fallido",
                                Toast.LENGTH_LONG).show();

                    }
                }, error -> Toast.makeText(EditMovieGenres.this, error.toString(),
                Toast.LENGTH_LONG).show()){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<String, String>();
                int ID = adminSession.getInt("USER_ID", 0);
                params.put("id_us", String.valueOf(ID));
                params.put("changeList", changeList);
                params.put("gusto", "movies");
                return params;
            }
        };
        //Para procesar las peticiones hechas por la aplicación
        Volley.newRequestQueue(EditMovieGenres.this).add(sr);
    }
}