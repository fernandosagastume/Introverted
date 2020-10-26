package com.introverted;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

public class Register extends AppCompatActivity {
    Button bday_date;
    Calendar calendar;
    DatePickerDialog dpd;
    Spinner spin;
    Button next;
    String yrVerify = "";
    EditText email, user, contraseña, repeat_pswrd;
    boolean dateSelected = false;
    String cumpleaños;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        bday_date = (Button) findViewById(R.id.bday_date);
        //------------------------ Drop down list Gender --------------------------------------------------//
        spin = (Spinner) findViewById(R.id.spinner_gender);
        //R.array.generos esta en el folder de values/strings -> allí se guardan los textos del spinner
        ArrayAdapter<String> adpt = new ArrayAdapter<String>(Register.this, R.layout.spinner_style,
                getResources().getStringArray(R.array.generos));
        adpt.setDropDownViewResource(R.layout.dropdown_gender);
        //adpt.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spin.setAdapter(adpt);
        //------------------------ Drop down list --------------------------------------------------//

        bday_date.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                calendar = Calendar.getInstance();
                calendar.add(Calendar.YEAR,-18);
                int dia = calendar.get(Calendar.DAY_OF_MONTH);
                int mes = calendar.get(Calendar.MONTH);
                int año = calendar.get(Calendar.YEAR);

                dpd = new DatePickerDialog(Register.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int año, int mes, int dia) {
                        bday_date.setText(dia + "/" + (mes+1) + "/" + año);
                        cumpleaños = año + "-" + (mes+1) + "-" + dia;
                        yrVerify = String.valueOf(año);
                        dateSelected = true;
                    }
                }, dia, mes, año);
                dpd.getDatePicker().setMaxDate(calendar.getTimeInMillis());;
                dpd.updateDate(año,mes,dia);
                dpd.show();
            }
        });

        next = findViewById(R.id.siguiente);
        email = findViewById(R.id.email);
        user = findViewById(R.id.username);
        contraseña = findViewById(R.id.pwrd_reg);
        repeat_pswrd = findViewById(R.id.pwrd_confirm);

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int añoselec = 10000;
                if(!(yrVerify.equals("")))
                    añoselec = Integer.parseInt(yrVerify);

                int añovalido = (Calendar.getInstance().get(Calendar.YEAR) - 100);


                if(!isValidEmail(email.getText().toString())){
                    email.setError("Ingresa un correo electrónico válido");
                }
                else if (TextUtils.isEmpty(user.getText().toString())){
                    user.setError("Ingresa un nombre de usuario");
                }
                else if (user.getText().toString().length() < 8){
                    user.setError("El nombre de usuario debe tener tamaño mínimo de 8");
                }
                else if (user.getText().toString().length() > 25){
                    user.setError("El nombre de usuario debe tener tamaño máximo de 25");
                }
               else if (!user.getText().toString().matches(".*[a-z].*")) {
                    user.setError("El nombre de usuario debe contener minúsculas");
                }
               else if (user.getText().toString().matches(".*[!@#$%^&*+=?.].*")) {
                    user.setError("El nombre de usuario solo puede contener '_'");
                }
                else if (!user.getText().toString().matches(".*\\d.*")){
                    user.setError("El nombre de usuario debe de contener al menos un dígito)");
                }
                else if(TextUtils.isEmpty(contraseña.getText().toString())){
                    contraseña.setError("Ingresa una contraseña por favor");
                }
                else if(TextUtils.equals(contraseña.getText(), user.getText())){
                    contraseña.setError("La contraseña no debe ser igual al nombre de usuario");
                }
                else if (!contraseña.getText().toString().matches(".*\\d.*")){
                    contraseña.setError("La contraseña debe de contener al menos un dígito)");
                }
                else if (contraseña.getText().toString().length() < 8){
                    contraseña.setError("La contraseña debe tener tamaño mínimo de 8");
                }
                else if (contraseña.getText().toString().length() > 25){
                    contraseña.setError("La contraseña debe tener tamaño máximo de 25");
                }
                else if(TextUtils.isEmpty(repeat_pswrd.getText().toString())){
                    repeat_pswrd.setError("Vuelve a escribir la contraseña por favor");
                }
                else if(!TextUtils.equals(contraseña.getText(), repeat_pswrd.getText())){
                    repeat_pswrd.setError("Las contraseñas no coinciden");
                }
                else if(añoselec < añovalido){
                    Toast.makeText(Register.this, "Ingrese una fecha correcta",
                            Toast.LENGTH_LONG).show();
                }
                else if (!dateSelected){
                    Toast.makeText(Register.this, "Ingrese una fecha",
                            Toast.LENGTH_LONG).show();
                }
                else if (spin.getSelectedItem().toString().equals("Selecciona un genero")){
                    Toast.makeText(Register.this, "Ingrese un genero por favor",
                            Toast.LENGTH_LONG).show();
                }
                else {

                    verifyParams();

                }
            }
        });

    }
    public static boolean isValidEmail(CharSequence target) {
        if (TextUtils.isEmpty(target)) {
            return false;
        }
        else
            return android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
    }

    public void verifyParams(){
        StringRequest request = new StringRequest(Request.Method.POST, "http://"+getString(R.string.IP)+":80/WebService-Introverted/validacion-data.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if(response.equals("11")){
                            email.setError("Ya hay una cuenta asociada a ese correo electrónico");
                            user.setError("Ya hay una cuenta asociada a ese nombre de usuario");
                        }
                        else if(response.equals("10")){
                            email.setError("Ya hay una cuenta asociada a ese correo electrónico");
                        }
                        else if(response.equals("01")){
                            user.setError("Ya hay una cuenta asociada a ese nombre de usuario");
                        }
                        else {
                            Intent in = new Intent(Register.this, RegistroGustos.class);
                            in.putExtra("MAIL", email.getText().toString());
                            in.putExtra("USER", user.getText().toString());
                            in.putExtra("PASSWORD", contraseña.getText().toString());
                            in.putExtra("GENDER", spin.getSelectedItem().toString());
                            in.putExtra("BDAY", cumpleaños);
                            startActivity(in);
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(Register.this, error.toString(),
                        Toast.LENGTH_LONG).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<>();
                params.put("email", email.getText().toString());
                params.put("user", user.getText().toString());
                return params;
            }
        };
        Volley.newRequestQueue(Register.this).add(request);
    }
}