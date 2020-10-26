package com.introverted;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

public class RegistroGustosPt6 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro_gustos_pt6);

        final Intent intent = getIntent();
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
        final String persona_bebe = intent.getStringExtra("PERSON_DRINK");



        Button siguientept6 = findViewById(R.id.siguientept6);
        final RadioGroup rg_fumador = findViewById(R.id.fumador);

        siguientept6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(rg_fumador.getCheckedRadioButtonId() != -1) {
                    int rbID = rg_fumador.getCheckedRadioButtonId();
                    RadioButton rb = (RadioButton) findViewById(rbID);
                    String persona_fuma = rb.getText().toString();
                    Intent in;
                    in = new Intent(RegistroGustosPt6.this, RegistroGustosPt7.class);

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
                    startActivity(in);
                    //finishAffinity();
                }
                else {
                    Toast.makeText(RegistroGustosPt6.this, "Seleccione una opción por favor",
                            Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}