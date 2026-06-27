package com.flin.online.gui;

import android.app.Activity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.flin.online.Preferences;
import com.flin.online.gui.util.Utils;
import com.flinc.core.R;
import com.nvidia.devtech.NvEventQueueActivity;

import java.util.Timer;
import java.util.TimerTask;

public class CustomScreen {
    public Activity activity;
    public Animation animation;
    public ConstraintLayout main_layout;
    public TextView status1;
    private Button repeat;

    public CustomScreen(Activity aactivity) {
        activity = aactivity;
        Animation animation = AnimationUtils.loadAnimation(aactivity, R.anim.click);

        main_layout = aactivity.findViewById(R.id.bloading_screen);
        main_layout.setVisibility(View.GONE);

        status1 = aactivity.findViewById(R.id.textView2);
        repeat = aactivity.findViewById(R.id.button);

        repeat.setText("Повторить");
        repeat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view.startAnimation(animation);
                startSR();
            }
        });
    }

    private void startSR()
    {
        Timer t = new Timer();
        t.schedule(new TimerTask(){
            @Override
            public void run() {
                   Connect();
             }
        }, 50L);
    }

    public void UpdateScreen(int status)
    {
     /**
	 CONNECTING, 0
	 CONNECTED, 1
	 SERVER_CLOSED_CONNECTION, 2
	 LOADED, 3
	 LOADING, 4
	 BANNED, 5
	 CONNECTION_LOST, 6
	 CONNECTION_ATTEMPT_FAILED, 7
     FULL_SERVER, 8
     INVALID_PASSWORD 9
     */

     if(status == 0) {
         status1.setText("Подключение к серверу...");
     } else if(status == 1) {
         status1.setText("Вход в игру..");
         HideLoading();
         ShowWelcome();
     } else if(status == 2) {
         status1.setText("Потеряно соединение с сервером...");
     } else if(status == 3) {
         try {
             Connect();
             //Toast.makeText(activity, "Client start connecting from servers.json", Toast.LENGTH_SHORT).show();
         } catch (Exception e) {
             e.printStackTrace();
         }
         status1.setText("Загрузка завершена...");
     } else if(status == 4) {
         ShowLoading();
         status1.setText("Загрузка игры...");
     } else if(status == 5) {
         status1.setText("Вы были заблокированы сервером");
         repeat.setVisibility(View.VISIBLE);
     } else if(status == 6) {
         status1.setText("Утеряно соединение с сервером");
     } else if(status == 7) {
         status1.setText("Попытка подключения неудачна");
     } else if(status == 8) {
         status1.setText("Сервер полон");
     } else if(status == 9) {
         status1.setText("Неверный пароль подключения к серверу");
         repeat.setVisibility(View.VISIBLE);
     } else HideLoading();
    }

    public void ShowLoading() {
        Utils.ShowLayout(main_layout, true);
    }

    public void HideLoading() {
        Utils.HideLayout(main_layout, true);
    }

    private void Connect(){
        repeat.setVisibility(View.GONE);
        NvEventQueueActivity.getInstance().setServer(Preferences.getW_IP(), Preferences.getW_Port());
    }

    private void ShowWelcome()
    {
        //NvEventQueueActivity.getInstance().showWelcome(true);
    }
}
