package com.flin.online;

import android.annotation.TargetApi;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.flin.online.jsonenter.PublicInfo;
import com.flinc.core.R;

import androidx.appcompat.app.AppCompatActivity;

import static com.flin.online.jsonenter.PublicInfo.donateUrl;
import static com.flin.online.jsonenter.PublicInfo.successDonate;


public class WebDonateActivity extends AppCompatActivity {


    int loadPage = 1;

    private WebView webView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_donate);

        webView = findViewById(R.id.webView);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setCacheMode(WebSettings.LOAD_DEFAULT);
        webView.getSettings().setDomStorageEnabled(true);
        webView.setWebViewClient(new MyWebViewClient());
        webView.loadUrl(donateUrl);

    }


    private class MyWebViewClient extends WebViewClient {
        @TargetApi(Build.VERSION_CODES.N)
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
            String url = request.getUrl().toString();
            //     Toast.makeText(getApplicationContext(), "Открыт: "+ url, Toast.LENGTH_SHORT).show();
            if(url.contains("https://flin-rp.com/donate_success.php") || url.contains("http://flin-rp.com/donate_success.php"))
            {
                successDonate = 1;
                Intent intent = new Intent(WebDonateActivity.this, MenuActivity.class);
                startActivity(intent);
                return false;
            }
            if(url.contains("https://flin-rp.com/donate_unsuccess.php")  || url.contains("http://flin-rp.com/donate_unsuccess.php"))
            {
                successDonate = 2;
                Intent intent = new Intent(WebDonateActivity.this, MenuActivity.class);
                startActivity(intent);
                return false;
            }

            view.loadUrl(request.getUrl().toString());
            return true;
        }

        // Для старых устройств
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {

            if(url.contains("https://flin-rp.com/donate_success.php") || url.contains("http://flin-rp.com/donate_success.php"))
            {
                successDonate = 1;
                Intent intent = new Intent(WebDonateActivity.this, MenuActivity.class);
                startActivity(intent);
                return false;
            }
            if(url.contains("https://flin-rp.com/donate_unsuccess.php")  || url.contains("http://flin-rp.com/donate_unsuccess.php"))
            {
                successDonate = 2;
                Intent intent = new Intent(WebDonateActivity.this, MenuActivity.class);
                startActivity(intent);
                return false;
            }
            view.loadUrl(url);
            return true;
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            if(url.contains("https://flin-rp.com/donate_success.php") || url.contains("http://flin-rp.com/donate_success.php"))
            {
                successDonate = 1;
                Intent intent = new Intent(WebDonateActivity.this, MenuActivity.class);
                startActivity(intent);

            }
            if(url.contains("https://flin-rp.com/donate_unsuccess.php")  || url.contains("http://flin-rp.com/donate_unsuccess.php"))
            {
                successDonate = 2;
                Intent intent = new Intent(WebDonateActivity.this, MenuActivity.class);
                startActivity(intent);
            }
            //  Toast.makeText(getApplicationContext(), "Страница загружена!", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
            if(loadPage == 0){
                loadPage++;
                Toast toast = Toast.makeText(getApplicationContext(),
                        "Идет загрузка страницы",
                        Toast.LENGTH_LONG);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
            }
            else {
                Toast toast = Toast.makeText(getApplicationContext(),
                        "Идет загрузка страницы",
                        Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
            }

            //Toast.makeText(getApplicationContext(), "Начата загрузка страницы", Toast.LENGTH_SHORT).show();
            if(url.contains("https://flin-rp.com/donate_success.php") || url.contains("http://flin-rp.com/donate_success.php"))
            {
                successDonate = 1;
                Intent intent = new Intent(WebDonateActivity.this, MenuActivity.class);
                startActivity(intent);
            }
            if(url.contains("https://flin-rp.com/donate_unsuccess.php")  || url.contains("http://flin-rp.com/donate_unsuccess.php"))
            {
                successDonate = 2;
                Intent intent = new Intent(WebDonateActivity.this, MenuActivity.class);
                startActivity(intent);
            }
        }

    }




}
