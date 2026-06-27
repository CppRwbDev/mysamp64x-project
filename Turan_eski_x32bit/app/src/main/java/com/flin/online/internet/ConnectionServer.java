package com.flin.online.internet;

import android.os.Build;


import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;
/*
public class ConnectionServer {

    private static HttpsURLConnection httpURLConnection = null;
    private static BufferedReader reader = null;
    private static String resultJson = "";

    public static String getJSON(String textUrl){
        try{


            URL url = new URL(textUrl);

            httpURLConnection = (HttpsURLConnection) url.openConnection();
            int sdk = android.os.Build.VERSION.SDK_INT;
            if (sdk < Build.VERSION_CODES.LOLLIPOP) {
                if (url.toString().startsWith("https")) {
                    try {
                        TLSSocketFactory sc = new TLSSocketFactory();
                        httpURLConnection.setSSLSocketFactory(sc);
                    } catch (Exception e) {
                        String sss = e.toString();
                    }
                }
            }
       //     httpURLConnection = (HttpsURLConnection) url.openConnection();
      //      httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setDoOutput(false);
            httpURLConnection.setRequestMethod("GET");
            httpURLConnection.connect();

            InputStream inputStream = httpURLConnection.getInputStream();
            StringBuffer buffer = new StringBuffer();
            reader = new BufferedReader(new InputStreamReader(inputStream));

            String line;
            while ((line = reader.readLine())!=null){
                buffer.append(line);
            }
            PublicInfo.status = 1;
            resultJson = buffer.toString();
        }catch (Exception e){
            PublicInfo.status = 2;
            System.out.println("МИХАИЛ ЛОГ:" +e);
            e.printStackTrace();
        }
        finally {
            httpURLConnection.disconnect();
        }
        return resultJson;
    }

}
*/



public class ConnectionServer {

    private static HttpURLConnection httpURLConnection = null;
    private static BufferedReader reader = null;
    private static String resultJson = "";

    public static String getJSON(String textUrl){
        try{
            URL url = new URL(textUrl);
            httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestProperty("User-Agent", "FlinLauncher-GP/v4.0");
            httpURLConnection.setRequestMethod("GET");
            httpURLConnection.connect();

            InputStream inputStream = httpURLConnection.getInputStream();
            StringBuffer buffer = new StringBuffer();
            reader = new BufferedReader(new InputStreamReader(inputStream));

            String line;
            while ((line = reader.readLine())!=null){
                buffer.append(line);
            }
            resultJson = buffer.toString();
        }catch (Exception e){
        }
        finally {
            httpURLConnection.disconnect();
        }
        return resultJson;
    }

}

