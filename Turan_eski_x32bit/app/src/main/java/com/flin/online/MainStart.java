package com.flin.online;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.Gravity;
import android.widget.Toast;

import com.flin.online.function.CheckLoadingFiles;
import com.flin.online.function.SendPostRequest;
import com.flin.online.jsonenter.PublicInfo;
import com.flinc.core.R;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.yandex.metrica.push.common.utils.Tracker;

import org.ini4j.Wini;

import java.io.File;
import java.io.IOException;



public class MainStart extends AppCompatActivity {
    private FirebaseAnalytics mFirebaseAnalytics;
    int gg = 0;
    boolean bOnce = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_start);

        System.out.println("Mihail checkPostDataDonate - "+PublicInfo.sendGameTelegram);
        if(PublicInfo.sendGameTelegram.length() > 3){
            try {
                Wini ini  = new Wini(new File(Environment.getExternalStorageDirectory() + "/FlinOnline/files/SAMP/settings.ini"));
                String nickName = ini.get("client", "name");
                new SendPostRequest().execute(PublicInfo.sendGameTelegram, nickName, "зашел в игру (v2.1)");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

/*
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        Bundle bundle = new Bundle();
        bundle.putString(FirebaseAnalytics.Param.ITEM_ID, "1");
        //bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, "Dimkov");
        //bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, "startGame");
        mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);


        Bundle params = new Bundle();
        params.putString("image_name", "StartGame");
        //params.putString("full_text", "Запуск игры Dimkov");
        mFirebaseAnalytics.logEvent("start_game", params);


        Bundle bundles = new Bundle();
        bundles.putString(FirebaseAnalytics.Param.METHOD, "startGamemethod");
        mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.LOGIN, bundle);
*/

        int mod_int = 0;
        try {
            mod_int = CheckLoadingFiles.getModsID();
        } catch (IOException e) {
            e.printStackTrace();
        }

        if(mod_int == 0) PublicInfo.GTAmods = 0;
        else {
            try {
                if(CheckLoadingFiles.getMods(mod_int)){
                    PublicInfo.GTAmods = 1;
                } else PublicInfo.GTAmods = 0;
            } catch (IOException e) {
                e.printStackTrace();
                PublicInfo.GTAmods = 0;
            }

        }
        if (!bOnce) {
            bOnce = true;
            String GetLogPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + "/" + "FlinLog";
            String GetFileLogPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + "/FlinLog/logcat.txt";
            try {
                if (!new File(GetLogPath).exists()) {
                    new File(GetLogPath).mkdirs();
                }
                File file = new File(GetFileLogPath);
                if (file.exists()) {
                    file.delete();
                }
                file.createNewFile();
                Runtime runtime = Runtime.getRuntime();
                runtime.exec("logcat -f " + file.getAbsolutePath());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        System.out.println("**** Loading MainStart");
        if(gg == 0) {
            gg = 1;
            finish();
            /*Intent intent = new Intent(this, com.flinc.core.GTASA.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK |Intent.FLAG_ACTIVITY_CLEAR_TOP);

            startActivity(intent);
*/
            //МИши код

            Intent intent = new Intent(MainStart.this, com.flin.core.GTASA.class);
            intent.putExtras(getIntent());

            startActivity(intent);
        }
        else {
            Toast toast = Toast.makeText(getApplicationContext(),
                    "Game запуск 2 raz!",
                    Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
            System.out.println("Mihail 2 запуск!!!");
        }
        finish();
    }
}