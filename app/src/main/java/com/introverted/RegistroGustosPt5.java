package com.introverted;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

public class RegistroGustosPt5 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro_gustos_pt5);

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
        final String food_type = intent.getStringExtra("FOOD_TYPE");

        Button siguientept5 = findViewById(R.id.siguientept5);
        final RadioGroup rg_bebedor = findViewById(R.id.bebedor);

        siguientept5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(rg_bebedor.getCheckedRadioButtonId() != -1) {
                    int rbID = rg_bebedor.getCheckedRadioButtonId();
                    RadioButton rb = (RadioButton) findViewById(rbID);
                    String persona_bebe = rb.getText().toString();
                    Intent in = new Intent(RegistroGustosPt5.this, RegistroGustosPt6.class);
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
                    startActivity(in);
                    //finishAffinity();
                }
                else {
                    Toast.makeText(RegistroGustosPt5.this, "Seleccione una opción por favor",
                            Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}