package com.flin.online.function;

import android.os.Environment;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class GetFileChecksum {


    public static String getHash(String folder) {
        String actualMD5 = null;
        File file = new File(folder);
        try (FileInputStream fis = new FileInputStream(file);

             BufferedInputStream bis = new BufferedInputStream(fis);
             DigestInputStream dis = new DigestInputStream(bis, MessageDigest.getInstance("MD5"))) {

            byte[] buffer = new byte[8192]; // размер буфера 8 КБ
            int bytesRead;
            while ((bytesRead = dis.read(buffer)) != -1) {
                // обработка считанных байтов
            }

            byte[] digest = dis.getMessageDigest().digest();
            actualMD5 = bytesToHex(digest);
            System.out.println("Mihail Путь: "+file.getPath()+ " Хеш файла: " + actualMD5);

        } catch (IOException | NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return actualMD5;
    }

    public static boolean checkHash(String folder, String fileName, String expectedMD5) {

        File file = new File(Environment.getExternalStorageDirectory() + folder, fileName);
        try (FileInputStream fis = new FileInputStream(file);

             BufferedInputStream bis = new BufferedInputStream(fis);
             DigestInputStream dis = new DigestInputStream(bis, MessageDigest.getInstance("MD5"))) {

            byte[] buffer = new byte[8192]; // размер буфера 8 КБ
            int bytesRead;
            while ((bytesRead = dis.read(buffer)) != -1) {
                // обработка считанных байтов
            }

            byte[] digest = dis.getMessageDigest().digest();
            String actualMD5 = bytesToHex(digest);
            System.out.println("Mihail Хеш файла :" + actualMD5);

            if (expectedMD5.equals(actualMD5)) {
                System.out.println("Mihail Хеш файла совпадает с ожидаемым значением.");
                return true;
            } else {
                System.out.println("Mihail Хеш файла не совпадает с ожидаемым значением.");
                return false;
            }
        } catch (IOException | NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return false;
    }
    public static String bytesToHex(byte[] bytes) {
        StringBuilder result = new StringBuilder();
        for (byte b : bytes) {
            result.append(String.format("%02x", b));
        }
        return result.toString();
    }

}
