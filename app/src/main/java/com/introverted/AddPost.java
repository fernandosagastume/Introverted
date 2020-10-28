package com.introverted;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class AddPost extends AppCompatActivity {

    TextInputEditText temaET, titleET, publicacionET;
    TextInputLayout inputLay, inputLay1, inputLay2;
    ImageButton cancelIm, addImPost;
    final int CODE_REQUEST_GAL = 999;
    Bitmap postImage;
    boolean postImageSelected = false;
    TextView withText, withImage;
    String ip;
    private SharedPreferences adminSession;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_post);

        init();


        temaET.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if(b)
                    inputLay.setHint("Tema");
                else{
                    inputLay.setHint("Escribe el tema que quiere discutir");
                }
            }
        });
        titleET.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if(b)
                    inputLay1.setHint("Titulo");
                else{
                    inputLay1.setHint("Escribe un titulo descriptivo");
                }
            }
        });
        publicacionET.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if(b)
                    inputLay2.setHint("Publicación");
                else{
                    inputLay2.setHint("Comparte tus pensamientos");
                }
            }
        });

    }

    public void init(){
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);

        addImPost = findViewById(R.id.addImPost);
        cancelIm = findViewById(R.id.cancelIm);

        withText = findViewById(R.id.withText);
        withImage = findViewById(R.id.withImage);

        temaET = findViewById(R.id.tema_publi);
        titleET = findViewById(R.id.titulo_publi);
        publicacionET = findViewById(R.id.publicacion);

        inputLay = findViewById(R.id.text_lay_ap);
        inputLay1 = findViewById(R.id.text_lay_ap1);
        inputLay2 = findViewById(R.id.text_lay_ap2);
        ip = getString(R.string.IP);
        adminSession = getSharedPreferences("LOGIN_PREF", MODE_PRIVATE);
    }

    public void postPublication(View view){
        if (TextUtils.isEmpty(temaET.getText().toString())){
            temaET.setError("Ingresa un tema por favor");
        }
        else if (TextUtils.isEmpty(titleET.getText().toString())){
            titleET.setError("Ingresa un titulo por favor");
        }
        else if (TextUtils.isEmpty(publicacionET.getText().toString())){
            publicacionET.setError("Escribe tu publicación");
        }
        else{
            insertPost();
            Intent in = new Intent(AddPost.this, Forum.class);
            in.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(in);
        }
    }



    private void insertPost(){
        String url = "http://" + ip +":80/WebService-Introverted/insertForumPost.php";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (!response.equals("1")) {
                    Log.e("ADD_POST: ","No se ha podido insertar los datos -> " + response);
                }
            }
        }, error -> Toast.makeText(AddPost.this, error.toString(),
                Toast.LENGTH_LONG).show()){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                int ID = adminSession.getInt("USER_ID", 0);
                if(postImageSelected) {
                    params.put("id_us", String.valueOf(ID));
                    params.put("subject", temaET.getText().toString());
                    params.put("title", titleET.getText().toString());
                    params.put("post_body", publicacionET.getText().toString());
                    params.put("post_image", bitmapToString(postImage));
                }else{
                    params.put("id_us", String.valueOf(ID));
                    params.put("subject", temaET.getText().toString());
                    params.put("title", titleET.getText().toString());
                    params.put("post_body", publicacionET.getText().toString());
                }
                return params;
            }
        };
        Volley.newRequestQueue(AddPost.this).add(stringRequest);
    }

    private String bitmapToString(Bitmap bitm){
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        bitm.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
        byte[] bytes = outputStream.toByteArray();
        return Base64.encodeToString(bytes, Base64.DEFAULT);
    }

    public void closeActivity(View view) {
        finish();
    }

    public void requestImagePick(View view) {
        ActivityCompat.requestPermissions(AddPost.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                CODE_REQUEST_GAL);
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
                Toast.makeText(AddPost.this, "Permiso denegado",
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
                postImage = BitmapFactory.decodeStream(inputStream);
                addImPost.setImageBitmap(postImage);
                postImageSelected = true;
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    public void setImIconVisible(View view) {
        withImage.setTextColor(Color.parseColor("#1D5480"));
        withText.setTextColor(getResources().getColor(R.color.colorBlack));
        addImPost.setVisibility(View.VISIBLE);
    }

    public void setImIconGone(View view) {
        withText.setTextColor(Color.parseColor("#1D5480"));
        withImage.setTextColor(getResources().getColor(R.color.colorBlack));
        if(addImPost.getVisibility() == View.VISIBLE){
            addImPost.setVisibility(View.GONE);
        }
    }
}