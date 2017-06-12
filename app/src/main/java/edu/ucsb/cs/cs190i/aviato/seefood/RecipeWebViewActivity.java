package edu.ucsb.cs.cs190i.aviato.seefood;

import android.content.Intent;
import android.net.Uri;
import android.net.http.SslError;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.SslErrorHandler;
import android.webkit.WebView;
import android.webkit.WebViewClient;
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
        webview.setWebViewClient(new NotSafeAtAllClient());
        setContentView(webview);
        webview.loadUrl(url);
    }

    private class NotSafeAtAllClient extends WebViewClient {
        @Override
        public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
            handler.proceed(); // Ignore SSL certificate errors
        }
    }
}
