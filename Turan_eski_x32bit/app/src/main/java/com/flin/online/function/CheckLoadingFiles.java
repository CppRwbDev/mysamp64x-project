package com.flin.online.function;


import android.os.Build;
import android.os.Environment;

import com.flin.online.LoadingApp;
import com.flin.online.MenuActivity;
import com.flin.online.jsonenter.PublicInfo;

import org.ini4j.Wini;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import androidx.annotation.RequiresApi;

public class CheckLoadingFiles {


    public static boolean getMods(int mod_id) throws IOException {
        // check if exist
        System.out.println("MIHAIL папка #"+mod_id);
        String filePacth = Environment.getExternalStorageDirectory() + "/Android/data/" + PublicInfo.checkReleasePackageName + "/mods/"+"/"+mod_id+"/";
        File dir = new File(filePacth);
        Path path = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            path = Paths.get(filePacth);
            if (Files.exists(path)) {
                System.out.println("MIHAIL папка найдена!!!");
            }
        }
        else return false;

        File file = new File( Environment.getExternalStorageDirectory() + "/Android/data/"+PublicInfo.checkReleasePackageName+"/mods/"+mod_id+"/files/"+"/texdb/gta3.img");
        if(file.exists()){
            //  PublicInfo.GTAmodsDir =
            System.out.println("MIHAIL getMods файлов найдены ");
            return true;
        }else {
            System.out.println("MIHAIL getMods файлов нету");
            return false;
        }
    }

    public static int getModsID() throws IOException {
        File fileIni = new File(Environment.getExternalStorageDirectory() + "/Android/data/"+PublicInfo.checkReleasePackageName+"/files.ini");
        if(fileIni.exists()){
            Wini inid = new Wini(fileIni);
            int mod_id;
            if(inid.get("mods", "mod_select", Integer.class) != null){
                mod_id = inid.get("mods", "mod_select", Integer.class);
            } else mod_id = 0;
            return mod_id;
        }else {
            System.out.println("Файла нет!");
            return -1;
        }
    }

    public static String getModsName() throws IOException {
        int mod_id = getModsID();
        String mod_name;
        if(mod_id == 0){
            mod_name = "Стандартная";
            return mod_name;
        }
        File fileIni = new File(Environment.getExternalStorageDirectory() + "/Android/data/"+PublicInfo.checkReleasePackageName+"/mods/"+mod_id+"/mod.ini");
        if(fileIni.exists()){
            Wini inid = new Wini(fileIni);
            if(inid.get("mods", "mod_name") != null){
               mod_name = inid.get("mods", "mod_name");
            }
            else mod_name = "Без имени[]";
        }else {
            System.out.println("Файла нет!");
            mod_name = "Без имени";
        }
        return mod_name;
    }

    public static boolean game() throws IOException {
        //  File file = new File("/storage/extSdCard/FlinOnline/files/texdb/gta3.img");

        File fileIni = new File(Environment.getExternalStorageDirectory() + "/FlinOnline/files.ini");
        if(fileIni.exists()){
            Wini inid = new Wini(fileIni);

            PublicInfo.dirGameLocalType = 0;
            try {
                PublicInfo.dirGameLocalType = inid.get("storage", "type", Integer.class);
                System.out.println("MIHAIL typeGameStorage ini "+  PublicInfo.dirGameLocalType);
            } catch (NullPointerException e) {
                e.printStackTrace();
                System.out.println("MIHAIL typeGameStorage error");
            }
            if(PublicInfo.dirGameLocalType == 0){
                MenuActivity.SaveOptims(1, "0");
            }
            if(PublicInfo.dirGameLocalType == 1) {
                PublicInfo.dirGameLocal = Environment.getExternalStorageDirectory() + "/FlinOnline/files/";
            }
            if(PublicInfo.dirGameLocalType == 2){
                PublicInfo.dirGameLocal = "0";
                try {
                    PublicInfo.dirGameLocal = inid.get("storage", "dirgame", String.class);
                    System.out.println("MIHAIL dirGameStorage ini"+  PublicInfo.dirGameLocalType);
                    System.out.println("MIHAIL dirGameLocal ini"+  PublicInfo.dirGameLocal);
                } catch (NullPointerException e) {
                    e.printStackTrace();
                    System.out.println("MIHAIL dirGameStorage error");
                }
            }
        }else {
            System.out.println("Файла нет!");
            return false;
        }



        File file = new File(PublicInfo.dirGameLocal+"/texdb/gta3.img");
        if(file.exists()){
            System.out.println("Михаил размер:"+getFileSizeMegaBytes(file));
        }else {
            System.out.println("Файла нет!");
            return false;
        }
        System.out.println("MIHAIL dirGameLocalType start"+PublicInfo.dirGameLocalType );
        System.out.println("MIHAIL dirGameLocal start"+PublicInfo.dirGameLocal );
        return true;
    }

    // длина файла в мегабайтах
    private static int getFileSizeMegaBytes(File file) {
        return (int) file.length()/(1024*1024);
    }


}
