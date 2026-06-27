package com.flin.online;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import cz.msebera.android.httpclient.Header;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.text.Html;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.flin.online.function.ReadNick;
import com.flin.online.jsonenter.IpData;
import com.flin.online.jsonenter.PublicInfo;
import com.flin.online.jsonenter.ReadMonitoring;
import com.flin.online.model.NewsModel;
import com.flin.online.query.SampQuery;
import com.flin.online.snap.AdapterSnapGeneric;
import com.flin.online.snap.Image;
import com.flin.online.snap.StartSnapHelper;
import com.flinc.core.R;
import com.google.android.gms.ads.identifier.AdvertisingIdClient;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;


import org.ini4j.Wini;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.nio.file.Files;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Random;

import static com.flin.online.jsonenter.PublicInfo.successDonate;

public class MenuActivity extends AppCompatActivity {

    TextView textViewServerOneCount;
    ProgressBar progressBarServerOne;

    ProgressBar progressBarServerTwo;
    TextView textViewServerTwoCount;
    static String nickName;
    final int[] checkBoxStatus = {0};

    public static List<NewsModel> itemsNews;

    //SharedPreferences
    public static final String APP_PREFERENCES = "settings_app";
    public static final String APP_PREFERENCES_DIALOG = "DialogStart";
    SharedPreferences mSettings;
    static SharedPreferences sharedPassword;
    static SharedPreferences sharedNickName;
    public static final String APP_PREFERENCES_LOGIN = "settings_as";
    public static final String APP_PREFERENCES_LOGIN_NICK = "settings_nn";

    private final int MY_PERMISSIONS_RECORD_AUDIO = 1;

    View startGameView;
    int saveButtonPressing; //нажатие на кнопку долго или 1

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
    }
    /** * Intent to send a telegram message * @param msg */

    public static List<File> listf(String directoryName) {
        File directory = new File(directoryName);

        List<File> resultList = new ArrayList<File>();

        // get all the files from a directory
        File[] fList = directory.listFiles();
        resultList.addAll(Arrays.asList(fList));
        for (File file : fList) {
            if (file.isFile()) {
                System.out.println(file.getAbsolutePath());
            } else if (file.isDirectory()) {
                resultList.addAll(listf(file.getAbsolutePath()));
            }
        }
        //System.out.println(fList);
        return resultList;
    }

    void intentMessageTelegram(String msg) {
      /* final String appName = "org.telegram.messenger";
        Intent myIntent = new Intent(Intent.ACTION_SEND_MULTIPLE);
        //myIntent.setType("/");
        myIntent.setPackage(appName);
        File imgCopy = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + "/gh1.jpg");
        File imgCopy2 = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + "/gh.jpg");
        File imgOrig = new File(Environment.getExternalStorageDirectory() + "/Android/data/"+PublicInfo.checkReleasePackageName+"/files/SAMP/crash_log.log");
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                copy(imgOrig,imgCopy);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        Uri uri = Uri.parse(Environment.getExternalStorageDirectory() + "/Android/data/"+PublicInfo.checkReleasePackageName+"/files/SAMP/gh.jpg");

      /*  myIntent.setAction(Intent.ACTION_SEND);
        ArrayList<Uri> imageUris  = new ArrayList<Uri>();
        imageUris.add(new Uri("Бегемот"));
        imageUris.add(Uri.fromFile(imgCopy));
        imageUris.add(Uri.fromFile(imgCopy2));
    //    imageUris.add( Uri.fromFile(imgCopy2));
        myIntent.putExtra(Intent.EXTRA_TEXT, "Hello");
        myIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        myIntent.putExtra(Intent.EXTRA_STREAM, uri);
        startActivity(Intent.createChooser(myIntent, "Share with"));*/

        PrintWriter writer = null;
        try {
            writer = new PrintWriter(new OutputStreamWriter(new FileOutputStream(new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + "/listfiles.txt")), "UTF-8"));
            writer.println("Файлы игрока");
            File dir = new File(Environment.getExternalStorageDirectory() + "/Android/data/"+PublicInfo.checkReleasePackageName+"/files"); //path указывает на директорию
            File[] arrFiles = dir.listFiles();
            List<File> lst = listf(Environment.getExternalStorageDirectory() + "/Android/data/"+PublicInfo.checkReleasePackageName+"/files/");
            for (int i = 0; i < lst.size(); i++) {
                System.out.println("МИХАИЛ счет "+lst.get(i));
                String str = String.valueOf(lst.get(i));
                writer.println(str.replace(Environment.getExternalStorageDirectory() + "/Android/data/"+PublicInfo.checkReleasePackageName+"/", "/"));
            }



            writer.close();

            File copyLog = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + "/samp_log.txt");
            File copyCrashLog = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + "/crash_log.log");
            File structureFilesClient = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + "/listfiles.txt");
            File OrigLog = new File(Environment.getExternalStorageDirectory() + "/Android/data/"+PublicInfo.checkReleasePackageName+"/files/SAMP/samp_log.txt");
            File OrigCrashLog = new File(Environment.getExternalStorageDirectory() + "/Android/data/"+PublicInfo.checkReleasePackageName+"/files/SAMP/crash_log.log");

            try {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    copy(OrigLog,copyLog);
                    copy(OrigCrashLog,copyCrashLog);

                    ArrayList<Uri> imageUris = new ArrayList<Uri>();
                    imageUris.add(Uri.fromFile(copyCrashLog));
                    imageUris.add(Uri.fromFile(copyLog));
                    imageUris.add(Uri.fromFile(structureFilesClient));

                    Intent shareIntent = new Intent();
                    shareIntent.setAction(Intent.ACTION_SEND_MULTIPLE);
                    shareIntent.setPackage("org.telegram.messenger");
                    shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    shareIntent.putExtra(Intent.EXTRA_TEXT, "Тут текст об устройстве телефона: "+Build.MODEL+"\nAndroid:"+ Build.VERSION.SDK_INT );
                    shareIntent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, imageUris);
                    shareIntent.setType("*/*");
                    startActivity(Intent.createChooser(shareIntent, "Share images to.."));
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    public static void copy(File origin, File dest) throws IOException {
        if (dest.delete()) {
            System.out.println("MIHAIL файл удален");
        } else
            System.out.println("MIHAIL    не обнаружено");
        Files.copy(origin.toPath(), dest.toPath());
    }

    private void startGame(){

        if(PublicInfo.checkStartClient > 0){
            if (PublicInfo.checkStartClient == 2 && Build.VERSION.SDK_INT <= Build.VERSION_CODES.M) {

                    Toast toast = Toast.makeText(getApplicationContext(),
                            "Бета доступна только на Android 6 и выше.",
                            Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                    return;
            }
            Intent intent = new Intent(MenuActivity.this, MainStart.class);
            startActivity(intent);
        }
        else {
            //Вызов диалога с первичным выбором
            dialogCreateShow(startGameView);
        }
    }
    private void requestAudioPermissions(int typeStart) {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.RECORD_AUDIO)
                != PackageManager.PERMISSION_GRANTED) {

            //When permission is not granted by user, show them message why this permission is needed.
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.RECORD_AUDIO)) {

                AlertDialog.Builder builderUpdateClient;
                builderUpdateClient = new AlertDialog.Builder(this);
                builderUpdateClient.setTitle("Разрешение доступа к микрофону");
                builderUpdateClient.setMessage("У вас отсутствует разрешение на доступ, разрешите доступ нажав на кнопку ниже.\n\nВ ином случае без этого разрешения возможны вылеты игры.");
                builderUpdateClient.setPositiveButton("Перейти к настройкам", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //Give user option to still opt-in the permissions
                        ActivityCompat.requestPermissions(MenuActivity.this,
                                new String[]{Manifest.permission.RECORD_AUDIO},
                                MY_PERMISSIONS_RECORD_AUDIO);
                    }
                });
                ;
                AlertDialog alert = builderUpdateClient.create();
                alert.show();


            } else {
                // Show user dialog to grant permission to record audio
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.RECORD_AUDIO},
                        MY_PERMISSIONS_RECORD_AUDIO);
            }
        }
        //If permission is granted, then go ahead recording audio
        else if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.RECORD_AUDIO)
                == PackageManager.PERMISSION_GRANTED) {

            //Go ahead with recording audio now
            if(typeStart == 1){
                startGame();
            }
            else{
                dialogCreateShow(startGameView);
            }
        }
    }

    //Handling callback
    @SuppressLint("MissingSuperCall")
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_RECORD_AUDIO: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if(saveButtonPressing == 1){
                        startGame();
                    }
                    else{
                        dialogCreateShow(startGameView);
                    }
                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    Toast.makeText(this, "Запуск игры без разрешения микрофона невозможен.", Toast.LENGTH_LONG).show();
                }
                return;
            }
        }
    }


    private List<Image> items = new ArrayList<>();
    private static Random r = new Random();

    public static int randInt(int max) {
        int min = 0;
        return r.nextInt((max - min) + 1) + min;
    }
    public class GetGoogleUidTask extends AsyncTask<Void, Void, String> {

        @Override
        protected String doInBackground(Void... params) {
            String googleUid = "";
            try {
                AdvertisingIdClient.Info adInfo = AdvertisingIdClient.getAdvertisingIdInfo(getApplicationContext());
                googleUid = adInfo.getId();
            } catch (IOException | GooglePlayServicesNotAvailableException | GooglePlayServicesRepairableException e) {
                e.printStackTrace();
            }
            return googleUid;
        }

        @Override
        protected void onPostExecute(String result) {
            // Обработка результата
            // Здесь вы можете передать результат обратно в вызывающий код
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        TextView infoVersionApp = (TextView) findViewById(R.id.textViewVersion);
        infoVersionApp.setText("v"+ PublicInfo.VersionNameAppStatic+" (Build "+PublicInfo.VersionAppStatic+")");


        Date dateNow = new Date();
        SimpleDateFormat formatForDateNow = new SimpleDateFormat("dd.MM.yyyy");



        System.out.println("MIHAIL Текущая дата " + formatForDateNow.format(dateNow));


        new GetGoogleUidTask() {
            @Override
            protected void onPostExecute(String result) {
                // Обработка результата
                // Здесь вы можете получить результат
                if(result.length() < 3) {
                    PublicInfo.UID = "none";
                } else {
                    PublicInfo.UID = result;
                }
                System.out.println("MIHAIL UID: " + PublicInfo.UID);

            }
        }.execute();

        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url("https://dimkov.online/ip.php")
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                    PublicInfo.UIP = "none";
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (!response.isSuccessful()) {
                    PublicInfo.UIP = "none";
                    throw new IOException("Unexpected code " + response);
                }
                String responseBody = response.body().string();
                Gson gson = new Gson();
                IpData.IPInfo ipInfo = gson.fromJson(responseBody, IpData.IPInfo.class);
                // Now you can access the IP address and other information from the IPInfo object
                String ipAddress = ipInfo.getIp();
                System.out.println("MIHAIL IP "+ipAddress);
                PublicInfo.UIP = ipAddress;
            }
        });


        final Animation animAlpha = AnimationUtils.loadAnimation(this, R.anim.click);


        progressBarServerOne = (ProgressBar) findViewById(R.id.progressBarServerOne);
        textViewServerOneCount = (TextView) findViewById(R.id.textViewServerOneCount);

        progressBarServerTwo = (ProgressBar) findViewById(R.id.progressBarServerTwo);
        textViewServerTwoCount = (TextView) findViewById(R.id.textViewServerTwoCount);

        ProgressBar progressBarServerOne = (ProgressBar) findViewById(R.id.progressBarServerOne);

        TextView textViewWelcomePlayer = (TextView) findViewById(R.id.textViewWelcomePlayer);
        ReadNick.show(textViewWelcomePlayer);

        Animation animAlphaStart = AnimationUtils.loadAnimation(this, R.anim.click);

        mSettings = getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
        OpenDialog();

        checkBoxStatus[0] = 0; //Сброс



        LinearLayout linearLayoutNews = (LinearLayout) findViewById(R.id.linearLayoutNews);


       // List<Image> items = getImageDate(this);

       // itemsNews =  new ArrayList<NewsModel>();

        if(PublicInfo.loagingNews == 1) {
            try {

                linearLayoutNews.setVisibility(View.VISIBLE);
                RecyclerView recyclerStart = findViewById(R.id.recyclerStart);

                recyclerStart.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

                JSONArray rootJSON = new JSONArray(new JSONTokener(PublicInfo.json_news));
                for (int i = 0; i < rootJSON.length(); i++) {
                    JSONObject o = rootJSON.getJSONObject(i);

                    itemsNews.add(new NewsModel(o.getString("name"), o.getString("data"), o.getString("description"), o.getString("img_1"), o.getInt("type"), o.getString("url")));

                 //   System.out.println("Mihail ID: " + o.getString("id") + " Type:" + o.getInt("type") + " NameJson:" + o.getString("name") + " FullText:" + o.getString("description"));
                }
                recyclerStart.setAdapter(new AdapterSnapGeneric(this, itemsNews, R.layout.item_snap_basic));
                recyclerStart.setOnFlingListener(null);
                new StartSnapHelper().attachToRecyclerView(recyclerStart);

            } catch (JSONException e) {
                e.printStackTrace();
            }


        }
        else {
            linearLayoutNews.setVisibility(View.GONE);
        }
        //recyclerStart.setOnClickListener();

       /* LinearLayout layout = (LinearLayout) findViewById(R.id.linear);
        LayoutInflater inflater = getLayoutInflater();
        View layer1, layer2;
        layer1 = inflater.inflate(R.layout.linear_monitoring, null);
        layer2 = inflater.inflate(R.layout.linear_monitoring, null);
        layout.addView(layer1);*/
       /* for (int i = 0; i < 10; i++) {
            ImageView imageView = new ImageView(this);
            imageView.setId(i);
            imageView.setPadding(4, 2, 4, 2);
            if(i == 2) {
                imageView.setImageBitmap(BitmapFactory.decodeResource(
                        getResources(), R.drawable.story_1));
            }
                else
                    imageView.setImageBitmap(BitmapFactory.decodeResource(
                    getResources(), R.drawable.ic_launcher));
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            layout.addView(imageView);
        }*/

       sharedPassword = getSharedPreferences(APP_PREFERENCES_LOGIN, MODE_PRIVATE);
       sharedNickName = getSharedPreferences(APP_PREFERENCES_LOGIN_NICK, MODE_PRIVATE);
       DialogLoginFragment.LoadPreferences();




        //GoogleAuthenticator gAuth = new GoogleAuthenticator();
    //    int code = gAuth.getTotpPassword("R2DEH5X5IIZJN55Y");*/






        //Нажатие отправка логов
        Button button5 = (Button) findViewById(R.id.button5);
        button5.setClickable(true);
        button5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                File srt = new File(Environment.getExternalStorageDirectory() + "/Android/data/"+PublicInfo.checkReleasePackageName+"/files/SAMP/crash_log.log");
                File drt = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + "/FlinLog/crash_log.log");
                File srtSamp = new File(Environment.getExternalStorageDirectory() + "/Android/data/"+PublicInfo.checkReleasePackageName+"/files/SAMP/samp_log.txt");
                File drtSamp = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + "/FlinLog/samp_log.txt");

                try {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        copy(srt, drt);
                        copy(srtSamp, drtSamp);
                        Toast toast = Toast.makeText(getApplicationContext(),
                                "Логи скопированы в загрузки!!!",
                                Toast.LENGTH_SHORT);
                        toast.setGravity(Gravity.CENTER, 0, 0);
                        toast.show();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        //Нажатие меню Сообщить
        ImageView buttonNameSendTechVK = (ImageView) findViewById(R.id.buttonNameSendTechVK);
        buttonNameSendTechVK.setClickable(true);
        buttonNameSendTechVK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builderUpdateClient;
                builderUpdateClient = new AlertDialog.Builder(MenuActivity.this);
                builderUpdateClient.setTitle("Вы нашли ошибку?");
                builderUpdateClient.setMessage("Перейдите в группу в ВК и напишите нам о своей проблеме\n\n"+ Html.fromHtml(""+PublicInfo.error_sendMessage));
                builderUpdateClient.setPositiveButton("Открыть группу", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://vk.com/im?sel=-182731020"));
                        startActivity(browserIntent);
                    }
                });
                builderUpdateClient.setNegativeButton("Закрыть", null);
                AlertDialog alert = builderUpdateClient.create();
                alert.show();
                v.startAnimation(animAlphaStart);
            }
        });


        //Нажатие меню Контактов
        ImageView imageViewContactForm = (ImageView) findViewById(R.id.imageViewContactForm);
        imageViewContactForm.setClickable(true);
        imageViewContactForm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
			    Intent intent = new Intent(MenuActivity.this, ContactActivity.class);
                startActivity(intent);
                v.startAnimation(animAlphaStart);


               /* File srt = new File(Environment.getExternalStorageDirectory() + "/Android/data/"+PublicInfo.checkReleasePackageName+"/files/SAMP/crash_log.log");
                File drt = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + "/FlinLog/crash_log.log");
                File srtSamp = new File(Environment.getExternalStorageDirectory() + "/Android/data/"+PublicInfo.checkReleasePackageName+"/files/SAMP/samp_log.txt");
                File drtSamp = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + "/FlinLog/samp_log.txt");

                try {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        copy(srt, drt);
                        copy(srtSamp, drtSamp);
                        Toast toast = Toast.makeText(getApplicationContext(),
                                "Логи скопированы в загрузки!!!",
                                Toast.LENGTH_SHORT);
                        toast.setGravity(Gravity.CENTER, 0, 0);
                        toast.show();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }*/
//мишки
                }


                // Change this to the saved secret from the running the above test.
              /*   String savedSecret = "F6EUJJMYK7GDC4KI";
                Provider[] secureRandomProviders = Security.getProviders("SecureRandom.SHA1PRNG");
                System.setProperty("com.warrenstrange.googleauth.rng.algorithmProvider",secureRandomProviders[0].getName());


                GoogleAuthenticator gAuth = new GoogleAuthenticator();
                int code = gAuth.getTotpPassword(savedSecret);
                System.out.println("MIHAIL getTotpPassword"+code);*/
                /* FragmentManager fragmentManager = getSupportFragmentManager();
                DialogLoginFragment newFragment = new DialogLoginFragment();
                FragmentTransaction transaction = fragmentManager.beginTransaction();
                transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                transaction.add(android.R.id.content, newFragment).addToBackStack(null).commit();
                */
              /*  try {
                    StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
                    StrictMode.setThreadPolicy(policy);
                    URL url = new URL("http://img.rockrust.ru/tg_log.php?nick=Mihail_Dimkov&g=adrenoddd");
                    URLConnection connection = url.openConnection();
                    connection.connect();
                } catch (IOException e) {
                    e.printStackTrace();
                }*/

            //}
        });




        //Нажатие отправка логов
        Button buttonShare = (Button) findViewById(R.id.buttonShare);
        buttonShare.setClickable(true);
        buttonShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                intentMessageTelegram("test");

            }
        });

        TextView textView38 = (TextView) findViewById(R.id.textView38);



        //Нажатие отправка логов
        Button button4 = (Button) findViewById(R.id.button4);
        button4.setClickable(true);
        button4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SampQuery query = new SampQuery("193.84.90.17", 7771);

                if (query.connect()) { // If a successful connection has been made
                    button4.setVisibility(View.INVISIBLE);
                    String[] serverInfo = query.getInfo(); // Get server info
                    System.out.println(serverInfo[0]+" - "+serverInfo[1]+"/"+serverInfo[2]+" - "+serverInfo[3]+" | "+serverInfo[4]+" | "+serverInfo[5]);
                    textView38.setText(serverInfo[0]+" - "+serverInfo[1]+"/"+serverInfo[2]+" - "+serverInfo[3]+" | "+serverInfo[4]+" | "+serverInfo[5]);
                    String[][] basicPlayers = query.getBasicPlayers(); // Get basic players, connection will time out if the player counter is above 100 and will return an empty array if no players are online
                    System.out.println("Basic players:");
                    for (int i = 0; basicPlayers.length > i; i++) {
                        System.out.println((i + 1)+") "+basicPlayers[i][0]+" - "+basicPlayers[i][1]);
                    }

                    String[][] detailedPlayers = query.getDetailedPlayers(); // Get detailed players, connection will time out if the player counter is above 100 and will return an empty array if no players are online
                    System.out.println("Detailed players:");
                    for (int i = 0; detailedPlayers.length > i; i++) {
                        System.out.println("("+detailedPlayers[i][0]+") "+detailedPlayers[i][1]+" | "+detailedPlayers[i][2]+" | "+detailedPlayers[i][3]);
                    }

                    System.out.println("Rules:");
                    String[][] rules = query.getRules(); // Get server rules
                    for (int i = 0; rules.length > i; i++) {
                        System.out.println(rules[i][0]+" : "+rules[i][1]);
                    }
                    query.close(); // Close the connection
                } else {
                    System.out.println("Server did not respond!");
                    button4.setVisibility(View.VISIBLE);
                }
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
                v.startAnimation(animAlpha);
                Intent intent = new Intent(MenuActivity.this, SetingsActivity.class);
                startActivity(intent);
            }
        });


        //Нажатие меню старта игры
        FloatingActionButton imageDownCenterMenu = (FloatingActionButton) findViewById(R.id.imageDownCenterMenu);
        imageDownCenterMenu.setClickable(true);
        imageDownCenterMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startGameView = v;
                saveButtonPressing = 1;
                if(PublicInfo.checkInstallLastVersion > PublicInfo.checkVerClient){
                    //ОБНОВИТЬКОД
                    AlertDialog.Builder builderUpdateClient;
                    builderUpdateClient = new AlertDialog.Builder(MenuActivity.this);
                    builderUpdateClient.setTitle("Новое обновление");
                    builderUpdateClient.setMessage("Доступна новая версия, вы не сможете запустить игру пока не обновите клиент\n\nОбновление от "+PublicInfo.dateUpdateApk);
                    builderUpdateClient.setPositiveButton("Обновить", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            PublicInfo.updateInstallTypeClient = 1;
                            PublicInfo.currentFilesUpdate = 1;
                            //ОБНОВИТЬКОД
                            Intent intent = new Intent(MenuActivity.this, ReUpdateClient.class);
                            startActivity(intent);
                        }
                    });
                    ;
                    AlertDialog alert = builderUpdateClient.create();
                    alert.show();
                }
                else requestAudioPermissions(1);
            }

        });

        imageDownCenterMenu.setOnLongClickListener(new View.OnLongClickListener() {
            public boolean onLongClick(View v) {
                saveButtonPressing = 2;
                if(PublicInfo.checkInstallLastVersion > PublicInfo.checkVerClient){
                    AlertDialog.Builder builderUpdateClient;
                    builderUpdateClient = new AlertDialog.Builder(MenuActivity.this);
                    builderUpdateClient.setTitle("Новое обновление");
                    builderUpdateClient.setMessage("Доступна новая версия, вы не сможете запустить игру пока не обновите клиент\n\nОбновление от "+PublicInfo.dateUpdateApk);
                    builderUpdateClient.setPositiveButton("Обновить", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            PublicInfo.updateInstallTypeClient = 1;
                            PublicInfo.currentFilesUpdate = 1;
                            //ОБНОВИТЬКОД
                            Intent intent = new Intent(MenuActivity.this, ReUpdateClient.class);
                            startActivity(intent);
                        }
                    });
                    ;
                    AlertDialog alert = builderUpdateClient.create();
                    alert.show();
                }
                else requestAudioPermissions(2);
                return false;
            }
        });
        requestData();

        try {
            //Чек версии
            Wini inid = new Wini(new File(Environment.getExternalStorageDirectory()  + "/FlinOnline/files.ini"));

            int clientfiles = inid.get("app", "clientfiles", Integer.class);
            int texcturefiles = inid.get("app", "texcturefiles", Integer.class);
            int texctureTextDBfiles = 0;
            try {
                texctureTextDBfiles = inid.get("app", "texcturetexdb", Integer.class);
            } catch (NullPointerException e) {
                //e.printStackTrace();
                //System.out.println("MIHAIL произошла ошибка texctureTextDBfiles");
            }
            PublicInfo.dirGameLocalType = 0;
            try {
                PublicInfo.dirGameLocalType = inid.get("storage", "type", Integer.class);
                System.out.println("MIHAIL typeGameStorage ini"+ PublicInfo.dirGameLocalType);
            } catch (NullPointerException e) {
                e.printStackTrace();
                System.out.println("MIHAIL typeGameStorage error");
            }

            int apkVersion = inid.get("app", "apk", Integer.class);


            if(PublicInfo.dirGameLocalType == 0){
                SaveOptims(1, "0");
            }

            if(PublicInfo.dirGameLocalType == 1) {
                PublicInfo.dirGameLocal = Environment.getExternalStorageDirectory()  + "/FlinOnline/files/";
            }

            PublicInfo.checkFilesTextDBVersionLocal = texctureTextDBfiles;
          //  System.out.println("МИХАИЛ checkFilesTextDBVersionLocal: "+texctureTextDBfiles);
          //  System.out.println("МИХАИЛ версия "+PublicInfo.checkVerLauncher);


            if(PublicInfo.verLauncher  > PublicInfo.checkVerLauncher ||
                    PublicInfo.verClient  > PublicInfo.checkVerClient)
            {
                //ОБНОВИТЬКОД
                AlertDialog.Builder builderUpdateClient;
                builderUpdateClient = new AlertDialog.Builder(this);
                builderUpdateClient.setTitle("Новое обновление");
                builderUpdateClient.setMessage("Доступна новая версия, вы хотите скачать и установить обновление?\n\nОбновление от "+PublicInfo.dateUpdateApk);
                builderUpdateClient.setPositiveButton("Да, обновить", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        PublicInfo.updateInstallTypeClient = 1;
                        PublicInfo.currentFilesUpdate = 1;
                        //ОБНОВИТЬКОД
                        Intent intent = new Intent(MenuActivity.this, ReUpdateClient.class);
                        startActivity(intent);
                    }
                });

                builderUpdateClient.setNegativeButton("Игнорировать", null);
                AlertDialog alert = builderUpdateClient.create();
                alert.show();
            }
            if(PublicInfo.checkSelectInstall == 1) {
                if (PublicInfo.checkFilesClientVersion7Z > clientfiles ||
                        PublicInfo.checkFilesTexctureVersion7Z > texcturefiles || PublicInfo.checkFilesTextDBVersion7Z > texctureTextDBfiles) {

                    //ОБНОВИТЬКОД
                    AlertDialog.Builder builderUpdateClient;
                    builderUpdateClient = new AlertDialog.Builder(this);
                    builderUpdateClient.setTitle("Новое обновление файлов клиента");
                    builderUpdateClient.setMessage("Доступна новая версия, вы хотите скачать и установить обновление?\n\nОбновление от " + PublicInfo.dateUpdateFiles);
                    builderUpdateClient.setPositiveButton("Да, обновить", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            PublicInfo.currentFilesUpdate = 2;
                            PublicInfo.updateInstallTypeClient = 1;
                            //ОБНОВИТЬКОД
                            Intent intent = new Intent(MenuActivity.this, LoadingGPU.class);
                            intent.putExtra("start_update", 1);
                            startActivity(intent);
                        }
                    });
                    ;
                    builderUpdateClient.setNegativeButton("Игнорировать", null);
                    AlertDialog alert = builderUpdateClient.create();
                    alert.show();
                }

            }
            else {
                if (PublicInfo.checkFilesClientVersion > clientfiles ||
                        PublicInfo.checkFilesTexctureVersion > texcturefiles || PublicInfo.checkFilesTextDBVersion > texctureTextDBfiles) {

                    //ОБНОВИТЬКОД
                    AlertDialog.Builder builderUpdateClient;
                    builderUpdateClient = new AlertDialog.Builder(this);
                    builderUpdateClient.setTitle("Новое обновление файлов клиента");
                    builderUpdateClient.setMessage("Доступна новая версия, вы хотите скачать и установить обновление?\n\nОбновление от " + PublicInfo.dateUpdateFiles);
                    builderUpdateClient.setPositiveButton("Да, обновить", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            PublicInfo.currentFilesUpdate = 2;
                            PublicInfo.updateInstallTypeClient = 1;
                            //ОБНОВИТЬКОД
                            Intent intent = new Intent(MenuActivity.this, ReUpdateClient.class);
                            startActivity(intent);
                        }
                    });
                    ;
                    builderUpdateClient.setNegativeButton("Игнорировать", null);
                    AlertDialog alert = builderUpdateClient.create();
                    alert.show();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }


        //Нажатие меню Доната
        TextView imageDownRightMenu = (TextView) findViewById(R.id.imageDownRightMenu);
        imageDownRightMenu.setClickable(true);
        imageDownRightMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MenuActivity.this, DonateActivity.class);
                startActivity(intent);
            }
        });


        //Перенос со старого
        if(PublicInfo.successInstallApk == true){
            PublicInfo.successInstallApk = false;
            AlertDialog.Builder builderSelectHelp;
            builderSelectHelp = new AlertDialog.Builder(this);
            builderSelectHelp.setTitle("Установка завершена!");
            builderSelectHelp.setMessage("Теперь можете нажать кнопку Play в нижней части экрана и наслаждаться игрой на нашем проекте.\nСпасибо, что выбрали наш проект!");
            builderSelectHelp.setPositiveButton("ОК", null);
            //builderSelectHelp.setNegativeButton("Нет", null);
            AlertDialog alert = builderSelectHelp.create();
            alert.show();
        }

        if(PublicInfo.successUpdateApk == true){
            PublicInfo.successUpdateApk = false;
            //ОБНОВИТЬКОД
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Обновление завершено!")
                    .setMessage("Теперь можете нажать кнопку Play в нижней части экрана и наслаждаться игрой")
                    //         .setIcon(R.mipmap.ic_launcher)
                    .setCancelable(false)
                    .setNegativeButton("ОК",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                }
                            });
            AlertDialog alert = builder.create();
            alert.show();
        }

        if(successDonate == 1){
            successDonate = 0;
            //ОБНОВИТЬКОД
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Ваш аккаунт пополнен!")
                    .setMessage("Оплата завершена успешно, можете теперь зайти в игру" )
                    // .setIcon(R.mipmap.ic_launcher)
                    .setCancelable(false)
                    .setNegativeButton("ОК",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                }
                            });
            AlertDialog alert = builder.create();
            alert.show();
        }

        if(successDonate == 2){
            successDonate = 0;
            //ОБНОВИТЬКОД
           /* AlertDialog.Builder builderSelectHelp;
            builderSelectHelp = new AlertDialog.Builder(this, R.style.AlertDialogTheme);
            builderSelectHelp.setTitle("Ваш аккаунт не пополнен!");
            builderSelectHelp.setIcon(R.mipmap.ic_launcher);
            builderSelectHelp.setMessage("Оплата завершилась не удачно, попробуйте позже или поспользуйтесь пополнением через сайт");
            builderSelectHelp.setNegativeButton("Перейти на сайт", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://flin-rp.su/donate"));
                    startActivity(browserIntent);
                }
            });
            builderSelectHelp.setPositiveButton("ОК", null);
            AlertDialog alert = builderSelectHelp.create();
            alert.show();*/
        }



        try {
            LoadDialogDonate();
        }catch (Exception e){

        }


    }


    public static void showCustomDialog(Context appThis, String text,String text_button) {
        final Dialog dialog = new Dialog(appThis);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); // before
        dialog.setContentView(R.layout.dialog_info);
        dialog.setCancelable(true);

        TextView contentTextInfo = dialog.findViewById(R.id.contentTextInfo);
        contentTextInfo.setText(text);
        AppCompatButton bt_close = dialog.findViewById(R.id.bt_close);
        bt_close.setText(text_button);

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;


        ((AppCompatButton) dialog.findViewById(R.id.bt_close)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
           //     SavePreferences("DDSSDFG",pass);
             //   SavePreferences("DDSSDFGS",passAdm);
                dialog.dismiss();
            }
        });
        dialog.show();
        dialog.getWindow().setAttributes(lp);
    }




    private static void SavePreferences(String key, String value) {
        SharedPreferences.Editor editor = sharedPassword.edit();
        editor.putString(key, value);
        // Сохраните изменения.
        editor.commit();
    }

    private void LoadPreferences() {
        if(sharedPassword.contains("DDSSDFG")) {
            PublicInfo.pp = sharedPassword.getString("DDSSDFG", "");
        }
        if(sharedPassword.contains("DDSSDFGS")) {
            PublicInfo.ppp = sharedPassword.getString("DDSSDFGS", "");
        }


    }

    private void dialogCreateShow(View v) {
        PublicInfo.checkStartClient = 1;
        Intent intent = new Intent(this, MainStart.class);
        startActivity(intent);
    }


    private void requestData() {
        //Everything below is part of the Android Asynchronous HTTP Client
        if(PublicInfo.urlMonitoring == null) {
            System.out.println("MIHAIL я ловлю краш");

            Intent intent = new Intent(MenuActivity.this, LoadingApp.class);
            startActivity(intent);
            return;
        }
        AsyncHttpClient client = new AsyncHttpClient(true, 80, 443);
        //  AsyncHttpClient client = new AsyncHttpClient();
        client.addHeader("Content-Type", "text/xml; charset=utf-8");
        client.get(PublicInfo.urlMonitoring, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers,
                                  JSONObject response) {
                System.out.println("Михаил response:"+response);
                GsonBuilder builder = new GsonBuilder();
                Gson gson = builder.create();
                ReadMonitoring info = gson.fromJson(response.toString(), ReadMonitoring.class);

                //Log.i("Вывод", "ДО nameUpdateClientFull: " + PublicInfo.nameUpdateClientFull + " checksumInstallFilesMail: " + PublicInfo.checksumInstallFilesMail);
                int ServerOne = info.server_one;
                int ServerTwo = info.server_two;

                PublicInfo.OnlineServerOne = ServerOne;
                PublicInfo.OnlineServerTwo = ServerTwo;

                textViewServerOneCount.setText(ServerOne+"/1000");
                progressBarServerOne.setProgress(ServerOne/10);

                textViewServerTwoCount.setText(ServerTwo+"/1000");
                progressBarServerTwo.setProgress(ServerTwo/10);

            }


            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable e, JSONObject response) {

            /*    Toast toast = Toast.makeText(getApplicationContext(),
                        "Ошибка с соединением сервера! Проверьте подключение или попробуйте позже.",
                        Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
                finish();*/
            }
        });
    }


    public static void SaveOptims(int storage, String dirgame) throws IOException {

        Wini inid = new Wini(new File(Environment.getExternalStorageDirectory() + "/FlinOnline/files.ini"));
        inid.put("storage", "type", storage);
        inid.put("storage", "dirgame", dirgame);
        inid.store();

        PublicInfo.dirGameLocalType = storage;
        System.out.println("MIHAIL typeGameStorage save def");
    }

    public void OpenDialog() {
        if(mSettings.contains(APP_PREFERENCES_DIALOG)) {
            PublicInfo.checkStartClient = mSettings.getInt(APP_PREFERENCES_DIALOG, 0);
        }

    }

    public void SaveDialog(int checkbox)  {
        SharedPreferences.Editor editor = mSettings.edit();
        editor.putInt(APP_PREFERENCES_DIALOG, checkbox);
        editor.apply();
    }


    static class DataObject {
        String imageURL;
        String imageURLLogo;
        int typeDonate;
        int text1;
        int text2;
        int text3;
        String addText;
        String adData;
    }

    public void LoadDialogDonate()  {
        String PREFS_NAME = "PrefsDonate";
        String LAST_OPEN_DATE = "lastOpenDate";
        // Получаем дату последнего открытия приложения из SharedPreferences
        SharedPreferences sharedPref = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        String lastOpenDateString = sharedPref.getString(LAST_OPEN_DATE, "");

        //Получаем текущую дату
        Date currentDate = Calendar.getInstance().getTime();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd", Locale.getDefault());
        String currentDateString = dateFormat.format(currentDate);
        //System.out.println("Mihail currentDateString: "+currentDateString);

        if(PublicInfo.checkPostDataDonate.length() < 3){
            return;
        }
        Gson gson = new Gson();
        DataObject data = gson.fromJson(PublicInfo.checkPostDataDonate, DataObject.class);

        // Если дата последнего открытия не совпадает с текущей датой, показываем диалог
        if (data.adData.equals(currentDateString)) {
            //System.out.println("Mihail Дата совпала : "+data.adData);
            if (!lastOpenDateString.equals(currentDateString)) {
                final Dialog dialog = new Dialog(this);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); // before
                dialog.setContentView(R.layout.dialog_achievement);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                dialog.setCancelable(true);

                LinearLayout linear = dialog.findViewById(R.id.dialog_linear1);
                LinearLayout linear2 = dialog.findViewById(R.id.dialog_linear2);
                LinearLayout linear3 = dialog.findViewById(R.id.dialog_linear3);
                TextView text1 = dialog.findViewById(R.id.dialog_text_1);
                TextView text2 = dialog.findViewById(R.id.dialog_text_2);
                TextView text3 = dialog.findViewById(R.id.dialog_text_3);
                TextView dialog_text_4 = dialog.findViewById(R.id.dialog_text_4);

                TextView textNumber1 = dialog.findViewById(R.id.dialog_text_number1);
                TextView textNumber2 = dialog.findViewById(R.id.dialog_text_number2);
                TextView textNumber3 = dialog.findViewById(R.id.dialog_text_number3);


                if (data.text1 != 0) {
                    linear.setVisibility(View.VISIBLE);
                    textNumber1.setText("x" + data.text1);
                }
                if (data.text2 != 0) {
                    linear.setVisibility(View.VISIBLE);
                    textNumber2.setText("x" + data.text2);
                }
                if (data.text3 != 0) {
                    linear.setVisibility(View.VISIBLE);
                    textNumber3.setText("x" + data.text3);
                }

                if (data.addText.length() > 3) {
                    dialog_text_4.setText("" + data.addText);
                } else dialog_text_4.setVisibility(View.GONE);

                ImageView imageLogo = dialog.findViewById(R.id.projectImageDonate);
                Glide.with(getApplication()).load(data.imageURL)
                        .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                        .into(imageLogo);

                ImageView dialogLogoCenter = dialog.findViewById(R.id.dialog_logo_center);
                Glide.with(getApplication()).load(data.imageURLLogo)
                        .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                        .into(dialogLogoCenter);


                dialog.findViewById(R.id.bt_action).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // Сохраняем текущую дату в SharedPreferences
                        SharedPreferences.Editor editor = sharedPref.edit();
                        editor.putString(LAST_OPEN_DATE, currentDateString);
                        editor.apply();
                        Intent intent = new Intent(MenuActivity.this, DonateActivity.class);
                        startActivity(intent);
                    }
                });
                dialog.findViewById(R.id.bt_action_close).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // Сохраняем текущую дату в SharedPreferences
                        SharedPreferences.Editor editor = sharedPref.edit();
                        editor.putString(LAST_OPEN_DATE, currentDateString);
                        editor.apply();
                        dialog.hide();
                    }
                });
                dialog.show();
            }
        }
    }
    public static boolean isOnline(Context context) {
        ConnectivityManager cm =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting()) {
            return true;
        }
        return false;
    }


}