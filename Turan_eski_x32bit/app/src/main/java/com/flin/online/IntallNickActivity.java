package com.flin.online;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.flin.online.jsonenter.PublicInfo;
import com.flinc.core.R;

import org.ini4j.Wini;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;

import androidx.appcompat.app.AppCompatActivity;

public class IntallNickActivity extends AppCompatActivity {

    String nickName;
    EditText editTextName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intall_nick);
        //setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LOCKED);


        try {
            OpenNick();
        } catch (IOException e) {
            e.printStackTrace();
        }

   /*     TextView infoVersionApp = (TextView) findViewById(R.id.textVersion);
        infoVersionApp.setText("v"+ PublicInfo.VersionNameAppStatic);*/

        //Установка даты
   /*     GregorianCalendar gcalendar = new GregorianCalendar();
        TextView textViewNameProjectFull = (TextView) findViewById(R.id.textViewNameProjectFull);
         textViewNameProjectFull.setText("FLIN RP "+ gcalendar.get(Calendar.YEAR));*/
        //Лого

        //Сохранение ника
        editTextName = (EditText) findViewById(R.id.editTextChangeName);
        if(nickName != null || nickName != "Flin_Game") editTextName.setText(nickName);


        ImageView buttonSaveNick = (ImageView) findViewById(R.id.buttonSaveNick);
        buttonSaveNick.setClickable(true);
        buttonSaveNick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    SaveNick();
                } catch (IOException e) {
                    e.printStackTrace();
                   /* Toast toast = Toast.makeText(getApplicationContext(),
                            "Вам необходимо сначала установить клиент",
                            Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                   // Intent intent = new Intent(IntallNickActivity.this, LoadingApp.class);
                   // startActivity(intent);
                    */
                   // Intent intent = new Intent(IntallNickActivity.this, InstallAPKActivity.class);
                 //  startActivity(intent);
                    Intent intent = new Intent(IntallNickActivity.this, MenuActivity.class);
                    startActivity(intent);
                }

            }
        });;

    }


    public void OpenNick() throws IOException {
        Wini ini = new Wini(new File(Environment.getExternalStorageDirectory()+"/FlinOnline/files/SAMP/settings.ini"));
        nickName = ini.get("client", "name");

        //Log.d(TAG, "Ваш ник: "+nickName);
    }

    public void SaveNick() throws IOException {
        Wini ini = new Wini(new File(Environment.getExternalStorageDirectory()+"/FlinOnline/files/SAMP/settings.ini"));
        nickName = editTextName.getText().toString();
        ini.put("client", "name", nickName);
        ini.store();


        System.out.println("МИХАИЛ Приложение установилось!");
        PublicInfo.successInstallApk = true;
        try {
            SaveSetings();
        } catch (IOException e) {
            e.printStackTrace();
        }


        Intent intent = new Intent(IntallNickActivity.this, MenuActivity.class);
        startActivity(intent);


    }

    public void SaveSetings() throws IOException {
        Wini ini = new Wini(new File(getExternalFilesDir(null)+"/settings.ini"));
        ini.put("app", "loading_start", 0);
        if(PublicInfo.SelectInstallTypeClient == 1)
            ini.put("app", "install", 4);
        else ini.put("app", "install", 5);
        ini.store();
    }

}
