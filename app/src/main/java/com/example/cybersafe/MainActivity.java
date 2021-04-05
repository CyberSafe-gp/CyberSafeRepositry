package com.example.cybersafe;


import android.annotation.TargetApi;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import android.util.Log;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.WebMessage;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.List;


public class MainActivity  extends AppCompatActivity {

    WebView web;
    // creating a variable for our text view
    private TextView messageTV;
    Button button;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent intent = getIntent();
        String action = intent.getAction();
        Uri data = intent.getData();

       /* messageTV.setText(data.getHost());*/

        button = findViewById(R.id.button);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri = Uri.parse("https://tikapi.io/account/authorize?client_id=c_8PGS0I4EY9redirect_uri=http://www.cybersafe.com"); // missing 'http://' will cause crashed
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            }
        });





/*        web = (WebView) findViewById(R.id.webView);
        WebSettings webSettings = web.getSettings();

        webSettings.setJavaScriptEnabled(true);

        //*enabled dom storage*
        webSettings.setDomStorageEnabled(true);
        //enabling javascript
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
        //database enabled
        webSettings.setDatabaseEnabled(true);
        //setwebcclient
        web.setWebViewClient(new WebViewClient());

        web.loadUrl("https://cybersafe-52e21.web.app/apiauthintication.html");*/


    }

    @Override
    protected void onResume() {
        super.onResume();

    }
}