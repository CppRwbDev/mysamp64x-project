package com.flin.online;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.flin.online.install.InstallClient;
import com.flin.online.jsonenter.PublicInfo;
import com.flinc.core.R;

import static com.flin.online.LoadingApp.GetFreeMemory;

import java.io.File;

public class SelectInstallClient extends AppCompatActivity {

    int TypeCache;
    boolean bOnce = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_install_client);


        TextView infoVersionApp = (TextView) findViewById(R.id.textViewVersion);
        infoVersionApp.setText("v"+ PublicInfo.VersionNameAppStatic+" (Build "+PublicInfo.VersionAppStatic+")");


     /*   TextView textViewGPU = (TextView) findViewById(R.id.textViewGPU);
        textViewGPU.setText("Ваш GPU:"+PublicInfo.gpu);*/

         /*   TextView infoVersionApp = (TextView) findViewById(R.id.textVersion);
            infoVersionApp.setText("v"+ PublicInfo.VersionNameAppStatic);
            infoVersionApp.setClickable(true);
            infoVersionApp.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(LoadingGPU.this, SelectInstallActivity.class);
                    startActivity(intent);
                }
            });*/

     /*       //Установка даты
            GregorianCalendar gcalendar = new GregorianCalendar();
            TextView textViewNameProjectFull = (TextView) findViewById(R.id.textViewNameProjectFull);
             textViewNameProjectFull.setText("FLIN RP "+ gcalendar.get(Calendar.YEAR));*/


        ImageView buttonCClient = (ImageView) findViewById(R.id.imageViewInstallButton);
        buttonCClient.setClickable(true);
        buttonCClient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!bOnce) {
                    bOnce = true;
                    String GetLogPath = Environment.getExternalStorageDirectory()  + "/FlinOnline/";
                    String GetFileLogPath = Environment.getExternalStorageDirectory()  + "/FlinOnline/logcat.txt";
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
                if(GetFreeMemory(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + "/") > 1800) {

                    PublicInfo.SelectInstallTypeClient = 2;
                    PublicInfo.checkReleaseClient = 1;
                    Intent intent = new Intent(SelectInstallClient.this, InstallClient.class);
                    startActivity(intent);
                }
                else {
                    int freeMemory = GetFreeMemory(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + "/");
                    int needMemory = 3200-freeMemory;
                    AlertDialog.Builder builderSelectHelp;
                    builderSelectHelp = new AlertDialog.Builder(SelectInstallClient.this);
                    builderSelectHelp.setTitle("Недостаточно места!");
                    builderSelectHelp.setMessage("Для установки игры нужно 3200MB, после установки игра будет занимать 2600MB, на вашем устройстве свободно "+freeMemory+" MB, освободите пространство на "+needMemory+" MB");
                    builderSelectHelp.setNegativeButton("Закрыть", null);
                    AlertDialog alert = builderSelectHelp.create();
                    alert.show();
                }

            }
        });


        ImageView imageViewInstallButtonGPU = (ImageView) findViewById(R.id.imageViewInstallButtonGPU);
        imageViewInstallButtonGPU.setClickable(true);
        imageViewInstallButtonGPU.setVisibility(View.VISIBLE);
        imageViewInstallButtonGPU.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(GetFreeMemory(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + "/") > 1800) {

                    PublicInfo.SelectInstallTypeClient = 2;
                    PublicInfo.checkReleaseClient = 1;
                    PublicInfo.checkGPU = 1;
                    Intent intent = new Intent(SelectInstallClient.this, InstallClient.class);
                    startActivity(intent);
                }
                else {
                    int freeMemory = GetFreeMemory(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + "/");
                    int needMemory = 2000-freeMemory;
                    AlertDialog.Builder builderSelectHelp;
                    builderSelectHelp = new AlertDialog.Builder(SelectInstallClient.this);
                    builderSelectHelp.setTitle("Недостаточно места!");
                    builderSelectHelp.setMessage("Для установки игры нужно 2000MB, после установки игра будет занимать 1400MB, на вашем устройстве свободно "+freeMemory+" MB, освободите пространство на "+needMemory+" MB");
                    builderSelectHelp.setNegativeButton("Закрыть", null);
                    AlertDialog alert = builderSelectHelp.create();
                    alert.show();
                }

            }
        });


           /* Button buttonBackToMenu;
            buttonBackToMenu = (Button) findViewById(R.id.buttonBackToMenu);
            buttonBackToMenu.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    PublicInfo.SelectInstallTypeClient = 1;
                    Intent intent = new Intent(LoadingGPU.this, HomeActivity.class);
                    startActivity(intent);
                }
            });*/
    }
}
