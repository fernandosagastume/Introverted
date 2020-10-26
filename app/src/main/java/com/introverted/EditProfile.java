package com.introverted;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.textservice.TextInfo;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

public class EditProfile extends AppCompatActivity {
    SharedPreferences adminSession;
    ListView generos;
    Button genButton, bday, drinkbt, smokebt;
    CircleImageView imProf;
    ImageButton change_pic, cancel, save, pickIm1, pickIm2, pickIm3;
    TextInputEditText nombre_text, apellido_text, descripcion;
    TextInputLayout gitl;
    TextView imCounter;
    String[] genList = {"Masculino", "Femenino"};
    int[] icons = {R.drawable.ic_masculino, R.drawable.ic_femenino};
    AlertDialog ad;
    Calendar calendar;
    DatePickerDialog dpd;
    String cumpleaños, nombre, apellido, descrip;
    String respuestaDrink = "";
    String respuestaSmoke = "";
    final int CODE_REQUEST_GAL = 999;
    final int CODE_REQUEST_GAL_MULTIPLE = 998;
    ArrayList<Uri> imagesProfile;
    Bitmap profilePicture;
    boolean profpic = false;
    boolean editprofInfo = false;
    boolean profilepictureSelected = false;
    String ip;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        ip = getString(R.string.IP);
        generos = new ListView(EditProfile.this);
        List<String> genders = new ArrayList<>();
        genders.add("Masculino");
        genders.add("Femenino");
        adminSession = getSharedPreferences("LOGIN_PREF", MODE_PRIVATE);
        ProgramAdapter programAdapter = new ProgramAdapter(EditProfile.this, icons, genList);
        generos.setAdapter(programAdapter);
        genButton = findViewById(R.id.gender_ep);
        promptAlert(generos, genButton);
        imagesProfile = new ArrayList<>();
        progressDialog = new ProgressDialog(EditProfile.this);
        imCounter = findViewById(R.id.imageCounter);
        bday = findViewById(R.id.bday_ep);
        change_pic = findViewById(R.id.change_pic);
        pickIm1 = findViewById(R.id.picIm1);
        pickIm2 = findViewById(R.id.picIm2);
        pickIm3 = findViewById(R.id.picIm3);

        drinkbt = findViewById(R.id.drinkbt);
        smokebt = findViewById(R.id.smokebt);
        imProf = findViewById(R.id.editprof_im);
        nombre_text = findViewById(R.id.nombre_text);
        apellido_text = findViewById(R.id.apellido_text);
        descripcion = findViewById(R.id.description);
        gitl = findViewById(R.id.google_textLay3);

        getMYSQLData("retrieve", null, null, null,null,null,
                null,null);

        bday.setOnClickListener(view -> {
            calendar = Calendar.getInstance();
            calendar.add(Calendar.YEAR,-18);
            int dia = calendar.get(Calendar.DAY_OF_MONTH);
            int mes = calendar.get(Calendar.MONTH);
            int año = calendar.get(Calendar.YEAR);

            dpd = new DatePickerDialog(EditProfile.this, (datePicker, año1, mes1, dia1) -> {
                bday.setText(dia1 + "/" + (mes1 +1) + "/" + año1);
                cumpleaños = año1 + "-" + (mes1 +1) + "-" + dia1;
            }, dia, mes, año);
            dpd.getDatePicker().setMaxDate(calendar.getTimeInMillis());;
            dpd.updateDate(año,mes,dia);
            dpd.show();
        });

        generos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                 genButton.setText(programAdapter.getItem(i));
                 ad.dismiss();
            }
        });

        drinkbt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String[] respuestas = {"Sí", "No", "Ocasionalmente", "Socialmente", "No, lo odio"};
                AlertDialog.Builder builder = new AlertDialog.Builder(EditProfile.this);
                builder.setTitle("¿Te gusta beber?");
                int checkit = 0;
                if(!respuestaDrink.equals("")){
                    for(int i = 0; i < respuestas.length; i++){
                        if(respuestas[i].equals(respuestaDrink)){
                            checkit = i;
                            break;
                        }
                    }
                }
                builder.setSingleChoiceItems(respuestas, checkit, (dialogInterface, i) -> respuestaDrink = respuestas[i]);
                builder.setPositiveButton("Aceptar", (dialogInterface, i) -> dialogInterface.dismiss());
                builder.setNegativeButton("Cancelar", (dialogInterface, i) -> dialogInterface.dismiss());
                builder.show();
            }
        });

        smokebt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String[] respuestas = {"Sí", "No", "Ocasionalmente", "Socialmente", "No, lo odio"};
                AlertDialog.Builder builder = new AlertDialog.Builder(EditProfile.this);
                builder.setTitle("¿Te gusta fumar?");
                int checkit = 0;
                if(!respuestaSmoke.equals("")){
                    for(int i = 0; i < respuestas.length; i++){
                        if(respuestas[i].equals(respuestaSmoke)){
                            checkit = i;
                            break;
                        }
                    }
                }
                builder.setSingleChoiceItems(respuestas, checkit, (dialogInterface, i) -> respuestaSmoke = respuestas[i]);
                builder.setPositiveButton("Aceptar", (dialogInterface, i) -> dialogInterface.dismiss());
                builder.setNegativeButton("Cancelar", (dialogInterface, i) -> dialogInterface.dismiss());
                builder.show();
            }
        });

        descripcion.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if(b)
                    gitl.setHint("Descripción");
                else{
                    gitl.setHint("Escribe acerca de ti");
                }
            }
        });

        change_pic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ActivityCompat.requestPermissions(EditProfile.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        CODE_REQUEST_GAL);
            }
        });

        pickIm1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ActivityCompat.requestPermissions(EditProfile.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        CODE_REQUEST_GAL_MULTIPLE);
            }
        });

        pickIm2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ActivityCompat.requestPermissions(EditProfile.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        CODE_REQUEST_GAL_MULTIPLE);
            }
        });

        pickIm3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ActivityCompat.requestPermissions(EditProfile.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        CODE_REQUEST_GAL_MULTIPLE);
            }
        });

        cancel = findViewById(R.id.cancelButt);
        save = findViewById(R.id.saveButt);

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (TextUtils.isEmpty(nombre_text.getText().toString())){
                    nombre_text.setError("Ingresa un nombre por favor");
                }
                else if (TextUtils.isEmpty(apellido_text.getText().toString())){
                    apellido_text.setError("Ingresa un apellido por favor");
                }
                else{
                    getMYSQLData("update", nombre_text.getText().toString(), apellido_text.getText().toString(),
                                cumpleaños, genButton.getText().toString(), respuestaDrink,respuestaSmoke,
                            (TextUtils.isEmpty(descripcion.getText().toString()))?("Aun no esta implementado"):
                                    (descripcion.getText().toString()));
                    getMYSQLData("uploadPic", null, null, null,null,null,
                            null,null);

                    Intent in = new Intent(EditProfile.this, Profile.class);
                    in.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(in);

                }
            }
        });

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode == CODE_REQUEST_GAL){
            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                Intent in = new Intent(Intent.ACTION_PICK);
                in.setType("image/*");
                startActivityForResult(Intent.createChooser(in, "Selecciona una imagen"), CODE_REQUEST_GAL);
            }
            else{
                Toast.makeText(EditProfile.this, "Permiso denegado",
                        Toast.LENGTH_LONG).show();
            }
            return;
        }
        else if(requestCode == CODE_REQUEST_GAL_MULTIPLE){
            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                Intent in = new Intent();
                in.setType("image/*");
                in.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                in.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(in, "Selecciona una o varias imagenes"), CODE_REQUEST_GAL_MULTIPLE);
            }
            else{
                Toast.makeText(EditProfile.this, "Permiso denegado",
                        Toast.LENGTH_LONG).show();
            }
            return;
        }

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(requestCode == CODE_REQUEST_GAL && resultCode == RESULT_OK && data != null){
            Uri path = data.getData();
            try {
                assert path != null;
                InputStream inputStream = getContentResolver().openInputStream(path);
                profilePicture = BitmapFactory.decodeStream(inputStream);
                imProf.setImageBitmap(profilePicture);
                profilepictureSelected = true;
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
        else if(requestCode == CODE_REQUEST_GAL_MULTIPLE && resultCode == RESULT_OK && data != null){
            if(data.getClipData()!=null) {
                int counter = data.getClipData().getItemCount();
                for (int i = 0; i < counter; i++) {
                    imagesProfile.add(data.getClipData().getItemAt(i).getUri());
                    try {
                        BMHelper.getInstance_EP().setBitmap(MediaStore.Images.Media.getBitmap(this.getContentResolver(),
                                                            data.getClipData().getItemAt(i).getUri()));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }else{
                Uri imageUri = data.getData();
                imagesProfile.add(imageUri);
            }
            if(imagesProfile.size() == 3){
                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imagesProfile.get(0));
                    pickIm1.setImageBitmap(bitmap);
                    Bitmap bitmap1 = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imagesProfile.get(1));
                    pickIm2.setImageBitmap(bitmap1);
                    Bitmap bitmap2 = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imagesProfile.get(2));
                    pickIm3.setImageBitmap(bitmap2);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            else if (imagesProfile.size() == 2){
                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imagesProfile.get(0));
                    pickIm1.setImageBitmap(bitmap);
                    Bitmap bitmap1 = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imagesProfile.get(1));
                    pickIm2.setImageBitmap(bitmap1);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            else if (imagesProfile.size() == 1){
                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imagesProfile.get(0));
                    pickIm1.setImageBitmap(bitmap);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            else if(imagesProfile.size() > 3){
                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imagesProfile.get(0));
                    pickIm1.setImageBitmap(bitmap);
                    Bitmap bitmap1 = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imagesProfile.get(1));
                    pickIm2.setImageBitmap(bitmap1);
                    Bitmap bitmap2 = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imagesProfile.get(2));
                    pickIm3.setForeground(getResources().getDrawable(R.drawable.transparent_effect));
                    if(imCounter.getVisibility() == View.GONE)
                        imCounter.setVisibility(View.VISIBLE);
                    imCounter.setText("+" + String.valueOf(imagesProfile.size()-3));
                    pickIm3.setImageBitmap(bitmap2);

                }catch (IOException e){
                    e.printStackTrace();
                }
            }
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    public void promptAlert(ListView lv, Button b){
        AlertDialog.Builder alert = new AlertDialog.Builder(EditProfile.this);
        alert.setCancelable(true);
        alert.setView(lv);
        ad = alert.create();
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ad.show();
            }
        });
    }

    private void getMYSQLData(final String data, @Nullable final String name, @Nullable final String last_name,
                              @Nullable final String brday,@Nullable final String generoUp, @Nullable final String drinks,
                              @Nullable final String smokes, @Nullable final String descriptions){
        String url = "http://" + ip +":80/WebService-Introverted/editProfileInfo.php";

        StringRequest request = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @RequiresApi(api = Build.VERSION_CODES.O)
                    @Override
                    public void onResponse(String response) {
                        try{
                            if(data.equals("retrieve")) {
                                if (!response.equals("0")) {
                                    JSONArray userData = new JSONArray(response);
                                    for (int i = 0; i < userData.length(); i++) {
                                        JSONObject jobj = userData.getJSONObject(i);
                                        if(i == 0) {
                                            String birth = jobj.getString("bday");
                                            cumpleaños = birth;
                                            String[] splt = birth.split("-");
                                            birth = splt[2] + "/" + splt[1] + "/" + splt[0];
                                            bday.setText(birth);
                                            genButton.setText(jobj.getString("sexo"));
                                            if (!jobj.isNull("nombre") && !jobj.isNull("apellido")) {
                                                nombre_text.setText(jobj.getString("nombre"));
                                                apellido_text.setText(jobj.getString("apellido"));
                                            }

                                            if (jobj.getString("sexo").toLowerCase().equals("femenino")) {
                                                imProf.setImageResource(R.drawable.ic_woman_user);
                                            }

                                            if (!jobj.getString("descripcion").toLowerCase().equals("aun no esta implementado")) {
                                                descrip = jobj.getString("descripcion");
                                                descripcion.setText(jobj.getString("descripcion"));
                                                gitl.setHint("Descripción");
                                            }
                                            respuestaDrink = jobj.getString("drink");
                                            respuestaSmoke = jobj.getString("smoke");
                                        }
                                        else{
                                            if(!jobj.isNull("saveLocation")) {
                                                String location = jobj.get("saveLocation").toString();
                                                location = location.replace("www/", "");
                                                String url1 = "http://" + ip + ":80/" + location;
                                                Log.e("TAG: ", url1);
                                                ImageRequest imageRequest = new ImageRequest(url1, new Response.Listener<Bitmap>() {
                                                    @Override
                                                    public void onResponse(Bitmap response) {
                                                        imProf.setImageBitmap(response);
                                                    }
                                                }, 0, 0, ImageView.ScaleType.CENTER_CROP, null,
                                                        new Response.ErrorListener() {
                                                            @Override
                                                            public void onErrorResponse(VolleyError error) {
                                                                Toast.makeText(EditProfile.this, "Algo salió mal: " + error,
                                                                        Toast.LENGTH_LONG).show();
                                                            }
                                                        });
                                                Volley.newRequestQueue(EditProfile.this).add(imageRequest);
                                            }
                                        }
                                    }
                                } else {
                                    Toast.makeText(EditProfile.this, "Error recuperando datos",
                                            Toast.LENGTH_LONG).show();
                                }
                            }
                            else if(data.equals("update")){
                                if (response.equals("1")) {
                                    Toast.makeText(EditProfile.this, "Actualización de datos existoso",
                                            Toast.LENGTH_LONG).show();
                                } else {
                                    Log.e("UPDATE_EDITPROFILE: ", response);
                                    Toast.makeText(EditProfile.this, "Actualización de datos fallido " + response,
                                            Toast.LENGTH_LONG).show();

                                }
                                editprofInfo = true;
                            }
                            else if(data.equals("uploadPic")){
                                if (response.equals("0")) {
                                    System.out.println("Actualización de foto de perfil fallido");
                                }
                                profpic = true;
                            }

                        }catch(JSONException js){
                            js.printStackTrace();
                        }
                        //progressDialog.dismiss();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(EditProfile.this, error.toString(),
                        Toast.LENGTH_LONG).show();
                //progressDialog.dismiss();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                int ID = adminSession.getInt("USER_ID", 0);
                params.put("id_us", String.valueOf(ID));
                params.put("accion", data);
                if(data.equals("update")){
                    params.put("nombre", name);
                    params.put("apellido", last_name);
                    params.put("bday", brday);
                    params.put("genero", generoUp);
                    params.put("drink", drinks);
                    params.put("smoke", smokes);
                    params.put("description", descriptions);
                }
                else if (data.equals("uploadPic") && profilepictureSelected){
                    params.put("profpic", (String)bitmapToString(profilePicture));
                }
                return params;
            }
        };
        Volley.newRequestQueue(EditProfile.this).add(request);
        //progressDialog.setMessage("Cargando...");
        //progressDialog.show();
    }
    private String bitmapToString(Bitmap bitm){
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        bitm.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
        byte[] bytes = outputStream.toByteArray();
        return Base64.encodeToString(bytes, Base64.DEFAULT);
    }

}