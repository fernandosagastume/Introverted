package com.introverted;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.widget.Toolbar;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.w3c.dom.Text;

import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileLink extends AppCompatActivity {

    private SharedPreferences adminSession;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_link);

        adminSession = getSharedPreferences("LOGIN_PREF", MODE_PRIVATE);
        TextView tv = findViewById(R.id.categoriaActual);
        switch (adminSession.getString("PERSON_TYPE_PREF", "null")){
            case "MELÓMANO":
                tv.setText("Actividades musicales favoritas");
                break;
            case "GAMER":
                tv.setText("Tipos de videojuegos favoritos");
                break;
            case "OTAKU":
                tv.setText("Géneros de anime favoritos");
                break;
            case "BIBLIÓFILO":
                tv.setText("Géneros de libro favoritos");
                break;
            case "ARTISTA":
                tv.setText("Géneros de arte favoritos");
                break;
        }

        toolbar = findViewById(R.id.tb_pl);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        ImageView userPicture = findViewById(R.id.otheruserIm);
        userPicture.setImageBitmap(BMHelper.getInstance_PL().getPpBM());
        TextView name = findViewById(R.id.ppName);
        TextView city = findViewById(R.id.ppCity);
        TextView distance = findViewById(R.id.ppDistancia);
        TextView age = findViewById(R.id.ppAge);
        final Intent intent = getIntent();
        name.setText(name.getText().toString() + intent.getStringExtra("NOMBRE"));
        city.setText(city.getText().toString() + intent.getStringExtra("CITY"));
        distance.setText(distance.getText().toString() + intent.getStringExtra("DISTANCE"));
        age.setText(age.getText().toString() + intent.getStringExtra("AGE") + " años");
    }

    private void updateFood(final String changeList){
        StringRequest sr = new StringRequest(Request.Method.POST, "http://"+getString(R.string.IP)+"/WebService-Introverted/updateGustos.php",
                response -> {
                    if (response.equals("1")) {
                        Log.i("PROFILE LINK", "La consulta fue exitosa");
                    } else {
                        Log.i("PROFILE LINK", "La consulta fue fallida");

                    }
                }, error -> Toast.makeText(ProfileLink.this, error.toString(),
                Toast.LENGTH_LONG).show()){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<String, String>();
                int ID = adminSession.getInt("USER_ID", 0);
                params.put("id_us", String.valueOf(ID));
                params.put("changeList", changeList);
                params.put("gusto", "food");
                return params;
            }
        };
        //Para procesar las peticiones hechas por la aplicación
        Volley.newRequestQueue(ProfileLink.this).add(sr);
    }

}