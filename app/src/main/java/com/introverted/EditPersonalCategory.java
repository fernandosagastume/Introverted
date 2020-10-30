package com.introverted;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.SparseBooleanArray;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Objects;

public class EditPersonalCategory extends AppCompatActivity {
    ListView personal;
    Toolbar toolbar;
    int[] icons;
    String[] personalList;
    private SharedPreferences adminSession;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_personal_category);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        adminSession = getSharedPreferences("LOGIN_PREF", MODE_PRIVATE);

        Intent intent = getIntent();
        String gender = intent.getStringExtra("PERSONAL");
        String[] gender_splt = gender.split("/");
        String cat = gender_splt[gender_splt.length-1];
        Arrays.copyOf(gender_splt, gender_splt.length-1);


        switch(cat){
            //Caso melómano
            case "melómano":
                personalList = getResources().getStringArray(R.array.music_activities);
                icons = new int[]{R.drawable.ic_concierto, R.drawable.ic_karaoke, R.drawable.ic_chill,
                        R.drawable.ic_play_instrument, R.drawable.ic_radio_de_coche, R.drawable.ic_banda};
                break;
            //Caso gamer
            case "gamer":
                personalList = getResources().getStringArray(R.array.videogames_genres);
                icons = new int[]{R.drawable.ic_shooter, R.drawable.ic_action_game, R.drawable.ic_world_war,
                        R.drawable.ic_sword, R.drawable.ic_sports_vg,R.drawable.ic_adventure_vg,
                        R.drawable.ic_tetris, R.drawable.ic_strategy,R.drawable.ic_racing_game,
                        R.drawable.ic_virtual_reality,R.drawable.ic_musical_vg, R.drawable.ic_party,
                        R.drawable.ic_fighting_game, R.drawable.ic_arcade, R.drawable.ic_action_vg};
                break;
            //Caso bibliófilo
            case "bibliófilo":
                personalList = getResources().getStringArray(R.array.biblio);
                icons = new int[]{R.drawable.ic_romance_book, R.drawable.ic_terror, R.drawable.ic_scifi,
                        R.drawable.ic_history_book, R.drawable.ic_bio_book,R.drawable.ic_poetry_book,
                        R.drawable.ic_crime_book, R.drawable.ic_humour_book,R.drawable.ic_mitologia,
                        R.drawable.ic_theater_book,R.drawable.ic_fairy, R.drawable.ic_gothic,
                        R.drawable.ic_adventure_book, R.drawable.ic_fantasia};
                break;
            //Caso artista
            case "artista":
                personalList = getResources().getStringArray(R.array.art_type);
                icons = new int[]{R.drawable.ic_arte_abstracto, R.drawable.ic_arte_moderno, R.drawable.ic_arte_clasico,
                        R.drawable.ic_conceptual, R.drawable.ic_arte_impresionista,R.drawable.ic_pintura_neoimpresionista,
                        R.drawable.ic_arte_pop, R.drawable.ic_cubista, R.drawable.ic_surrealismo,
                        R.drawable.ic_contemporaneo, R.drawable.ic_arte_fantasia,R.drawable.ic_graffiti,
                        R.drawable.ic_barroco};
                break;
            case "anime":
                personalList = getResources().getStringArray(R.array.anime);
                icons = new int[]{R.drawable.ic_naruto, R.drawable.ic_shojo,
                        R.drawable.ic_adult, R.drawable.ic_goblin,
                        R.drawable.ic_sentai, R.drawable.ic_bycicle,
                        R.drawable.ic_robot,R.drawable.ic_suspense, R.drawable.ic_cyberpunk,
                        R.drawable.ic_comedy_anime, R.drawable.ic_polyamory};
                break;
            default:
                break;
        }

        personal = findViewById(R.id.edit_personalCat);
        personal.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        ProgramAdapter programAdapter = new ProgramAdapter(EditPersonalCategory.this, icons, personalList);
        personal.setAdapter(programAdapter);
        // Attaching the layout to the toolbar object
        toolbar = findViewById(R.id.toolbar_mg_personal);
        // Setting toolbar as the ActionBar with setSupportActionBar() call
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setTitle(null);
        ImageButton back = findViewById(R.id.cancel_musicG);
        ImageButton save = findViewById(R.id.save_musicG);

        int cnt_lv = personal.getCount();
        final ArrayList<String> lista_selec = new ArrayList<>();
        int cont = 0;
        //Se iteran los tipos de comida obtenidos de la base de datos y se seleccionan en el listview
        while(cont < gender_splt.length) {
            for (int i = 0; i < cnt_lv; i++) {
                String selectedFromList = (personal.getItemAtPosition(i).toString());
                if (selectedFromList.equals(gender_splt[cont])){
                    personal.setItemChecked(i, true);
                    lista_selec.add(selectedFromList);
                }
            }
            cont++;
        }

        personal.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int id, long l) {
                int cnt_lv = personal.getCount();
                SparseBooleanArray sba = personal.getCheckedItemPositions();
                for(int i = 0; i < cnt_lv; i++){
                    if(sba.get(i)){
                        if(!lista_selec.contains(personal.getItemAtPosition(i).toString()))
                            lista_selec.add(personal.getItemAtPosition(i).toString());
                    }
                    else lista_selec.remove(personal.getItemAtPosition(i).toString());
                }
            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!lista_selec.isEmpty()){
                    String personalCat = "";
                    int cont = 0;
                    Iterator it = lista_selec.iterator();
                    while (it.hasNext()) {
                        if(cont == 0)
                            personalCat = (String) it.next();
                        else
                            personalCat += "/" + it.next();
                        cont++;
                    }
                    updatePersonalCat(personalCat);
                }
                else{
                    Toast.makeText(EditPersonalCategory.this, "Seleccione una opción por favor",
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

    private void updatePersonalCat(final String changeList){
        StringRequest sr = new StringRequest(Request.Method.POST, "http://"+getString(R.string.IP)+"/WebService-Introverted/updateGustos.php",
                response -> {
                    if (response.equals("1")) {
                        Toast.makeText(EditPersonalCategory.this, "Actualización de datos existoso",
                                Toast.LENGTH_LONG).show();
                        finish();
                    } else {
                        Toast.makeText(EditPersonalCategory.this, "Actualización de datos fallido" + response,
                                Toast.LENGTH_LONG).show();

                    }
                }, error -> Toast.makeText(EditPersonalCategory.this, error.toString(),
                Toast.LENGTH_LONG).show()){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<String, String>();
                int ID = adminSession.getInt("USER_ID", 0);
                params.put("id_us", String.valueOf(ID));
                params.put("changeList", changeList);
                params.put("gusto", "categoria_personal");
                return params;
            }
        };
        //Para procesar las peticiones hechas por la aplicación
        Volley.newRequestQueue(EditPersonalCategory.this).add(sr);
    }

}