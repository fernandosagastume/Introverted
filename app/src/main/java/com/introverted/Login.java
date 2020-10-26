package com.introverted;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Login extends AppCompatActivity {
    private Button register_button, login_button, not_remember;
    private EditText usermail, contraseña;
    // User Session Manager Class
    private SharedPreferences adminSession;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        (Login.this).getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE |
                                                    WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        register_button = (Button) findViewById(R.id.register);
        login_button = findViewById(R.id.login_button);
        usermail = findViewById(R.id.emailoruser);
        contraseña = findViewById(R.id.pswrd);
        not_remember = findViewById(R.id.not_remember);
        adminSession = getSharedPreferences("LOGIN_PREF", MODE_PRIVATE);
        //Se verifica que no haya ningún usuario loggeado
        sessionCheck();
        not_remember.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent in = new Intent(Login.this, PasswordForget.class);
                startActivity(in);
            }
        });

        register_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent in = new Intent(Login.this, Register.class);
                startActivity(in);
            }
        });

        login_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String userormail = usermail.getText().toString();

                if(userormail.contains("@")){
                    //Se agrega también la condición de la consulta de la base de datos
                    if(!android.util.Patterns.EMAIL_ADDRESS.matcher(userormail).matches()){
                        usermail.setError("Ingresa un correo electrónico válido");
                    }
                    else
                        verifyLogin(usermail.getText().toString());
                }
                else if (TextUtils.isEmpty(usermail.getText().toString())){
                    usermail.setError("Por favor ingresa un correo o nombre de usuario");
                }
                else if (usermail.getText().toString().matches(".*[!#$%^&*+=?./-].*")) {
                    usermail.setError("El nombre de usuario solo puede contener '_'");
                }
                else if (TextUtils.isEmpty(contraseña.getText().toString())){
                    contraseña.setError("Por favor ingresa la contraseña");
                }
                else {
                    verifyLogin(usermail.getText().toString());
                }
            }
        });

    }

    private void verifyLogin(final String emailuser){
        StringRequest request = new StringRequest(Request.Method.POST, "http://" +getString(R.string.IP)+":80/WebService-Introverted/validacion-login.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                            try {
                               if(response.equals("0")) {
                                   Toast.makeText(Login.this, "Nombre de usuario, correo electrónico o contraseña incorrecta",
                                           Toast.LENGTH_LONG).show();
                               }
                               else{
                                   String[] splt = response.split("/");
                                   SharedPreferences.Editor editor = adminSession.edit();
                                   editor.putInt("USER_ID", Integer.parseInt(splt[0]));
                                   editor.putString("EMAIL_OR_USER_PREF", emailuser);
                                   editor.putString("PERSON_TYPE_PREF", splt[1]);
                                   editor.commit();
                                   Intent in = new Intent(Login.this, Dashboard.class);
                                   startActivity(in);
                                   finishAffinity();
                               }
                            }
                            catch (Exception js){
                                Toast.makeText(Login.this, "Error en autenticación",
                                        Toast.LENGTH_LONG).show();
                                js.printStackTrace();
                            }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(Login.this, error.toString(),
                        Toast.LENGTH_LONG).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<>();
                params.put("emailuser", usermail.getText().toString());
                params.put("pass", contraseña.getText().toString());
                return params;
            }
        };
        Volley.newRequestQueue(Login.this).add(request);
    }

    private void sessionCheck(){
        int userID  = adminSession.getInt("USER_ID", 0);
        String emaiuser  = adminSession.getString("EMAIL_OR_USER_PREF", "null");
        if(userID > 0 && !emaiuser.equals("null")){
            Intent in = new Intent(Login.this, Dashboard.class);
            startActivity(in);
            finishAffinity();
        }
    }

}