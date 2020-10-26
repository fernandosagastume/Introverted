package com.introverted;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
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

public class RegistroGustosPt4 extends AppCompatActivity {
    private Button sgt;
    private ArrayList<String> lista = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro_gustos_pt4);

        Intent intent = getIntent();
        final String email = intent.getStringExtra("MAIL");
        final String user = intent.getStringExtra("USER");
        final String contraseña = intent.getStringExtra("PASSWORD");
        final String gender = intent.getStringExtra("GENDER");
        final String bday = intent.getStringExtra("BDAY");
        final String tipo_persona = intent.getStringExtra("TYPE_PERSON");
        final String aficiones = intent.getStringExtra("HOBBIE_LIST");
        final String generos_musica = intent.getStringExtra("GENRES_LIST");

        //tv = findViewById(R.id.prubs);
        //tv.setText(email + "\n" + user + "\n" +contraseña + "\n" +gender + "\n" +bday + "\n" +tipo_persona + "\n" +
          //      aficiones + "\n" +generos_musica);

        final ListView listView = findViewById(R.id.listaPelis);
        ArrayAdapter<String> lvAdapter = new ArrayAdapter<String>(RegistroGustosPt4.this, R.layout.genres_style,
                getResources().getStringArray(R.array.movie_genres));
        listView.setAdapter(lvAdapter);
        listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        final ArrayList<String> listaPeliculas = new ArrayList<>();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int id, long l) {
                int cnt_lv = listView.getCount();
                SparseBooleanArray sba = listView.getCheckedItemPositions();
                for(int i = 0; i < cnt_lv; i++){
                    if(sba.get(i)){
                        if(!listaPeliculas.contains(listView.getItemAtPosition(i).toString()))
                            listaPeliculas.add(listView.getItemAtPosition(i).toString());
                    }
                    else listaPeliculas.remove(listView.getItemAtPosition(i).toString());
                }
            }
        });

        sgt = findViewById(R.id.siguientept4);
        sgt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!listaPeliculas.isEmpty()){
                Intent in = new Intent(RegistroGustosPt4.this, RegistroGustosPt4_1.class);
                in.putExtra("MAIL", email);
                in.putExtra("USER", user);
                in.putExtra("PASSWORD", contraseña);
                in.putExtra("GENDER", gender);
                in.putExtra("BDAY", bday);
                in.putExtra("TYPE_PERSON", tipo_persona);
                in.putExtra("HOBBIE_LIST", aficiones);
                in.putExtra("GENRES_LIST", generos_musica);
                String pelis = "";
                int cont = 0;
                Iterator it = listaPeliculas.iterator();
                while (it.hasNext()) {
                    if(cont == 0)
                        pelis = (String) it.next();
                    else
                        pelis += "/" + it.next();
                    cont++;
                }
                in.putExtra("MOVIE_GENRES", pelis);
                startActivity(in);
            }
                else{
                Toast.makeText(RegistroGustosPt4.this, "Seleccione una opción por favor",
                        Toast.LENGTH_LONG).show();
            }
            }
        });

    }
}