package com.flin.online;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;


import cz.msebera.android.httpclient.entity.mime.HttpMultipartMode;
import cz.msebera.android.httpclient.entity.mime.MultipartEntityBuilder;
import cz.msebera.android.httpclient.entity.mime.content.FileBody;
import io.sentry.Attachment;
import io.sentry.Sentry;
import io.sentry.protocol.User;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okio.Buffer;
import okio.BufferedSink;
import okio.ForwardingSink;
import okio.Okio;
import okio.Sink;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.flin.online.jsonenter.PublicInfo;
import com.flinc.core.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;


import org.ini4j.Wini;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class ContactActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.l_activity_contact);

        TextView infoVersionApp = (TextView) findViewById(R.id.textViewVersion);
        infoVersionApp.setText("v"+ PublicInfo.VersionNameAppStatic+" (Build "+PublicInfo.VersionAppStatic+")");

        LinearLayout dialog_error_connect_btn_close = (LinearLayout) findViewById(R.id.dialog_error_connect_btn_close);
        dialog_error_connect_btn_close.setClickable(false);
        dialog_error_connect_btn_close.setVisibility(View.GONE);
        dialog_error_connect_btn_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    uploadFile("/FlinOnline/files/SAMP/samp_log.txt","samp_log", 1);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        LinearLayout dialog_send_logcat = (LinearLayout) findViewById(R.id.dialog_send_logcat);
        dialog_send_logcat.setClickable(false);
        dialog_send_logcat.setVisibility(View.GONE);
        dialog_send_logcat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    uploadFile("/FlinLog/logcat.txt","logcat", 2);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });


        LinearLayout dialog_send_crash = (LinearLayout) findViewById(R.id.dialog_send_crash);
        dialog_send_crash.setClickable(false);
        dialog_send_crash.setVisibility(View.GONE);
        dialog_send_crash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    uploadFile("/FlinOnline/files/SAMP/crash_log.log","crash_log", 1);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        ImageView imageViewIconTg = (ImageView) findViewById(R.id.imageViewIconTg);
        imageViewIconTg.setClickable(true);
        imageViewIconTg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ( !isOnline(ContactActivity.this) ){
                    Toast toast = Toast.makeText(getApplicationContext(),
                            "Вы не подключены к интернету!",
                            Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                }
                else {
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(PublicInfo.contactTelegram));
                    startActivity(browserIntent);
                }
            }
        });


        ImageView imageViewIconDiscord = (ImageView) findViewById(R.id.imageViewIconDiscord);
        imageViewIconDiscord.setClickable(true);
        imageViewIconDiscord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ( !isOnline(ContactActivity.this) ){
                    Toast toast = Toast.makeText(getApplicationContext(),
                            "Вы не подключены к интернету!",
                            Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                }
                else {
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(PublicInfo.contactDiscord));
                    startActivity(browserIntent);
                }
            }
        });

        ImageView imageViewIconForum = (ImageView) findViewById(R.id.imageViewIconForum);
        imageViewIconForum.setClickable(true);
        imageViewIconForum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ( !isOnline(ContactActivity.this) ){
                    Toast toast = Toast.makeText(getApplicationContext(),
                            "Вы не подключены к интернету!",
                            Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                }
                else {
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(PublicInfo.contactForum));
                    startActivity(browserIntent);
                }
            }
        });


        ImageView imageViewIconVK = (ImageView) findViewById(R.id.imageViewIconVK);
        imageViewIconVK.setClickable(true);
        imageViewIconVK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ( !isOnline(ContactActivity.this) ){
                    Toast toast = Toast.makeText(getApplicationContext(),
                            "Вы не подключены к интернету!",
                            Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                }
                else {
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(PublicInfo.contactVK));
                    startActivity(browserIntent);
                }
            }
        });

        ImageView imageViewIconSite = (ImageView) findViewById(R.id.imageViewIconSite);
        imageViewIconSite.setClickable(true);
        imageViewIconSite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ( !isOnline(ContactActivity.this) ){
                    Toast toast = Toast.makeText(getApplicationContext(),
                            "Вы не подключены к интернету!",
                            Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                }
                else {
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(PublicInfo.contactSite));
                    startActivity(browserIntent);
                }
            }
        });

        ImageView imageViewIconYt = (ImageView) findViewById(R.id.imageViewIconYt);
        imageViewIconYt.setClickable(true);
        imageViewIconYt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ( !isOnline(ContactActivity.this) ){
                    Toast toast = Toast.makeText(getApplicationContext(),
                            "Вы не подключены к интернету!",
                            Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                }
                else {
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(PublicInfo.contactYouTube));
                    startActivity(browserIntent);
                }
            }
        });



        //Меню
        //Нажатие меню настроек
        TextView imageDownLeftMenu = (TextView) findViewById(R.id.imageDownLeftMenu);
        imageDownLeftMenu.setClickable(true);
        imageDownLeftMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //ОБНОВИТЬКОД
                Intent intent = new Intent(ContactActivity.this, SetingsActivity.class);
                startActivity(intent);
            }
        });

        //Нажатие меню старта игры
        FloatingActionButton imageDownCenterMenu = (FloatingActionButton) findViewById(R.id.imageDownCenterMenu);
        imageDownCenterMenu.setClickable(true);
        imageDownCenterMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ContactActivity.this, MenuActivity.class);
                startActivity(intent);
            }

        });

        //Нажатие меню Доната
        TextView imageDownRightMenu = (TextView) findViewById(R.id.imageDownRightMenu);
        imageDownRightMenu.setClickable(true);
        imageDownRightMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ContactActivity.this, DonateActivity.class);
                startActivity(intent);
            }
        });


    }

    final String UPLOAD_URL = "https://php-gm-api.gta-android.ru/android_logs/upload.php"; // URL-адрес вашего PHP-скрипта для загрузки файла
    final int PICK_TXT_REQUEST = 1;


    private ProgressDialog progressDialog;
    private void uploadFile(String path, String nameFile, int type) throws IOException {
        // Создаем OkHttpClient
        OkHttpClient client = new OkHttpClient.Builder()
                .addNetworkInterceptor(new Interceptor() {
                    @Override
                    public Response intercept(Chain chain) throws IOException {
                        Request originalRequest = chain.request();
                        Request.Builder requestBuilder = originalRequest.newBuilder();
                        Request progressRequest = requestBuilder
                                .method(originalRequest.method(), new ProgressRequestBody(originalRequest.body(), new ProgressRequestBody.UploadProgressListener() {
                                    @Override
                                    public void onProgressChanged(long bytesWritten, long contentLength) {
                                        // Вычисляем процент загрузки и обновляем ProgressDialog
                                        int progress = (int) (bytesWritten * 100 / contentLength);
                                        progressDialog.setProgress(progress);
                                    }
                                }))
                                .build();
                        return chain.proceed(progressRequest);
                    }
                })
                .build();


        Wini ini = new Wini(new File(Environment.getExternalStorageDirectory() + "/FlinOnline/files/SAMP/settings.ini"));
        String nickName = ini.get("client", "name");

        // Получаем файл для отправки
        File file = null;
        if(type == 1) {
            file = new File(Environment.getExternalStorageDirectory() + path);
        }
        else file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + path);

        long timestamp = file.lastModified();
        Date date = new Date(timestamp);
        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy_HH_mm");
        sdf.setTimeZone(TimeZone.getTimeZone("GMT+3"));
        String formattedDate = sdf.format(date);
        System.out.println("File creation date: " + formattedDate);


        // Создаем тело запроса с данными в multipart/form-data формате
        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("file", nameFile + "_" + nickName + "_" + formattedDate + ".txt",
                        RequestBody.create(MediaType.parse("text/plain"), file))
                .build();

        // Создаем запрос на сервер
        Request request = new Request.Builder()
                .url("https://php-gm-api.gta-android.ru/android_logs/upload.php")
                .post(requestBody)
                .build();

        // Отправляем запрос на сервер
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                progressDialog.dismiss();
                if (response.isSuccessful()) {
                    // Обработка успешного ответа от сервера
                    String responseBody = response.body().string();
                    System.out.println("Mihail isSuccessful Response " + responseBody);

                    // Вывод сообщения Toast в основном потоке приложения
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(ContactActivity.this, "Файл успешно загружен", Toast.LENGTH_LONG).show();
                        }
                    });
                } else {
                    // Обработка ошибки от сервера
                    String errorBody = response.body().string();

                    // Вывод сообщения Toast в основном потоке приложения
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(ContactActivity.this, "Произошла ошибка загрузки. Причина:"+response.code(), Toast.LENGTH_LONG).show();
                        }
                    });

                    System.out.println("Mihail Error Response " +  response.code()+ " " + errorBody);
                }
            }


            @Override
            public void onFailure(Call call, IOException e) {
                System.out.println("Mihail onFailure Response " + e);
                progressDialog.dismiss();
                // Обработка ошибки во время выполнения запроса
                e.printStackTrace();
            }


        });

        // Отображаем ProgressDialog
        progressDialog = new ProgressDialog(ContactActivity.this);
        progressDialog.setMessage("Uploading...");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressDialog.setCancelable(false);
        progressDialog.show();

    }


    private static class ProgressRequestBody extends RequestBody {
        private static final int DEFAULT_BUFFER_SIZE = 2048;

        private final RequestBody requestBody;
        private final UploadProgressListener progressListener;

        public ProgressRequestBody(RequestBody requestBody, UploadProgressListener progressListener) {
            this.requestBody = requestBody;
            this.progressListener = progressListener;
        }

        @Override
        public MediaType contentType() {
            return requestBody.contentType();
        }

        @Override
        public long contentLength() throws IOException {
            return requestBody.contentLength();
        }

        @Override
        public void writeTo(BufferedSink sink) throws IOException {
            BufferedSink progressSink = Okio.buffer(new ProgressSink(sink, progressListener, contentLength()));
            requestBody.writeTo(progressSink);
            progressSink.flush();
        }

        public interface UploadProgressListener {
            void onProgressChanged(long bytesWritten, long contentLength);
        }

        private static class ProgressSink extends ForwardingSink {
            private long bytesWritten = 0L;
            private final UploadProgressListener progressListener;
            private final long contentLength;

            public ProgressSink(Sink delegate, UploadProgressListener progressListener, long contentLength) {
                super(delegate);
                this.progressListener = progressListener;
                this.contentLength = contentLength;
            }

            @Override
            public void write(Buffer source, long byteCount) throws IOException {
                super.write(source, byteCount);
                bytesWritten += byteCount;
                progressListener.onProgressChanged(bytesWritten, contentLength);
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