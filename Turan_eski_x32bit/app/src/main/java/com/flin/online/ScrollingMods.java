package com.flin.online;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.flin.online.function.CheckLoadingFiles;
import com.flin.online.function.ViewPagerAdapter;
import com.flin.online.jsonenter.PublicInfo;
import com.flinc.core.R;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.ini4j.Wini;

import java.io.File;
import java.io.IOException;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

public class ScrollingMods extends AppCompatActivity {

    ViewPager mViewPager;

    int mod_install;

    // images array
    String[] images = {};
    // Creating Object of ViewPagerAdapter
    ViewPagerAdapter mViewPagerAdapter;

    @SuppressLint("RestrictedApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        setContentView(R.layout.activity_scrolling_mods);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        CollapsingToolbarLayout toolBarLayout = (CollapsingToolbarLayout) findViewById(R.id.toolbar_layout);

        Intent intent = getIntent();
        int mod_id = Integer.parseInt(intent.getStringExtra("mod_id"))-1;
        System.out.println("MIHAIL DIMKOV "+mod_id);
        String mod_name = intent.getStringExtra("mod_name");
        String mod_full_description = intent.getStringExtra("mod_full_description");
        mod_install = intent.getIntExtra("mod_install", 0);
        images = intent.getStringArrayExtra("mod_img");
        int mod_version = intent.getIntExtra("mod_version", 0);
        String mod_versionName = intent.getStringExtra("mod_versionName");
        String mod_versionUpdate = intent.getStringExtra("mod_versionUpdate"); //Текст с описанием обновления
        String mod_archive = intent.getStringExtra("mod_archive"); //Текст с описанием обновления
        int mod_archive_count =  intent.getIntExtra("mod_archive_count", 0);
        String mod_url = intent.getStringExtra("mod_url"); //Текст с описанием обновления



        toolBarLayout.setTitle(""+mod_name);

        TextView textViewModsFullDescription = (TextView) findViewById(R.id.textViewModsFullDescription);
        textViewModsFullDescription.setText(mod_full_description);

        // Initializing the ViewPager Object
        mViewPager = (ViewPager)findViewById(R.id.viewPagerMain);


        // Initializing the ViewPagerAdapter
        mViewPagerAdapter = new ViewPagerAdapter(ScrollingMods.this, images);

        // Adding the Adapter to the ViewPager
        mViewPager.setAdapter(mViewPagerAdapter);


        LinearLayout button_click_select_mods = (LinearLayout) findViewById(R.id.button_click_select_mods);
        button_click_select_mods.setClickable(true);
        button_click_select_mods.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast toast = Toast.makeText(getApplicationContext(),
                        "Вы успешно выбрали сборку",
                        Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();

                Wini ini = null;
                try {
                    ini = new Wini(new File(Environment.getExternalStorageDirectory() + "/Android/data/" + PublicInfo.checkReleasePackageName + "/files.ini"));
                    ini.put("mods", "mod_select", mod_id);
                    ini.put("mods", "mod_name", mod_name);
                    ini.put("mods", "mod_version", mod_version);
                    ini.store();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                Intent intent = new Intent(ScrollingMods.this, MenuActivity.class);
                startActivity(intent);
            }
        });


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                if(CheckLoadingFiles.getMods(mod_id)){
                    fab.setImageResource(R.drawable.baseline_delete_24);
                    mod_install = 0;
                }
                else {
                    fab.setImageResource(R.drawable.baseline_cloud_download_24);
                    mod_install = 1;
                    button_click_select_mods.setVisibility(View.INVISIBLE);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            fab.setImageResource(R.drawable.baseline_cloud_download_24);
        }
        //Проверка на стандартную сборку с GTA
        if(mod_id == 0) {
            fab.setVisibility(View.INVISIBLE);
            button_click_select_mods.setVisibility(View.VISIBLE);
        }
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               /*Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();*/
                if(mod_install == 1){
                    Intent intent = new Intent(ScrollingMods.this, ModsInstall.class);
                    intent.putExtra("mod_id", mod_id);
                    intent.putExtra("mod_name", mod_name);
                    intent.putExtra("mod_version", mod_version);
                    intent.putExtra("mod_versionName", mod_versionName);
                    intent.putExtra("mod_versionUpdate", mod_versionUpdate);
                    intent.putExtra("mod_archive", mod_archive);
                    intent.putExtra("mod_archive_count", mod_archive_count);
                    intent.putExtra("mod_url", mod_url);
                    startActivity(intent);
                }
                else {
                    AlertDialog.Builder builderUpdateClient;
                    builderUpdateClient = new AlertDialog.Builder(ScrollingMods.this);
                    builderUpdateClient.setTitle("Удаление сборки");
                    builderUpdateClient.setMessage("Вы действительно хотите удалить сборку?");
                    builderUpdateClient.setPositiveButton("Да", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });
                    ;
                    builderUpdateClient.setNegativeButton("Нет", null);
                    AlertDialog alert = builderUpdateClient.create();
                    alert.show();
                }

            }
        });


    }
}