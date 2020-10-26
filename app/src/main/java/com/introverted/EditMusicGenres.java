package com.introverted;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
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

public class EditMusicGenres extends AppCompatActivity {
    ListView music;
    Toolbar toolbar;
    int[] icons = {R.drawable.ic_pop, R.drawable.ic_jazz, R.drawable.ic_blues,
            R.drawable.ic_indie, R.drawable.ic_rock_alternativo,R.drawable.ic_country,
            R.drawable.ic_reggae, R.drawable.ic_rock_clasico,R.drawable.ic_rock,
            R.drawable.ic_soul,R.drawable.ic_heavy_metal};
    String[] genres = {"Pop", "Jazz", "R&B", "Indie", "Rock Alternativo",
                        "Country", "Reggae", "Rock clásico,", "Rock", "Soul", "Heavy Metal"};
    private SharedPreferences adminSession;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_music_genres);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        adminSession = getSharedPreferences("LOGIN_PREF", MODE_PRIVATE);
        music = findViewById(R.id.edit_music);
        music.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        ProgramAdapter programAdapter = new ProgramAdapter(EditMusicGenres.this, icons, genres);
        music.setAdapter(programAdapter);
        // Attaching the layout to the toolbar object
        toolbar = findViewById(R.id.toolbar_mg);
        // Setting toolbar as the ActionBar with setSupportActionBar() call
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setTitle(null);
        ImageButton back = findViewById(R.id.cancel_musicG);
        ImageButton save = findViewById(R.id.save_musicG);
        Intent intent = getIntent();
        String gender = intent.getStringExtra("MUSIC_GENRES");
        String[] gender_splt = gender.split("/");

        int cnt_lv = music.getCount();
        final ArrayList<String> lista_selec = new ArrayList<>();
        int cont = 0;
        //Se iteran los generos obtenidos de la base de datos y se seleccionan en el listview
        while(cont < gender_splt.length) {
            for (int i = 0; i < cnt_lv; i++) {
                String selectedFromList = (music.getItemAtPosition(i).toString());
                if (selectedFromList.equals(gender_splt[cont])){
                    music.setItemChecked(i, true);
                    lista_selec.add(selectedFromList);
                }
            }
            cont++;
        }

        music.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int id, long l) {
                int cnt_lv = music.getCount();
                SparseBooleanArray sba = music.getCheckedItemPositions();
                for(int i = 0; i < cnt_lv; i++){
                    if(sba.get(i)){
                        if(!lista_selec.contains(music.getItemAtPosition(i).toString()))
                            lista_selec.add(music.getItemAtPosition(i).toString());
                    }
                    else lista_selec.remove(music.getItemAtPosition(i).toString());
                }
            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!lista_selec.isEmpty()){
                    String musica = "";
                    int cont = 0;
                    Iterator it = lista_selec.iterator();
                    while (it.hasNext()) {
                        if(cont == 0)
                            musica = (String) it.next();
                        else
                            musica += "/" + it.next();
                        cont++;
                    }
                    updateMusic(musica);
                }
                else{
                    Toast.makeText(EditMusicGenres.this, "Seleccione una opción por favor",
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

    private void updateMusic(final String changeList){
        StringRequest sr = new StringRequest(Request.Method.POST, "http://"+getString(R.string.IP)+"/WebService-Introverted/updateGustos.php",
                response -> {
                    if (response.equals("1")) {
                        Toast.makeText(EditMusicGenres.this, "Actualización de datos existoso",
                                Toast.LENGTH_LONG).show();
                        finish();
                    } else {
                        Toast.makeText(EditMusicGenres.this, "Actualización de datos fallido",
                                Toast.LENGTH_LONG).show();

                    }
                }, error -> Toast.makeText(EditMusicGenres.this, error.toString(),
                Toast.LENGTH_LONG).show()){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<String, String>();
                int ID = adminSession.getInt("USER_ID", 0);
                params.put("id_us", String.valueOf(ID));
                params.put("changeList", changeList);
                params.put("gusto", "music");
                return params;
            }
        };
        //Para procesar las peticiones hechas por la aplicación
        Volley.newRequestQueue(EditMusicGenres.this).add(sr);
    }
}
class ProgramAdapter extends ArrayAdapter<String>{
    Context context;
    int[] icons;
    String[] gustosList;
    public ProgramAdapter(Context context, int[] icons, String[] gustosList) {
        super(context, R.layout.elementos_profile_lvs, R.id.lv_text, gustosList);
        this.context = context;
        this.icons = icons;
        this.gustosList = gustosList;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View item = convertView;
        ProgramViewHolder holder = null;
        if(item  == null){
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            item = layoutInflater.inflate(R.layout.elementos_profile_lvs, parent, false);
            holder = new ProgramViewHolder(item);
            item.setTag(holder);
        }else {
            holder = (ProgramViewHolder) item.getTag();
        }
        holder.gustosIcon.setImageResource(icons[position]);
        holder.gustosText.setText(gustosList[position]);

        return item;
    }
}
class ProgramViewHolder{
    ImageView gustosIcon;
    TextView gustosText;
    ProgramViewHolder(View view){
        gustosIcon = view.findViewById(R.id.gustoIcon);
        gustosText = view.findViewById(R.id.lv_text);
    }
}