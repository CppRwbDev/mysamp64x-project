package com.flin.online;

import android.app.AlertDialog;
import android.app.DownloadManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.CursorIndexOutOfBoundsException;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;

import androidx.appcompat.app.AppCompatActivity;
import io.sentry.Sentry;
import io.sentry.protocol.User;

import android.os.Bundle;
import android.os.StatFs;
import android.text.Html;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.flin.online.fragments.ChangeFontDialog;
import com.flin.online.function.CheckLoadingFiles;
import com.flin.online.function.ReadNick;
import com.flin.online.install.InstallClient;
import com.flin.online.jsonenter.PublicInfo;
import com.flinc.core.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.obsez.android.lib.filechooser.ChooserDialog;
import com.yandex.metrica.YandexMetrica;

import org.ini4j.Wini;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

public class SetingsActivity<MyTask> extends AppCompatActivity  {


    int fps;
    int chat;
    Button buttonOptimalSettings;


    Button buttonLeftSetingsFPS;
    Button buttonRightSetingsFPS;


    Button buttonLeftSetingsChat;
    Button buttonMiddleSetingsChat;
    Button buttonRightSetingsChat;

    String nickName;
    EditText editTextName;


    private Thread downloadThreadR;
    private SetingsActivity.DownloadProcess downloadThread;
    long download_id = -1;
    final int[] select = new int[1];
    final int[] selectPath = new int[1];

    boolean start;

    //SharedPreferences
    public static final String APP_PREFERENCES = "settings_app";
    public static final String APP_PREFERENCES_DIALOG = "DialogStart";
    SharedPreferences mSettings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setings);

        TextView infoVersionApp = (TextView) findViewById(R.id.textViewVersion);
        infoVersionApp.setText("v"+ PublicInfo.VersionNameAppStatic+" (Build "+PublicInfo.VersionAppStatic+")");


        final TextView textviewDonateSelectServer = (TextView) findViewById(R.id.textviewDonateSelectServer);

        start =false;


        int dialogStart;
        mSettings = getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
        if(mSettings.contains(APP_PREFERENCES_DIALOG)) {
            dialogStart = mSettings.getInt(APP_PREFERENCES_DIALOG, 0);
            System.out.println("МИХАИЛ Начальные данные: "+dialogStart);
            if(dialogStart == 1)  {
                select[0] = 1;
                textviewDonateSelectServer.setText("Стабильная");
            }
            else {
                select[0] = 2;
                textviewDonateSelectServer.setText("Бета");
            }
        }
        else {
            select[0] = 2;
            textviewDonateSelectServer.setText("Не выбрано");
        }




        ImageView imageViewBtnDonateBack = (ImageView) findViewById(R.id.imageDonateBack);
        imageViewBtnDonateBack.setClickable(true);
        imageViewBtnDonateBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(select[0] == 1) {
                    select[0] = 2;
                    SaveDialog(2);
                    textviewDonateSelectServer.setText("Бета");
                }
                else {
                    select[0] = 1;
                    SaveDialog(1);
                    textviewDonateSelectServer.setText("Стабильная");
                }

                //  Toast.makeText(HomeListActivity.this, "Михаил Выбраный сервер "+select[0], Toast.LENGTH_SHORT).show();

            }
        });




        ImageView imageViewBtnDonateNext = (ImageView) findViewById(R.id.imageDonateNext);
        imageViewBtnDonateNext.setClickable(true);
        imageViewBtnDonateNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(select[0] == 2) {
                    select[0] = 1;
                    SaveDialog(1);
                    textviewDonateSelectServer.setText("Стабильная");
                }
                else {
                    select[0] = 2;
                    SaveDialog(2);
                    textviewDonateSelectServer.setText("Бета");
                }
                //  Toast.makeText(HomeListActivity.this, "Михаил Выбраный "+select[0], Toast.LENGTH_SHORT).show();
            }
        });

        final TextView textviewChangePath = (TextView) findViewById(R.id.textviewChangePath);


        if (PublicInfo.dirGameLocalType == 1){
            textviewChangePath.setText("Память телефона");
        }
        else {
            textviewChangePath.setText("Флеш память");
        }

        ImageView imageViewChangePathBack = (ImageView) findViewById(R.id.imageChangePathBack);
        imageViewChangePathBack.setClickable(true);
        imageViewChangePathBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (PublicInfo.dirGameLocalType == 1){
                    ShowDialogChangeGame(2);
                }
                else {
                    ShowDialogChangeGame(1);
                }
            }
        });


        ImageView imageChangePathNext = (ImageView) findViewById(R.id.imageChangePathNext);
        imageChangePathNext.setClickable(true);
        imageChangePathNext.setOnClickListener(new View.OnClickListener() {
            private Object FileFilter;
            @Override
            public void onClick(View v) {
                if (PublicInfo.dirGameLocalType == 1){
                    ShowDialogChangeGame(2);
                }
                else {
                    ShowDialogChangeGame(1);
                }

            }
        });

        TextView textviewChangeFont = (TextView) findViewById(R.id.textviewChangeFont);
        ImageView imageChangeFontNext = (ImageView) findViewById(R.id.imageChangeFontNext);
        imageChangeFontNext.setRotation(90);

        ImageView imageChangeFontBack = (ImageView) findViewById(R.id.imageChangeFontBack);
        imageChangeFontBack.setClickable(true);
        imageChangeFontBack.setRotation(-90);

        LinearLayout linearLayoutChangeFont = (LinearLayout) findViewById(R.id.linearLayoutChangeFont);
        linearLayoutChangeFont.setClickable(true);
        linearLayoutChangeFont.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ChangeFontDialog blankFragment = new ChangeFontDialog(getApplicationContext(),Environment.getExternalStorageDirectory() + "/Android/data/"+PublicInfo.checkReleasePackageName,"/files/SAMP/settings.ini", textviewChangeFont);
                blankFragment.show(getSupportFragmentManager(), "FRAGMENT_CHANGE_FONT");
            }
        });

        TextView textViewMods = (TextView) findViewById(R.id.textViewMods);
        try {
            textViewMods.setText(CheckLoadingFiles.getModsName());
        } catch (IOException e) {
            textViewMods.setText("Без имени3");
        }

        LinearLayout linearLayoutMods = (LinearLayout) findViewById(R.id.linearLayoutMods);
        linearLayoutMods.setClickable(true);
        linearLayoutMods.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    if(PublicInfo.checkInstallModsLastVersion > PublicInfo.checkVerClient){
                        //ОБНОВИТЬКОД
                        AlertDialog.Builder builderUpdateClient;
                        builderUpdateClient = new AlertDialog.Builder(SetingsActivity.this);
                        builderUpdateClient.setTitle("Новое обновление");
                        builderUpdateClient.setMessage("Вы не сможете открыть меню выбора сборок пока не установите новую версию лаунчера.");
                        builderUpdateClient.setPositiveButton("Обновить", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                PublicInfo.updateInstallTypeClient = 1;
                                PublicInfo.currentFilesUpdate = 1;
                                //ОБНОВИТЬКОД
                                Intent intent = new Intent(SetingsActivity.this, ReUpdateClient.class);
                                startActivity(intent);
                            }
                        });
                        ;
                        AlertDialog alert = builderUpdateClient.create();
                        alert.show();
                    }
                    else {
                        Intent intent = new Intent(SetingsActivity.this, SelectMods.class);
                        startActivity(intent);
                    }
                }
                else {
                    Toast toast = Toast.makeText(getApplicationContext(),
                            "Сборки работают только на Android 8 и выше.",
                            Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                }
            }
        });

        try {
            OpenNick();
        } catch (IOException e) {
            e.printStackTrace();
        }
        String eventParameters = "{\"name\":\"Mihail_Dimkov\", \"android\":\"21\"}";
        YandexMetrica.reportEvent("SetingsActivity", eventParameters);

        //Сохранение ника
        editTextName = (EditText) findViewById(R.id.editTextChangeName);
        if(nickName != null || nickName != "Flin_Game") editTextName.setText(nickName);
        editTextName.setSelection(editTextName.getText().length());
        ImageView imageViewSaveNick = (ImageView) findViewById(R.id.imageViewSaveNick);
        imageViewSaveNick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    SaveNick();
                } catch (IOException e) {
                    e.printStackTrace();
                    System.out.println("МИХАИЛ лог"+e.toString());
                    Intent intent = new Intent(SetingsActivity.this, MenuActivity.class);
                    startActivity(intent);
                }

            }
        });

        //Переустановка игры
        LinearLayout linearLayoutReinstallGame = (LinearLayout) findViewById(R.id.linearLayoutReinstallGame);
        linearLayoutReinstallGame.setClickable(true);
        linearLayoutReinstallGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder builderSelectHelp;
                builderSelectHelp = new AlertDialog.Builder(SetingsActivity.this);
                builderSelectHelp.setTitle("Переустановка игры");
                builderSelectHelp.setMessage("Вы действительно хотите переустановить игру?");
                builderSelectHelp.setPositiveButton("Да", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(SetingsActivity.this, LoadingGPU.class);
                        startActivity(intent);

                    }
                });
                ;
                builderSelectHelp.setNegativeButton("Нет", null);
                AlertDialog alert = builderSelectHelp.create();
                alert.show();

            }
        });

        //Менеджер паролей
        LinearLayout linearLayoutManagerPassword = (LinearLayout) findViewById(R.id.linearLayoutManagerPassword);
        linearLayoutManagerPassword.setClickable(true);
        linearLayoutManagerPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SetingsActivity.this, ListActivityServers.class);
                startActivity(intent);
            }
        });

        //Кнопка сообщить
        Button buttonSendTechMsg = (Button) findViewById(R.id.buttonSendTechMsg);
        buttonSendTechMsg.setClickable(true);
        buttonSendTechMsg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse( PublicInfo.urlSendTechProblem));
                startActivity(browserIntent);
            }
        });


        //Нажатие проверки файлов
        LinearLayout linearLayoutCheckFiles = (LinearLayout) findViewById(R.id.linearLayoutCheckFiles);
        linearLayoutCheckFiles.setClickable(true);
        linearLayoutCheckFiles.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //ОБНОВИТЬКОД
                // Intent intent = new Intent(MenuActivity.this, test.class);
                Intent intent = new Intent(SetingsActivity.this, CheckFilesActivity.class);
                startActivity(intent);
            }
        });




        //Нажатие меню настроек
        TextView imageDownLeftMenu = (TextView) findViewById(R.id.imageDownLeftMenu);
        imageDownLeftMenu.setClickable(true);
        imageDownLeftMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //ОБНОВИТЬКОД
                // Intent intent = new Intent(MenuActivity.this, test.class);
                Intent intent = new Intent(SetingsActivity.this, SetingsActivity.class);
                startActivity(intent);
            }
        });

        //Нажатие меню старта игры
        FloatingActionButton imageDownCenterMenu = (FloatingActionButton) findViewById(R.id.imageDownCenterMenu);
        imageDownCenterMenu.setClickable(true);
        imageDownCenterMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SetingsActivity.this, MenuActivity.class);
                startActivity(intent);
            }

        });

        //Нажатие меню Доната
        TextView imageDownRightMenu = (TextView) findViewById(R.id.imageDownRightMenu);
        imageDownRightMenu.setClickable(true);
        imageDownRightMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SetingsActivity.this, DonateActivity.class);
                startActivity(intent);
            }
        });


        //Переключатель улуч графики
        Switch switchGraphics =  (Switch) findViewById(R.id.switchGraphics);

        try {
            int clientgraphics = 0;
            Wini inid = new Wini(new File(Environment.getExternalStorageDirectory() + "/Android/data/"+PublicInfo.checkReleasePackageName+"/files.ini"));
            if(inid.get("app", "clientgraphics", Integer.class) != null){
                clientgraphics = inid.get("app", "clientgraphics", Integer.class);
            }

            if(clientgraphics == 1) switchGraphics.setChecked(true);
            else switchGraphics.setChecked(false);

        } catch (IOException e) {
            e.printStackTrace();
        }
        catch (NullPointerException e) {
            e.printStackTrace();
        }
        switchGraphics.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(!isChecked) {

                    //ОБНОВИТЬКОД
                    AlertDialog.Builder builderUpdateClient;
                    builderUpdateClient = new AlertDialog.Builder(SetingsActivity.this);
                    builderUpdateClient.setTitle("Выключить улучшенную графику?");
                    builderUpdateClient.setMessage("Нажав выключить, загрузятся стандартные оптимальные настройки игры\n");
                    builderUpdateClient.setPositiveButton("Выключить", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Toast.makeText(SetingsActivity.this, "Улучшенная графика:  OFF", Toast.LENGTH_SHORT).show();
                            DownloadFiles("http://d1.flin-rp.com/client/settings/off/gta_sa.set", "gta_sa.set", "Идет скачивание архивов игры", "Ресурсы", "Скачивание "+PublicInfo.checkCountDownloadsFilesCurrent+" из "+PublicInfo.checkCountDownloadsFiles);
                            try {
                                SaveSetingsGraphics(false);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                    ;
                    builderUpdateClient.setNegativeButton("Отмена", null);
                    AlertDialog alert = builderUpdateClient.create();
                    alert.show();


                }
                else
                {


                    //ОБНОВИТЬКОД
                    AlertDialog.Builder builderUpdateClient;
                    builderUpdateClient = new AlertDialog.Builder(SetingsActivity.this);
                    builderUpdateClient.setTitle("Выключить улучшенную графику?");
                    builderUpdateClient.setMessage("Нажав включить, загрузятся новые настройки игры\n");
                    builderUpdateClient.setPositiveButton("Включить", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Toast.makeText(SetingsActivity.this, "Улучшенная графика: ON", Toast.LENGTH_SHORT).show();
                            DownloadFiles("http://d1.flin-rp.com/client/settings/on/gta_sa.set", "gta_sa.set", "Идет скачивание архивов игры", "Ресурсы", "Скачивание "+PublicInfo.checkCountDownloadsFilesCurrent+" из "+PublicInfo.checkCountDownloadsFiles);
                            try {
                                SaveSetingsGraphics(true);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                    ;
                    builderUpdateClient.setNegativeButton("Отмена", null);
                    AlertDialog alert = builderUpdateClient.create();
                    alert.show();


                }
            }
        });


    }




    /*public void ChangeButtonFPS(int Type) throws IOException {
        String mode;
        Wini ini = new Wini(new File(Environment.getExternalStorageDirectory() + "/Android/data/com.rockstargames.gtasa/files/SAMP/settings.ini"));

        if(Type == 1) {
            mode = "true";
            Toast.makeText(getApplicationContext(), "Счетчик FPS включен", Toast.LENGTH_LONG).show();
            buttonTurnFPS.setText("Выключить счетчик FPS");
        }
        else {
            mode = "false";
            Toast.makeText(getApplicationContext(), "Счетчик FPS выключен", Toast.LENGTH_LONG).show();
            buttonTurnFPS.setText("Включить счетчик FPS");
        }
        ini.put("debug", "debug_fps", mode);
        ini.store();


    }*/
    public void ShowDialogChangeGame(int type){
        if(type == 2) {
            AlertDialog.Builder builderUpdateClient;
            builderUpdateClient = new AlertDialog.Builder(SetingsActivity.this);
            builderUpdateClient.setTitle("Смена расположения игры");
            builderUpdateClient.setMessage("Вы действительно хотите сменить расположение игры на SD-card вашего телефона?\n\n"+ Html.fromHtml(TextColor("Внимание!", "red"))+"\nЭто экспериментальная версия и мы не гарантируем работу на всех устройствах, если у вас не запускается игра, смените обратно на местоположение игры на память телефона\nИгру с памяти телефона можно удалить вручную, если игра запускается с флешки.");
            builderUpdateClient.setPositiveButton("Да, сменить", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    ShowDialogHelpSelect();
                }
            });
            ;
            builderUpdateClient.setNegativeButton("Отмена", null);
            AlertDialog alert = builderUpdateClient.create();
            alert.show();
        }
        else {
            AlertDialog.Builder builderUpdateClient;
            builderUpdateClient = new AlertDialog.Builder(SetingsActivity.this);
            builderUpdateClient.setTitle("Смена расположения игры");
            builderUpdateClient.setMessage("Вы действительно хотите сменить расположение игры на память вашего телефона?\n\n");
            builderUpdateClient.setPositiveButton("Да, сменить", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    ShowDialogChangeMemoryPhone();
                }
            });
            ;
            builderUpdateClient.setNegativeButton("Отмена", null);
            AlertDialog alert = builderUpdateClient.create();
            alert.show();
        }
    }

    public void ShowDialogChangeMemoryPhone(){
        AlertDialog.Builder builderUpdateClient;
        builderUpdateClient = new AlertDialog.Builder(SetingsActivity.this);
        builderUpdateClient.setTitle("Настройки изменены!");
        builderUpdateClient.setMessage("Запуск игры теперь осуществляется с памяти телефона.");
        builderUpdateClient.setPositiveButton("ОК", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                try {
                    Wini inid = new Wini(new File(Environment.getExternalStorageDirectory() + "/Android/data/"+ PublicInfo.checkReleasePackageName+"/files.ini"));
                    inid.put("storage", "type", 1);
                    // inid.put("storage", "dirgame", dirDest+"/files/");
                    inid.store();
                    PublicInfo.dirGameLocalType = 1;
                    Intent intent = new Intent(SetingsActivity.this, MenuActivity.class);
                    startActivity(intent);
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        });
        ;
        AlertDialog alert = builderUpdateClient.create();
        alert.show();
    }


    public void ShowDialogHelpSelect(){
        AlertDialog.Builder builderUpdateClient;
        builderUpdateClient = new AlertDialog.Builder(SetingsActivity.this);
        builderUpdateClient.setTitle("Подсказка!");
        builderUpdateClient.setMessage("В следующем меню, выберите корневой каталог вашей флешки.\n\nНе выбирайте никакие папки для установки игры на флеш карту, только корневую дирректорию где находятся все содержимое флеш карты.");
        builderUpdateClient.setPositiveButton("Понял", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                new ChooserDialog(SetingsActivity.this)
                        .withFilter(true, false)
                        .withStartFile(String.valueOf(Environment.getExternalStorageDirectory()))
                        .titleFollowsDir(true)
                        .withResources(R.string.app_title_choose_any_file, R.string.app_title_choose, R.string.dialog_cancel)
                        .withChosenListener(new ChooserDialog.Result() {
                                                @Override
                                                public void onChoosePath(String path, File pathFile) {

                                                    Toast.makeText(SetingsActivity.this, "FOLDER: " + path, Toast.LENGTH_SHORT).show();
                                                    PublicInfo.dirGameLocalSelectCard = path;
                                                    StatFs statFs = new StatFs(Environment.getRootDirectory().getAbsolutePath());
                                                    long   free   = (statFs.getAvailableBlocks() * statFs.getBlockSize());
                                                  /*  Intent intent = new Intent(SetingsActivity.this, InstallSDcard.class);
                                                    startActivity(intent);*/
                                                }
                                            }
                        )
                        .build()
                        .show();
            }
        });
        ;
        builderUpdateClient.setNegativeButton("Отмена", null);
        AlertDialog alert = builderUpdateClient.create();
        alert.show();
    }

    private static String TextColor(String text, String color)
    {
        text = "<font color='"+color+"'>"+text+"</font>";
        return text;
    }
    public void OpenSettingsIni() throws IOException {
        String PackageName = "";
        if(PublicInfo.checkReleaseClient == 1) PackageName = "com.rockstargames.gtasa";
        else PackageName = PublicInfo.checkReleasePackageName;
        Wini ini = new Wini(new File(Environment.getExternalStorageDirectory() + "/Android/data/"+PackageName+"/files/SAMP/settings.ini"));

        if(PublicInfo.checkReleaseClient == 1) fps = ini.get("client", "fps", int.class);
        else  fps = ini.get("gui", "fps", int.class);
        chat = ini.get("gui", "ChatMaxMessages", int.class);
        System.out.println("Михаил FPS "+fps);
        System.out.println("Михаил chat "+chat);
        //   ChangeButtonColorFPS(fps);
        //   ChangeButtonColorChat(chat);
        //Log.d(TAG, "Ваш ник: "+nickName);
    }


  /*  public  void ChangeButtonColorChat (int type){
        switch (type){
            case 0:{
                buttonLeftSetingsChat.setBackgroundResource(R.drawable.btn_style_left_rectangle_false);
                buttonMiddleSetingsChat.setBackgroundResource(R.color.colorButtonSettingsUnSelect);
                buttonRightSetingsChat.setBackgroundResource(R.drawable.btn_style_r_rectangle_false);
                break;

            }
            case 8:{
                buttonLeftSetingsChat.setBackgroundResource(R.drawable.btn_style_left_rectangle_true);
                buttonMiddleSetingsChat.setBackgroundResource(R.color.colorButtonSettingsUnSelect);
                buttonRightSetingsChat.setBackgroundResource(R.drawable.btn_style_r_rectangle_false);
                break;

            }
            case 10:{
                buttonLeftSetingsChat.setBackgroundResource(R.drawable.btn_style_left_rectangle_false);
                buttonMiddleSetingsChat.setBackgroundResource(R.color.colorMain);
                buttonRightSetingsChat.setBackgroundResource(R.drawable.btn_style_r_rectangle_false);
                break;

            }
            case 16:{
                buttonLeftSetingsChat.setBackgroundResource(R.drawable.btn_style_left_rectangle_false);
                buttonMiddleSetingsChat.setBackgroundResource(R.color.colorButtonSettingsUnSelect);
                buttonRightSetingsChat.setBackgroundResource(R.drawable.btn_style_r_rectangle_true);
                break;

            }
        }

    }*/

    public void SaveNick() throws IOException {
        Wini ini = new Wini(new File(Environment.getExternalStorageDirectory() +"/FlinOnline/files/SAMP/settings.ini"));
        nickName = editTextName.getText().toString();
        System.out.println("МИХАИЛ лог ник"+PublicInfo.dirGameLocal+"/SAMP/settings.ini" );
        ini.put("client", "name", nickName.replaceAll(" ", ""));
        ini.store();


        Sentry.configureScope(scope -> {
            User user = new User();
            user.setEmail(nickName);
            user.setUsername(nickName);
            Sentry.setUser(user);
            scope.setTag("launcherVer", String.valueOf(PublicInfo.checkVerLauncher));
            scope.setTag("clientVer", String.valueOf(PublicInfo.checkVerClient));
            scope.setTag("appVerStatic", PublicInfo.VersionNameAppStatic+"("+PublicInfo.VersionAppStatic+")");
            scope.setTag("nickName", nickName);
        });


        ReadNick.writeNickMods(CheckLoadingFiles.getModsID());

        Toast toast = Toast.makeText(getApplicationContext(),
                "Ваш ник: "+nickName+"\nУспешно сохранен!",Toast.LENGTH_LONG);
        toast.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL, 0, 0);
        toast.show();
        Intent intent = new Intent(SetingsActivity.this, MenuActivity.class);
        startActivity(intent);

    }
/*
    public  void ChangeButtonColorFPS (int type){
        switch (type){
            case 0:{
                buttonLeftSetingsFPS.setBackgroundResource(R.drawable.btn_style_left_rectangle_false);
                buttonRightSetingsFPS.setBackgroundResource(R.drawable.btn_style_r_rectangle_false);
                break;

            }
            case 30:{
                buttonLeftSetingsFPS.setBackgroundResource(R.drawable.btn_style_left_rectangle_true);
                buttonRightSetingsFPS.setBackgroundResource(R.drawable.btn_style_r_rectangle_false);
                break;

            }
            case 60:{
                buttonLeftSetingsFPS.setBackgroundResource(R.drawable.btn_style_left_rectangle_false);
                buttonRightSetingsFPS.setBackgroundResource(R.drawable.btn_style_r_rectangle_true);
                break;

            }
        }

    }*/

    public void SaveSettingsFPS(int type) throws IOException {


        String PackageName = "";
        if(PublicInfo.checkReleaseClient == 1) PackageName = "com.rockstargames.gtasa";
        else PackageName = PublicInfo.checkReleasePackageName;
        Wini ini = new Wini(new File(Environment.getExternalStorageDirectory() + "/Android/data/"+PackageName+"/files/SAMP/settings.ini"));

        if(PublicInfo.checkReleaseClient == 1) ini.put("client", "fps", type);
        else  ini.put("gui", "fps", type);
        ini.store();
    }
    public void SaveSettingsChat(int type) throws IOException {
        String PackageName = "";
        if(PublicInfo.checkReleaseClient == 1) PackageName = "com.rockstargames.gtasa";
        else PackageName = PublicInfo.checkReleasePackageName;
        Wini ini = new Wini(new File(Environment.getExternalStorageDirectory() + "/Android/data/"+PackageName+"/files/SAMP/settings.ini"));

        ini.put("gui", "ChatMaxMessages", type);
        ini.store();
    }

    public void OpenNick() throws IOException {
        Wini ini = new Wini(new File(PublicInfo.dirGameLocal+"/SAMP/settings.ini"));
        nickName = ini.get("client", "name");
        if(nickName == null || nickName == "Flin_Game"){
   /*         final Dialog view;
            view = new Dialog(SetingsActivity.this);
            view.requestWindowFeature(Window.FEATURE_NO_TITLE);
            view.setContentView(R.layout.dialog_enter_nick);
            view.show();
*//*
            final EditText editTextDialogChangeNickName = (EditText) view.findViewById(R.id.editTextDialogChangeNickName);
            final Button buttonDialogSaveNick = (Button) view.findViewById(R.id.buttonDialogSaveNick);
            buttonDialogSaveNick.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        String PackageName = "";
                        if(PublicInfo.checkReleaseClient == 1) PackageName = "com.rockstargames.gtasa";
                        else PackageName = PublicInfo.checkReleasePackageName;

                        Wini ini = new Wini(new File(Environment.getExternalStorageDirectory() + "/Android/data/"+PackageName+"/files/SAMP/settings.ini"));
                        String nickName = editTextDialogChangeNickName.getText().toString();
                        ini.put("client", "name", nickName);
                        ini.store();
                        Toast toast = Toast.makeText(getApplicationContext(),
                                "Ваш ник: "+nickName+"\nУспешно сохранен!",
                                Toast.LENGTH_LONG);
                        toast.setGravity(Gravity.CENTER, 0, 0);
                        toast.show();
                    } catch (IOException e) {
                        e.printStackTrace();
                        Toast toast = Toast.makeText(getApplicationContext(),
                                "Ошибка сохранения ника",
                                Toast.LENGTH_LONG);
                        toast.setGravity(Gravity.CENTER, 0, 0);
                        toast.show();
                    }
                    view.dismiss();
                }
            });*/
        }

        //Log.d("DIMKOV", "Ваш ник: "+nickName);
    }



    public void SaveSetingsGraphics(boolean checked) throws IOException {
        Wini ini = new Wini(new File(Environment.getExternalStorageDirectory() + "/Android/data/"+PublicInfo.checkReleasePackageName+"/files.ini"));
        if(checked) ini.put("app", "clientgraphics", 1);
        else ini.put("app", "clientgraphics", 2);
        ini.store();
    }


    public void DownloadFiles(String URL, String FileName, String nameStatus, String nameDescription,  String namesetTitle) {
        File path = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + "/" + FileName);//
        path.delete();

        String urlDownload = URL;
        downloadThread = new SetingsActivity.DownloadProcess();
        downloadThread.setUrlDownload(urlDownload);
        downloadThread.nameStatus(nameStatus);
        downloadThread.setFileName(FileName);
        downloadThreadR = new Thread(downloadThread);
        downloadThreadR.start();


    }


    private class DownloadProcess implements Runnable {
        volatile boolean downloading = true;
        private boolean apk = false;

        private String urlDownload;
        private String filename;
        private String nameStatus;

        public void setFileName(String fileName) {
            this.filename = fileName;
        }

        public void nameStatus(String name) {
            this.nameStatus = name;
        }
        public void setDownloading(boolean downloading) {
            this.downloading = downloading;
        }

        public void setUrlDownload(String urlDownload) {
            this.urlDownload = urlDownload;
        }

        public void setApk(boolean apk) {
            this.apk = apk;
        }

        @Override
        public void run() {
            DownloadManager.Request request = new DownloadManager.Request(Uri.parse(urlDownload));

            MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
            String mimeString = mimeTypeMap.getMimeTypeFromExtension(MimeTypeMap.getFileExtensionFromUrl(urlDownload));
            request.setMimeType(mimeString);

            // request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI);
            request.setAllowedOverRoaming(false);
            request.addRequestHeader("User-Agent", "FlinLauncher-GP/"+PublicInfo.VersionNameAppStatic);
            request.setDescription(filename);
            request.setTitle(filename);
            System.out.println("МИХАИЛ ФАЙЛ:"+filename);
            request.allowScanningByMediaScanner();
            request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE);
            request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, filename);
            final DownloadManager manager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);

            final long downloadId = manager.enqueue(request);
            download_id = downloadId;

            //  final ProgressBar mProgressBar = (ProgressBar) findViewById(R.id.progressBarInstall);
            //   final TextView textViewPercents = (TextView) findViewById(R.id.textViewInfoInstallFull);
            //   final TextView textViewProgress = (TextView) findViewById(R.id.textViewProgressFull);

            while (downloading) {
                try {
                    DownloadManager.Query q = new DownloadManager.Query();
                    q.setFilterById(downloadId);

                    final Cursor cursor = manager.query(q);
                    cursor.moveToFirst();
                    final int bytes_downloaded = cursor.getInt(cursor
                            .getColumnIndex(DownloadManager.COLUMN_BYTES_DOWNLOADED_SO_FAR));
                    final int bytes_total = cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_TOTAL_SIZE_BYTES));

                    if (cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_STATUS)) == DownloadManager.STATUS_SUCCESSFUL) {
                        downloading = false;
                    }

                    int dl_progress = 0;

                    try {
                        dl_progress = (int) ((bytes_downloaded * 100L) / bytes_total);
                    }
                    catch (ArithmeticException e) {

                        dl_progress = 0;
                    }

                    /*
                    Cursor cursor = contentResolver.query(MY_URI, new String[] { "first" }, null, null, null);
                    if (cursor != null) {
                      if (cursor.moveToFirst()) {
                          first = cursor.getString(cursor.getColumnIndex("first"));
                      }
                      cursor.close(); ///// Changed here
                    }

                     */
                    if( cursor != null && cursor.moveToFirst() ) {
                        final int status = cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_STATUS));

                        final int finalDl_progress = dl_progress;
                        runOnUiThread(new Runnable() {

                            @Override
                            public void run() {
                                if (finalDl_progress >= 0 && finalDl_progress <= 100) {
                                    //     mProgressBar.setProgress((int) finalDl_progress);
                                    //     textViewPercents.setText(nameStatus.replace("---", String.valueOf((int) finalDl_progress)));
                                } else {
                                    //  textViewPercents.setText("Не могу получить информацию о размере файла");
                                    //  textViewProgress.setText("Не могу получить информацию о размере файла");
                                }
                                if (bytes_total != -1) {
                                  /*  textViewProgress.setText(getString(R.string.progress)
                                            .replace("%chunk%", String.valueOf(humanReadableByteCount(bytes_downloaded, true)))
                                            .replace("%total%", String.valueOf(humanReadableByteCount(bytes_total, true))));*/
                                }
                                int intPercents = (int) finalDl_progress;
                                //      textViewPercent.setText(intPercents+"%");
                                statusMessage(status);
                            }
                        });
                    }
                    cursor.close();
                } catch (CursorIndexOutOfBoundsException e) {
                    System.out.println("МИХАИЛ"+e.toString());
//                    ModsActivity.SendLogServer(1, e.toString());
                }
            }
        }
    }
    private void statusMessage(int status) {
        switch (status) {
            case DownloadManager.STATUS_SUCCESSFUL:
                try {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        Files.move(Paths.get(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + "/gta_sa.set"), Paths.get(Environment.getExternalStorageDirectory() + "/Android/data/"+PublicInfo.checkReleasePackageName+"/files/gta_sa.set"), StandardCopyOption.REPLACE_EXISTING);
                    }
                    else {
                        moveFileSettings();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
        }
    }

    public void moveFileSettings() throws IOException {
        moveFile(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)  +"/", "gta_sa.set",Environment.getExternalStorageDirectory() + "/Android/data/"+PublicInfo.checkReleasePackageName+"/files/");
        Toast.makeText(this, "Настройки загружены!", Toast.LENGTH_LONG).show();
    }

    // https://stackoverflow.com/a/3758880
    public static String humanReadableByteCount(long bytes, boolean si) {
        int unit = si ? 1000 : 1024;
        if (bytes < unit) return bytes + " B";
        int exp = (int) (Math.log(bytes) / Math.log(unit));
        String pre = (si ? "kMGTPE" : "KMGTPE").charAt(exp-1) + (si ? "" : "i");
        return String.format("%.1f %sB", bytes / Math.pow(unit, exp), pre);
    }


    private void moveFile(String inputPath, String inputFile, String outputPath) {
        InputStream in = null;
        OutputStream out = null;
        try {
            File dir = new File (outputPath);
            if (!dir.exists())
            {
                dir.mkdirs();
            }
            in = new FileInputStream(inputPath + inputFile);
            out = new FileOutputStream(outputPath + inputFile);

            byte[] buffer = new byte[1024];
            int read;
            while ((read = in.read(buffer)) != -1) {
                out.write(buffer, 0, read);
            }
            in.close();
            in = null;

            // write the output file
            out.flush();
            out.close();
            out = null;

            // delete the original file
            new File(inputPath + inputFile).delete();
        }
        catch (FileNotFoundException fnfe1) {
            Log.e("tag", fnfe1.getMessage());
        }
        catch (Exception e) {
            Log.e("tag", e.getMessage());
        }

    }


    public void SaveDialog(int checkbox)  {
        SharedPreferences.Editor editor = mSettings.edit();
        editor.putInt(APP_PREFERENCES_DIALOG, checkbox);
        editor.apply();
        System.out.println("МИХАИЛ Сохранился: "+checkbox);
    }
}
