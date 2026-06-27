package com.flin.online.function;

import android.content.Context;
import android.os.Environment;
import android.widget.TextView;

import com.flin.online.jsonenter.PublicInfo;
import com.flinc.core.R;
import com.google.firebase.crashlytics.FirebaseCrashlytics;

import org.ini4j.Wini;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class ReadNick {

    public static void show(TextView textView){

        Wini ini;
        try {
            ini = new Wini(new File(Environment.getExternalStorageDirectory()+"/FlinOnline/files/SAMP/settings.ini"));
            String nickName = ini.get("client", "name");
            PublicInfo.getSelectServerConnectNick = nickName;
            if(nickName != null || nickName != "Flin_Game") {
                textView.setText("Добро пожаловать, "+ nickName+"!");
                FirebaseCrashlytics.getInstance().setUserId(nickName);
            }
            else textView.setText("Добро пожаловать!");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String getNick(){
        String nickName = null;
        Wini ini;
        try {
            ini = new Wini(new File(Environment.getExternalStorageDirectory()+"/FlinOnline/files/SAMP/settings.ini"));
            nickName = ini.get("client", "name");


        } catch (IOException e) {
            e.printStackTrace();
        }
        return nickName;
    }

    public static void writeNickMods(int mod_int){
        try {
            Wini ini = new Wini(new File(Environment.getExternalStorageDirectory()+"/FlinOnline/files/SAMP/settings.ini"));
            String nickName = ini.get("client", "name");

            Wini iniWrite = new Wini(new File(Environment.getExternalStorageDirectory() + "/Android/data/"+PublicInfo.checkReleasePackageName+"/mods/"+mod_int+"/files/SAMP/settings.ini"));
            iniWrite.put("client", "name", nickName);
            iniWrite.store();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
