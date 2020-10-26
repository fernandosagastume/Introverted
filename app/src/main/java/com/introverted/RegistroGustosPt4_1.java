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
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Iterator;

public class RegistroGustosPt4_1 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro_gustos_pt4_1);

        Intent intent = getIntent();
        final String email = intent.getStringExtra("MAIL");
        final String user = intent.getStringExtra("USER");
        final String contraseña = intent.getStringExtra("PASSWORD");
        final String gender = intent.getStringExtra("GENDER");
        final String bday = intent.getStringExtra("BDAY");
        final String tipo_persona = intent.getStringExtra("TYPE_PERSON");
        final String aficiones = intent.getStringExtra("HOBBIE_LIST");
        final String generos_musica = intent.getStringExtra("GENRES_LIST");
        final String generos_movies = intent.getStringExtra("MOVIE_GENRES");

        final ListView listView = findViewById(R.id.listaComida);
        ArrayAdapter<String> lvAdapter = new ArrayAdapter<String>(RegistroGustosPt4_1.this, R.layout.genres_style,
                getResources().getStringArray(R.array.food_type));
        listView.setAdapter(lvAdapter);
        listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        final ArrayList<String> listaComida = new ArrayList<>();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int id, long l) {
                int cnt_lv = listView.getCount();
                SparseBooleanArray sba = listView.getCheckedItemPositions();
                for(int i = 0; i < cnt_lv; i++){
                    if(sba.get(i)){
                        if(!listaComida.contains(listView.getItemAtPosition(i).toString()))
                            listaComida.add(listView.getItemAtPosition(i).toString());
                    }
                    else listaComida.remove(listView.getItemAtPosition(i).toString());
                }
            }
        });

        Button sgt = findViewById(R.id.siguientept4_1);
        sgt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!listaComida.isEmpty()){
                Intent in = new Intent(RegistroGustosPt4_1.this, RegistroGustosPt5.class);
                in.putExtra("MAIL", email);
                in.putExtra("USER", user);
                in.putExtra("PASSWORD", contraseña);
                in.putExtra("GENDER", gender);
                in.putExtra("BDAY", bday);
                in.putExtra("TYPE_PERSON", tipo_persona);
                in.putExtra("HOBBIE_LIST", aficiones);
                in.putExtra("GENRES_LIST", generos_musica);
                in.putExtra("MOVIE_GENRES", generos_movies);

                String food = "";
                int cont = 0;
                Iterator it = listaComida.iterator();
                while (it.hasNext()) {
                    if(cont == 0)
                        food = (String) it.next();
                    else
                        food += "/" + it.next();
                    cont++;
                }
                in.putExtra("FOOD_TYPE", food);
                startActivity(in);
            }
                else{
                Toast.makeText(RegistroGustosPt4_1.this, "Seleccione una opción por favor",
                        Toast.LENGTH_LONG).show();
            }
            }
        });

    }
}