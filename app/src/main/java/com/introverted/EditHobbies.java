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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Objects;

public class EditHobbies extends AppCompatActivity {
    ListView hobbies;
    Toolbar toolbar;
    int[] icons = {R.drawable.ic_pop, R.drawable.ic_jazz, R.drawable.ic_blues,
            R.drawable.ic_indie, R.drawable.ic_rock_alternativo,R.drawable.ic_country,
            R.drawable.ic_reggae, R.drawable.ic_rock_clasico,R.drawable.ic_rock,
            R.drawable.ic_soul,R.drawable.ic_heavy_metal, R.drawable.ic_heavy_metal};
    String[] hobbyList = {"Fotografía",
            "Escritura",
            "Pintura",
            "Cocina",
            "Poesía" ,
            "Caminatas",
            "Jardinería",
            "Videojuegos",
            "Dibujo",
            "Blogs",
            "Viajar",
            "Deportes"};
    private SharedPreferences adminSession;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_hobbies);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        adminSession = getSharedPreferences("LOGIN_PREF", MODE_PRIVATE);
        hobbies = findViewById(R.id.edit_hobbies);
        hobbies.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        ProgramAdapter programAdapter = new ProgramAdapter(EditHobbies.this, icons, hobbyList);
        hobbies.setAdapter(programAdapter);
        // Attaching the layout to the toolbar object
        toolbar = findViewById(R.id.toolbar_mg_h);
        // Setting toolbar as the ActionBar with setSupportActionBar() call
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setTitle(null);
        ImageButton back = findViewById(R.id.cancel_musicG);
        ImageButton save = findViewById(R.id.save_musicG);
        Intent intent = getIntent();
        String gender = intent.getStringExtra("HOBBIES");
        String[] gender_splt = gender.split("/");

        int cnt_lv = hobbies.getCount();
        final ArrayList<String> lista_selec = new ArrayList<>();
        int cont = 0;
        //Se iteran los hobbies obtenidos de la base de datos y se seleccionan en el listview
        while(cont < gender_splt.length) {
            for (int i = 0; i < cnt_lv; i++) {
                String selectedFromList = (hobbies.getItemAtPosition(i).toString());
                if (selectedFromList.equals(gender_splt[cont])){
                    hobbies.setItemChecked(i, true);
                    lista_selec.add(selectedFromList);
                }
            }
            cont++;
        }

        hobbies.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int id, long l) {
                int cnt_lv = hobbies.getCount();
                SparseBooleanArray sba = hobbies.getCheckedItemPositions();
                for(int i = 0; i < cnt_lv; i++){
                    if(sba.get(i)){
                        if(!lista_selec.contains(hobbies.getItemAtPosition(i).toString()))
                            lista_selec.add(hobbies.getItemAtPosition(i).toString());
                    }
                    else lista_selec.remove(hobbies.getItemAtPosition(i).toString());
                }
            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!lista_selec.isEmpty()){
                    String aficiones = "";
                    int cont = 0;
                    Iterator it = lista_selec.iterator();
                    while (it.hasNext()) {
                        if(cont == 0)
                            aficiones = (String) it.next();
                        else
                            aficiones += "/" + it.next();
                        cont++;
                    }
                    updateHobbies(aficiones);
                }
                else{
                    Toast.makeText(EditHobbies.this, "Seleccione una opción por favor",
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

    private void updateHobbies(final String changeList){
        StringRequest sr = new StringRequest(Request.Method.POST, "http://"+getString(R.string.IP)+"/WebService-Introverted/updateGustos.php",
                response -> {
                    if (response.equals("1")) {
                        Toast.makeText(EditHobbies.this, "Actualización de datos existoso",
                                Toast.LENGTH_LONG).show();
                        finish();
                    } else {
                        Toast.makeText(EditHobbies.this, "Actualización de datos fallido",
                                Toast.LENGTH_LONG).show();

                    }
                }, error -> Toast.makeText(EditHobbies.this, error.toString(),
                Toast.LENGTH_LONG).show()){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<String, String>();
                int ID = adminSession.getInt("USER_ID", 0);
                params.put("id_us", String.valueOf(ID));
                params.put("changeList", changeList);
                params.put("gusto", "hobbies");
                return params;
            }
        };
        //Para procesar las peticiones hechas por la aplicación
        Volley.newRequestQueue(EditHobbies.this).add(sr);
    }
}