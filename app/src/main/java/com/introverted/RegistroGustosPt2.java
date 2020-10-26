package com.introverted;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.SparseBooleanArray;
import android.view.View;
import android.widget.*;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class RegistroGustosPt2 extends AppCompatActivity {
    private ListView listView;
    private Button sgt;
    private ArrayAdapter<String> lvAdapter;
    private ArrayList<String> lista = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro_gustos_pt2);
        Intent intent = getIntent();

        final String email = intent.getStringExtra("MAIL");
        final String user = intent.getStringExtra("USER");
        final String contraseña = intent.getStringExtra("PASSWORD");
        final String gender = intent.getStringExtra("GENDER");
        final String bday = intent.getStringExtra("BDAY");
        final String tipo_persona = intent.getStringExtra("TYPE_PERSON");

        listView = findViewById(R.id.listaAficiones);
        lvAdapter = new ArrayAdapter<String>(RegistroGustosPt2.this, R.layout.hobby_lv_style,
                getResources().getStringArray(R.array.aficiones));
        listView.setAdapter(lvAdapter);
        listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        final ArrayList<String> listaAficiones = new ArrayList<>();
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int id, long l) {
                int cnt_lv = listView.getCount();
                SparseBooleanArray sba = listView.getCheckedItemPositions();
                for(int i = 0; i < cnt_lv; i++){
                    if(sba.get(i)){
                        if(!listaAficiones.contains(listView.getItemAtPosition(i).toString()))
                        listaAficiones.add(listView.getItemAtPosition(i).toString());
                    }
                    else listaAficiones.remove(listView.getItemAtPosition(i).toString());
                }
            }
        });

        sgt = findViewById(R.id.siguientept2);
        sgt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!listaAficiones.isEmpty()) {
                    Intent in = new Intent(RegistroGustosPt2.this, RegistroGustosPt3.class);
                    in.putExtra("MAIL", email);
                    in.putExtra("USER", user);
                    in.putExtra("PASSWORD", contraseña);
                    in.putExtra("GENDER", gender);
                    in.putExtra("BDAY", bday);
                    in.putExtra("TYPE_PERSON", tipo_persona);
                    String hobbies = "";
                    int cont = 0;
                    Iterator it = listaAficiones.iterator();
                    while (it.hasNext()) {
                        if (cont == 0)
                            hobbies = (String) it.next();
                        else
                            hobbies += "/" + it.next();
                        cont++;
                    }
                    in.putExtra("HOBBIE_LIST", hobbies);
                    startActivity(in);
                }
                else{
                    Toast.makeText(RegistroGustosPt2.this, "Seleccione una opción por favor",
                            Toast.LENGTH_LONG).show();
                }
            }
        });

    }
}