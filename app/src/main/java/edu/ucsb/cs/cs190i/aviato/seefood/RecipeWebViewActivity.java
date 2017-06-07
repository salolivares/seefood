package edu.ucsb.cs.cs190i.aviato.seefood;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.ListView;

public class RecipeWebViewActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        String url = intent.getStringExtra("recipe_url");

        openWebPage(url);
    }

    private void openWebPage(String url) {
        WebView webview = new WebView(this);
        setContentView(webview);
        webview.loadUrl(url);
    }
}
