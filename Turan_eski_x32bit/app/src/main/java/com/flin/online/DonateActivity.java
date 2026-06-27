package com.flin.online;

import android.app.AlertDialog;
import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.flin.online.internet.ConnectionServer;
import  com.flin.online.jsonenter.PublicInfo;
import com.flinc.core.BuildConfig;
import com.flinc.core.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.ini4j.Wini;

import java.io.File;
import java.io.IOException;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import static com.flin.online.jsonenter.PublicInfo.donateUrl;

public class DonateActivity extends AppCompatActivity {

    String nickName;
    String enterSum;
    final int[] select = new int[1];
    final int[] selectPayment = new int[1];
    TextView textviewSelectPayment;
    boolean ButtonUse = false;


    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_donate);
        try {
            OpenNick();
        } catch (IOException e) {
            e.printStackTrace();
        }

        TextView infoVersionApp = (TextView) findViewById(R.id.textViewVersion);
        infoVersionApp.setText("v"+ PublicInfo.VersionNameAppStatic+" (Build "+PublicInfo.VersionAppStatic+")");

       /* TextView infoVersionApp = (TextView) findViewById(R.id.textVersion);
        infoVersionApp.setText("v"+ PublicInfo.VersionNameAppStatic);*/

       /* //Установка даты
        GregorianCalendar gcalendar = new GregorianCalendar();
        TextView textViewNameProjectFull = (TextView) findViewById(R.id.textViewNameProjectFull);
        textViewNameProjectFull.setText("FLIN RP "+ gcalendar.get(Calendar.YEAR));*/


      /*  textviewSelectPayment = (TextView) findViewById(R.id.textviewSelectPayment);
        if(PublicInfo.donatetype == 1) {
            textviewSelectPayment.setText("QIWI");
            selectPayment[0] = 1;
        }
        else {
            textviewSelectPayment.setText("Unitpay");
            selectPayment[0] = 2;
        }*/



       /* //Нажатие меню настроек
        ImageView imageDownLeftMenu = (ImageView) findViewById(R.id.imageDownLeftMenu);
        imageDownLeftMenu.setClickable(true);
        imageDownLeftMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DonateActivity.this, SetingsActivity.class);
                startActivity(intent);

            }
        });

        //Нажатие меню старта игры
        ImageView imageDownCenterMenu = (ImageView) findViewById(R.id.imageDownCenterMenu);
        imageDownCenterMenu.setClickable(true);
        imageDownCenterMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DonateActivity.this, MenuActivity.class);
                startActivity(intent);
            }
        });

        //Нажатие меню Доната
        ImageView imageDownRightMenu = (ImageView) findViewById(R.id.imageDownRightMenu);
        imageDownRightMenu.setClickable(true);
        imageDownRightMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DonateActivity.this, DonateActivity.class);
                startActivity(intent);
            }
        });*/

        //Нажатие меню настроек
        TextView imageDownLeftMenu = (TextView) findViewById(R.id.imageDownLeftMenu);
        imageDownLeftMenu.setClickable(true);
        imageDownLeftMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //ОБНОВИТЬКОД
                // Intent intent = new Intent(MenuActivity.this, test.class);
                Intent intent = new Intent(DonateActivity.this, SetingsActivity.class);
                startActivity(intent);
            }
        });

        //Нажатие меню старта игры
        FloatingActionButton imageDownCenterMenu = (FloatingActionButton) findViewById(R.id.imageDownCenterMenu);
        imageDownCenterMenu.setClickable(true);
        imageDownCenterMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DonateActivity.this, MenuActivity.class);
                startActivity(intent);
            }

        });

        //Нажатие меню Доната
        TextView imageDownRightMenu = (TextView) findViewById(R.id.imageDownRightMenu);
        imageDownRightMenu.setClickable(true);
        imageDownRightMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DonateActivity.this, DonateActivity.class);
                startActivity(intent);
            }
        });


        select[0] = 1;
        selectPayment[0] = 2;

        final EditText editTextName = (EditText) findViewById(R.id.editTextChangeName);
        final EditText editTextSum = (EditText) findViewById(R.id.editTextSum);
        if(nickName != null || nickName != "YourNickName") editTextName.setText(nickName);


   /*     Button buttonPayProblemPayment = (Button) findViewById(R.id.buttonPayProblemPayment);
        buttonPayProblemPayment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DonateActivity.this, DonateProblem.class);
                startActivity(intent);
            }
        });;*/


        ImageView buttonLoadingSite = (ImageView) findViewById(R.id.buttonLoadingSite);
        buttonLoadingSite.setClickable(true);
        buttonLoadingSite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String EMAIL_PATTERN =
                        "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@" +
                                "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

                nickName = editTextName.getText().toString().trim();
                enterSum = editTextSum.getText().toString().trim();

                if (nickName.length() == 0) {
                    Toast.makeText(getApplicationContext(), "Вы не ввели ник!", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (enterSum.length() == 0) {
                    Toast.makeText(getApplicationContext(), "Вы не ввели сумму!", Toast.LENGTH_SHORT).show();
                    return;
                }
              /*  Pattern patternNick = Pattern.compile ("[a-zA-Z_]");
                Matcher mNick = patternNick.matcher(nickName);
                if(mNick.matches() == false){
                    Toast.makeText(getApplicationContext(), "Введите ник в формате Nick_Name!", Toast.LENGTH_SHORT).show();
                    return;
                }*/
                Pattern patternSum = Pattern.compile ("[0-9]+");
                Matcher mSum = patternSum.matcher(enterSum);
                if(mSum.matches() == false){
                    Toast.makeText(getApplicationContext(), "Введите сумму только цифрами!", Toast.LENGTH_SHORT).show();
                    return;
                }

                //Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://unitpay.ru/pay/182051-6b933?sum="+enterSum+"&account="+nickName+"&desc=Пополнение%20счета%20"+nickName+"%20через%20Android&customerEmail="+enterEmail));
                // startActivity(browserIntent);



                new GetOnline().execute();
            }
        });;





        final TextView textviewDonateSelectServer = (TextView) findViewById(R.id.textviewDonateSelectServer);



        ImageView imageViewBtnDonateBack = (ImageView) findViewById(R.id.imageDonateBack);
        imageViewBtnDonateBack.setClickable(true);
        imageViewBtnDonateBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(select[0] == 1) select[0] = 2;
                else select[0] = 1;
                textviewDonateSelectServer.setText("Сервер №"+select[0]);
                //  Toast.makeText(HomeListActivity.this, "Михаил Выбраный сервер "+select[0], Toast.LENGTH_SHORT).show();

            }
        });


        ImageView imageViewBtnDonateNext = (ImageView) findViewById(R.id.imageDonateNext);
        imageViewBtnDonateNext.setClickable(true);
        imageViewBtnDonateNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(select[0] == 2) select[0] = 1;
                else select[0] = 2;
                textviewDonateSelectServer.setText("Сервер №"+select[0]);
                //  Toast.makeText(HomeListActivity.this, "Михаил Выбраный "+select[0], Toast.LENGTH_SHORT).show();
            }
        });


/*
        ImageView imagePaymentBack = (ImageView) findViewById(R.id.imagePaymentBack);
        imagePaymentBack.setClickable(true);
        imagePaymentBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(selectPayment[0] == 1) {
                    selectPayment[0] = 2;
                    textviewSelectPayment.setText("Unitpay");
                }
                else {
                    selectPayment[0] = 1;
                    textviewSelectPayment.setText("QIWI");
                }

                //  Toast.makeText(HomeListActivity.this, "Михаил Выбраный сервер "+select[0], Toast.LENGTH_SHORT).show();

            }
        });

*/
     /*   ImageView imagePaymentNext = (ImageView) findViewById(R.id.imagePaymentNext);
        imagePaymentNext.setClickable(true);
        imagePaymentNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(selectPayment[0] == 2) {
                    textviewSelectPayment.setText("QIWI");
                    selectPayment[0] = 1;
                }
                else {
                    selectPayment[0] = 2;
                    textviewSelectPayment.setText("Unitpay");
                }

                //  Toast.makeText(HomeListActivity.this, "Михаил Выбраный "+select[0], Toast.LENGTH_SHORT).show();
            }
        });*/



      /*  //Лого
        ImageView imgFavorite = (ImageView) findViewById(R.id.imageButtonLogoFull);
        imgFavorite.setClickable(true);
        imgFavorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if ( !isOnline(DonateActivity.this) ){
                    Toast toast = Toast.makeText(getApplicationContext(),
                            "Вы не подключены к интернету!",
                            Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                }
                else {

                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://flin-rp.com/"));
                    startActivity(browserIntent);
                }
            }
        });*/

    /*    //Помощь
        ImageView imgQuest = (ImageView) findViewById(R.id.imageSelectViewFull);
        imgQuest.setClickable(true);
        imgQuest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder builderSelectHelp;
                builderSelectHelp = new AlertDialog.Builder(DonateActivity.this, R.style.AlertDialogTheme);
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
        });

*/

    }
    public class GetOnline extends AsyncTask<String, Void,String> {

        @Override
        protected String doInBackground(String... strings) {
            System.out.println("МИХАИЛ"+"https://flin-rp.com/app/getonline.php?nick="+nickName+"&server="+select[0]);
            return ConnectionServer.getJSON("https://flin-rp.com/app/getonline.php?nick="+nickName+"&server="+select[0]);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            int status = Integer.parseInt(s);
            if(status == 0){
                if(selectPayment[0] == 2) {
                    if (select[0] == 1)
                        donateUrl = PublicInfo.PayMethodServer1 + enterSum + "&account=" + nickName + "&desc=Пополнение%20счета%20" + nickName + "%20через%20Android%20S1";
                    // if(select[0] == 1) donateUrl = "http://yandex.com/";
                    if (select[0] == 2)
                        donateUrl = PublicInfo.PayMethodServer2 + enterSum + "&account=" + nickName + "&desc=Пополнение%20счета%20" + nickName + "%20через%20Android%20S2";
                    //  donateUrl = "https://flin-rp.su/app/don.html";
                    Intent intent = new Intent(DonateActivity.this, WebDonateActivity.class);
                    startActivity(intent);
                    //System.out.println("donateUrl "+donateUrl);
                }
            }
            else if (status == 1){
                AlertDialog.Builder builder = new AlertDialog.Builder(DonateActivity.this);
                builder.setTitle("Вы в онлайне!")
                        .setMessage("Чтобы пополнить счет, выйдите с игры!" )
                      //  .setIcon(R.mipmap.ic_launcher)
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
            else if (status == 2){
                AlertDialog.Builder builder = new AlertDialog.Builder(DonateActivity.this);
                builder.setTitle("Не найден!")
                        .setMessage("Такого аккаунта не существует, измените ник!" )
            //            .setIcon(R.mipmap.ic_launcher)
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
            else {
                AlertDialog.Builder builderSelectHelp;
                builderSelectHelp = new AlertDialog.Builder(DonateActivity.this);
                builderSelectHelp.setTitle("Ошибка!");
              //  builderSelectHelp.setIcon(R.mipmap.ic_launcher);
                builderSelectHelp.setMessage("Произошла неизвестная ошибка, попробуйте позже или поспользуйтесь пополнением через сайт");
                builderSelectHelp.setNegativeButton("Перейти на сайт", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://flin-rp.com/donate"));
                        startActivity(browserIntent);
                    }
                });
                builderSelectHelp.setPositiveButton("ОК", null);
                AlertDialog alert = builderSelectHelp.create();
                alert.show();
            }
        }
    }

    public static boolean isOnline(Context context)
    {
        ConnectivityManager cm =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting())
        {
            return true;
        }
        return false;
    }

    public void OpenNick() throws IOException {

        String PackageName = "";
        PackageName = PublicInfo.checkReleasePackageName;

        Wini ini = new Wini(new File(Environment.getExternalStorageDirectory()+"/FlinOnline/files/SAMP/settings.ini"));
        nickName = ini.get("client", "name");
        //Log.d(TAG, "Ваш ник: "+nickName);
    }

/*
    public void UpdateDownLoadLauncher(){
        String destination = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + "/";
        String fileName = PublicInfo.qiwiUpdateNameAPK;

        destination += fileName;
        final Uri uri = Uri.parse("file://" + destination);
        File file = new File(destination);
        if (file.exists()) {
            file.delete();
        }
        String url = PublicInfo.qiwiUpdateUrl+PublicInfo.qiwiUpdateNameAPK;
        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));
        request.setDescription("Launcher v"+PublicInfo.qiwiUpdateVersion);
        request.setTitle("Скачивание обновления");
        request.setDestinationUri(uri);
        final DownloadManager manager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
        final long downloadId = manager.enqueue(request);
        final String finalFileName = fileName;
        BroadcastReceiver onComplete = new BroadcastReceiver() {
            public void onReceive(Context ctxt, Intent intent) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    Uri contentUri = (Uri) FileProvider.getUriForFile(getApplicationContext(), BuildConfig.APPLICATION_ID, new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + "/" + finalFileName));
                    Intent openFileIntent = new Intent(Intent.ACTION_VIEW);
                    openFileIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    openFileIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    openFileIntent.setData(contentUri);
                    startActivity(openFileIntent);
                } else {
                    Intent install = new Intent(Intent.ACTION_VIEW);
                    install.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    install.setDataAndType(uri,
                            "application/vnd.android.package-archive");
                    startActivity(install);
                }
                unregisterReceiver(this);
                finish();
                ButtonUse = false;
            }
        };
        registerReceiver(onComplete, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));
    }*/

}
