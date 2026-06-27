package com.flin.online;

        import android.annotation.SuppressLint;
        import android.app.Activity;
        import android.app.AlertDialog;
        import android.content.DialogInterface;
        import android.content.Intent;
        import android.content.pm.PackageInfo;
        import android.content.pm.PackageManager;
        import android.net.Uri;
        import android.os.AsyncTask;
        import android.os.Build;
        import android.os.Environment;

        import androidx.annotation.RequiresApi;
        import androidx.appcompat.app.AppCompatActivity;
        import androidx.core.content.FileProvider;
        import android.os.Bundle;
        import android.provider.Settings;
        import android.view.Gravity;
        import android.view.View;
        import android.widget.Button;
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
        import java.util.concurrent.TimeUnit;

public class InstallAPKActivity extends AppCompatActivity {

    AlertDialog.Builder builderSelectChooseAPKinstall;
    Button buttonCheckFiles;

    long currentDataPackage;
    boolean updatePackage;

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_install_apk);

      /*  TextView textVersionAndSignTesters = (TextView) findViewById(R.id.textVersionAndSignTesters);
        textVersionAndSignTesters.setText("v"+ PublicInfo.VersionNameAppStatic);
*/
        //Установка даты
    /*    GregorianCalendar gcalendar = new GregorianCalendar();
        TextView textViewNameProjectFull = (TextView) findViewById(R.id.textViewNameProjectFull);
         textViewNameProjectFull.setText("FLIN RP "+ gcalendar.get(Calendar.YEAR));*/

        updatePackage = false;

        builderSelectChooseAPKinstall = new AlertDialog.Builder(InstallAPKActivity.this);
        builderSelectChooseAPKinstall.setTitle("Выбор");
        builderSelectChooseAPKinstall.setMessage("Установить APK вручную или автоматическом режиме?");
        builderSelectChooseAPKinstall.setPositiveButton("Авто", new DialogInterface.OnClickListener() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        InstallApk();
                } else {
                    InstallApk();
                }

            }
        });
        builderSelectChooseAPKinstall.setNegativeButton("Вручную", new DialogInterface.OnClickListener() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onClick(DialogInterface dialog, int which) {
                AlertDialog.Builder builder = new AlertDialog.Builder(InstallAPKActivity.this);
                builder.setTitle("Ручная установка")
                        .setMessage("Откройте проводник, затем найдите папку Download, откройте ее и запустите файл "+ PublicInfo.urlUpdateAPK)
                       // .setIcon(R.mipmap.ic_launcher)
                        .setCancelable(false)
                        .setNegativeButton("Понял",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.cancel();
                                        Uri selectedUri = Uri.parse(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + "/");
                                        Intent intent = new Intent(Intent.ACTION_VIEW);
                                        intent.setDataAndType(selectedUri, "resource/folder");

                                        if (intent.resolveActivityInfo(getPackageManager(), 0) != null)
                                        {
                                            startActivity(intent);
                                        }
                                    }
                                });
                AlertDialog alert = builder.create();
                alert.show();
                //alert.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(R.color.colorAccent);
            }
        });

        //Помощь
        /*
        ImageView imgQuest = (ImageView) findViewById(R.id.imageSelectViewFull);
        imgQuest.setClickable(true);
        imgQuest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder builderSelectHelp;
                builderSelectHelp = new AlertDialog.Builder(InstallAPKActivity.this, R.style.AlertDialogTheme);
                builderSelectHelp.setTitle("Помощь!");
                builderSelectHelp.setMessage("Возникли проблемы с установкой? Попробуйте в ручном режиме");
                builderSelectHelp.setPositiveButton("Перейти на сайт", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://vk.com/@flin_rp-kak-nachat-igrat-otvet-est"));
                        startActivity(browserIntent);
                    }
                });
                ;
                builderSelectHelp.setNegativeButton("Закрыть", null);
                AlertDialog alert = builderSelectHelp.create();
                alert.show();

            }
        });*/

        //Лого
        /*
        ImageView imgFavorite = (ImageView) findViewById(R.id.imageButtonLogoFull);
        imgFavorite.setClickable(true);
        imgFavorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://flin-rp.su/"));
                startActivity(browserIntent);

            }
        });
*/


        /*
        //Нажатие Нет
        Button questionButtonNo = (Button) findViewById(R.id.buttonSelectInstallNo);
        questionButtonNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast toast = Toast.makeText(getApplicationContext(),
                        "Нажал нет",
                        Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER, 0, 0);
            //    toast.show();

            }
        });*/

        //Нажатие переустановить
        ImageView buttonReinstallApk = (ImageView) findViewById(R.id.buttonReInsta);
        buttonReinstallApk.setClickable(true);
        buttonReinstallApk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog alert = builderSelectChooseAPKinstall.create();
                alert.show();

            }
        });


/*

        //Нажатие чек файлов
        buttonCheckFiles = (Button) findViewById(R.id.buttonCheckFiles);
        buttonCheckFiles.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new InstallAPKActivity.CheckFiles().execute();


            }
        });
*/
        AlertDialog alert = builderSelectChooseAPKinstall.create();
        alert.show();

    }


    String SAMPfiles [] = {"files",
            "files/GTASAMP10.b",
            "files/gtasatelem.set",
            "files/gta_sa.set",
            "files/SAMP",
            "files/texdb",
            "files/SAMP/fonts",
            "files/SAMP/gta.dat",
            "files/SAMP/handling.cfg",
            "files/SAMP/main.scm",
            "files/SAMP/peds.ide",
            "files/SAMP/script.img",
            "files/SAMP/settings.ini",
            "files/SAMP/vehicles.ide",
            "files/SAMP/WEAPON.dat",
            "files/SAMP/fonts/arial.ttf",
            "files/SAMP/fonts/sampaux3.ttf",
            "files/texdb/samp",
            "files/texdb/SAMP.ide",
            "files/texdb/samp.img",
            "files/texdb/SAMP.ipl",
            "files/texdb/SAMPCOL.img",
            "files/texdb/samp/samp.dxt.dat",
            "files/texdb/samp/samp.dxt.tmb",
            "files/texdb/samp/samp.dxt.toc",
            "files/texdb/samp/samp.etc.dat",
            "files/texdb/samp/samp.etc.tmb",
            "files/texdb/samp/samp.etc.toc",
            "files/texdb/samp/samp.pvr.dat",
            "files/texdb/samp/samp.pvr.tmb",
            "files/texdb/samp/samp.pvr.toc",
            "files/texdb/samp/samp.txt",
            "files/texdb/samp/samp.unc.dat",
            "files/texdb/samp/samp.unc.tmb",
            "files/texdb/samp/samp.unc.toc"
    };

    @Override
    protected void onResume() {
        super.onResume();
        if(updatePackage == true){

            if(installClient() == true){
                if(currentDataPackage < getPackageUpdateTime()){
                    System.out.println("МИХАИЛ Приложение установилось!");
                    updatePackage = false;
                    PublicInfo.successUpdateApk = true;
                    try {
                        SaveSetings();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    Intent intent = new Intent(InstallAPKActivity.this, MenuActivity.class);
                    startActivity(intent);



                }
                if(currentDataPackage == getPackageUpdateTime()){
                    System.out.println("МИХАИЛ Приложение не установилось!");
                    updatePackage = false;
                }
            }
            else {
                System.out.println("МИХАИЛ Приложение не установилось из за какой то ошибки!");
            }
        }


    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1234 && resultCode == Activity.RESULT_OK) {
            if (getPackageManager().canRequestPackageInstalls()) {

                System.out.println("Михаил успешно прошел проверку 2222");
                InstallApk();
            }
        } else {
            //give the error
        }
    }

    private class CheckFiles extends AsyncTask<String, Void, Void> {
        int currentFile;
        int allFile;

        @Override
        protected void onPreExecute() {

            buttonCheckFiles.setText("1/2");
            buttonCheckFiles.setClickable(false);
            allFile = SAMPfiles.length;
            super.onPreExecute();
        }

        @SuppressLint("ResourceAsColor")
        @Override
        protected void onProgressUpdate(Void... values) {
            int procent = currentFile * 100 / allFile;
            buttonCheckFiles.setText(procent+"% ("+currentFile+"/"+allFile+" файлов)");
            super.onProgressUpdate(values);
        }

        @Override
        protected Void doInBackground(String... filepath) {

            for (int i = 0; i < SAMPfiles.length; i++) {
                currentFile = i+1;
                publishProgress();
                try {
                    TimeUnit.SECONDS.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println(SAMPfiles[i]);
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            buttonCheckFiles.setClickable(true);
            super.onPostExecute(aVoid);
        }
    }


    public void InstallApk() {
        currentDataPackage   = getPackageUpdateTime();
        final String name;
        name = PublicInfo.urlUpdateAPK;
        System.out.println("МИХАИЛ название апк при установке "+name );
        final Uri uri = Uri.parse("file://" + Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + "/" + name);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            Uri contentUri = (Uri) FileProvider.getUriForFile(getApplicationContext(), "com.flin.online", new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + "/" + name));
            Intent openFileIntent = new Intent(Intent.ACTION_VIEW);
            openFileIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            openFileIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            openFileIntent.setData(contentUri);
            sendBroadcast(openFileIntent);
            startActivity(openFileIntent);
            updatePackage = true;

        } else {
            Intent install = new Intent(Intent.ACTION_VIEW);
            install.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            install.setDataAndType(uri,
                    "application/vnd.android.package-archive");
            sendBroadcast(install);
            startActivity(install);
            updatePackage = true;
        }




    }

    public void SaveSetings() throws IOException {
        Wini ini = new Wini(new File(getExternalFilesDir(null)+"/settings.ini"));
        ini.put("app", "loading_start", 0);
        if(PublicInfo.SelectInstallTypeClient == 1)
            ini.put("app", "install", 4);
        else ini.put("app", "install", 5);
        ini.store();
    }


    public long getPackageUpdateTime(){

        long updateTime = 0; // install time is conveniently provided in milliseconds
        PackageManager packageManager =  this.getPackageManager();
        PackageInfo packageInfo = null;
        try {
            packageManager =  this.getPackageManager();
            updateTime = packageManager.getPackageInfo(PublicInfo.checkReleasePackageName, 0).lastUpdateTime;

        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        System.out.println("МИХАИЛ getPackageUpdateTime: "+ updateTime);
        return updateTime;

    }



    public Boolean installClient() {
        android.content.Intent launchIntent = null;
        launchIntent = getPackageManager().getLaunchIntentForPackage(PublicInfo.checkReleasePackageName);
        if (launchIntent != null) {
            return true;
        } else return false;


    }

}
