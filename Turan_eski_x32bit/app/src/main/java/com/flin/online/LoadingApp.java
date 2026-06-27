package com.flin.online;

        import android.Manifest;
        import android.annotation.SuppressLint;
        import android.app.AlertDialog;
        import android.app.Application;
        import android.content.Context;
        import android.content.DialogInterface;
        import android.content.Intent;
        import android.content.pm.PackageInfo;
        import android.content.pm.PackageManager;
        import android.net.Uri;
        import android.os.AsyncTask;
        import android.os.Build;
        import android.os.Environment;
        import android.os.StatFs;

        import androidx.annotation.NonNull;
        import androidx.annotation.RequiresApi;
        import androidx.appcompat.app.AppCompatActivity;
        import androidx.core.app.ActivityCompat;
        import androidx.core.content.ContextCompat;
        import android.os.Bundle;

        import android.util.Log;
        import android.view.Gravity;

        import android.widget.Toast;

        import com.flin.online.function.CheckLoadingFiles;
        import com.flin.online.internet.ConnectionServer;
        import com.flin.online.internet.LinkConnect;

        import com.flin.online.jsonenter.DownloadData;
        import com.flin.online.jsonenter.GetDeviceInfo;
        import com.flin.online.jsonenter.PublicInfo;
        import com.flin.online.model.NewsModel;
        import com.flinc.core.R;
        import com.google.android.gms.tasks.OnCompleteListener;
        import com.google.android.gms.tasks.OnFailureListener;
        import com.google.android.gms.tasks.Task;
        import com.google.firebase.crashlytics.FirebaseCrashlytics;
        import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
        import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings;
        import com.google.gson.Gson;
        import com.google.gson.GsonBuilder;
        import com.loopj.android.http.AsyncHttpClient;
        import com.loopj.android.http.JsonHttpResponseHandler;
        import com.yandex.metrica.YandexMetrica;
        import com.yandex.metrica.YandexMetricaConfig;
        import com.yandex.metrica.push.YandexMetricaPush;


        import org.ini4j.Wini;

        import org.json.JSONException;
        import org.json.JSONObject;


        import java.io.File;
        import java.io.IOException;
        import java.io.UnsupportedEncodingException;

        import java.security.MessageDigest;
        import java.security.NoSuchAlgorithmException;
        import java.util.ArrayList;


        import cz.msebera.android.httpclient.Header;



public class LoadingApp extends AppCompatActivity {
    Context context;
    FirebaseRemoteConfig mFirebaseRemoteConfig;

    @SuppressLint("NewApi")
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
      setContentView(R.layout.activity_loading_app);

        MenuActivity.itemsNews =  new ArrayList<NewsModel>();
//        MenuActivity.itemsNews.add(new NewsModel("Я загрузил эт раньше всех", "1.01.2022", "ввв", "https://i.imgur.com/jh2vVZv.jpeg"));
       // FirebaseCrashlytics.getInstance().setUserId("Test_Ver113");

        //YandexMetricaPush.init(getApplicationContext()); //Yandex Push

        context = getApplicationContext();
        // requestForPermission();

        GetDeviceInfo getDeviceInfo = new GetDeviceInfo();

        //System.out.println("Михаил тест рей:"+ getDeviceInfo.GetDeviceInfo());
        /*
        //System.out.println("Михаил тест мд:"+ PublicInfo.checkVerClientBeta);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
        //  if (Build.VERSION.SDK_INT != Build.VERSION_CODES.KITKAT){
            if (this.checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                new LoadDataStartApp().execute();

                //  Intent intent = new Intent(LoadingApp.this, InstallAPKActivity.class);
                // startActivity(intent);
                //requestData();
            }
            else {
                ActivityCompat.requestPermissions(LoadingApp.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
            }
        }
        else {
                new LoadDataStartApp().execute();

        }
        */

        /*int permissionStatus = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permissionStatus == PackageManager.PERMISSION_GRANTED) {

        } else {

            ActivityCompat.requestPermissions(LoadingApp.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
        }
        */


        YandexMetricaConfig config = YandexMetricaConfig.newConfigBuilder("30317b43-85e0-400d-8d1f-c32484799569").build();
        YandexMetrica.activate(getApplicationContext(), config);
        YandexMetrica.enableActivityAutoTracking((Application) getApplicationContext());


        mFirebaseRemoteConfig = FirebaseRemoteConfig.getInstance();
        FirebaseRemoteConfigSettings configSettings = new FirebaseRemoteConfigSettings.Builder()
                .setMinimumFetchIntervalInSeconds(10)
                .build();
        mFirebaseRemoteConfig.setConfigSettingsAsync(configSettings);
        mFirebaseRemoteConfig.setDefaultsAsync(R.xml.remote_config_defaults);

        YandexMetricaPush.init(getApplicationContext());



        if (Build.VERSION.SDK_INT >= 23) {
            //динамическое получение прав на WRITE_EXTERNAL_STORAGE
            if (checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                mFirebaseRemoteConfig.fetchAndActivate()
                        .addOnCompleteListener(this, new OnCompleteListener<Boolean>() {
                            @Override
                            public void onComplete(@NonNull Task<Boolean> task) {
                                if (task.isSuccessful()) {
                                    boolean updated = task.getResult();
                                    try {
                                        requestDataFirebaseConfig();
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                } else {
                                    requestData();
                                }


                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.i("FLIN", "Error connect firebase");
                                Log.i("FLIN", e+"");
                                requestData();
                            }
                        });
            } else { ;
                //запрашиваем разрешение
                ActivityCompat.requestPermissions(LoadingApp.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
            }
        }
        else {

            mFirebaseRemoteConfig.fetchAndActivate()
                    .addOnCompleteListener(this, new OnCompleteListener<Boolean>() {
                        @Override
                        public void onComplete(@NonNull Task<Boolean> task) {
                            if (task.isSuccessful()) {
                                boolean updated = task.getResult();
                                try {
                                    requestDataFirebaseConfig();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }


                            } else {
                                requestData();
                            }


                        }
                    });
          /*  AlertDialog.Builder builderSelectHelp;
            builderSelectHelp = new AlertDialog.Builder(LoadingApp.this, R.style.AlertDialogTheme);
            builderSelectHelp.setTitle("Flin Online");
            builderSelectHelp.setMessage("Это приложение было создано для более ранней версии Android и может работать со сбоями. Проверьте наличие обновлений или свяжитесь с разработчиком.");
            builderSelectHelp.setPositiveButton("ОК", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    finish();
                }
            });
            ;
            AlertDialog alert = builderSelectHelp.create();
            alert.show();*/
        }


        //  System.out.println("МИХАИЛ Свободно:"+ GetFreeMemory(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + "/")+ " Мб");

        //System.out.println("Crash "+10/0);


        int loading_start;
        int loading_install;
        try {
            Wini ini = new Wini(new File(getExternalFilesDir(null)+"/settings.ini"));
            loading_start = ini.get("app", "loading_start", int.class);
            loading_install = ini.get("app", "install", int.class);
            System.out.println("МИХАИЛ loading_start:111"+loading_start);
            System.out.println("МИХАИЛ loading_install:1111"+loading_install);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }




    //После получения прав возобновить код кнопки
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){


            mFirebaseRemoteConfig = FirebaseRemoteConfig.getInstance();
            FirebaseRemoteConfigSettings configSettings = new FirebaseRemoteConfigSettings.Builder()
                    .setMinimumFetchIntervalInSeconds(10)
                    .build();
            mFirebaseRemoteConfig.setConfigSettingsAsync(configSettings);
            mFirebaseRemoteConfig.setDefaultsAsync(R.xml.remote_config_defaults);
            mFirebaseRemoteConfig.fetchAndActivate()
                    .addOnCompleteListener(this, new OnCompleteListener<Boolean>() {
                        @Override
                        public void onComplete(@NonNull Task<Boolean> task) {
                            if (task.isSuccessful()) {
                                boolean updated = task.getResult();
                                try {
                                    requestDataFirebaseConfig();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            } else {
                                requestData();
                            }


                        }
                    });


        }
        else {
            ActivityCompat.requestPermissions(LoadingApp.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
        }
    }


    public boolean requestForPermission() {
        boolean isPermissionOn = true;
        final int version = Build.VERSION.SDK_INT;
        if (version >= 23) {
            if (!canAccessExternalSd()) {
                isPermissionOn = false;
                ActivityCompat.requestPermissions(this, EXTERNAL_PERMS, EXTERNAL_REQUEST);
            }
            else {
                requestData();
            }
        }
        return isPermissionOn;
    }



    private void requestDataFirebaseConfig() throws IOException {

        PublicInfo.checkReleaseClient = (int) mFirebaseRemoteConfig.getLong("checkReleaseClient");
        PublicInfo.checkReleasePackageName	 = mFirebaseRemoteConfig.getString("checkReleasePackageName");

        PublicInfo.checkFilesGameURL = mFirebaseRemoteConfig.getString("checkFilesGameURL");
        PublicInfo.checkFilesGame1	 = mFirebaseRemoteConfig.getString("checkFilesGame1");
        PublicInfo.checkFilesGame2	 = mFirebaseRemoteConfig.getString("checkFilesGame2");
        PublicInfo.checkFilesGame3	 = mFirebaseRemoteConfig.getString("checkFilesGame3");
        PublicInfo.checkFilesGame4	 = mFirebaseRemoteConfig.getString("checkFilesGame4");
        PublicInfo.checkFilesGame5	 = mFirebaseRemoteConfig.getString("checkFilesGame5");
        PublicInfo.checkFilesGame6	 = mFirebaseRemoteConfig.getString("checkFilesGame6");
        PublicInfo.checkFilesGame7	 = mFirebaseRemoteConfig.getString("checkFilesGame7");
        PublicInfo.checkFilesGame8	 = mFirebaseRemoteConfig.getString("checkFilesGame8");
        PublicInfo.checkFilesGame9	 = mFirebaseRemoteConfig.getString("checkFilesGame9");
        PublicInfo.checkFilesGame10	 = mFirebaseRemoteConfig.getString("checkFilesGame10");
        PublicInfo.checkFilesGame11	 = mFirebaseRemoteConfig.getString("checkFilesGame11");
        PublicInfo.checkFilesGame12	 = mFirebaseRemoteConfig.getString("checkFilesGame12");
        PublicInfo.checkFilesGame13	 = mFirebaseRemoteConfig.getString("checkFilesGame13");
        PublicInfo.checkFilesGame14	 = mFirebaseRemoteConfig.getString("checkFilesGame14");
        PublicInfo.checkFilesGameArchive = mFirebaseRemoteConfig.getString("checkFilesGameArchive");
        PublicInfo.checkFilesGameArchiveVersion	=  Integer.parseInt(mFirebaseRemoteConfig.getString("checkFilesGameArchiveVersion"));

        PublicInfo.checkFilesClientURL	 = mFirebaseRemoteConfig.getString("checkFilesClientURL");
        PublicInfo.checkFilesClient1	 = mFirebaseRemoteConfig.getString("checkFilesClient1");
        PublicInfo.checkFilesClient2	 = mFirebaseRemoteConfig.getString("checkFilesClient2");
        PublicInfo.checkFilesClient3	 = mFirebaseRemoteConfig.getString("checkFilesClient3");
        PublicInfo.checkFilesClientVersion = Integer.parseInt(mFirebaseRemoteConfig.getString("checkFilesClientVersion"));


        PublicInfo.checkFilesClientBetaURL	 = mFirebaseRemoteConfig.getString("checkFilesClientBetaURL");
        PublicInfo.checkFilesClientBeta	 = mFirebaseRemoteConfig.getString("checkFilesClientBeta");
        PublicInfo.checkFilesClientBetaVersion	= Integer.parseInt(mFirebaseRemoteConfig.getString("checkFilesClientBetaVersion"));


        PublicInfo.checkFilesTexctureURL	 = mFirebaseRemoteConfig.getString("checkFilesTexctureURL");
        PublicInfo.checkFilesTexcture1	 = mFirebaseRemoteConfig.getString("checkFilesTexcture1");
        PublicInfo.checkFilesTexcture2	 = mFirebaseRemoteConfig.getString("checkFilesTexcture2");
        PublicInfo.checkFilesTexcture3	 = mFirebaseRemoteConfig.getString("checkFilesTexcture3");
        PublicInfo.checkFilesTexcture4	 = mFirebaseRemoteConfig.getString("checkFilesTexcture4");
        PublicInfo.checkFilesTexcture5	 = mFirebaseRemoteConfig.getString("checkFilesTexcture5");
        PublicInfo.checkFilesTexctureVersion = Integer.parseInt(mFirebaseRemoteConfig.getString("checkFilesTexctureVersion"));



        PublicInfo.checkFilesTextDBURL	= mFirebaseRemoteConfig.getString("checkFilesTextDBURL");
        PublicInfo.checkFilesTextDB1	 = mFirebaseRemoteConfig.getString("checkFilesTextDB1");
        PublicInfo.checkFilesTextDB2	 = mFirebaseRemoteConfig.getString("checkFilesTextDB2");
        PublicInfo.checkFilesTextDB3	 = mFirebaseRemoteConfig.getString("checkFilesTextDB3");
        PublicInfo.checkFilesTextDBVersion = Integer.parseInt(mFirebaseRemoteConfig.getString("checkFilesTextDBVersion"));


       // System.out.println("МИХАИЛ checkFilesTextDBVersion: "+PublicInfo.checkFilesTextDBVersion);

        PublicInfo.checkFilesTexctureURLBeta	 = mFirebaseRemoteConfig.getString("checkFilesTexctureURLBeta");
        PublicInfo.checkFilesTexctureBeta	 = mFirebaseRemoteConfig.getString("checkFilesTexctureBeta");

        PublicInfo.checkFilesPatchUpdateURL	 = mFirebaseRemoteConfig.getString("checkFilesPatchUpdateURL");
        PublicInfo.checkFilesPatchUpdate1	 = mFirebaseRemoteConfig.getString("checkFilesPatchUpdate1");
        PublicInfo.checkFilesPatchUpdate2	 = mFirebaseRemoteConfig.getString("checkFilesPatchUpdate2");
        PublicInfo.checkFilesPatchUpdate3	 = mFirebaseRemoteConfig.getString("checkFilesPatchUpdate3");
        PublicInfo.checkFilesPatchUpdate4	 = mFirebaseRemoteConfig.getString("checkFilesPatchUpdate4");
        PublicInfo.checkFilesPatchUpdate5	 = mFirebaseRemoteConfig.getString("checkFilesPatchUpdate5");
        PublicInfo.checkFilesPatchUpdateVersion	 = Integer.parseInt(mFirebaseRemoteConfig.getString("checkFilesPatchUpdateVersion"));

        PublicInfo.checkDownLoadAPK	 = mFirebaseRemoteConfig.getString("checkDownLoadAPK");
        PublicInfo.checkDownLoadAPKName	 = mFirebaseRemoteConfig.getString("checkDownLoadAPKName");
        PublicInfo.checkDownLoadAPKVersion = Integer.parseInt(mFirebaseRemoteConfig.getString("checkDownLoadAPKVersion"));

        PublicInfo.checkDownLoadBetaAPK	= mFirebaseRemoteConfig.getString("checkDownLoadBetaAPK");
        PublicInfo.checkDownLoadAPKBetaName = mFirebaseRemoteConfig.getString("checkDownLoadAPKBetaName");
        PublicInfo.checkDownLoadBetaVersion	 = Integer.parseInt(mFirebaseRemoteConfig.getString("checkDownLoadBetaVersion"));

        PublicInfo.PayMethodServer1	 = mFirebaseRemoteConfig.getString("PayMethodServer1");
        PublicInfo.PayMethodServer2	 = mFirebaseRemoteConfig.getString("PayMethodServer2");
        PublicInfo.urlMonitoring	 = mFirebaseRemoteConfig.getString("urlMonitoring");

        PublicInfo.contactTelegram	 = mFirebaseRemoteConfig.getString("contactTelegram");
        PublicInfo.contactVK	 = mFirebaseRemoteConfig.getString("contactVK");
        PublicInfo.contactDiscord	 = mFirebaseRemoteConfig.getString("contactDiscord");
        PublicInfo.contactYouTube	 = mFirebaseRemoteConfig.getString("contactYouTube");
        PublicInfo.contactForum	 = mFirebaseRemoteConfig.getString("contactForum");
        PublicInfo.contactSite	 = mFirebaseRemoteConfig.getString("contactSite");
        PublicInfo.urlSendTechProblem	 = mFirebaseRemoteConfig.getString("urlSendTechProblem");

        PublicInfo.urlUpdateAPK	 = mFirebaseRemoteConfig.getString("urlUpdateAPK");
        PublicInfo.verClient	 = Integer.parseInt(mFirebaseRemoteConfig.getString("app_version_client"));
        PublicInfo.verLauncher	 = Integer.parseInt(mFirebaseRemoteConfig.getString("app_version"));

        //Обновление
        PublicInfo.dateUpdateFiles	 = mFirebaseRemoteConfig.getString("dateUpdateFiles");
        PublicInfo.dateUpdateApk	 = mFirebaseRemoteConfig.getString("dateUpdateApk");

        //Новые архивы
        PublicInfo.checkFilesGame7ZURL = mFirebaseRemoteConfig.getString("checkFilesGame7ZURL");
        PublicInfo.checkFilesGame7Z1	 = mFirebaseRemoteConfig.getString("checkFilesGame7Z1");
        PublicInfo.checkFilesGame7Z2	 = mFirebaseRemoteConfig.getString("checkFilesGame7Z2");
        PublicInfo.checkFilesGame7Z3	 = mFirebaseRemoteConfig.getString("checkFilesGame7Z3");
        PublicInfo.checkFilesGame7Z4	 = mFirebaseRemoteConfig.getString("checkFilesGame7Z4");
        PublicInfo.checkFilesGame7Z5	 = mFirebaseRemoteConfig.getString("checkFilesGame7Z5");
        PublicInfo.checkFilesGameArchive7Z	 = mFirebaseRemoteConfig.getString("checkFilesGameArchive7Z");
        PublicInfo.checkFilesGame7ZUpdateLoading	 = Integer.parseInt(mFirebaseRemoteConfig.getString("checkFilesGame7ZUpdateLoading"));
        PublicInfo.checkSelectInstall	 = Integer.parseInt(mFirebaseRemoteConfig.getString("checkSelectInstall"));

        PublicInfo.checkFilesTexctureURL7Z = mFirebaseRemoteConfig.getString("checkFilesTexctureURL7Z");
        PublicInfo.checkFilesTexcture7Z1 = mFirebaseRemoteConfig.getString("checkFilesTexcture7Z1");
        PublicInfo.checkFilesTexcture7Z2 = mFirebaseRemoteConfig.getString("checkFilesTexcture7Z2");
        PublicInfo.checkFilesTexcture7Z3 = mFirebaseRemoteConfig.getString("checkFilesTexcture7Z3");
        PublicInfo.checkFilesTexcture7Z3 = mFirebaseRemoteConfig.getString("checkFilesTexcture7Z3");
        PublicInfo.checkFilesTexcture7Z5 = mFirebaseRemoteConfig.getString("checkFilesTexcture7Z5");
        PublicInfo.checkFilesTexctureVersion7Z	 = Integer.parseInt(mFirebaseRemoteConfig.getString("checkFilesTexctureVersion7Z"));
        PublicInfo.checkFilesClientURL7Z = mFirebaseRemoteConfig.getString("checkFilesClientURL7Z");
        PublicInfo.checkFilesClient7Z1 = mFirebaseRemoteConfig.getString("checkFilesClient7Z1");
        PublicInfo.checkFilesClient7Z2 = mFirebaseRemoteConfig.getString("checkFilesClient7Z2");
        PublicInfo.checkFilesClient7Z3 = mFirebaseRemoteConfig.getString("checkFilesClient7Z3");
        PublicInfo.checkFilesClientVersion7Z	 = Integer.parseInt(mFirebaseRemoteConfig.getString("checkFilesClientVersion7Z"));

        PublicInfo.checkFilesTextDBURL7Z = mFirebaseRemoteConfig.getString("checkFilesTextDBURL7Z");
        PublicInfo.checkFilesTextDB7Z1 = mFirebaseRemoteConfig.getString("checkFilesTextDB7Z1");
        PublicInfo.checkFilesTextDB7Z2 = mFirebaseRemoteConfig.getString("checkFilesTextDB7Z2");
        PublicInfo.checkFilesTextDB7Z3 = mFirebaseRemoteConfig.getString("checkFilesTextDB7Z3");
        PublicInfo.checkFilesTextDBVersion7Z	 = Integer.parseInt(mFirebaseRemoteConfig.getString("checkFilesTextDBVersion7Z"));


        PublicInfo.checkInstallLastVersion	 = Integer.parseInt(mFirebaseRemoteConfig.getString("checkInstallLastVersion"));


        PublicInfo.error_sendMessage = mFirebaseRemoteConfig.getString("errorSendMessage");
        PublicInfo.checkPostDataDonate = mFirebaseRemoteConfig.getString("checkPostDataDonate");
        PublicInfo.sendGameTelegram = mFirebaseRemoteConfig.getString("sendGameTelegram");


        //Mods
        PublicInfo.checkInstallModsLastVersion	 = Integer.parseInt(mFirebaseRemoteConfig.getString("checkInstallModsLastVersion"));
        PublicInfo.jsonMods	 = mFirebaseRemoteConfig.getString("json_mods");
        PublicInfo.checkModsURL	 = mFirebaseRemoteConfig.getString("checkModsURL");
        PublicInfo.json_news	 = mFirebaseRemoteConfig.getString("json_news");

        PublicInfo.dialogID_pass	 = Integer.parseInt(mFirebaseRemoteConfig.getString("dialogID_pass"));
        PublicInfo.dialogID_admPass	 = Integer.parseInt(mFirebaseRemoteConfig.getString("dialogID_admPass"));
        PublicInfo.dialogID_google	 = Integer.parseInt(mFirebaseRemoteConfig.getString("dialogID_google"));

        PublicInfo.loagingNews	 = Integer.parseInt(mFirebaseRemoteConfig.getString("loagingNews"));

       // System.out.println("MIHAIL json "+PublicInfo.json_news);

        //   PublicInfo.verClient = 50;
        //Донат
        PublicInfo.donatetype 	 = Integer.parseInt(mFirebaseRemoteConfig.getString("donatetype"));
        System.out.println("МИХАИЛ RemoteConfig verClient: "+PublicInfo.verClient+"URL: "+PublicInfo.checkFilesGameURL+" checkFilesGame4: "+PublicInfo.checkFilesGame4);

        // new LoadDataModsApp().execute();

        PublicInfo.checkCountDownloadsFiles =1;

        //PublicInfo.checkReleaseClient = 2;


        //PublicInfo.checkFilesGame7ZUpdateLoading = 0;
        //PublicInfo.checkFilesGame7ZURL = "https://d1.flin-rp.com/client/filesgame/7z/06.01.2023_android12_1/";
        //PublicInfo.checkFilesGame7ZURL = "https://d1.flin-rp.com/client/filesgame/7z/11.01.22/";
        //CRMP
        //PublicInfo.checkFilesGame7ZURL = "https://d1.flin-rp.com/client/filesgame/7z/06.01.2023_o/";

        /*PublicInfo.checkFilesClientURL7Z = "https://d1.flin-rp.com/client/filesclient/7z/27.02.2023_02/";
        PublicInfo.checkFilesTexctureURL7Z = "https://d1.flin-rp.com/client/filestexture/7z/27.02.2023_02/";
        PublicInfo.checkFilesTexctureVersion7Z = 40;
        PublicInfo.dateUpdateFiles = "FIX crash PVR №2";*/

/*
        PublicInfo.checkFilesTexctureVersion7Z = 41;
        PublicInfo.checkFilesGame7ZUpdateLoading = 0;
        PublicInfo.checkFilesGame7ZURL = "https://d1.flin-rp.com/client/filesgame/7z/11.01.22/";
 */
        if(PublicInfo.checkFilesGame1.trim().length() > 1) PublicInfo.checkCountDownloadsFiles+=2;
        if(PublicInfo.checkFilesGame2.trim().length() > 1) PublicInfo.checkCountDownloadsFiles++;
        if(PublicInfo.checkFilesGame3.trim().length() > 1) PublicInfo.checkCountDownloadsFiles++;
        if(PublicInfo.checkFilesGame4.trim().length() > 1) PublicInfo.checkCountDownloadsFiles++;
        if(PublicInfo.checkFilesGame5.trim().length() > 1) PublicInfo.checkCountDownloadsFiles++;
        if(PublicInfo.checkFilesGame6.trim().length() > 1) PublicInfo.checkCountDownloadsFiles++;
        if(PublicInfo.checkFilesGame7.trim().length() > 1) PublicInfo.checkCountDownloadsFiles++;
        if(PublicInfo.checkFilesGame8.trim().length() > 1) PublicInfo.checkCountDownloadsFiles++;
        if(PublicInfo.checkFilesGame9.trim().length() > 1) PublicInfo.checkCountDownloadsFiles++;
        if(PublicInfo.checkFilesGame10.trim().length() > 1) PublicInfo.checkCountDownloadsFiles++;
        if(PublicInfo.checkFilesGame11.trim().length() > 1) PublicInfo.checkCountDownloadsFiles++;
        if(PublicInfo.checkFilesGame12.trim().length() > 1) PublicInfo.checkCountDownloadsFiles++;
        if(PublicInfo.checkFilesGame13.trim().length() > 1) PublicInfo.checkCountDownloadsFiles++;
        if(PublicInfo.checkFilesGame14.trim().length() > 1) PublicInfo.checkCountDownloadsFiles++;

        if(PublicInfo.checkFilesClient1.trim().length() > 1) PublicInfo.checkCountDownloadsFiles+=2;
        if(PublicInfo.checkFilesClient2.trim().length() > 1) PublicInfo.checkCountDownloadsFiles++;
        if(PublicInfo.checkFilesClient3.trim().length() > 1) PublicInfo.checkCountDownloadsFiles++;

        if(PublicInfo.checkFilesTexcture1.trim().length() > 1) PublicInfo.checkCountDownloadsFiles+=2;
        if(PublicInfo.checkFilesTexcture2.trim().length() > 1) PublicInfo.checkCountDownloadsFiles++;
        if(PublicInfo.checkFilesTexcture3.trim().length() > 1) PublicInfo.checkCountDownloadsFiles++;
        if(PublicInfo.checkFilesTexcture4.trim().length() > 1) PublicInfo.checkCountDownloadsFiles++;
        if(PublicInfo.checkFilesTexcture5.trim().length() > 1) PublicInfo.checkCountDownloadsFiles++;

        if(PublicInfo.checkFilesPatchUpdate1.trim().length() > 1) PublicInfo.checkCountDownloadsFiles++;
        if(PublicInfo.checkFilesPatchUpdate2.trim().length() > 1) PublicInfo.checkCountDownloadsFiles++;
        if(PublicInfo.checkFilesPatchUpdate3.trim().length() > 1) PublicInfo.checkCountDownloadsFiles++;
        if(PublicInfo.checkFilesPatchUpdate4.trim().length() > 1) PublicInfo.checkCountDownloadsFiles++;
        if(PublicInfo.checkFilesPatchUpdate5.trim().length() > 1) PublicInfo.checkCountDownloadsFiles++;

        //Новые архивы
        if(PublicInfo.checkFilesGame7Z1.trim().length() > 1) PublicInfo.checkCountDownloadsFiles7z+=2;
        if(PublicInfo.checkFilesGame7Z2.trim().length() > 1) PublicInfo.checkCountDownloadsFiles7z++;
        if(PublicInfo.checkFilesGame7Z3.trim().length() > 1) PublicInfo.checkCountDownloadsFiles7z++;
        if(PublicInfo.checkFilesGame7Z4.trim().length() > 1) PublicInfo.checkCountDownloadsFiles7z++;
        if(PublicInfo.checkFilesGame7Z5.trim().length() > 1) PublicInfo.checkCountDownloadsFiles7z++;


        //Для проверки обновы
       /* PublicInfo.checkFilesTexctureVersion7Z = 55;
        PublicInfo.checkFilesTexctureURL7Z = "https://d1.flin-rp.com/client/filestexture/7z/05.12.2022/";
        PublicInfo.checkFilesClientURL7Z = "https://d1.flin-rp.com/client/filesclient/7z/05.12.2022/";
        PublicInfo.checkFilesGame7ZUpdateLoading = 1;*/



            String GetLogPath = Environment.getExternalStorageDirectory()  + "/FlinOnline/";
            String GetFileLogPath = Environment.getExternalStorageDirectory()  + "/FlinOnline/logcat2.txt";
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


        if(CheckLoadingFiles.game()) {
            Intent intent = new Intent(LoadingApp.this, MenuActivity.class);
            startActivity(intent);
        }
        else {
            File file = new File(Environment.getExternalStorageDirectory() + "/Android/data/"+PublicInfo.checkReleasePackageName+"/files/texdb/gta3.img");
            if(file.exists()){
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Изменение директории игры!")
                        .setMessage("Ранее директория игры находилась в системной папке, что могло вызывать некоторые неудобства при ее поиске и установке других сборок. Однако теперь мы приняли решение переместить директорию игры возле Android в папку FlinOnline, что упростит ее доступность для игроков.\\nСтоит отметить, что после такого перемещения игра будет полностью скачена заново в новую директорию. Это связано с тем, что файлы игры были адаптированы под старую директорию, и для правильной работы игры в новой папке необходимо загрузить ее заново.\\nНесмотря на это, новое расположение директории игры не должно причинить проблем пользователям. Более того, оно может повысить удобство и быстроту доступа к игре.")
                        //         .setIcon(R.mipmap.ic_launcher)
                        .setCancelable(false)
                        .setNegativeButton("Отмена",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.cancel();
                                        finish();
                                    }
                                })
                        .setPositiveButton("Загрузить",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.cancel();
                                        Intent intent = new Intent(LoadingApp.this, LoadingGPU.class);
                                        startActivity(intent);
                                    }
                                });

                AlertDialog alert = builder.create();
                alert.show();

            }
            else {
                Intent intent = new Intent(LoadingApp.this, LoadingGPU.class);
                startActivity(intent);
            }
        }
    }


    private void requestData() {
        //Everything below is part of the Android Asynchronous HTTP Client
        AsyncHttpClient client = new AsyncHttpClient(true, 80, 443);
        //  AsyncHttpClient client = new AsyncHttpClient();
        client.addHeader("Content-Type", "text/xml; charset=utf-8");
    //    client.get(LinkConnect.URL_GET_LINK_SERVER_FULL, new JsonHttpResponseHandler() {
            client.get("https://raw.githubusercontent.com/m-dimkov/launcher/master/client_launcher.json", new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers,
                                  JSONObject response) {
                System.out.println("Михаил response:"+response);
                GsonBuilder builder = new GsonBuilder();
                Gson gson = builder.create();
                DownloadData info = gson.fromJson(response.toString(), DownloadData.class);

                //Log.i("Вывод", "ДО nameUpdateClientFull: " + PublicInfo.nameUpdateClientFull + " checksumInstallFilesMail: " + PublicInfo.checksumInstallFilesMail);
                PublicInfo.jsonVersion = info.jsonVersion;



                PublicInfo.	checkReleaseClient = info.checkReleaseClient	;
                PublicInfo.	checkCountDownloadsFiles	 = info.checkCountDownloadsFiles	;
                PublicInfo.	checkCountDownloadsFilesCurrent	 = info.checkCountDownloadsFilesCurrent	;
                PublicInfo.	checkReleasePackageName	 = info.	checkReleasePackageName	;

                PublicInfo.	checkFilesGameURL	 = info.checkFilesGameURL	;
                PublicInfo.	checkFilesGame1	 = info.checkFilesGame1	;
                PublicInfo.	checkFilesGame2	 = info.checkFilesGame2	;
                PublicInfo.	checkFilesGame3	 = info.checkFilesGame3	;
                PublicInfo.	checkFilesGame4	 = info.checkFilesGame4	;
                PublicInfo.	checkFilesGame5	 = info.checkFilesGame5	;
                PublicInfo.	checkFilesGame6	 = info.checkFilesGame6	;
                PublicInfo.	checkFilesGame7	 = info.checkFilesGame7	;
                PublicInfo.	checkFilesGame8	 = info.checkFilesGame8	;
                PublicInfo.	checkFilesGame9	 = info.checkFilesGame9	;
                PublicInfo.	checkFilesGame10	 = info.checkFilesGame10	;
                PublicInfo.	checkFilesGame11	 = info.checkFilesGame11	;
                PublicInfo.	checkFilesGame12	 = info.checkFilesGame12	;
                PublicInfo.	checkFilesGame13	 = info.checkFilesGame13	;
                PublicInfo.	checkFilesGame14	 = info.checkFilesGame14	;
                PublicInfo.	checkFilesGameArchive	 = info.checkFilesGameArchive	;
                PublicInfo.	checkFilesGameArchiveVersion	 = info.checkFilesGameArchiveVersion	;

                PublicInfo.checkFilesClientURL	 = info.checkFilesClientURL	;
                PublicInfo.	checkFilesClient1	 = info.checkFilesClient1	;
                PublicInfo.	checkFilesClient2	 = info.checkFilesClient2	;
                PublicInfo.	checkFilesClient3	 = info.checkFilesClient3	;
                PublicInfo.	checkFilesClientVersion	 = info.checkFilesClientVersion	;


                PublicInfo.	checkFilesClientBetaURL	 = info.checkFilesClientBetaURL	;
                PublicInfo.	checkFilesClientBeta	 = info.checkFilesClientBeta	;
                PublicInfo.	checkFilesClientBetaVersion	 = info.checkFilesClientBetaVersion	;


                PublicInfo.	checkFilesTexctureURL	 = info.checkFilesTexctureURL	;
                PublicInfo.	checkFilesTexcture1	 = info.checkFilesTexcture1	;
                PublicInfo.	checkFilesTexcture2	 = info.checkFilesTexcture2	;
                PublicInfo.	checkFilesTexcture3	 = info.checkFilesTexcture3	;
                PublicInfo.	checkFilesTexcture4	 = info.checkFilesTexcture4	;
                PublicInfo.	checkFilesTexcture5	 = info.checkFilesTexcture5	;
                PublicInfo.	checkFilesTexctureVersion	 = info.checkFilesTexctureVersion	;

                PublicInfo.	checkFilesTexctureURLBeta	 = info.checkFilesTexctureURLBeta	;
                PublicInfo.	checkFilesTexctureBeta	 = info.checkFilesTexctureBeta	;

                PublicInfo.	checkFilesPatchUpdateURL	 = info.checkFilesPatchUpdateURL	;
                PublicInfo.	checkFilesPatchUpdate1	 = info.checkFilesPatchUpdate1	;
                PublicInfo.	checkFilesPatchUpdate2	 = info.checkFilesPatchUpdate2	;
                PublicInfo.	checkFilesPatchUpdate3	 = info.checkFilesPatchUpdate3	;
                PublicInfo.	checkFilesPatchUpdate4	 = info.checkFilesPatchUpdate4	;
                PublicInfo.	checkFilesPatchUpdate5	 = info.checkFilesPatchUpdate5	;
                PublicInfo.	checkFilesPatchUpdateVersion	 = info.checkFilesPatchUpdateVersion	;


                PublicInfo.	checkDownLoadAPK	 = info.checkDownLoadAPK	;
                PublicInfo.	checkDownLoadAPKName	 = info.checkDownLoadAPKName	;
                PublicInfo.	checkDownLoadAPKVersion	 = info.checkDownLoadAPKVersion	;

                PublicInfo.	checkDownLoadBetaAPK	 = info.checkDownLoadBetaAPK;	;
                PublicInfo.	checkDownLoadAPKBetaName	 = info.checkDownLoadAPKBetaName	;
                PublicInfo.	checkDownLoadBetaVersion	 = info.checkDownLoadBetaVersion	;

                PublicInfo.	PayMethodServer1	 = info.PayMethodServer1;
                PublicInfo.	PayMethodServer2	 = info.PayMethodServer2	;
                PublicInfo.urlMonitoring	 = info.urlMonitoring;


                PublicInfo.contactTelegram	 = info.contactTelegram;
                PublicInfo.contactVK	 = info.contactVK;
                PublicInfo.contactDiscord	 = info.contactDiscord;
                PublicInfo.contactYouTube	 = info.contactYouTube;
                PublicInfo.contactForum	 = info.contactForum;
                PublicInfo.contactSite	 = info.contactSite;

                PublicInfo.urlUpdateAPK	 = info.urlUpdateAPK;
                PublicInfo.verClient	 = info.verClient;
                PublicInfo.verLauncher	 = info.verLauncher;

                //Донат
                PublicInfo.donatetype 	 = info.donatetype;


                // new LoadDataModsApp().execute();

                PublicInfo.checkCountDownloadsFiles =1;

                //PublicInfo.checkReleaseClient = 2;



                if(PublicInfo.checkFilesGame1.trim().length() > 1) PublicInfo.checkCountDownloadsFiles+=2;
                if(PublicInfo.checkFilesGame2.trim().length() > 1) PublicInfo.checkCountDownloadsFiles++;
                if(PublicInfo.checkFilesGame3.trim().length() > 1) PublicInfo.checkCountDownloadsFiles++;
                if(PublicInfo.checkFilesGame4.trim().length() > 1) PublicInfo.checkCountDownloadsFiles++;
                if(PublicInfo.checkFilesGame5.trim().length() > 1) PublicInfo.checkCountDownloadsFiles++;
                if(PublicInfo.checkFilesGame6.trim().length() > 1) PublicInfo.checkCountDownloadsFiles++;
                if(PublicInfo.checkFilesGame7.trim().length() > 1) PublicInfo.checkCountDownloadsFiles++;
                if(PublicInfo.checkFilesGame8.trim().length() > 1) PublicInfo.checkCountDownloadsFiles++;
                if(PublicInfo.checkFilesGame9.trim().length() > 1) PublicInfo.checkCountDownloadsFiles++;
                if(PublicInfo.checkFilesGame10.trim().length() > 1) PublicInfo.checkCountDownloadsFiles++;
                if(PublicInfo.checkFilesGame11.trim().length() > 1) PublicInfo.checkCountDownloadsFiles++;
                if(PublicInfo.checkFilesGame12.trim().length() > 1) PublicInfo.checkCountDownloadsFiles++;
                if(PublicInfo.checkFilesGame13.trim().length() > 1) PublicInfo.checkCountDownloadsFiles++;
                if(PublicInfo.checkFilesGame14.trim().length() > 1) PublicInfo.checkCountDownloadsFiles++;

                if(PublicInfo.checkFilesClient1.trim().length() > 1) PublicInfo.checkCountDownloadsFiles+=2;
                if(PublicInfo.checkFilesClient2.trim().length() > 1) PublicInfo.checkCountDownloadsFiles++;
                if(PublicInfo.checkFilesClient3.trim().length() > 1) PublicInfo.checkCountDownloadsFiles++;

                if(PublicInfo.checkFilesTexcture1.trim().length() > 1) PublicInfo.checkCountDownloadsFiles+=2;
                if(PublicInfo.checkFilesTexcture2.trim().length() > 1) PublicInfo.checkCountDownloadsFiles++;
                if(PublicInfo.checkFilesTexcture3.trim().length() > 1) PublicInfo.checkCountDownloadsFiles++;
                if(PublicInfo.checkFilesTexcture4.trim().length() > 1) PublicInfo.checkCountDownloadsFiles++;
                if(PublicInfo.checkFilesTexcture5.trim().length() > 1) PublicInfo.checkCountDownloadsFiles++;

                if(PublicInfo.checkFilesPatchUpdate1.trim().length() > 1) PublicInfo.checkCountDownloadsFiles++;
                if(PublicInfo.checkFilesPatchUpdate2.trim().length() > 1) PublicInfo.checkCountDownloadsFiles++;
                if(PublicInfo.checkFilesPatchUpdate3.trim().length() > 1) PublicInfo.checkCountDownloadsFiles++;
                if(PublicInfo.checkFilesPatchUpdate4.trim().length() > 1) PublicInfo.checkCountDownloadsFiles++;
                if(PublicInfo.checkFilesPatchUpdate5.trim().length() > 1) PublicInfo.checkCountDownloadsFiles++;

              /*PublicInfo.qiwiURLProblem = "http://mdimkov.online/statuspay.php?nick=";
                PublicInfo.qiwiTypePayment = 1;
                PublicInfo.donatetype = 1;
                PublicInfo.qiwiPayUrl = "http://mdimkov.online/pay.php?sum=";
                */

                try {
                    if(CheckLoadingFiles.game()) {
                        Intent intent = new Intent(LoadingApp.this, MenuActivity.class);
                        startActivity(intent);
                    }
                    else {
                        Intent intent = new Intent(LoadingApp.this, LoadingGPU.class);
                        startActivity(intent);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }


            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable e, JSONObject response) {

                Toast toast = Toast.makeText(getApplicationContext(),
                        "Ошибка с соединением сервера! Проверьте подключение или попробуйте позже.",
                        Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
                finish();
            }
        });
    }



    public static int GetFreeMemory(String fileName) {
        File f = new File(fileName);
        StatFs stat = new StatFs(f.getPath());
        long bytesAvailable = stat.getBlockSizeLong() * stat.getAvailableBlocksLong();
        int total = (int) (bytesAvailable / (1024.f * 1024.f));
        return total;
    }

    public final String[] EXTERNAL_PERMS = {Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE};
    public final int EXTERNAL_REQUEST = 138;

    public boolean canAccessExternalSd() {
        return (hasPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE));
    }

    private boolean hasPermission(String perm) {
        return (PackageManager.PERMISSION_GRANTED == ContextCompat.checkSelfPermission(this, perm));
    }

    public static long folderSize(File directory) {
        if (directory == null ){
            return 0;
        }
        long length = 0;
        for (File file : directory.listFiles()) {
            if (file.isFile())
                length += file.length();
            else
                length += folderSize(file);
        }
        return length;
    }




    public static long getFolderSize(File file) {
        long size = 0;
        if (file.isDirectory()) {
            for (File child : file.listFiles()) {
                size += getFolderSize(child);
            }
        } else {
            size = file.length();
        }
        return size;
    }

    public Boolean installClient(String packageName) {
        android.content.Intent launchIntent = getPackageManager().getLaunchIntentForPackage(packageName);
        if (launchIntent != null) {
            return true;
        } else return false;
    }

    public static String SHA1(String text) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        MessageDigest md = MessageDigest.getInstance("SHA-1");
        byte[] textBytes = text.getBytes("iso-8859-1");
        md.update(textBytes, 0, textBytes.length);
        byte[] sha1hash = md.digest();
        return convertToHex(sha1hash);
    }


    private static String convertToHex(byte[] data) {
        StringBuilder buf = new StringBuilder();
        for (byte b : data) {
            int halfbyte = (b >>> 4) & 0x0F;
            int two_halfs = 0;
            do {
                buf.append((0 <= halfbyte) && (halfbyte <= 9) ? (char) ('0' + halfbyte) : (char) ('a' + (halfbyte - 10)));
                halfbyte = b & 0x0F;
            } while (two_halfs++ < 1);
        }
        return buf.toString();
    }

    public class LoadDataModsApp extends AsyncTask<Void, Void, String> {

        @Override
        protected String doInBackground(Void... params) {
            return ConnectionServer.getJSON(LinkConnect.URL_GET_LINK_CHECK_MODS);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @RequiresApi(api = Build.VERSION_CODES.N)
        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            System.out.println("Михаил Вывод сайта:" + s);
            try {
                JSONObject object = new JSONObject(s);
                JSONObject responseObject = object.getJSONObject("response");

                PublicInfo.modsVersion = responseObject.getInt("VersionMods");
                PublicInfo.modsVersionCancel = responseObject.getInt("VersionCancel");
                PublicInfo.modsAccess = responseObject.getInt("Mod");
                PublicInfo.modsAccessText = responseObject.getString("ModText");
                PublicInfo.modsDomainHost = responseObject.getString("domainHost");
                PublicInfo.modsFolderMods = responseObject.getString("folderMods");
                PublicInfo.modsName = responseObject.getString("modName");
                PublicInfo.modsSize = responseObject.getInt("modSize");
                PublicInfo.modsCheckSum = responseObject.getString("modCheckSum");

                //    System.out.println("Михаил мод загрузка: modsVersion:"+ PublicInfo.modsVersion+ " modsVersionCancel"+ PublicInfo.modsVersionCancel+" Доступ: "+ PublicInfo.modsAccess+ " Мод текст: "+PublicInfo.modsAccessText+" Домен: "+PublicInfo.modsDomainHost+ " Путь " + PublicInfo.modsFolderMods+" Имя: "+PublicInfo.modsName+" Мод размер: "+PublicInfo.modsSize+ " Чек сумм "+PublicInfo.modsCheckSum );
            } catch (JSONException e1) {
                e1.printStackTrace();
            }
        }
    }




}
