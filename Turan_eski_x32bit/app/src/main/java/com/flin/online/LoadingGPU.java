package com.flin.online;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ConfigurationInfo;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.flin.online.jsonenter.PublicInfo;
import com.flinc.core.R;

import java.util.Random;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public class LoadingGPU extends AppCompatActivity  implements GLSurfaceView.Renderer{
    private TextView textView;
    private GLSurfaceView glSurfaceView;
    Intent intent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
      //  setContentView(R.layout.activity_new_install_client);
        setContentView(R.layout.activity_loading_app);
        textView = (TextView) findViewById(R.id.textLoagingApp);
        textView.setText("Определяем ваш графический процессор...");
        final ActivityManager activityManager =  (ActivityManager)getSystemService(Context.ACTIVITY_SERVICE);
        final ConfigurationInfo configurationInfo = activityManager
                .getDeviceConfigurationInfo();
        this.glSurfaceView = new GLSurfaceView(this);
        this.glSurfaceView.setRenderer(this);
        ((ViewGroup)textView.getParent()).addView(this.glSurfaceView);
        intent = null;
        int data = 0;
        if (this.getIntent().getExtras() != null && this.getIntent().getExtras().containsKey("start_update")) {
            data = getIntent().getExtras().getInt("start_update");
            if(data == 1) intent = new Intent(this, ReUpdateClient.class);
            else intent = new Intent(this, SelectInstallClient.class);

        }
        else {
            intent = new Intent(this, SelectInstallClient.class);
        }


        new CountDownTimer(3000, 9999) {
            public void onTick(long millisUntilFinished) {
            }

            public void onFinish() {
                startActivity(intent);
                //Подпись GPU / Test
               /* Toast toast = Toast.makeText(getApplicationContext(),
                        "GPU:" +PublicInfo.gpuID,
                        Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();*/
                finish();
            }
        }.start();

    }


    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        //GL_VENDOR GL_VERSION GL_EXTENSIONS
        String getGPUinfo = gl.glGetString(GL10.GL_RENDERER).toLowerCase();
        PublicInfo.gpu = getGPUinfo;
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                //textView.setText("Обновился"+sb.toString()+"\n\nСкинь скрин в конфу тестеров!!!");

                if (getGPUinfo.contains("adreno")) {
                    PublicInfo.gpuID = 1;
                }
                if (getGPUinfo.contains("mali")) {
                    PublicInfo.gpuID = 2;
                }
                if (getGPUinfo.contains("power")) {
                    PublicInfo.gpuID = 3;
                }
                glSurfaceView.setVisibility(View.GONE);
            }
        });
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
    }

    @Override
    public void onDrawFrame(GL10 gl) {
    }
}