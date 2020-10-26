package com.introverted;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.SparseBooleanArray;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Iterator;

public class RegistroGustosPt3 extends AppCompatActivity {
    private Button sgt;
    private ArrayList<String> lista = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro_gustos_pt3);

        Intent intent = getIntent();

        final String email = intent.getStringExtra("MAIL");
        final String user = intent.getStringExtra("USER");
        final String contraseña = intent.getStringExtra("PASSWORD");
        final String gender = intent.getStringExtra("GENDER");
        final String bday = intent.getStringExtra("BDAY");
        final String tipo_persona = intent.getStringExtra("TYPE_PERSON");
        final String aficiones = intent.getStringExtra("HOBBIE_LIST");

        final ListView listView = findViewById(R.id.listaMusica);
        ArrayAdapter<String> lvAdapter = new ArrayAdapter<String>(RegistroGustosPt3.this, R.layout.genres_style,
                getResources().getStringArray(R.array.music_genres));
        listView.setAdapter(lvAdapter);
        listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        final ArrayList<String> listaGenres = new ArrayList<>();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int id, long l) {
                int cnt_lv = listView.getCount();
                SparseBooleanArray sba = listView.getCheckedItemPositions();
                for(int i = 0; i < cnt_lv; i++){
                    if(sba.get(i)){
                        if(!listaGenres.contains(listView.getItemAtPosition(i).toString()))
                            listaGenres.add(listView.getItemAtPosition(i).toString());
                    }
                    else listaGenres.remove(listView.getItemAtPosition(i).toString());
                }
            }
        });

        sgt = findViewById(R.id.siguientept3);
        sgt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!listaGenres.isEmpty()){
                Intent in = new Intent(RegistroGustosPt3.this, RegistroGustosPt4.class);
                in.putExtra("MAIL", email);
                in.putExtra("USER", user);
                in.putExtra("PASSWORD", contraseña);
                in.putExtra("GENDER", gender);
                in.putExtra("BDAY", bday);
                in.putExtra("TYPE_PERSON", tipo_persona);
                in.putExtra("HOBBIE_LIST", aficiones);
                String generos = "";
                int cont = 0;
                Iterator it = listaGenres.iterator();
                while (it.hasNext()) {
                    if(cont == 0)
                        generos = (String) it.next();
                    else
                        generos += "/" + it.next();
                    cont++;
                }
                in.putExtra("GENRES_LIST", generos);
                startActivity(in);
                }
                else{
                    Toast.makeText(RegistroGustosPt3.this, "Seleccione una opción por favor",
                            Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}