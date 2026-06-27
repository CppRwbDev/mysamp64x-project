package com.flin.online;

import android.app.Application;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.Gravity;
import android.view.KeyEvent;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.wardrumstudios.utils.WarMedia;
import com.yandex.metrica.YandexMetrica;
import com.yandex.metrica.YandexMetricaConfig;
import com.yandex.metrica.push.YandexMetricaPush;

import java.io.UnsupportedEncodingException;

public class GTASA extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        YandexMetricaConfig config = YandexMetricaConfig.newConfigBuilder("30317b43-85e0-400d-8d1f-c32484799569").build();
        YandexMetrica.activate(getApplicationContext(), config);
        YandexMetrica.enableActivityAutoTracking((Application) getApplicationContext());
        YandexMetricaPush.init(getApplicationContext());





        Intent intent=new Intent(this, LoadingApp.class);
        intent.putExtras(getIntent());
        startActivity(intent);
        finish();
    }
}