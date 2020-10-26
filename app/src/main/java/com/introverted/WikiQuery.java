package com.introverted;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class WikiQuery extends AppCompatActivity {
    private WebView wiki_view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wiki_query);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);

        wiki_view = findViewById(R.id.wiki_view);
        wiki_view.setWebViewClient(new WebViewClient());
        Intent intent = getIntent();
        final String query = intent.getStringExtra("QUERY");
        if(query.equals("artista")){
            wiki_view.loadUrl("http://es.wikipedia.org/wiki/Artista");
        }
        else if(query.equals("otaku")){
            wiki_view.loadUrl("http://es.wikipedia.org/wiki/Otaku");
        }
        else if(query.equals("gamer")){
            wiki_view.loadUrl("http://es.wikipedia.org/wiki/Jugador_de_videojuegos");
        }
        else if(query.equals("melómano")){
            wiki_view.loadUrl("https://es.wiktionary.org/wiki/meloman%C3%ADa");
        }
        else if(query.equals("bibliófilo")){
            wiki_view.loadUrl("http://es.wikipedia.org/wiki/Bibliofilia");
        }
        WebSettings webSettings = wiki_view.getSettings();
        webSettings.setJavaScriptEnabled(true);


    }

    @Override
    public void onBackPressed() {
        if (wiki_view.canGoBack()) {
            wiki_view.goBack();
        } else {
            super.onBackPressed();
        }
    }
}