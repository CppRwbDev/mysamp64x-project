package com.flin.core;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.KeyEvent;
import android.widget.Toast;

import com.flin.online.jsonenter.PublicInfo;
import com.wardrumstudios.utils.WarMedia;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;

public class GTASA extends WarMedia {
    public static GTASA gtasaSelf = null;
    static String vmVersion;
    private boolean once = false;

    {
        vmVersion = null;
        System.out.println("**** Loading SO's");
        try {
            vmVersion = System.getProperty("java.vm.version");
            System.out.println("vmVersion " + vmVersion);
            System.loadLibrary("ImmEmulatorJ");
        } catch (ExceptionInInitializerError | UnsatisfiedLinkError e) {
            // Обработка исключений если необходимо
        }

        String libPath = System.getProperty("java.library.path");
        System.out.println("МИХАИЛ libPath:" + libPath);

        System.loadLibrary("GTASA");
        System.loadLibrary("online"); // Всегда загружаем библиотеку "samp"

        try {
            vmVersion = System.getProperty("java.vm.version");
            System.out.println("vmVersion " + vmVersion);
            System.loadLibrary("ImmEmulatorJ");
            System.out.println("МИХАИЛ Запущен клиент");
        } catch (ExceptionInInitializerError | UnsatisfiedLinkError e) {
            // Обработка исключений если необходимо
        }
    }



    public static void staticEnterSocialClub()
    {
        gtasaSelf.EnterSocialClub();
    }

    public static void staticExitSocialClub() {
        gtasaSelf.ExitSocialClub();
    }

    public void AfterDownloadFunction() {

    }

    public void EnterSocialClub() {

    }

    public void ExitSocialClub() {

    }

    public boolean ServiceAppCommand(String str, String str2)
    {
        return false;
    }

    public int ServiceAppCommandValue(String str, String str2)
    {
        return 0;
    }

    public native void main();

    public void onActivityResult(int i, int i2, Intent intent)
    {
        super.onActivityResult(i, i2, intent);
    }

    public void onConfigurationChanged(Configuration configuration)
    {
        super.onConfigurationChanged(configuration);
    }

    private native void setClientIp(byte[] ip);
    private native void setClientPort(int port);
    private native void setClientLauncherVersion(int version);
    private native void setClientModpackVersion(int version);
    private static native void setClientUuid(byte[] uuid);
    private native void setClientMajorVersion(int version);
    private native void setClientMinorVersion(int version);
    private native boolean isAPAKAndLibSame(int gameEdition);


    public static void setUUID(String uid){
        try {
            byte[] dataBytes = uid.getBytes("windows-1251");
            setClientUuid(dataBytes);
            System.out.println("MIHAIL setUUID"+uid);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            System.out.println("MIHAIL код не выполнился"+uid);

        }
    }

    public void onCreate(Bundle bundle)
    {
        System.out.println("GTASA onCreate");
        gtasaSelf = this;
        wantsMultitouch = true;
        wantsAccelerometer = true;
        super.onCreate(bundle);
    }

    public void onDestroy()
    {
        System.out.println("GTASA onDestroy");
        super.onDestroy();
    }

    public boolean onKeyDown(int i, KeyEvent keyEvent)
    {
        return super.onKeyDown(i, keyEvent);
    }

    public void onPause()
    {
        System.out.println("GTASA onPause");
        super.onPause();
    }

    public void onRestart()
    {
        System.out.println("GTASA onRestart");
        super.onRestart();
    }

    public void onResume()
    {
        System.out.println("GTASA onResume");
        super.onResume();
    }

    public void onStart()
    {
        System.out.println("GTASA onStart");
        super.onStart();
    }

    public void onStop()
    {
        System.out.println("GTASA onStop");

        super.onStop();
    }

    public native void setCurrentScreenSize(int i, int i2);
}