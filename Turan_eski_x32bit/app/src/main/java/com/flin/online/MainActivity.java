package com.flin.online;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import com.flinc.core.R;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);




        AlertDialog.Builder builderSelectHelp;
        builderSelectHelp = new AlertDialog.Builder(this);
        builderSelectHelp.setTitle("Старт!");
        builderSelectHelp.setMessage("Вы действительно хотите запустить игру?");
        builderSelectHelp.setPositiveButton("Да", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent=new Intent(MainActivity.this, com.flin.core.GTASA.class);
                intent.putExtras(getIntent());
                startActivity(intent);
                finish();
            }
        });
        ;
        builderSelectHelp.setNegativeButton("Нет", null);
        AlertDialog alert = builderSelectHelp.create();
        alert.show();


    }
}