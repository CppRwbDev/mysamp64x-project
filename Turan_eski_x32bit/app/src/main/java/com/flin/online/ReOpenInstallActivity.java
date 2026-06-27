package com.flin.online;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.widget.TextView;

import com.flin.online.install.InstallClient;
import com.flin.online.jsonenter.PublicInfo;
import com.flinc.core.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings;

import static com.flin.online.LoadingApp.GetFreeMemory;

public class ReOpenInstallActivity extends AppCompatActivity {
    FirebaseRemoteConfig mFirebaseRemoteConfig;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_re_open_install);

        TextView infoVersionApp = (TextView) findViewById(R.id.textViewVersion);
        infoVersionApp.setText("v"+ PublicInfo.VersionNameAppStatic+" (Build "+PublicInfo.VersionAppStatic+")");

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
                            requestDataFirebaseConfig();
                        } else {
                          //  requestData();
                        }


                    }
                });

    }


    private void requestDataFirebaseConfig() {

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

        PublicInfo.urlUpdateAPK	 = mFirebaseRemoteConfig.getString("urlUpdateAPK");
        PublicInfo.verClient	 = Integer.parseInt(mFirebaseRemoteConfig.getString("app_version_client"));
        PublicInfo.verLauncher	 = Integer.parseInt(mFirebaseRemoteConfig.getString("app_version"));

        //Донат
        PublicInfo.donatetype 	 = Integer.parseInt(mFirebaseRemoteConfig.getString("donatetype"));
        System.out.println("МИХАИЛ RemoteConfig verClient: "+PublicInfo.verClient+"URL: "+PublicInfo.checkFilesGameURL+" checkFilesGame4: "+PublicInfo.checkFilesGame4);

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

        if(GetFreeMemory(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + "/") > 1800) {

            PublicInfo.SelectInstallTypeClient = 2;
            PublicInfo.checkReleaseClient = 1;
            Intent intent = new Intent(ReOpenInstallActivity.this, InstallClient.class);
            startActivity(intent);
        }
        else {
            int freeMemory = GetFreeMemory(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + "/");
            int needMemory = 1800-freeMemory;
            AlertDialog.Builder builderSelectHelp;
            builderSelectHelp = new AlertDialog.Builder(ReOpenInstallActivity.this);
            builderSelectHelp.setTitle("Недостаточно места!");
            builderSelectHelp.setMessage("Для установки игры нужно 1800MB, после установки игра будет занимать 1650MB, на вашем устройстве свободно "+freeMemory+" MB, освободите пространство на "+needMemory+" MB");
            builderSelectHelp.setNegativeButton("Закрыть", null);
            AlertDialog alert = builderSelectHelp.create();
            alert.show();
        }
    }
}