package com.flin.online.function;

import android.os.AsyncTask;
import android.util.Log;

import java.io.IOException;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class SendPostRequest extends AsyncTask<String, Void, String> {

    private static final String TAG = "SendPostRequest";

    @Override
    protected String doInBackground(String... params) {
        OkHttpClient client = new OkHttpClient();
        String url = params[0];
        String param1 = params[1];
        String param2 = params[2];

        // Создание тела запроса с параметрами
        RequestBody formBody = new FormBody.Builder()
                .add("nick", param1)
                .add("text", param2)
                .build();

        // Создание POST-запроса
        Request request = new Request.Builder()
                .url(url)
                .post(formBody)
                .build();

        // Отправка запроса и получение ответа
        try {
            Response response = client.newCall(request).execute();
            if (response.isSuccessful()) {
                return response.body().string();
            } else {
                throw new IOException("Unexpected code " + response);
            }
        } catch (IOException e) {
            Log.e(TAG, "Error sending POST request: " + e.getMessage());
            return null;
        }
    }

    @Override
    protected void onPostExecute(String result) {
        // Обработка ответа
        if (result != null) {
            Log.d(TAG, "POST response: " + result);
        } else {
            Log.e(TAG, "POST request failed");
        }
    }
}
