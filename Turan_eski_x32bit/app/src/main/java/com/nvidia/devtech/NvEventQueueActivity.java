//----------------------------------------------------------------------------------
// File:            libs\src\com\nvidia\devtech\NvEventQueueActivity.java
// Samples Version: Android NVIDIA samples 2 
// Email:           tegradev@nvidia.com
// Forum:           http://developer.nvidia.com/tegra/forums/tegra-forums/android-development
//
// Copyright 2009-2010 NVIDIA Corporation
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//   http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.
//
//----------------------------------------------------------------------------------
package com.nvidia.devtech;

import static com.flin.online.jsonenter.PublicInfo.successDonate;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.Display;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.flin.core.DialogClientSettings;
import com.flin.online.MenuActivity;
import com.flin.online.function.CheckLoadingFiles;
import com.flin.online.gui.CustomScreen;
import com.flin.online.gui.HudManager;
//import com.flin.online.gui.Speedometer;
//import com.flin.online.gui.Welcome;
import com.flin.online.gui.tab.Tab;
import com.flin.online.jsonenter.PublicInfo;
import com.flinc.core.DialogManager;
import com.flinc.core.GetAct;
import com.flinc.core.KeyboardStandardManager;
import com.flinc.core.NotificationDialogCrash;
import com.flinc.core.R;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;

import javax.microedition.khronos.egl.EGL10;
import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.egl.EGLContext;
import javax.microedition.khronos.egl.EGLDisplay;
import javax.microedition.khronos.egl.EGLSurface;
import javax.microedition.khronos.opengles.GL10;
import javax.microedition.khronos.opengles.GL11;

/**
 A base class used to provide a native-code event-loop interface to an
 application.  This class is designed to be subclassed by the application
 with very little need to extend the Java.  Paired with its native static-link
 library, libnv_event.a, this package makes it possible for native applciations
 to avoid any direct use of Java code.  In addition, input and other events are
 automatically queued and provided to the application in native code via a
 classic event queue-like API.  EGL functionality such as bind/unbind and swap
 are also made available to the native code for ease of application porting.
 Please see the external SDK documentation for an introduction to the use of
 this class and its paired native library.
 */
public abstract class NvEventQueueActivity
        extends AppCompatActivity
        implements SensorEventListener, InputManager.InputListener, View.OnTouchListener, HeightProvider.HeightListener {


    protected Handler handler = null;
    private static NvEventQueueActivity instance = null;

    private int SwapBufferSkip = 0;

    protected boolean paused = false;

    protected boolean wantsMultitouch = false;

    protected boolean supportPauseResume = true;
    protected boolean ResumeEventDone = false;

    //accelerometer related
    protected boolean wantsAccelerometer = false;
    protected SensorManager mSensorManager = null;
    protected ClipboardManager mClipboardManager = null;
    protected int mSensorDelay = SensorManager.SENSOR_DELAY_GAME; //other options: SensorManager.SENSOR_DELAY_FASTEST, SensorManager.SENSOR_DELAY_NORMAL and SensorManager.SENSOR_DELAY_UI
    protected Display display = null;

    FrameLayout mAndroidUI = null;

    private static final int EGL_RENDERABLE_TYPE = 0x3040;
    private static final int EGL_OPENGL_ES2_BIT = 0x0004;
    private static final int EGL_OPENGL_ES3_BIT = 64;
    private static final int EGL_CONTEXT_CLIENT_VERSION = 0x3098;
    EGL10 egl = null;
    GL11 gl = null;

    private boolean ranInit = false;
    protected EGLSurface eglSurface = null;
    protected EGLDisplay eglDisplay = null;
    protected EGLContext eglContext = null;
    protected EGLConfig eglConfig = null;

    protected SurfaceHolder cachedSurfaceHolder = null;
    private int surfaceWidth = 0;
    private int surfaceHeight = 0;

    private int fixedWidth = 0;
    private int fixedHeight = 0;
    private boolean HasGLExtensions = false;
    private String glVendor = null;
    private String glExtensions = null;
    private String glRenderer = null;
    private String glVersion = null;
    private boolean GameIsFocused = false;
    private boolean viewIsActive = false;

    private FrameLayout mRootFrame = null;
    private FrameLayout mRenderMoreViews = null;
    private SurfaceView mSurfaceView = null;

    public KeyboardStandardManager mKeyboardStandardManager;


    private InputManager mInputManager = null;
    private HeightProvider mHeightProvider = null;
    private DialogClientSettings mDialogClientSettings = null;
    private GetAct mGetAct = null;
    private HudManager mHudManager = null;
    //private Speedometer mSpeedometer = null;
    //private Welcome mWelcome = null;
    private CustomScreen mCustomScreen = null;
    private Tab mTab = null;

    public DialogManager mDialogManager;

    ConstraintLayout constraintLayout;
    ConstraintLayout button_G;
    ConstraintLayout button_ALT;
    ConstraintLayout button_BIND;
    ConstraintLayout button_Y;
    ConstraintLayout button_N;

    ConstraintLayout dialog_layout; //конструкция всего диалога

    TextView dialog_caption; //Текст заголовка диалога

    ConstraintLayout connect_view; //конструкция входа
    int iranConnect = 0;
    ImageView imageViewIranConnect;
    ImageView imageViewOneServer;
    ImageView imageViewTwoServer;
    TextView textViewConnectOnlineOne;
    TextView textViewConnectOnlineTwo;

    ConstraintLayout keyboardStandardView;
    boolean renderActiveMainRender = false;
    boolean activeDialog = false;
    TextView exampleDialogText;


    TextView textView31;

    ConstraintLayout constraintlayoutDonate;
    WebView webView;

    private int exitGame = 0;


    /* *
     * Helper function to select fixed window size.
     * */
    public void setFixedSize(int fw, int fh)
    {
        fixedWidth = fw;
        fixedHeight = fh;
    }

    public native void onEventBackPressed();

    public native void onSettingsWindowSave();
    public native void onSettingsWindowDefaults(int category);


    public native void setServer(String ip, int port);
    public native void sendALT();
    public native void sendCTRL();
    public native void sendY();
    public native void sendN();
    public native void sendH();
    public native void sendG();

    public native void setNativeCutoutSettings(boolean b);
    public native void setNativeKeyboardSettings(boolean b);
    public native void setNativeFpsCounterSettings(boolean b);
    public native void setNativeOutfitGunsSettings(boolean b);
    public native void setNativeHpArmourText(boolean b);
    public native void setNativeRadarrect(boolean b);
    public native void setNativePcMoney(boolean b);
    public native void setNativeNameTag(boolean b);
    public native void setNative3DText(boolean b);
    public native void setNativeVoice(boolean b);
    public native void setNativeSkyBox (boolean b);
    public native void setNativeDialogNew(boolean b);
    public native void setNativeCacheTextDraw(boolean b);
    public native boolean setNativeButtonAttackNew(boolean b);
   // public native boolean setNativeCrossHair(boolean b);
   public native void setUID(byte[] data);
   public native void setUIP(byte[] data);

    public native void responseDialog(int dialogid, int response, int listitem, byte[] inputtext);

    public native void setNativeSelectServer(int b);

    public native boolean getNativeCutoutSettings();
    public native boolean getNativeKeyboardSettings();
    public native boolean getNativeFpsCounterSettings();
    public native boolean getNativeOutfitGunsSettings();
    public native boolean getNativeHpArmourText();
    public native boolean getNativeRadarrect();
    public native boolean getNativePcMoney();
    public native boolean getNativeNameTag();
    public native boolean getNative3DText();
    public native boolean getNativeVoice();
    public native boolean getNativeSkyBox();
    public native boolean getNativeDialogNew();
    public native boolean getNativeCacheTextDraw();
   //public native boolean getNativeCrossHair();
   // public native boolean getNativeButtonAttackNew();

    public native void setNativeHudElementColor(int id, int a, int r, int g, int b);
    public native byte[] getNativeHudElementColor(int id);

    public native void setNativeHudElementPosition(int id, int x, int y);
    public native int[] getNativeHudElementPosition(int id);
    public native void setNativeHudElementScale(int id, int x, int y);
    public native int[] getNativeHudElementScale(int id);

    public native void setNativeWidgetPositionAndScale(int id, int x, int y, int scale);
    public native int[] getNativeWidgetPositionAndScale(int id);

    //public native int getNativeWidgetSensitivity(int id);
    //public native void setNativeWidgetSensitivity(int id, int value);

    public String getHudElementColor(int id)
    {
        byte[] color = getNativeHudElementColor(id);
        String str = null;
        try {
            str = new String(color, "windows-1251");
        }
        catch(UnsupportedEncodingException e)
        {

        }

        return str;

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        onEventBackPressed();
    }

    private int mUseFullscreen = 0;

    private void processCutout()
    {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P)
        {
            if(mUseFullscreen == 1)
            {
                getWindow().getAttributes().layoutInDisplayCutoutMode = WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES;
            }
        }
    }

    public void setUseFullscreen(int b)
    {
        mUseFullscreen = b;
    }

    public void ToastMakeText(byte[] bArr){

        final String text = new String(bArr, StandardCharsets.UTF_8);

        Toast toastMessage = Toast.makeText(getApplicationContext(), text, Toast.LENGTH_LONG);
        toastMessage.setGravity(Gravity.CENTER, 0, 0);
        toastMessage.show();
    }

    public void ToastMakeString(String text){
        Toast toastMessage = Toast.makeText(getApplicationContext(), text, Toast.LENGTH_LONG);
        toastMessage.setGravity(Gravity.CENTER, 0, 0);
        toastMessage.show();
    }

    public void BuildDialog(int dialogId, byte[] bArr, byte[] bArr2, byte[] bArr3, byte[] bArr4, int dialogTypeId) {
        final String caption = new String(bArr, StandardCharsets.UTF_8);
        final String content = new String(bArr2, StandardCharsets.UTF_8);
        final String leftBtnText = new String(bArr3, StandardCharsets.UTF_8);
        final String rightBtnText = new String(bArr4, StandardCharsets.UTF_8);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            runOnUiThread(() -> this.mDialogManager.buildDialog(dialogId, caption, content, leftBtnText, rightBtnText, dialogTypeId));
        }
    }
/*
    public void BuildDialog(int dialogId, String title, String content, String button1, String button2, int typeDialog){
        //System.out.println("called java buildddd dialog");

        runOnUiThread(new Runnable() {
                          @Override
                          public void run() {

                              textView31.setText("ИД Диалога:"+dialogId+"\ntitle:"+title);
                              mDialogManager.buildDialog(dialogId, title, content, button1, button2, typeDialog);
                              //System.out.println("MIHAIL buildDialog typeDialog: "+typeDialog);
                              //System.out.println("MIHAIL buildDialog dialogId: "+dialogId+" title: "+title+" content: "+content+" button1:"+button1+" button2:"+button2+" typeDialog:"+typeDialog);

                          }
        });
    }
*/

    public void showClientSettings()
    {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(mDialogClientSettings != null)
                {
                    mDialogClientSettings = null;
                }
                mDialogClientSettings = new DialogClientSettings();
                mDialogClientSettings.show(getSupportFragmentManager(), "test");
                /*if (constraintLayout.getVisibility() == View.VISIBLE) {
                    constraintLayout.setVisibility(View.INVISIBLE);
                } else {
                    constraintLayout.setVisibility(View.VISIBLE);
                }*/
            }
        });
    }

    public void hideSystemUI() {
        // Enables regular immersive mode.
        // For "lean back" mode, remove SYSTEM_UI_FLAG_IMMERSIVE.
        // Or for "sticky immersive," replace it with SYSTEM_UI_FLAG_IMMERSIVE_STICKY



        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_IMMERSIVE
                        // Set the content to appear under the system bars so that the
                        // content doesn't resize when the system bars hide and show.
                        | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        // Hide the nav bar and status bar
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
    }

    @Override
    public boolean onTouch(View view, MotionEvent event)
    {

            if (wantsMultitouch)
            {
                int x1 = 0, y1 = 0, x2 = 0, y2 = 0, x3 = 0, y3 = 0;
                // marshal up the data.
                int numEvents = event.getPointerCount();
                for (int i=0; i<numEvents; i++)
                {
                    // only use pointers 0 and 1, 2, 3
                    int pointerId = event.getPointerId(i);
                    if (pointerId == 0)
                    {
                        x1 = (int)event.getX(i);
                        y1 = (int)event.getY(i);
                    }
                    else if (pointerId == 1)
                    {
                        x2 = (int)event.getX(i);
                        y2 = (int)event.getY(i);
                    }
                    else if (pointerId == 2)
                    {
                        x3 = (int)event.getX(i);
                        y3 = (int)event.getY(i);
                    }
                }

                int pointerId = event.getPointerId(event.getActionIndex());
                int action = event.getActionMasked();
                customMultiTouchEvent(action, pointerId, x1, y1, x2, y2,
                        x3, y3);
            }
            else // old style input.*/
            {
                touchEvent(event.getAction(), (int)event.getX(), (int)event.getY(), event);
            }

        return true;
    }

   private native void onNativeHeightChanged(int orientation, int height);

    @Override
    public void onHeightChanged(int orientation, int height)
    {
        if(mInputManager != null)
        {
            mInputManager.onHeightChanged(height);
        }
        if(orientation == Configuration.ORIENTATION_LANDSCAPE)
        {
           onNativeHeightChanged(orientation, height + findViewById(R.id.main_input).getHeight());
        }
    }

    public native void onWeaponChanged();

    public native void showTab();
    public native void onTabClose();


    /**
     * Helper class used to pass raw data around.  
     */
    public class RawData
    {
        /** The actual data bytes. */
        public byte[] data;
        /** The length of the data. */
        public int length;
    }
    /**
     * Helper class used to pass a raw texture around. 
     */
    public class RawTexture extends RawData
    {
        /** The width of the texture. */
        public int width;
        /** The height of the texture. */
        public int height;
    }

    /**
     * Helper function to load a file into a {@link RawData} object.
     * It'll first try loading the file from "/data/" and if the file doesn't
     * exist there, it'll try loading it from the assets directory inside the
     * .APK file. This is to allow the files inside the apk to be overridden
     * or not be part of the .APK at all during the development phase of the
     * application, decreasing the size needed to be transmitted to the device
     * between changes to the code.
     *
     * @param filename The file to load.
     * @return The RawData object representing the file's fully loaded data,
     * or null if loading failed. 
     */
    public RawData loadFile(String filename)
    {
        InputStream is = null;
        RawData ret = new RawData();
        try {
            try
            {
                is = new FileInputStream("/data/" + filename);
            }
            catch (Exception e)
            {
                try
                {
                    is = getAssets().open(filename);
                }
                catch (Exception e2)
                {
                }
            }
            int size = is.available();
            ret.length = size;
            ret.data = new byte[size];
            is.read(ret.data);
        }
        catch (IOException ioe)
        {
        }
        finally
        {
            if (is != null)
            {
                try { is.close(); } catch (Exception e) {}
            }
        }
        return ret;
    }

    /**
     * Helper function to load a texture file into a {@link RawTexture} object.
     * It'll first try loading the texture from "/data/" and if the file doesn't
     * exist there, it'll try loading it from the assets directory inside the
     * .APK file. This is to allow the files inside the apk to be overridden
     * or not be part of the .APK at all during the development phase of the
     * application, decreasing the size needed to be transmitted to the device
     * between changes to the code.
     *
     * The texture data will be flipped and bit-twiddled to fit being loaded directly
     * into OpenGL ES via the glTexImage2D call.
     *
     * @param filename The file to load.
     * @return The RawTexture object representing the texture's fully loaded data,
     * or null if loading failed. 
     */
    public RawTexture loadTexture(String filename)
    {
        RawTexture ret = new RawTexture();
        try {
            InputStream is = null;
            try
            {
                is = new FileInputStream("/data/" + filename);
            }
            catch (Exception e)
            {
                try
                {
                    is = getAssets().open(filename);
                }
                catch (Exception e2)
                {
                }
            }

            Bitmap bmp = BitmapFactory.decodeStream(is);
            ret.width = bmp.getWidth();
            ret.height = bmp.getHeight();
            int[] pixels = new int[bmp.getWidth() * bmp.getHeight()];
            bmp.getPixels(pixels, 0, bmp.getWidth(), 0, 0, bmp.getWidth(), bmp.getHeight());

            // Flip texture
            int[] tmp = new int[bmp.getWidth()];
            final int w = bmp.getWidth();
            final int h = bmp.getHeight();
            for (int i = 0; i < h>>1; i++)
            {
                System.arraycopy(pixels, i*w, tmp, 0, w);
                System.arraycopy(pixels, (h-1-i)*w, pixels, i*w, w);
                System.arraycopy(tmp, 0, pixels, (h-1-i)*w, w);
            }

            // Convert from ARGB -> RGBA and put into the byte array
            ret.length = pixels.length * 4;
            ret.data = new byte[ret.length];
            int pos = 0;
            int bpos = 0;
            for (int y = 0; y < h; y++)
            {
                for (int x = 0; x < w; x++, pos++)
                {
                    int p = pixels[pos];
                    ret.data[bpos++] = (byte) ((p>>16)&0xff);
                    ret.data[bpos++] = (byte) ((p>> 8)&0xff);
                    ret.data[bpos++] = (byte) ((p>> 0)&0xff);
                    ret.data[bpos++] = (byte) ((p>>24)&0xff);
                }
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return ret;
    }

    /**
     * Function called when app requests accelerometer events.
     * Applications need/should NOT overide this function - it will provide
     * accelerometer events into the event queue that is accessible
     * via the calls in nv_event.h
     *
     * @param values0: values[0] passed to onSensorChanged(). For accelerometer: Acceleration minus Gx on the x-axis.
     * @param values1: values[1] passed to onSensorChanged(). For accelerometer: Acceleration minus Gy on the y-axis.
     * @param values2: values[2] passed to onSensorChanged(). For accelerometer: Acceleration minus Gz on the z-axis.
     * @return True if the event was handled.
     */
    public native boolean accelerometerEvent(float values0, float values1, float values2);

    /**
     * The following indented function implementations are defined in libnvevent.a
     * The application does not and should not overide this; nv_event handles this internally
     * And remaps as needed into the native calls exposed by nv_event.h
     */
    public native void cleanup();
    public native boolean init(boolean z);
    public native void initSAMP();
    public native void setWindowSize(int w, int h);
    public native void quitAndWait();
    public native void postCleanup();

    public native void imeClosed();

    public native void lowMemoryEvent(); // TODO: implement this
    public native boolean processTouchpadAsPointer(ViewParent viewParent, boolean z);
    public native void notifyChange(String str, int i);
    public native void changeConnection(boolean z);

    public native void pauseEvent();
    public native void resumeEvent();
    public native boolean touchEvent(int action, int x, int y, MotionEvent event);
    public native boolean multiTouchEvent(int action, int count,
                                          int x0, int y0, int x1, int y1, MotionEvent event);
    public native boolean keyEvent(int action, int keycode, int unicodeChar, int metaState, KeyEvent event);

    public native boolean customMultiTouchEvent(int action, int count, int x1, int y1, int x2, int y2,
                                                int x3, int y3);
    /**
     * END indented block, see in comment at top of block
     */

    /**
     * Declaration for function defined in nv_time/nv_time.cpp
     * It initializes and returns time through Nvidia's egl extension for time.
     * It is useful while debugging the demo using PerfHUD.
     *
     * @see: nv_time/nv_time.cpp for implementation details.
     */
    public native void nvAcquireTimeExtension();
    public native long nvGetSystemTime();




    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        System.out.println("**** onCreate");
        super.onCreate(savedInstanceState);
        instance = this;
        if(supportPauseResume)
        {
            System.out.println("Calling init(false)");
            init(false);
            System.out.println("Calling initSAMP");
            initSAMP();
            System.out.println("Called");
        }
        handler = new Handler();
        if(wantsAccelerometer && (mSensorManager == null)) {
            mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        }

        mClipboardManager = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);

        NvUtil.getInstance().setActivity(this);
        NvAPKFileHelper.getInstance().setContext(this);

        display = ((WindowManager)this.getSystemService(WINDOW_SERVICE)).getDefaultDisplay();

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P)
        {
            // TODO: cutout
            //getWindow().getAttributes().layoutInDisplayCutoutMode = WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES;
        }

        systemInit();

        hideSystemUI();

        //((TextView)findViewById(R.id.main_version_text)).setText(BuildConfig.VERSION_NAME);

        getWindow().getDecorView().setOnSystemUiVisibilityChangeListener(new View.OnSystemUiVisibilityChangeListener() {
            @Override
            public void onSystemUiVisibilityChange(int i) {
                if ((i & View.SYSTEM_UI_FLAG_FULLSCREEN) == 0) {
                    hideSystemUI();
                } else {
                    // TODO: The system bars are NOT visible. Make any desired
                    // adjustments to your UI, such as hiding the action bar or
                    // other navigational controls.
                }

            }
        });

        try {
            int mod_id = CheckLoadingFiles.getModsID();
            if(mod_id > 0) Toast.makeText(this, "Запускается сборка №"+mod_id, Toast.LENGTH_LONG).show();
        } catch (IOException e) {
            e.printStackTrace();
        }

        processCutout();
    }



    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    public void onWindowFocusChanged(boolean hasFocus)
    {
        if(mDialogClientSettings != null)
        {
            if(mDialogClientSettings.getDialog() != null) {
                if(mDialogClientSettings.getDialog().isShowing())
                {
                    hideSystemUI();
                    super.onWindowFocusChanged(hasFocus);
                    return;
                }
            }
        }
        if (ResumeEventDone && viewIsActive && !paused)
        {
            if (GameIsFocused && !hasFocus)
            {
                if(mInputManager != null)
                {
                    if(!mInputManager.IsShowing())
                    {
                        pauseEvent();
                    }
                }
                else
                {
                    pauseEvent();
                }
            }
            else if (!GameIsFocused && hasFocus)
            {
                resumeEvent();
            }
            GameIsFocused = hasFocus;
        }
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus)
        {
            hideSystemUI();
        }
    }

    /**
     * Implementation function: defined in libnvevent.a
     * The application does not and should not overide this; nv_event handles this internally
     * And remaps as needed into the native calls exposed by nv_event.h
     */
    @Override
    protected void onResume()
    {
        System.out.println("**** onResume");
        super.onResume();
        if(mSensorManager != null)
            mSensorManager.registerListener(
                    this,
                    mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                    mSensorDelay);
        paused = false;

        if(mHeightProvider != null)
        {
            mHeightProvider.init(mRootFrame);
        }

        if (viewIsActive && ResumeEventDone)
        {
            resumeEvent();
            if (cachedSurfaceHolder != null)
            {
                cachedSurfaceHolder.setKeepScreenOn(true);
            }
        }
    }

    /**
     * Implementation function: defined in libnvevent.a
     * The application does not and should not overide this; nv_event handles this internally
     * And remaps as needed into the native calls exposed by nv_event.h
     */
    @Override
    protected void onRestart()
    {
        System.out.println("**** onRestart");
        super.onRestart();
    }

    /**
     * Implementation function: defined in libnvevent.a
     * The application does not and should not overide this; nv_event handles this internally
     * And remaps as needed into the native calls exposed by nv_event.h
     */
    @Override
    protected void onPause()
    {
        System.out.println("**** onPause");
        super.onPause();
        paused = true;

        if (ResumeEventDone)
        {
            System.out.println("java is invoking pauseEvent(), this will block until\nthe client calls NVEventPauseProcessed");
            pauseEvent();
            System.out.println("pauseEvent() returned");
        }
    }

    /**
     * Implementation function: defined in libnvevent.a
     * The application does not and should not overide this; nv_event handles this internally
     * And remaps as needed into the native calls exposed by nv_event.h
     */
    @Override
    protected void onStop()
    {
        System.out.println("**** onStop");
        if(mSensorManager != null)
            mSensorManager.unregisterListener(this);
        super.onStop();
    }

    /**
     * Implementation function: defined in libnvevent.a
     * The application should *probably* not overide this; nv_event handles this internally
     * And remaps as needed into the native calls exposed by nv_event.h
     *
     * NOTE: An application may need to override this if the app has an
     *       in-process instance of the Service class and the native side wants to
     *       keep running. The app would want to execute the content of the
     *       if(supportPauseResume) clause when it is time to exit.
     */
    @Override
    public void onDestroy()
    {
        System.out.println("**** onDestroy");
        if(supportPauseResume)
        {
         //   quitAndWait();
            finish();
        }
        super.onDestroy();
        systemCleanup();
    }

    /**
     * Implementation function: defined in libnvevent.a
     * The application does not and should not overide this; nv_event handles this internally
     * And remaps as needed into the native calls exposed by nv_event.h
     */

    public void mSleep(long milis) {
        try {
            Thread.sleep(milis);
        } catch (InterruptedException e) {
        }
    }

    public void DoResumeEvent()
    {
        new Thread(new Runnable() {
            public void run() {
                while (NvEventQueueActivity.this.cachedSurfaceHolder == null)
                {
                    NvEventQueueActivity.this.mSleep(1000);
                }
                System.out.println("Call from DoResumeEvent");
                NvEventQueueActivity.this.resumeEvent();
                NvEventQueueActivity.this.ResumeEventDone = true;
            }
        }).start();
    }

    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // Auto-generated method stub
    }

    /**
     * Implementation function: defined in libnvevent.a
     * The application does not and should not overide this; nv_event handles this internally
     * And remaps as needed into the native calls exposed by nv_event.h
     */
    public void onSensorChanged(SensorEvent event) {
        // Auto-generated method stub
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER)
        {
            float roll = 0.0f;
            float pitch = 0.0f;
            switch (this.display.getRotation()) {
                case 0:
                    roll = -event.values[0];
                    pitch = event.values[1];
                    break;
                case 1:
                    roll = event.values[1];
                    pitch = event.values[0];
                    break;
                case 2:
                    roll = event.values[0];
                    pitch = event.values[1];
                    break;
                case 3:
                    roll = -event.values[1];
                    pitch = event.values[0];
                    break;
            }
            accelerometerEvent(roll, pitch, event.values[2]);
        }
    }

    /**
     * Implementation function: defined in libnvevent.a
     * The application does not and should not overide this; nv_event handles this internally
     * And remaps as needed into the native calls exposed by nv_event.h
     */
    @Override
    public boolean onTouchEvent(MotionEvent event)
    {
        return super.onTouchEvent(event);
    }


    /**
     * Implementation function: defined in libnvevent.a
     * The application does not and should not overide this; nv_event handles this internally
     * And remaps as needed into the native calls exposed by nv_event.h
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        boolean ret = false;

        if(keyCode == KeyEvent.KEYCODE_BACK)
        {
            onEventBackPressed();
        }

        if (keyCode == 24 || keyCode == 25) {
            return super.onKeyDown(keyCode, event);
        }
        if (keyCode == 89 || keyCode == 85 || keyCode == 90) {
            return false;
        }
        if (!(keyCode == 82 || keyCode == 4)) {
            ret = super.onKeyDown(keyCode, event);
        }
        if (!ret) {
            ret = keyEvent(event.getAction(), keyCode, event.getUnicodeChar(), event.getMetaState(), event);
        }
        return ret;
    }

    /**
     * Implementation function: defined in libnvevent.a
     * The application does not and should not overide this; nv_event handles this internally
     * And remaps as needed into the native calls exposed by nv_event.h
     */
    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event)
    {
        if (keyCode == 115 && Build.VERSION.SDK_INT >= 11)
        {
            boolean capsLockOn = event.isCapsLockOn();
            keyEvent(capsLockOn ? 3 : 4, 115, 0, 0, event);
        }
        if (keyCode == 89 || keyCode == 85 || keyCode == 90)
        {
            return false;
        }
        boolean onKeyUp = super.onKeyUp(keyCode, event);
        if (onKeyUp)
        {
            return onKeyUp;
        }
        return keyEvent(event.getAction(), keyCode, event.getUnicodeChar(), event.getMetaState(), event);
    }

    public boolean InitEGLAndGLES2(int i)
    {
        System.out.println("lnitEGLAndGLES2");
        if (cachedSurfaceHolder == null)
        {
            System.out.println("InitEGLAndGLES2 failed, cachedSurfaceHoIder is null");
            return false;
        }

        boolean eglInitialized = true;
        if (eglContext == null)
        {
            eglInitialized = initEGL();
        }
        if (eglInitialized)
        {
            System.out.println("Should we create a surface?");
            if (!viewIsActive)
            {
                System.out.println("Yes! Calling create surface");
                createEGLSurface(this.cachedSurfaceHolder);
                System.out.println("Done creating surface");
            }
            viewIsActive = true;
            SwapBufferSkip = 1;
        }
        else
        {
            System.out.println("initEGlAndGLES2 failed, core EGL init failure");
            return false;
        }

        return true;
    }

    /**
     * Implementation function: defined in libnvevent.a
     * The application does not and should not overide this; nv_event handles this internally
     * And remaps as needed into the native calls exposed by nv_event.h
     */

    private native void onInputEnd(byte[] str);
    @Override
    public void OnInputEnd(String str)
    {
        byte[] toReturn = null;
        try
        {
            toReturn = str.getBytes("windows-1251");
        }
        catch(UnsupportedEncodingException e)
        {

        }

        onInputEnd(toReturn);
    }

    public native void onKeyboardClose();

    public void SetVisibleKeyboardStandard(int active, int type){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mKeyboardStandardManager.setVisible(active, type, false);
                mKeyboardStandardManager.setCallableEnter(new Runnable() {
                    @Override
                    public void run() {
                        OnInputEnd(mKeyboardStandardManager.keyboardTextInput.getText().toString());
                    }
                });

                mKeyboardStandardManager.setCallableClose(null);
            }
        });
    }

    public SurfaceView GetSurfaceView()
    {
        return mSurfaceView;
    }

    public void ToggleRender(int active){
    runOnUiThread(new Runnable() {
        @Override
        public void run() {
            renderActiveMainRender = active == 1 ? true : false;

            if(renderActiveMainRender == false){
                mRenderMoreViews.setVisibility(View.INVISIBLE);
            }
            if(renderActiveMainRender == true){
                mRenderMoreViews.setVisibility(View.VISIBLE);
            }
        }
    });
    }

    public void SetVisibleDialog(int active){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    mDialogManager.SetVisibleDialog(active);
                }
            }
        });
    }

    @SuppressLint("NewApi")
    protected boolean systemInit()
    {

        final NvEventQueueActivity act = this;

        System.out.println("ln systemInit");

        setContentView(R.layout.main_render_screen);

        SurfaceView view = findViewById(R.id.main_sv);


        SurfaceHolder holder = view.getHolder();
        holder.setType(SurfaceHolder.SURFACE_TYPE_GPU);
        holder.setKeepScreenOn(true);

        mSurfaceView = view;
        mRootFrame = findViewById(R.id.main_fl_root);
        mAndroidUI = findViewById(R.id.ui_layout);

        //Донат в игре
        constraintlayoutDonate = findViewById(R.id.constraintlayoutDonate);
        webView = findViewById(R.id.webViewDonate);
        WebSettings set =  webView.getSettings();
        set.setJavaScriptEnabled(true);
        set.setCacheMode(WebSettings.LOAD_DEFAULT);
        set.setDomStorageEnabled(true);

        mRenderMoreViews = new FrameLayout(this);
        mRenderMoreViews.setLayoutParams(new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

        mKeyboardStandardManager = new KeyboardStandardManager(this);
        mKeyboardStandardManager.selectLang(mKeyboardStandardManager.KEYBOARD_LANG_ENG);

        mDialogManager = new DialogManager(this);
        mHudManager = new HudManager(this);
        mCustomScreen = new CustomScreen(this);
       // mSpeedometer = new Speedometer(this);
        mTab = new Tab(this);
        //mWelcome = new Welcome(this);

        mRootFrame.addView(mRenderMoreViews);
        mRenderMoreViews.addView(mDialogManager.getView());
        mRenderMoreViews.addView(mKeyboardStandardManager.getView());


        textView31 = findViewById(R.id.textView31);
        textView31.setVisibility(View.INVISIBLE);
        textView31.setClickable(true);
        textView31.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MenuActivity.showCustomDialog(NvEventQueueActivity.this, "Ты добавил Google Authenticator в менеджер паролей.\nПри следующей авторизации тебе только нужно нажать 'Ввод' когда будет запрошен Google код.", "Закрыть");

               // textView31.setText("Тест");
            }
        });

        ImageView donate_clossed = findViewById(R.id.donate_clossed);
        donate_clossed.setClickable(true);
        donate_clossed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                constraintlayoutDonate.setVisibility(View.GONE);
            }
        });


        ////new DialogScrollBarView(this, scrollBarView, sliderView, scrollParentContent, scrollChildContent);

        //this.addContentView(ll, new android.view.WindowManager.LayoutParams(-1, -1));

        /*
        constraintLayout = findViewById(R.id.buttonpanel_layout);
        constraintLayout.setVisibility(View.INVISIBLE);

        ConstraintLayout button_first = findViewById(R.id.button_first);

        //Вывод всего диалога
        dialog_layout = findViewById(R.id.dialog_layout);
        dialog_layout.setVisibility(View.INVISIBLE);
        //Ввод текста в диалог
        ConstraintLayout dialog_input_layout = findViewById(R.id.dialog_input_layout);
        dialog_input_layout.setVisibility(View.GONE);


        ConstraintLayout dialog_list_layout = findViewById(R.id.dialog_list_layout);
        dialog_list_layout.setVisibility(View.GONE);

        dialog_caption = findViewById(R.id.dialog_caption);
        dialog_caption.setText("Вова вот смена заголовка");


        ScrollView dialog_text_layout  = findViewById(R.id.dialog_text_layout);
        dialog_text_layout.setVisibility(View.VISIBLE);
        TextView dialog_text = findViewById(R.id.dialog_text);
        dialog_text.setText("Вова вот манал я ваши плюсы\nJava Forever\nТут даже по названию можно понять структуру верстки");

        button_G = findViewById(R.id.button_G);
        button_G.setVisibility(View.INVISIBLE);
        button_ALT = findViewById(R.id.button_ALT);
        button_ALT.setVisibility(View.INVISIBLE);
        button_BIND = findViewById(R.id.button_BIND);
        button_BIND.setVisibility(View.INVISIBLE);
        button_Y = findViewById(R.id.button_Y);
        button_Y.setVisibility(View.INVISIBLE);
        button_N = findViewById(R.id.button_N);
        button_N.setVisibility(View.INVISIBLE);


        button_first.setClickable(true);
        button_first.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (button_G.getVisibility() == View.VISIBLE) {
                    button_G.setVisibility(View.INVISIBLE);
                    button_ALT.setVisibility(View.INVISIBLE);
                    button_BIND.setVisibility(View.INVISIBLE);
                    button_Y.setVisibility(View.INVISIBLE);
                    button_N.setVisibility(View.INVISIBLE);
                } else {
                    button_G.setVisibility(View.VISIBLE);
                    button_ALT.setVisibility(View.VISIBLE);
                    button_BIND.setVisibility(View.VISIBLE);
                    button_Y.setVisibility(View.VISIBLE);
                    button_N.setVisibility(View.VISIBLE);
                }

            }
        });*/


        connect_view = (ConstraintLayout) findViewById(R.id.connect_view);
        textViewConnectOnlineOne = (TextView) findViewById(R.id.textViewConnectOnlineOne);
        imageViewOneServer = (ImageView) findViewById(R.id.imageViewOneServer);
        textViewConnectOnlineTwo = (TextView) findViewById(R.id.textViewConnectOnlineTwo);
        imageViewTwoServer = (ImageView) findViewById(R.id.imageViewTwoServer);
        textViewConnectOnlineOne.setText(PublicInfo.OnlineServerOne +"/1000");
        textViewConnectOnlineTwo.setText(PublicInfo.OnlineServerTwo +"/1000");

        final Animation animAlpha = AnimationUtils.loadAnimation(this, R.anim.click);




        imageViewIranConnect = (ImageView) findViewById(R.id.imageViewIranConnect);
        imageViewIranConnect.setClickable(true);
        imageViewIranConnect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(iranConnect == 0){
                    iranConnect = 1;
                    imageViewOneServer.setImageDrawable(getResources().getDrawable(R.drawable.icon_flag_iran, getApplicationContext().getTheme()));
                    imageViewTwoServer.setImageDrawable(getResources().getDrawable(R.drawable.icon_flag_iran, getApplicationContext().getTheme()));
                } else {
                    iranConnect = 0;
                    imageViewOneServer.setImageDrawable(getResources().getDrawable(R.drawable.background_flin_server_icon, getApplicationContext().getTheme()));
                    imageViewTwoServer.setImageDrawable(getResources().getDrawable(R.drawable.background_flin_server_icon, getApplicationContext().getTheme()));
                }
            }
        });

        ImageView buttonConnectServerOne = (ImageView) findViewById(R.id.buttonConnectOneSever);
        buttonConnectServerOne.setClickable(true);
        buttonConnectServerOne.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PublicInfo.selectServerConnect = 1;
                if(iranConnect == 1){
                    setNativeSelectServer(4);
                } else setNativeSelectServer(0);
                v.startAnimation(animAlpha);
            }
        });

        buttonConnectServerOne.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_DOWN){
                    v.animate().scaleX(0.85f).scaleY(0.85f).setDuration(150);
                }
                if(event.getAction() == MotionEvent.ACTION_UP){
                    v.animate().scaleX(1f).scaleY(1f).setDuration(150);
                }
                return false;
            }
        });

        ImageView buttonConnectServerTwo = (ImageView) findViewById(R.id.buttonConnectTwoSever);
        buttonConnectServerTwo.setClickable(true);
        buttonConnectServerTwo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PublicInfo.selectServerConnect = 2;
                if(iranConnect == 1){
                    setNativeSelectServer(5);
                } else setNativeSelectServer(1);
                v.startAnimation(animAlpha);
            }
        });
        buttonConnectServerTwo.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_DOWN){
                    v.animate().scaleX(0.85f).scaleY(0.85f).setDuration(150);
                }
                if(event.getAction() == MotionEvent.ACTION_UP){
                    v.animate().scaleX(1f).scaleY(1f).setDuration(150);
                }
                return false;
            }
        });


        ImageView imageViewTestSelectServer1 = (ImageView) findViewById(R.id.imageViewTestSelectServer1);
        imageViewTestSelectServer1.setClickable(true);
        imageViewTestSelectServer1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PublicInfo.selectServerConnect = 0;
                setNativeSelectServer(2);
                v.startAnimation(animAlpha);
            }
        });

        ImageView imageViewTestSelectServer2 = (ImageView) findViewById(R.id.imageViewTestSelectServer2);
        imageViewTestSelectServer2.setClickable(true);
        imageViewTestSelectServer2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PublicInfo.selectServerConnect = 3;
                setNativeSelectServer(3);
                v.startAnimation(animAlpha);
            }
        });

        ImageView imageViewTestServer = (ImageView) findViewById(R.id.imageViewTestServer);
        imageViewTestServer.setClickable(true);
        imageViewTestServer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.startAnimation(animAlpha);
                imageViewTestSelectServer1.setVisibility(View.VISIBLE);
                imageViewTestSelectServer2.setVisibility(View.VISIBLE);
            }
        });




        imageViewTestServer.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_DOWN){
                    v.animate().scaleX(0.85f).scaleY(0.85f).setDuration(150);
                }
                if(event.getAction() == MotionEvent.ACTION_UP){
                    v.animate().scaleX(1f).scaleY(1f).setDuration(150);
                }
                return false;
            }
        });



        //SurfaceHolder holder = view.getHolder();
        //holder.setType(SurfaceHolder.SURFACE_TYPE_GPU);
        //holder.setKeepScreenOn(true);

        view.setFocusable(true);
        view.setFocusableInTouchMode(true);

        mRootFrame.setOnTouchListener(this);


        mInputManager = new InputManager(this);
        mHeightProvider = new HeightProvider(this).init(mRootFrame).setHeightListener(this);

        DoResumeEvent();

        holder.addCallback(new Callback()
        {
            // @Override
            public void surfaceCreated(SurfaceHolder holder)
            {
                System.out.println("systemInit.surfaceCreated");
                @SuppressWarnings("unused")
                boolean firstRun = cachedSurfaceHolder == null;
                cachedSurfaceHolder = holder;

                if (fixedWidth!=0 && fixedHeight!=0)
                {
                    System.out.println("Setting fixed window size");
                    holder.setFixedSize(fixedWidth, fixedHeight);
                }

                ranInit = true;
                if(!supportPauseResume && !init(true))
                {
                    handler.post(new Runnable()
                                 {
                                     public void run()
                                     {
                                         new AlertDialog.Builder(act)
                                                 .setMessage("Application initialization failed. The application will exit.")
                                                 .setPositiveButton("Ok",
                                                         new DialogInterface.OnClickListener ()
                                                         {
                                                             public void onClick(DialogInterface i, int a)
                                                             {
                                                                 finish();
                                                             }
                                                         }
                                                 )
                                                 .setCancelable(false)
                                                 .show();
                                     }
                                 }
                    );
                }

                if (!firstRun && ResumeEventDone)
                {
                    System.out.println("entering resumeEvent");
                    resumeEvent();
                    System.out.println("returned from resumeEvent");
                }
                setWindowSize(surfaceWidth, surfaceHeight);
            }

            /**
             * Implementation function: defined in libnvevent.a
             * The application does not and should not overide this; nv_event handles this internally
             * And remaps as needed into the native calls exposed by nv_event.h
             */
            // @Override
            public void surfaceChanged(SurfaceHolder holder, int format,
                                       int width, int height)
            {
                System.out.println("Surface changed: " + width + ", " + height);
                surfaceWidth = width;
                surfaceHeight = height;
                setWindowSize(surfaceWidth, surfaceHeight);
            }

            /**
             * Implementation function: defined in libnvevent.a
             * The application does not and should not overide this; nv_event handles this internally
             * And remaps as needed into the native calls exposed by nv_event.h
             */
            // @Override
            public void surfaceDestroyed(SurfaceHolder holder)
            {
                System.out.println("systemInit.surfaceDestroyed");
                viewIsActive = false;
                pauseEvent();
                destroyEGLSurface();
            }
        });
        return true;
    }


    /** The number of bits requested for the red component */
    protected int redSize     = 5;
    /** The number of bits requested for the green component */
    protected int greenSize   = 6;
    /** The number of bits requested for the blue component */
    protected int blueSize    = 5;
    /** The number of bits requested for the alpha component */
    protected int alphaSize   = 0;
    /** The number of bits requested for the stencil component */
    protected int stencilSize = 0;
    /** The number of bits requested for the depth component */
    protected int depthSize   = 16;

    /** Attributes used when selecting the EGLConfig */
    protected int[] configAttrs = null;
    /** Attributes used when creating the context */
    protected int[] contextAttrs = null;

    /**
     * Called to initialize EGL. This function should not be called by the inheriting
     * activity, but can be overridden if needed.
     *
     * @return True if successful
     */
    protected boolean initEGL()
    {
        if (configAttrs == null)
            configAttrs = new int[] {EGL10.EGL_NONE};
        int[] oldConf = configAttrs;

        configAttrs = new int[3 + oldConf.length-1];
        int i = 0;
        for (i = 0; i < oldConf.length-1; i++)
            configAttrs[i] = oldConf[i];
        configAttrs[i++] = EGL_RENDERABLE_TYPE;
        configAttrs[i++] = EGL_OPENGL_ES2_BIT;
        configAttrs[i++] = EGL10.EGL_NONE;

        contextAttrs = new int[]
                {
                        EGL_CONTEXT_CLIENT_VERSION, 2,
                        EGL10.EGL_NONE
                };

        if (configAttrs == null)
            configAttrs = new int[] {EGL10.EGL_NONE};
        int[] oldConfES2 = configAttrs;

        configAttrs = new int[13 + oldConfES2.length-1];
        for (i = 0; i < oldConfES2.length-1; i++)
            configAttrs[i] = oldConfES2[i];
        configAttrs[i++] = EGL10.EGL_RED_SIZE;
        configAttrs[i++] = redSize;
        configAttrs[i++] = EGL10.EGL_GREEN_SIZE;
        configAttrs[i++] = greenSize;
        configAttrs[i++] = EGL10.EGL_BLUE_SIZE;
        configAttrs[i++] = blueSize;
        configAttrs[i++] = EGL10.EGL_ALPHA_SIZE;
        configAttrs[i++] = alphaSize;
        configAttrs[i++] = EGL10.EGL_STENCIL_SIZE;
        configAttrs[i++] = stencilSize;
        configAttrs[i++] = EGL10.EGL_DEPTH_SIZE;
        configAttrs[i++] = depthSize;
        configAttrs[i++] = EGL10.EGL_NONE;

        egl = (EGL10) EGLContext.getEGL();
        egl.eglGetError();
        eglDisplay = egl.eglGetDisplay(EGL10.EGL_DEFAULT_DISPLAY);
        System.out.println("eglDisplay: " + eglDisplay + ", errr: " + egl.eglGetError());
        int[] version = new int[2];
        boolean ret = egl.eglInitialize(eglDisplay, version);
        System.out.println("EGLInitialize returned: " + ret);
        if (!ret)
        {
            return false;
        }
        int eglErr = egl.eglGetError();
        if (eglErr != EGL10.EGL_SUCCESS)
            return false;
        System.out.println("eglInitialize err: " + eglErr);

        final EGLConfig[] config = new EGLConfig[20];
        int num_configs[] = new int[1];
        egl.eglChooseConfig(eglDisplay, configAttrs, config, config.length, num_configs);
        System.out.println("eglChooseConfig err: " + egl.eglGetError());

        int score = 1<<24; // to make sure even worst score is better than this, like 8888 when request 565...
        int val[] = new int[1];
        for (i = 0; i < num_configs[0]; i++)
        {
            boolean cont = true;
            int currScore = 0;
            int r, g, b, a, d, s;
            for (int j = 0; j < (oldConf.length-1)>>1; j++)
            {
                egl.eglGetConfigAttrib(eglDisplay, config[i], configAttrs[j*2], val);
                if ((val[0] & configAttrs[j*2+1]) != configAttrs[j*2+1])
                {
                    cont = false; // Doesn't match the "must have" configs
                    break;
                }
            }
            if (!cont)
                continue;
            egl.eglGetConfigAttrib(eglDisplay, config[i], EGL10.EGL_RED_SIZE, val); r = val[0];
            egl.eglGetConfigAttrib(eglDisplay, config[i], EGL10.EGL_GREEN_SIZE, val); g = val[0];
            egl.eglGetConfigAttrib(eglDisplay, config[i], EGL10.EGL_BLUE_SIZE, val); b = val[0];
            egl.eglGetConfigAttrib(eglDisplay, config[i], EGL10.EGL_ALPHA_SIZE, val); a = val[0];
            egl.eglGetConfigAttrib(eglDisplay, config[i], EGL10.EGL_DEPTH_SIZE, val); d = val[0];
            egl.eglGetConfigAttrib(eglDisplay, config[i], EGL10.EGL_STENCIL_SIZE, val); s = val[0];

            System.out.println(">>> EGL Config ["+i+"] R"+r+"G"+g+"B"+b+"A"+a+" D"+d+"S"+s);

            currScore = (Math.abs(r - redSize) + Math.abs(g - greenSize) + Math.abs(b - blueSize) + Math.abs(a - alphaSize)) << 16;
            currScore += Math.abs(d - depthSize) << 8;
            currScore += Math.abs(s - stencilSize);

            if (currScore < score)
            {
                System.out.println("--------------------------");
                System.out.println("New config chosen: " + i);
                for (int j = 0; j < (configAttrs.length-1)>>1; j++)
                {
                    egl.eglGetConfigAttrib(eglDisplay, config[i], configAttrs[j*2], val);
                    if (val[0] >= configAttrs[j*2+1])
                        System.out.println("setting " + j + ", matches: " + val[0]);
                }

                score = currScore;
                eglConfig = config[i];
            }
        }
        eglContext = egl.eglCreateContext(eglDisplay, eglConfig, EGL10.EGL_NO_CONTEXT, contextAttrs);
        System.out.println("eglCreateContext: " + egl.eglGetError());

        gl = (GL11) eglContext.getGL();
        return true;
    }

    /**
     * Called to create the EGLSurface to be used for rendering. This function should not be called by the inheriting
     * activity, but can be overridden if needed.
     *
     * @param surface The SurfaceHolder that holds the surface that we are going to render to.
     * @return True if successful
     */
    protected boolean createEGLSurface(SurfaceHolder surface)
    {
        eglSurface = egl.eglCreateWindowSurface(eglDisplay, eglConfig, surface, null);

        System.out.println("eglSurface: " + eglSurface + ", err: " + egl.eglGetError());
        int sizes[] = new int[1];

        egl.eglQuerySurface(eglDisplay, eglSurface, EGL10.EGL_WIDTH, sizes);
        surfaceWidth = sizes[0];
        egl.eglQuerySurface(eglDisplay, eglSurface, EGL10.EGL_HEIGHT, sizes);
        surfaceHeight = sizes[0];

        System.out.println("checking glVendor == null?");
        if (this.glVendor == null) {
            System.out.println("Making current and back");
            makeCurrent();
            unMakeCurrent();
        }

        System.out.println("Done create EGL surface");

        return true;
    }

    /**
     * Destroys the EGLSurface used for rendering. This function should not be called by the inheriting
     * activity, but can be overridden if needed.
     */
    protected void destroyEGLSurface()
    {
        System.out.println("*** destroyEGLSurface");
        if (eglDisplay != null && eglSurface != null)
            egl.eglMakeCurrent(eglDisplay, EGL10.EGL_NO_SURFACE, EGL10.EGL_NO_SURFACE, EGL10.EGL_NO_CONTEXT);
        if (eglSurface != null)
            egl.eglDestroySurface(eglDisplay, eglSurface);
        eglSurface = null;
        if(exitGame == 1020){
            runOnUiThread(new Runnable() {
                public void run() {
                    new NotificationDialogCrash().show(NvEventQueueActivity.this.getSupportFragmentManager(), "alert");
                }
            });
        }
    }

    /**
     * Called to clean up egl. This function should not be called by the inheriting
     * activity, but can be overridden if needed.
     */
    protected void cleanupEGL()
    {
        System.out.println("cleanupEGL");
        destroyEGLSurface();
        if (eglDisplay != null)
            egl.eglMakeCurrent(eglDisplay, EGL10.EGL_NO_SURFACE, EGL10.EGL_NO_SURFACE, EGL10.EGL_NO_CONTEXT);
        if (eglContext != null)
            egl.eglDestroyContext(eglDisplay, eglContext);
        if (eglDisplay != null)
            egl.eglTerminate(eglDisplay);

        eglDisplay = null;
        eglContext = null;
        eglSurface = null;

        ranInit = false;
        eglConfig = null;

        cachedSurfaceHolder = null;
        surfaceWidth = 0;
        surfaceHeight = 0;
        if(exitGame == 1020){
            runOnUiThread(new Runnable() {
                public void run() {
                    new NotificationDialogCrash().show(NvEventQueueActivity.this.getSupportFragmentManager(), "alert");
                }
            });
        }
    }

    /**
     * Implementation function: 
     * The application does not and should not overide or call this directly
     * Instead, the application should call NVEventEGLSwapBuffers(),
     * which is declared in nv_event.h
     */

    public boolean swapBuffers()
    {
        //long stopTime;
        //long startTime = nvGetSystemTime();

        if (SwapBufferSkip > 0) {
            SwapBufferSkip--;
            System.out.println("swapBuffer wait");
            return true;
        }
        if (eglSurface == null)
        {
            System.out.println("eglSurface is NULL");
            return false;
        }
        else if (!egl.eglSwapBuffers(eglDisplay, eglSurface))
        {
            System.out.println("eglSwapBufferrr: " + egl.eglGetError());
            return false;
        }
        //stopTime = nvGetSystemTime();
        //String s = String.format("%d ms in eglSwapBuffers", (int)(stopTime - startTime));
        //Log.v("EventAccelerometer", s);

        return true;
    }

    public boolean getSupportPauseResume()
    {
        return supportPauseResume;
    }

    public int getSurfaceWidth()
    {
        return surfaceWidth;
    }

    public int getSurfaceHeight()
    {
        return surfaceHeight;
    }

    /**
     * Implementation function: 
     * The application does not and should not overide or call this directly
     * Instead, the application should call NVEventEGLMakeCurrent(),
     * which is declared in nv_event.h
     */

    public void GetGLExtensions()
    {
        if (!HasGLExtensions && gl != null && this.cachedSurfaceHolder != null)
        {
            glVendor = gl.glGetString(GL10.GL_VENDOR);
            glExtensions = gl.glGetString(GL10.GL_EXTENSIONS);
            glRenderer = gl.glGetString(GL10.GL_RENDERER);
            glVersion = gl.glGetString(GL10.GL_VERSION);
            System.out.println("Vendor: " + glVendor);
            System.out.println("Extensions " + glExtensions);
            System.out.println("Renderer: " + glRenderer);
            System.out.println("GIVersion: " + glVersion);
            if (this.glVendor != null)
            {
                this.HasGLExtensions = true;
            }
        }
    }

    public boolean makeCurrent()
    {
        if (eglContext == null)
        {
            System.out.println("eglContext is NULL");
            return false;
        }
        else if (eglSurface == null)
        {
            System.out.println("eglSurface is NULL");
            return false;
        }
        else if (!egl.eglMakeCurrent(eglDisplay, eglSurface, eglSurface, eglContext))
        {
            if (!egl.eglMakeCurrent(eglDisplay, eglSurface, eglSurface, eglContext))
            {
                System.out.println("eglMakeCurrent err: " + egl.eglGetError());
                return false;
            }
        }

        // This must be called after we have bound an EGL context
        //nvAcquireTimeExtension();
        GetGLExtensions();
        return true;
    }

    public int getOrientation()
    {
        return display.getOrientation();
    }

    /**
     * Implementation function: 
     * The application does not and should not overide or call this directly
     * Instead, the application should call NVEventEGLUnmakeCurrent(),
     * which is declared in nv_event.h
     */
    public boolean unMakeCurrent()
    {
        if (!egl.eglMakeCurrent(eglDisplay, EGL10.EGL_NO_SURFACE, EGL10.EGL_NO_SURFACE, EGL10.EGL_NO_CONTEXT))
        {
            System.out.println("egl(Un)MakeCurrent err: " + egl.eglGetError());
            return false;
        }

        return true;
    }

    /**
     * Called when the Activity is exiting and it is time to cleanup.
     * Kept separate from the {@link #cleanup()} function so that subclasses
     * in their simplest form do not need to call any of the parent class' functions. This to make
     * it easier for pure C/C++ application so that these do not need to call java functions from C/C++
     * code.
     *
     * @see #cleanup()
     */


    protected void systemCleanup()
    {
        if (ranInit)
            cleanup();
        cleanupEGL();

        //postCleanup();
    }

    public void callLauncherActivity(final int type)
    {
        System.out.println("MIHAIL callLauncherActivity"+type);
        if(type == 1555){

            runOnUiThread(new Runnable() {

                public void run() {
                    connect_view.setVisibility(View.VISIBLE);
                }
            });
        }
        if(type == 122){

            runOnUiThread(new Runnable() {
                public void run() {
                    connect_view.setVisibility(View.INVISIBLE);
                }
            });
        }
        if(type == 100){
            try {
                byte[] dataBytes = PublicInfo.UIP.getBytes("windows-1251");
                setUIP(dataBytes);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        if(type == 101){
            try {
                byte[] dataBytes = PublicInfo.UID.getBytes("windows-1251");
                setUID(dataBytes);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        if(type == 123){
            runOnUiThread(new Runnable() {
                public void run() {
                    connect_view.setVisibility(View.INVISIBLE);

                    Toast toastMessage = Toast.makeText(getApplicationContext(), "У вас установлена модифицированная папка SAMP где изменен файл weapon.dat\n\nПерезайдите заново в игру!", Toast.LENGTH_LONG);
                    toastMessage.setGravity(Gravity.CENTER, 0, 0);
                    toastMessage.show();
                }
            });
        }
        if(type == 14555){
            runOnUiThread(new Runnable() {
                public void run() {
                    System.out.println("MIHAIL 2 выполнился выход из игры!");
                    finishActivity(0);
                    finish();
                    Intent intent = new Intent(NvEventQueueActivity.this, MenuActivity.class);
                    startActivity(intent);
                }
            });
        }
        if(type == 15001){
            runOnUiThread(new Runnable() {
                public void run() {
                    showDialogDonate(NvEventQueueActivity.this);
                }
            });
        }

    }

    public void ExitActivity() {
        runOnUiThread(new Runnable() {
            public void run() {
                System.out.println("MIHAIL выполнился выход из игры!");
                finishActivity(0);
            }
        });
    }

    public void dialogTextSend( String ttitle, String ttext, String tbtn1, String tbtn2)
    {


    }



    public void showInputLayout()
    {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mInputManager.ShowInputLayout();
            }
        });
    }

    public void hideInputLayout()
    {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mInputManager.HideInputLayout();
            }
        });
    }

    public byte[] getClipboardText()
    {
        String retn = " ";

        if(mClipboardManager.getPrimaryClip() != null)
        {
            ClipData.Item item = mClipboardManager.getPrimaryClip().getItemAt(0);
            if(item != null)
            {
                CharSequence sequence = item.getText();
                if(sequence != null)
                {
                    retn = sequence.toString();
                }
            }
        }

        byte[] toReturn = null;
        try
        {
            toReturn = retn.getBytes("windows-1251");
        }
        catch(UnsupportedEncodingException e)
        {

        }

        return toReturn;
    }

    public static NvEventQueueActivity getInstance() {
        return instance;
    }

    //public void showWelcome(boolean isRegister) { runOnUiThread(() -> mWelcome.show(isRegister) ); }

    public void updateHudInfo(int health, int armour, int hunger, int weaponid, int ammo, int playerid, int money, int wanted) { runOnUiThread(() -> { mHudManager.UpdateHudInfo(health, armour, hunger, weaponid, ammo, playerid, money, wanted); }); }

    public void updateLoading(int status) { runOnUiThread(() -> { mCustomScreen.UpdateScreen(status); }); }

    public void showHud() { runOnUiThread(() -> {  mHudManager.ShowHud(); }); }

    public void hideHud() { runOnUiThread(() -> { mHudManager.HideHud(); }); }

    /*public void setPauseState(boolean z2) {
        if (mAndroidUI == null) {
            mAndroidUI = (FrameLayout) findViewById(R.id.ui_layout);
        }
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mAndroidUI.setVisibility(z2 ? View.GONE:View.VISIBLE);
           }
        });
    }*/

    public void setPauseState(boolean z2) {
        if (mAndroidUI == null) {
            mAndroidUI = (FrameLayout) findViewById(R.id.ui_layout);
        }
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mAndroidUI.setVisibility(z2 ? View.GONE : View.VISIBLE);

                if (z2) {
                    hideHud();  // Скрытие HUD при открытии меню
                } else {
                    showHud();  // Отображение HUD при закрытии меню
                }
            }
        });
    }

    //public void updateSpeedInfo(int speed, int fuel, int hp, int mileage, int engine, int light, int belt, int lock) { runOnUiThread(() -> { mSpeedometer.UpdateSpeedInfo(speed, fuel, hp, mileage, engine, light, belt, lock); }); }

    //public void showSpeed() { runOnUiThread(() -> { mSpeedometer.ShowSpeed(); }); }

    //public void hideSpeed() { runOnUiThread(() -> { mSpeedometer.HideSpeed(); }); }

   // public void showWelcome(boolean isRegister) { runOnUiThread(() -> mWelcome.show(isRegister) ); }


    public void showTabWindow() { runOnUiThread(() -> mTab.show(true)); }
    public void setTabStat(int id, String name, int score, int ping) { runOnUiThread(() -> mTab.setStat(id, name, score, ping) ); }



    public void showHudButtonTab() {
        runOnUiThread(() -> mHudManager.ShowTabButton());
    }

    public void hideHudButtonTab() {
        runOnUiThread(() -> mHudManager.HideTabButton());
    }

    public void showHudButtonVeh() {
        runOnUiThread(() -> mHudManager.ShowVehButton());
    }

    public void showHudLogo(boolean show) {
        runOnUiThread(() -> mHudManager.ShowLogo(show));
    }

    public void showHudButtonG() {
        runOnUiThread(() -> mHudManager.ShowVehButtonG());
    }

    public void hideHudButtonG() {
        runOnUiThread(() -> mHudManager.HideVehButtonG());
    }

    public void showHudDT() {
        runOnUiThread(() -> mHudManager.ShowTimeData());
    }

    public void hideHudDT() {
        runOnUiThread(() -> mHudManager.HideTimeData());
    }

    public void hideHudButtonVeh() {
        runOnUiThread(() -> mHudManager.HideVehButton());
    }



    public class MyWebViewClient extends WebViewClient {
        @TargetApi(Build.VERSION_CODES.N)
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
            String url = request.getUrl().toString();
            //     Toast.makeText(getApplicationContext(), "Открыт: "+ url, Toast.LENGTH_SHORT).show();
            if(url.contains("https://flin-rp.com/donate_success.php") || url.contains("http://flin-rp.com/donate_success.php"))
            {
                successDonate = 1;
               // Intent intent = new Intent(, MenuActivity.class);
                //startActivity(intent);
                return false;
            }
            if(url.contains("https://flin-rp.com/donate_unsuccess.php")  || url.contains("http://flin-rp.com/donate_unsuccess.php"))
            {
                successDonate = 2;
              //  Intent intent = new Intent(WebDonateActivity.this, MenuActivity.class);
               // startActivity(intent);
                return false;
            }

            view.loadUrl(request.getUrl().toString());
            return true;
        }
        int loadPage = 1;
        // Для старых устройств
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {

            if(url.contains("https://flin-rp.com/donate_success.php") || url.contains("http://flin-rp.com/donate_success.php"))
            {
                successDonate = 1;
                //Intent intent = new Intent(WebDonateActivity.this, MenuActivity.class);
                //startActivity(intent);
                return false;
            }
            if(url.contains("https://flin-rp.com/donate_unsuccess.php")  || url.contains("http://flin-rp.com/donate_unsuccess.php"))
            {
                successDonate = 2;
                //Intent intent = new Intent(WebDonateActivity.this, MenuActivity.class);
              //  startActivity(intent);
                return false;
            }
            view.loadUrl(url);
            return true;
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            if(url.contains("https://flin-rp.com/donate_success.php") || url.contains("http://flin-rp.com/donate_success.php"))
            {
                successDonate = 1;
                //Intent intent = new Intent(WebDonateActivity.this, MenuActivity.class);
                //startActivity(intent);

            }
            if(url.contains("https://flin-rp.com/donate_unsuccess.php")  || url.contains("http://flin-rp.com/donate_unsuccess.php"))
            {
                successDonate = 2;
                //Intent intent = new Intent(WebDonateActivity.this, MenuActivity.class);
                //startActivity(intent);
            }
            //  Toast.makeText(getApplicationContext(), "Страница загружена!", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
            if(loadPage == 0){
                loadPage++;
                ToastMakeString("Идет загрузка страницы");
            }
            else {
                ToastMakeString("Идет загрузка страницы");
            }

            //Toast.makeText(getApplicationContext(), "Начата загрузка страницы", Toast.LENGTH_SHORT).show();
            if(url.contains("https://flin-rp.com/donate_success.php") || url.contains("http://flin-rp.com/donate_success.php"))
            {
                successDonate = 1;
             ///   Intent intent = new Intent(WebDonateActivity.this, MenuActivity.class);
              //  startActivity(intent);
            }
            if(url.contains("https://flin-rp.com/donate_unsuccess.php")  || url.contains("http://flin-rp.com/donate_unsuccess.php"))
            {
                successDonate = 2;
                //Intent intent = new Intent(WebDonateActivity.this, MenuActivity.class);
                //startActivity(intent);
            }
        }

    }


    public void showDialogDonate(Context appThis) {
        final Dialog dialog = new Dialog(appThis);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_info);
        dialog.setCancelable(true);

        EditText editTextSum = dialog.findViewById(R.id.editTextSum);
        editTextSum.setVisibility(View.VISIBLE);
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(editTextSum, InputMethodManager.SHOW_IMPLICIT);
        editTextSum.requestFocus();


        ImageView icon = dialog.findViewById(R.id.icon);
        icon.setImageResource(R.drawable.payments);

        TextView contentTextInfo = dialog.findViewById(R.id.contentTextInfo);
        contentTextInfo.setVisibility(View.GONE);

        TextView title = dialog.findViewById(R.id.title);
        title.setText("Введите сумму");

        AppCompatButton bt_close = dialog.findViewById(R.id.bt_close);
        bt_close.setText("Пополнить");

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;


        ((AppCompatButton) dialog.findViewById(R.id.bt_close)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String enterSum = editTextSum.getText().toString().trim();
                if (enterSum.length() == 0) {
                    Toast.makeText(getApplicationContext(), "Вы не ввели сумму!", Toast.LENGTH_SHORT).show();
                    return;
                }
                constraintlayoutDonate.setVisibility(View.VISIBLE);
                WebSettings set =  webView.getSettings();
                set.setCacheMode(WebSettings.LOAD_DEFAULT);
                set.setDomStorageEnabled(true);
                set.setBuiltInZoomControls(false);//отменяем зум
                set.setJavaScriptEnabled(true);
                webView.setWebChromeClient(new WebChromeClient());
                webView.setWebViewClient(new MyWebViewClient());
                if(PublicInfo.selectServerConnect == 2) webView.loadUrl(PublicInfo.PayMethodServer2 + enterSum + "&account=" + PublicInfo.getSelectServerConnectNick  + "&desc=Пополнение%20счета%20" + PublicInfo.getSelectServerConnectNick  + "%20через%20Android%20S2");
                if(PublicInfo.selectServerConnect == 1) webView.loadUrl(PublicInfo.PayMethodServer1 + enterSum + "&account=" + PublicInfo.getSelectServerConnectNick  + "&desc=Пополнение%20счета%20" + PublicInfo.getSelectServerConnectNick  + "%20через%20Android%20S2");
                if(PublicInfo.selectServerConnect == 0) webView.loadUrl(PublicInfo.PayMethodServer1 + enterSum + "&account=" + PublicInfo.getSelectServerConnectNick  + "&desc=Пополнение%20счета%20" + PublicInfo.getSelectServerConnectNick  + "%20через%20Android%20S2");
                dialog.dismiss();
            }
        });
        dialog.show();
        dialog.getWindow().setAttributes(lp);
    }





}
