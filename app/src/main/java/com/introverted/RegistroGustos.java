package com.introverted;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

public class RegistroGustos extends AppCompatActivity {
    Button final_siguiente, wiki_button;
    RadioGroup typeperson;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro_gustos);
        Intent intent = getIntent();

        final String email = intent.getStringExtra("MAIL");
        final String user = intent.getStringExtra("USER");
        final String contraseña = intent.getStringExtra("PASSWORD");
        final String gender = intent.getStringExtra("GENDER");
        final String bday = intent.getStringExtra("BDAY");


        final_siguiente = findViewById(R.id.siguiente_gustos);
        typeperson = findViewById(R.id.person_type);
        wiki_button = findViewById(R.id.wiki_button);

        typeperson.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    default:
                        RadioButton rb = findViewById(checkedId);
                        String tipo_persona = rb.getText().toString().toLowerCase();
                        tipo_persona = tipo_persona.substring(0,1).toUpperCase() + tipo_persona.substring(1);
                        wiki_button.setText(tipo_persona);
                }
            }
        });

        wiki_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(typeperson.getCheckedRadioButtonId() != -1) {
                    int rbID = typeperson.getCheckedRadioButtonId();
                    RadioButton rb = findViewById(rbID);
                    String tipo_persona = rb.getText().toString().toLowerCase();
                    Intent in = new Intent(RegistroGustos.this, WikiQuery.class);
                    in.putExtra("QUERY", tipo_persona);
                    startActivity(in);
                }
            }
        });

        final_siguiente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(typeperson.getCheckedRadioButtonId() != -1) {
                    int rbID = typeperson.getCheckedRadioButtonId();
                    RadioButton rb = findViewById(rbID);
                    String tipo_persona = rb.getText().toString();
                    Intent in = new Intent(RegistroGustos.this, RegistroGustosPt2.class);
                    in.putExtra("MAIL", email);
                    in.putExtra("USER", user);
                    in.putExtra("PASSWORD", contraseña);
                    in.putExtra("GENDER", gender);
                    in.putExtra("BDAY", bday);
                    in.putExtra("TYPE_PERSON", tipo_persona);
                    startActivity(in);
                }
                else {
                    Toast.makeText(RegistroGustos.this, "Seleccione una opción por favor",
                            Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}