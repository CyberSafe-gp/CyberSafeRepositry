package com.example.cybersafe;


import android.os.Bundle;

import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.appcompat.app.AppCompatActivity;


import java.util.Properties;

public class MainActivity  extends AppCompatActivity {


    WebView web;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        web = findViewById(R.id.webView);
        WebSettings webSettings=web.getSettings();
        webSettings.setJavaScriptEnabled(true);
        web.setWebViewClient(new WebViewClient());
        web.loadUrl("https://tikapi.io/account/authorize?client_id=c_8PGS0I4EY9&redirect_uri=https://tikapi.io/success");


       // web.loadUrl("https://tikapi.io/account/authorize?client_id=c_8PGS0I4EY9&redirect_uri=https://tikapi.io/success");
        /*Log.d("Rhino", "onCreate: " + runScript(this));*/


    }}




/*
    public static String runScript(Context androidContextObject) {
        // Get the JavaScript in previous section
        try {

            Resources resources = androidContextObject.getResources();
            InputStream rawResource = resources.openRawResource(R.raw.sdk);


            Properties properties = new Properties();
            properties.load(rawResource);

            String source = properties.getProperty("use strict");
            String functionName = "window.TikApi";
            Object[] functionParams = new Object[]{};
            // Every Rhino VM begins with the enter()
            // This Context is not Android's Context
            org.mozilla.javascript.Context rhino = org.mozilla.javascript.Context.enter();

            // Turn off optimization to make Rhino Android compatible
            rhino.setOptimizationLevel(-1);

            Scriptable scope = rhino.initStandardObjects();

            // This line set the javaContext variable in JavaScript
            //ScriptableObject.putProperty(scope, "javaContext", org.mozilla.javascript.Context.javaToJS(androidContextObject, scope));

            // Note the forth argument is 1, which means the JavaScript source has
            // been compressed to only one line using something like YUI
            rhino.evaluateString(scope, source, "JavaScript", 1, null);

            // We get the hello function defined in JavaScript
            Object obj = scope.get(functionName, scope);

            if (obj instanceof Function) {
                Function function = (Function) obj;
                // Call the hello function with params
                Object result = function.call(rhino, scope, scope, functionParams);
                // After the hello function is invoked, you will see logcat output

                // Finally we want to print the result of hello function
                String response = org.mozilla.javascript.Context.toString(result);
                return response;
            } else {
                return null;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            // We must exit the Rhino VM
            org.mozilla.javascript.Context.exit();
        }

        return null;
    }
    */


