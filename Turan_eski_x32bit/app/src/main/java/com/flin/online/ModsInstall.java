package com.flin.online;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.CursorIndexOutOfBoundsException;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.PowerManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.MimeTypeMap;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.flin.online.function.ReadNick;
import com.flin.online.jsonenter.PublicInfo;
import com.flinc.core.R;
import com.mg.zeearchiver.Archive;
import com.mg.zeearchiver.ExtractCallback;

import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.exception.ZipException;
import net.lingala.zip4j.model.FileHeader;
import net.lingala.zip4j.progress.ProgressMonitor;

import org.ini4j.Wini;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicBoolean;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import io.sentry.Sentry;
import io.sentry.protocol.User;

public class ModsInstall extends AppCompatActivity {


    ProgressBar loadingProgressBar;
    TextView textViewProgressUpdate;
    TextView textViewUpdates;
    TextView textViewInfoInstall;
    TextView textViewLabelWarning;
    TextView textViewPercent;


    int AlternationCount = 0;

    int SelectUserUpdate;
    int SelectTypeAPP;

    long download_id = -1;
    Cursor cursor;


    private Thread downloadThreadR;
    private ModsInstall.DownloadProcess downloadThread;
    AlertDialog.Builder  builderOneStepUpdateClient;

    String URLDownload;


    int mod_id;
    int mod_version;
    String mod_name;
    String mod_versionName;
    String mod_versionUpdate;
    String mod_archive;
    int mod_archive_count;
    String mod_url;
    String patch;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_install_client);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);



        textViewProgressUpdate = (TextView) findViewById(R.id.textViewProgressFull);
        textViewUpdates = (TextView) findViewById(R.id.textViewInfoInstallFull);


        textViewLabelWarning = (TextView) findViewById(R.id.textViewLabelWarning);
        textViewPercent = (TextView) findViewById(R.id.textViewPercent);


        textViewInfoInstall = (TextView) findViewById(R.id.textViewProgressFull);

        TextView infoVersionApp = (TextView) findViewById(R.id.textViewVersion);
        infoVersionApp.setText("v"+ PublicInfo.VersionNameAppStatic+" (Build "+ PublicInfo.VersionAppStatic+")");
        //Установка даты
       /* GregorianCalendar gcalendar = new GregorianCalendar();
        TextView textViewNameProjectFull = (TextView) findViewById(R.id.textViewNameProjectFull);
         textViewNameProjectFull.setText("FLIN RP "+ gcalendar.get(Calendar.YEAR));*/

        loadingProgressBar = (ProgressBar) findViewById(R.id.progressBarInstall);
        loadingProgressBar.getIndeterminateDrawable().setColorFilter(0xFFFF0000,android.graphics.PorterDuff.Mode.MULTIPLY);


        Intent intent = getIntent();
        mod_id = intent.getIntExtra("mod_id", 0);
        mod_name = intent.getStringExtra("mod_name");
        mod_version = intent.getIntExtra("mod_version", 0);
        mod_versionName = intent.getStringExtra("mod_versionName");
        mod_versionUpdate = intent.getStringExtra("mod_versionUpdate"); //Текст с описанием обновления
        mod_archive = intent.getStringExtra("mod_archive");
        mod_archive_count = intent.getIntExtra("mod_archive_count", 0); //Текст с описанием обновления
        mod_url = intent.getStringExtra("mod_url");  // /01/


        patch = Environment.getExternalStorageDirectory() + "/Android/data/" + PublicInfo.checkReleasePackageName + "/mods/"+"/"+mod_id+"/";
        System.out.println("MIHAIL patchPatch:"+patch);

        URLDownload = PublicInfo.checkModsURL+"/"+mod_id+"/"+mod_url;
        System.out.println("MIHAIL URLDownload:"+URLDownload);

        System.out.println("MIHAIL URLDownload Архив:"+URLDownload+mod_archive+mod_archive_count);

/*
        if (mod_archive_count >= 3)  System.out.println("MIHAIL mod_archive_count:"+1);
        if (mod_archive_count >= 3)  System.out.println("MIHAIL mod_archive_count:"+2);
        if (mod_archive_count >= 3)  System.out.println("MIHAIL mod_archive_count:"+3);
        if (mod_archive_count >= 4)  System.out.println("MIHAIL mod_archive_count:"+4);
        if (mod_archive_count >= 5)  System.out.println("MIHAIL mod_archive_count:"+5);*/

        if (Build.VERSION.SDK_INT != Build.VERSION_CODES.KITKAT){
            int permissionStatus = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
            if (permissionStatus == PackageManager.PERMISSION_GRANTED) {
                SelectTypeAPP = 1;
                AlternationInstall(AlternationCount +=1);
            } else {
                ActivityCompat.requestPermissions(ModsInstall.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
            }
        }
        else {
            SelectTypeAPP = 1;
            AlternationInstall(AlternationCount +=1);
        }
    }

    public static void deleteArchive(String nameFile)
    {
        File file1 = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + "/" + nameFile);
        if (file1.delete()) {
            System.out.println("MIHAIL файл который был удален с загрузок:"+nameFile);
        } else System.out.println("MIHAIL файл который был не удален с загрузок:"+nameFile);
    }



    public void AlternationInstall(int count) {
            switch (count) {
                case 1: {
                //     AlternationInstall(AlternationCount =7);
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                    PublicInfo.checkCountDownloadsFilesCurrent = 1;
                    String nameArchive = mod_archive+"1";
                    String url = URLDownload+nameArchive;
                    System.out.println("MIHAIL конечный архив url"+url);
                    DownloadFiles(url, nameArchive, "Идет скачивание архивов игры", "Ресурсы", "Скачивание " + PublicInfo.checkCountDownloadsFilesCurrent + " из " + mod_archive_count);

                    break;
                }
                case 2: {
                    //AlternationInstall(AlternationCount +=1);
                    PublicInfo.checkCountDownloadsFilesCurrent++;
                    String nameArchive = mod_archive+"2";
                    String url = URLDownload+nameArchive;
                    DownloadFiles(url, nameArchive, "Идет скачивание архивов игры", "Ресурсы", "Скачивание " + PublicInfo.checkCountDownloadsFilesCurrent + " из " + mod_archive_count);
                    break;
                }
                case 3: {
                    PublicInfo.checkCountDownloadsFilesCurrent++;
                    String nameArchive = mod_archive+"3";
                    String url = URLDownload+nameArchive;
                    DownloadFiles(url, nameArchive, "Идет скачивание архивов игры", "Ресурсы", "Скачивание " + PublicInfo.checkCountDownloadsFilesCurrent + " из " + mod_archive_count);
                    break;
                }
                case 4: {
                    if (mod_archive_count >= 4) {
                        PublicInfo.checkCountDownloadsFilesCurrent++;
                        String nameArchive = mod_archive+"4";
                        String url = URLDownload+nameArchive;
                        DownloadFiles(url, nameArchive, "Идет скачивание архивов игры", "Ресурсы", "Скачивание " + PublicInfo.checkCountDownloadsFilesCurrent + " из " + mod_archive_count);
                    } else AlternationInstall(AlternationCount += 1);
                    break;
                }
                case 5: {
                    if (mod_archive_count >= 5) {
                        PublicInfo.checkCountDownloadsFilesCurrent++;
                        String nameArchive = mod_archive+"5";
                        String url = URLDownload+nameArchive;
                        DownloadFiles(url, nameArchive, "Идет скачивание архивов игры", "Ресурсы", "Скачивание " + PublicInfo.checkCountDownloadsFilesCurrent + " из " + mod_archive_count);
                    } else AlternationInstall(AlternationCount += 1);
                    break;
                }
                case 6: {
                    if (mod_archive_count >= 6) {
                        PublicInfo.checkCountDownloadsFilesCurrent++;
                        String nameArchive = mod_archive+"6";
                        String url = URLDownload+nameArchive;
                        DownloadFiles(url, nameArchive, "Идет скачивание архивов игры", "Ресурсы", "Скачивание " + PublicInfo.checkCountDownloadsFilesCurrent + " из " + mod_archive_count);
                    } else AlternationInstall(AlternationCount += 1);
                    break;
                }
                case 7: {
                    PublicInfo.checkCountDownloadsFilesCurrent++;
                    textViewUpdates.setText("Распаковка архива");

                    String name_archive = mod_archive+"1";

                    ArchiveExtractCallback arcExt = new ArchiveExtractCallback();
                    arcExt.execute(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + "/" + name_archive, patch);
                    break;
                }
                case 8: {
                    PublicInfo.checkCountDownloadsFilesCurrent++;
                    if (mod_archive_count >= 1) deleteArchive(mod_archive+"1");
                    if (mod_archive_count >= 2) deleteArchive(mod_archive+"2");
                    if (mod_archive_count >= 3) deleteArchive(mod_archive+"3");
                    if (mod_archive_count >= 4) deleteArchive(mod_archive+"4");
                    if (mod_archive_count >= 5) deleteArchive(mod_archive+"5");
                    if (mod_archive_count >= 6) deleteArchive(mod_archive+"6");
                    AlternationInstall(AlternationCount +=1);
                    break;
                }
                case 9: {
                    File f = new File(Environment.getExternalStorageDirectory() + "/Android/data/" + PublicInfo.checkReleasePackageName + "/files.ini");
                    try {
                        f.createNewFile();

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    try {
                        Wini ini = new Wini(new File(Environment.getExternalStorageDirectory() + "/Android/data/" + PublicInfo.checkReleasePackageName + "/files.ini"));
                        ini.put("mods", "mod_select", mod_id);
                        ini.put("mods", "mod_name", mod_name);
                        ini.put("mods", "mod_version", mod_version);
                        ini.store();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    File fd = new File(Environment.getExternalStorageDirectory() + "/Android/data/" + PublicInfo.checkReleasePackageName + "/mods/"+"/"+mod_id+"/mod.ini");
                    try {
                        fd.createNewFile();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    try {
                        Wini iniMod = new Wini(new File(Environment.getExternalStorageDirectory() + "/Android/data/" + PublicInfo.checkReleasePackageName + "/mods/"+"/"+mod_id+"/mod.ini"));
                        iniMod.put("mods", "mod_id", mod_id);
                        iniMod.put("mods", "mod_name", mod_name);
                        iniMod.put("mods", "mod_version", mod_version);
                        System.out.println("MIHAIL mod_version install"+mod_version);
                        iniMod.store();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    ReadNick.writeNickMods(mod_id);
                    System.out.println("MIHAIL Установка завершена");

                    Sentry.configureScope(scope -> {

                        String nick = ReadNick.getNick();
                        User user = new User();
                        user.setEmail(nick);
                        user.setUsername(nick);
                        Sentry.setUser(user);
                        //scope.setTag("launcherVer", String.valueOf(PublicInfo.checkVerLauncher));
                       // scope.setTag("clientVer", String.valueOf(PublicInfo.checkVerClient));
                      //  scope.setTag("appVerStatic", PublicInfo.VersionNameAppStatic+"("+ PublicInfo.VersionAppStatic+")");
                        scope.setTag("nickName", nick);
                        //Sentry.captureMessage("SucSborka"+nick);
                    });


                    Toast toast = Toast.makeText(getApplicationContext(),
                            "Установка завершена!\nНажми долго Play и выбери \"Клиент-сборка\"",
                            Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                    Intent intent = new Intent(ModsInstall.this, SetingsActivity.class);
                    startActivity(intent);
                    break;
                }
            }
    }




    private class UnZipFiles extends AsyncTask<String, Void, Void> {
        int currentFile;
        int allFile;
        boolean bigFile;

        String sourceFile;
        String destinationFile;
        public UnZipFiles(String source, String destination) {
            sourceFile = source;
            destinationFile = destination;
        }

        @Override
        protected void onPreExecute() {
            bigFile = false;
            textViewLabelWarning.setText(R.string.text_loading_warning_unzip);
            super.onPreExecute();
        }

        @SuppressLint("ResourceAsColor")
        @Override
        protected void onProgressUpdate(Void... values) {
            if(currentFile == -1) {
                textViewProgressUpdate.setText("0%");
                textViewInfoInstall.setText("");
                textViewUpdates.setText("");
                Toast.makeText(getApplicationContext(), "Файл с архивом не найден!", Toast.LENGTH_LONG).show();
                return;
            }
            else {
                int procent = currentFile * 100 / allFile;
                loadingProgressBar.setProgress((int) procent);
                textViewProgressUpdate.setText("" + currentFile + "/" + allFile + " файлов");
                textViewPercent.setText(procent +"%");
            }

            if(bigFile) {
                textViewLabelWarning.setTextColor(R.color.colorWarning);
                textViewLabelWarning.setText(R.string.text_loading_warning_unzip_bigfile);
            }
            else {
                textViewLabelWarning.setTextColor(R.color.colorBlack);
                textViewLabelWarning.setText(R.string.text_loading_warning_unzip);
            }
            super.onProgressUpdate(values);
        }

        @Override
        protected Void doInBackground(String... filepath) {

            //if(SelectUserUpdate == 1) source = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + "/"+PublicInfo.nameUpdateClientFilesFull;
            // if(SelectUserUpdate == 2) source = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + "/"+PublicInfo.nameUpdateClientFilesLite;
            // String destination = Environment.getExternalStorageDirectory() + "/Android/data/com.rockstargames.gtasa/";
            try {
                if(new File(sourceFile).exists()) {
                    ZipFile zipFile = new ZipFile(sourceFile);
                    ProgressMonitor progressMonitor = zipFile.getProgressMonitor();
                    zipFile.setRunInThread(false);


                    List fileHeaderList = zipFile.getFileHeaders();
                    for (int i = 0; i < fileHeaderList.size(); i++) {
                        FileHeader fileHeader = (FileHeader) fileHeaderList.get(i);
                        if(fileHeader.getUncompressedSize() > 335906950) bigFile = true;
                        else bigFile = false;
                        System.out.println("MIHAIL getUncompressedSize: " + fileHeader.getUncompressedSize());
                        System.out.println("MIHAIL File: " + fileHeader.getFileName());
                        System.out.println("MIHAIL Percent "+zipFile.getProgressMonitor().getPercentDone());
                        System.out.println("MIHAIL Progress: " + (i + 1) + " / " + fileHeaderList.size());
                        currentFile = i + 1;
                        allFile = fileHeaderList.size();
                        publishProgress();
                        zipFile.extractFile(fileHeader, destinationFile);

                    }
                }
                else {
                    currentFile = -1;
                    publishProgress();
                }
            } catch (ZipException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            AlternationInstall(AlternationCount +=1);
            super.onPostExecute(aVoid);
        }
    }


    public void DownloadFiles(String URL, String FileName, String nameStatus, String nameDescription,  String namesetTitle) {
        File sdPath = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + "/" + FileName);//
        sdPath.delete();

        textViewUpdates.setText(nameStatus);

        String urlDownload = URL;

        downloadThread = new DownloadProcess();
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
            request.addRequestHeader("User-Agent", "FlinLauncher-GP/"+ PublicInfo.VersionNameAppStatic);
            request.setDescription(filename);
            request.setTitle(filename);
            System.out.println("МИХАИЛ ФАЙЛ:"+filename);
            request.allowScanningByMediaScanner();
            request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE);
            request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, filename);

            final DownloadManager manager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);

            final long downloadId = manager.enqueue(request);
            download_id = downloadId;

            final ProgressBar mProgressBar = (ProgressBar) findViewById(R.id.progressBarInstall);
            final TextView textViewPercents = (TextView) findViewById(R.id.textViewInfoInstallFull);
            final TextView textViewProgress = (TextView) findViewById(R.id.textViewProgressFull);

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
                                    mProgressBar.setProgress((int) finalDl_progress);
                                    textViewPercents.setText(nameStatus.replace("---", String.valueOf((int) finalDl_progress)));
                                } else {
                                    textViewPercents.setText("Не могу получить информацию о размере файла");
                                    textViewProgress.setText("Не могу получить информацию о размере файла");
                                }
                                if (bytes_total != -1) {
                                    textViewProgress.setText(getString(R.string.progress)
                                            .replace("%chunk%", String.valueOf(humanReadableByteCount(bytes_downloaded, true)))
                                            .replace("%total%", String.valueOf(humanReadableByteCount(bytes_total, true))));
                                }
                                int intPercents = (int) finalDl_progress;
                                textViewPercent.setText(intPercents+"%");
                                statusMessage(status, textViewProgress, textViewPercents, mProgressBar, apk, manager, downloadId);
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
    private void statusMessage(int status, TextView progress, TextView procents, ProgressBar progressBar, boolean apk, DownloadManager manager, long downloadId) {
        switch (status) {
            case DownloadManager.STATUS_SUCCESSFUL:
                progressBar.setProgress(100);
                AlternationInstall(AlternationCount +=1);
                break;
            /*case DownloadManager.STATUS_FAILED:
                System.out.println("МИХАИЛ STATUS_FAILED");
                manager.remove(download_id);
                Intent intent = new Intent(InstallClient.this, DonateActivity.class);
                startActivity(intent);
                break;
            case DownloadManager.STATUS_PENDING:
                System.out.println("МИХАИЛ STATUS_PENDING");
                break;*/
        }
    }

    private void startProgress(){

    }

    // https://stackoverflow.com/a/3758880
    public static String humanReadableByteCount(long bytes, boolean si) {
        int unit = si ? 1000 : 1024;
        if (bytes < unit) return bytes + " B";
        int exp = (int) (Math.log(bytes) / Math.log(unit));
        String pre = (si ? "kMGTPE" : "KMGTPE").charAt(exp-1) + (si ? "" : "i");
        return String.format("%.1f %sB", bytes / Math.pow(unit, exp), pre);
    }
    //После получения прав возобновить код кнопки
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){

            SelectTypeAPP = 1;
            AlternationInstall(AlternationCount +=1);
        }
    }

    private boolean lastTaskDone = true;


    class ExtractProgressDialogView
    {
        TextView currItem,percentage;
        View root;
        Context con;
        AlertDialog pd;
        public ExtractProgressDialogView(Context context,int layout) {
            con=context;
            root= LayoutInflater.from(context).inflate(layout, null);
            currItem=(TextView)root.findViewById(R.id.current_file);
            currItem.setEllipsize(TextUtils.TruncateAt.MARQUEE);
            currItem.setSelected(true);
            percentage = (TextView) root.findViewById(R.id.comp_ratio);
        }
        public View getRoot()
        {
            return root;
        }
        public void showDialog(String title)
        {
            //view =new UpdateProgressDialogView(mainScreen.getContext(), R.layout.progress_dialog);
            AlertDialog.Builder  builder=new AlertDialog.Builder(con);
            pd = builder.setView(root).setTitle(title)
                    .setCancelable(false).create();
            //pd.getWindow().setWindowAnimations(R.style.moving_dialog);
            //   pd.show();
        }
        public void dismiss()
        {
            if(pd != null && pd.isShowing())
                pd.dismiss();
        }
        public void setDialogTitle(String title)
        {
            pd.setTitle(title);
        }
        public void setDialogTitle(int resid)
        {
            pd.setTitle(resid);
        }
        public void setCurrentItemText(String st)
        {
            currItem.setText(st);
            textViewProgressUpdate.setText(st);
        }
        public void setPercentage(String st)
        {
            percentage.setText(st);
        }
        public void setPercentage(long percent)
        {
            //"Compression Ratio:"+ratio+"%"
            percentage.setText("Ratio"+":"+percent+"%");

        }
    }

    class ArchiveExtractCallback extends AsyncTask<String, String, Void>
    {
        private ExtractProgressDialogView pd;
        private PowerManager.WakeLock wl = null;
        private boolean errorDetected = false;
        private long curBytes,totalBytes,totalFiles,curFiles,inSize,outSize;
        private boolean bytesProgressMode = true;

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();

            lastTaskDone = false;
            PowerManager pm = (PowerManager) ModsInstall.this.
                    getSystemService(Context.POWER_SERVICE);
            wl = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "TAG"+ UUID.randomUUID().toString());
            wl.acquire();
            //pd=ProgressDialog.show(ExtractionActivity.this, "Extracting", "Please wait...", true);
            pd = new ExtractProgressDialogView(ModsInstall.this, R.layout.progress_dialog);
            pd.showDialog("Распаковка: ");
            //console.setText("");
        }

        @Override
        protected void onProgressUpdate(String... values) {
            // TODO Auto-generated method stub
            super.onProgressUpdate(values);
            if(values != null)
            {
                if( values.length == 1)
                {
                    //  console.setText(values[0]);
                    pd.setCurrentItemText(values[0]);
                   // System.out.println("MIHAIL file: "+values[0]);
                }
                else if(values.length == 2)
                {
                    if(values[0].equalsIgnoreCase("-E"))
                    {
                        // console.setText("getString(R.string.error)"+values[1]);
                        // showAlert(values[1], "getString(R.string.error)");
                    }
                    else if(values[0].equalsIgnoreCase("-P"))
                    {
                        if (totalBytes == 0)
                            totalBytes = 1;
                        int percentValue = (int)(curBytes * 100 / totalBytes);
                        if(percentValue != Integer.MAX_VALUE)
                        {
                            pd.setDialogTitle("getString(R.string.extracting)"+
                                    " "+percentValue +"%");
                            textViewPercent.setText(percentValue+"%");
                            setProgress(percentValue*100);
                            loadingProgressBar.setProgress((int) percentValue);
                        }
                    }
                    else if(values[0].equalsIgnoreCase("-R"))
                    {
                        long packSize = inSize ,unpackSize= outSize;
                        if(unpackSize != Long.MAX_VALUE && packSize!= Long.MAX_VALUE && unpackSize!= 0)
                        {
                            long ratio = packSize * 100 / unpackSize;
                            // pd.setPercentage("Compression Ratio:"+ratio+"%");
                            pd.setPercentage(ratio);
                        }
                    }
                }
            }
        }
        public void ShowPasswordDialog(final ExtractCallback callback)
        {
            ModsInstall.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    // TODO Auto-generated method stub
                    //	PassWordDialog pd = new PassWordDialog(ExtractionActivity.this, callback);
                    //	pd.show();
                }
            });
        }

        @Override
        protected Void doInBackground(String... params) {
            // TODO Auto-generated method stub
            Archive v=new Archive();
            ExtractCallback exback;
            if(params!=null && params.length==2)
            {
                int ret=0;
                if( ( ret = v.extractArchive(params[0], params[1],exback = new ExtractCallback() {
                    String pass = null;
                    AtomicBoolean passSet = new AtomicBoolean(false);
                    final int S_OK = 0x00000000;
                    final int S_FALSE = 0x00000001;
                    final int E_NOTIMPL = 0x80004001 ;
                    final int  E_NOINTERFACE = 0x80004002;
                    final int E_ABORT = 0x80004004;
                    final int E_FAIL = 0x80004005;
                    final int STG_E_INVALIDFUNCTION = 0x80030001;
                    final int E_OUTOFMEMORY = 0x8007000E;
                    final int E_INVALIDARG = 0x80070057;

                    final int ERROR_NO_MORE_FILES = 0x100123;
                    @Override
                    public void guiSetPassword(String pass)
                    {
                        passSet.set(true);
                        this.pass =pass;
                    }
                    @Override
                    public String guiGetPassword()
                    {
                        return pass;
                    }
                    @Override
                    public boolean guiIsPasswordSet()
                    {
                        return pass!=null;
                    }
                    @Override
                    public long thereAreNoFiles() {
                        // TODO Auto-generated method stub
                        return 0;
                    }

                    @Override
                    public long showMessage(String message) {
                        // TODO Auto-generated method stub
                        return 0;
                    }

                    @Override
                    public long setTotal(long total) {
                        // TODO Auto-generated method stub
                        totalBytes = total;
                        curBytes = 0;
                        return 0;
                    }

                    @Override
                    public long setRatioInfo(long inSize, long outSize) {
                        // TODO Auto-generated method stub
                        //Log.i(TAG,"InSize is:"+inSize+", outSize is:"+outSize);
                        ModsInstall.ArchiveExtractCallback.this.inSize = inSize;
                        ModsInstall.ArchiveExtractCallback.this.outSize = outSize;
                        publishProgress("-R","");
                        return 0;
                    }

                    @Override
                    public long setPassword(String password) {
                        // TODO Auto-generated method stub
                        return 0;
                    }

                    @Override
                    public long setOperationResult(int operationResult,long numfiles, boolean encrypted) {
                        // TODO Auto-generated method stub
                        curFiles = numfiles;
                        String error = "Unknown Error!";
                        if(operationResult!= 0)//Error case
                        {

                            switch(operationResult)
                            {
                                case 1:// kUnSupportedMethod
                                    error = "Error:UNSUPPORTED_METHOD ";//Log.i(TAG,"Error:UNSUPPORTED_METHOD " );
                                    break ;
                                case 2: //kDataError
                                    if(encrypted)
                                        error = "Error:DATA_ERROR_ENCRYPTED";//	Log.i(TAG,"Error:DATA_ERROR_ENCRYPTED" );
                                    else
                                        error = "Error:DATA_ERROR";//	 Log.i(TAG,"Error:DATA_ERROR");
                                    break;
                                case 3: //kCRCError
                                    if(encrypted)
                                        error = "Error:CRC_ENCRYPTED";// 	Log.i(TAG,"Error:CRC_ENCRYPTED" );
                                    else
                                        error = "CRC Error";//	 Log.i(TAG,"CRC Error");
                                    break;
                                default:
                                    Log.d("TAG","operation failed with Result:"+error);
                                    return E_FAIL;
                            }
                            System.out.println("MIHAIL 7Z Error:"+ error);
                            //publishProgress(error);
                            //addErrorMessage(error);
                        }
                        Log.d("TAG","operation failed with Result:"+error);
                        //Log.i(TAG,"setOperationResult called opres="+ operationResult );
                        return 0;
                    }

                    @Override
                    public long setNumFiles(long numFiles) {
                        // TODO Auto-generated method stub
                        totalFiles = numFiles;
                        return 0;
                    }

                    @Override
                    public long setCurrentFilePath(String filePath,long numFilesCur) {
                        // TODO Auto-generated method stub
                        curFiles = numFilesCur;
                        publishProgress(filePath);
                        return 0;
                    }

                    @Override
                    public long setCompleted(long value) {
                        // TODO Auto-generated method stub
                        curBytes = value;
                        publishProgress("-P","");
                        return 0;
                    }

                    @Override
                    public long prepareOperation(String name, boolean isFolder,
                                                 int askExtractMode, long position) {
                        // TODO Auto-generated method stub
                        return 0;
                    }

                    @Override
                    public boolean open_WasPasswordAsked() {
                        // TODO Auto-generated method stub
                        return false;
                    }

                    @Override
                    public long open_SetTotal(long numFiles, long numBytes) {
                        // TODO Auto-generated method stub
                        totalFiles = numFiles ;
                        return 0;
                    }

                    @Override
                    public long open_SetCompleted(long numFiles, long numBytes) {
                        // TODO Auto-generated method stub
                        return 0;
                    }

                    @Override
                    public long open_GetPasswordIfAny(String password) {
                        // TODO Auto-generated method stub
                        return 0;
                    }

                    @Override
                    public long open_CryptoGetTextPassword(String password) {
                        // TODO Auto-generated method stub
                        return 0;
                    }

                    @Override
                    public void open_ClearPasswordWasAskedFlag() {
                        // TODO Auto-generated method stub

                    }

                    @Override
                    public long open_CheckBreak() {
                        // TODO Auto-generated method stub
                        return 0;
                    }

                    @Override
                    public void openResult(String name, long result, boolean encrypted) {
                        // TODO Auto-generated method stub
                        //Log.i(TAG, "openResult="+result);
					/*if(result!=S_OK)
					{
						String txt;
						if(result==S_FALSE)
						{
							Log.i(TAG,(encrypted?"Cannot open encrypted archive":"Cannot open archive")
								+" :"+name);
							txt=encrypted?"Cannot open encrypted archive":"Cannot open archive";
						}
						else
						{
							if (result == E_OUTOFMEMORY)
							{//E_OUTOFMEMORY
								Log.i(TAG, "Memory Error openning archive");
								txt= "Memory Error openning archive";
							}
							else
							{

								switch((int)result) {
							    case ERROR_NO_MORE_FILES   : txt = "No more files"; break ;
							    case E_NOTIMPL             : txt = "E_NOTIMPL"; break ;
							    case E_NOINTERFACE         : txt = "E_NOINTERFACE"; break ;
							    case E_ABORT               : txt = "E_ABORT"; break ;
							    case E_FAIL                : txt = "E_FAIL"; break ;
							    case STG_E_INVALIDFUNCTION : txt = "STG_E_INVALIDFUNCTION"; break ;
							    case E_OUTOFMEMORY         : txt = "E_OUTOFMEMORY"; break ;
							    case E_INVALIDARG          : txt = "E_INVALIDARG"; break ;
							    default:
							      txt = "UnKnown Error";
							  }
							}
						}
						publishProgress("-E",txt);
						return;
					}*/
                        Log.d("TAG","Archive opened successfully:"+name);
                    }

                    @Override
                    public long messageError(String message) {
                        // TODO Auto-generated method stub
                        return 0;
                    }

                    @Override
                    public void extractResult(long result) {
                        // TODO Auto-generated method stub

                    }

                    @Override
                    public String cryptoGetTextPassword(String password) {
                        // TODO Auto-generated method stub
                        //publishProgress("-s","Pass");
                        ShowPasswordDialog(this);
                        synchronized(this)
                        {
                            while(!passSet.get())
                                try {
                                    wait();
                                } catch (InterruptedException e) {
                                    // TODO Auto-generated catch block
                                    e.printStackTrace();
                                }
                        }
                        return pass;
                    }

                    @Override
                    public void beforeOpen(String name) {
                        // TODO Auto-generated method stub

                    }

                    @Override
                    public long askWrite(String srcPath, int srcIsFolder, long srcTime,
                                         long srcSize, String destPathRequest, String destPathResult,
                                         int writeAnswer) {
                        // TODO Auto-generated method stub
                        return 0;
                    }

                    @Override
                    public long askOverwrite(String existName, long existTime, long existSize,
                                             String newName, long newTime, long newSize, int answer) {
                        // TODO Auto-generated method stub
                        return 0;
                    }

                    @Override
                    public void addErrorMessage(String message) {
                        // TODO Auto-generated method stub
                        publishProgress("-E",message);
                    }
                }))!=0)	//error case
                {
                    Log.i("TAG", "Extract Archive Error:"+ret);
                    publishProgress("-E","Extract Archive Error:"+ret);
                    errorDetected=true;
                }
                else
                    errorDetected=false;

            }
            return null;
        }
        @Override
        protected void onPostExecute(Void result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);
            lastTaskDone = true;
            pd.dismiss();
            setProgress(Window.PROGRESS_END);
            if(!errorDetected) {
                // console.setText("");
                //      Toast.makeText(InstallClient.this,"Extraction done successfully !",Toast.LENGTH_LONG).show();
                AlternationInstall(AlternationCount +=1);
            }

            if(wl  != null && wl.isHeld() )
            {
                Log.d("Tag","Releasing WakeLock...");
                wl.release();
            }
        }

    }

}
