package com.flin.core;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.SeekBar;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.SwitchCompat;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import com.flin.online.jsonenter.PublicInfo;
import com.flinc.core.ISaveableFragment;
import com.flinc.core.NotificationDialogFragment;
import com.flinc.core.R;
import com.nvidia.devtech.NvEventQueueActivity;

import java.util.HashMap;

public class DialogClientSettingsCommonFragment extends Fragment implements ISaveableFragment {
    private SwitchCompat mSwitchKeyboard;
    private SwitchCompat mSwitchCutout;
    private SwitchCompat mSwitchFPSCounter;
    private SwitchCompat mSwitchOutfit;
    private SwitchCompat mSwitchHpArmour;
    private SwitchCompat mSwitchRadarrect;
    private SwitchCompat mSwitchPCMoney;
    private SwitchCompat mSwitchNameTag;
    private SwitchCompat mSwitchTexdD;
    private SwitchCompat mSwitchVoiceChat;
    private SwitchCompat mSwitchSky;
    private SwitchCompat mSwitchDialogNew;
    private SwitchCompat mSwitchButtonAttackNew;
    private SwitchCompat mSwitchCacheTextDraw;
    private SwitchCompat mSwitchButtonCrossHair;


    private NvEventQueueActivity mContext = null;

    private View mRootView = null;

    private boolean bChangeAllowed = true;

    private SeekBar.OnSeekBarChangeListener mListenerSeekBars;
    private HashMap<ViewGroup, Drawable> mOldDrawables;

    private ViewGroup mParentView = null;

    public static DialogClientSettingsCommonFragment createInstance(String txt)
    {
        DialogClientSettingsCommonFragment fragment = new DialogClientSettingsCommonFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(null);
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(null);
    }

    @Override
    public void onSaveInstanceState(final Bundle outState) {
        outState.putSerializable("android:support:fragments", null);
        super.onSaveInstanceState(outState);
        outState.putSerializable("android:support:fragments", null);
    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {



                mContext = (NvEventQueueActivity) getActivity();

        mRootView = inflater.inflate(R.layout.dialog_settings_common,container,false);

        mSwitchKeyboard = mRootView.findViewById(R.id.switch_android_keyboard);
        mSwitchCutout = mRootView.findViewById(R.id.switch_cutout);
        mSwitchFPSCounter = mRootView.findViewById(R.id.switch_fps_counter);
        mSwitchHpArmour = mRootView.findViewById(R.id.switch_info_bar);
        mSwitchOutfit = mRootView.findViewById(R.id.switch_nick_name);
        mSwitchRadarrect = mRootView.findViewById(R.id.switch_radar_rect);
        mSwitchPCMoney = mRootView.findViewById(R.id.switch_pc_money);
        mSwitchNameTag = mRootView.findViewById(R.id.switch_nick_name);
        mSwitchTexdD = mRootView.findViewById(R.id.switch_texd_d);
        mSwitchVoiceChat = mRootView.findViewById(R.id.switch_voice_chat);
        mSwitchSky = mRootView.findViewById(R.id.switch_sky);
        mSwitchDialogNew = mRootView.findViewById(R.id.switch_dialog_new);
       // mSwitchButtonAttackNew = mRootView.findViewById(R.id.switch_button_attack_new);
        mSwitchCacheTextDraw = mRootView.findViewById(R.id.switch_cache_textdraw);
        mSwitchButtonCrossHair = mRootView.findViewById(R.id.switch_button_crosshair);
        getValues();
        setSeekBarListeners();


        mSwitchCutout.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                mContext.setNativeCutoutSettings(b);

                DialogFragment newFragment = new NotificationDialogFragment();
                newFragment.show(mContext.getSupportFragmentManager(), "missiles");

            }
        });


        mSwitchNameTag.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                mContext.setNativeNameTag(b);
            }
        });

        mSwitchTexdD.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                mContext.setNative3DText(b);
            }
        });
        mSwitchVoiceChat.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                mContext.setNativeVoice(b);
            }
        });

        mSwitchSky.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                mContext.setNativeSkyBox (b);
            }
        });

        mSwitchDialogNew.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                mContext.setNativeDialogNew(b);
            }
        });

       /* mSwitchButtonAttackNew.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
              if(!mContext.setNativeButtonAttackNew(b)){
                    //mSwitchButtonAttackNew.setChecked(!b);
                }
            }
        });
*/

 /*
        mSwitchButtonCrossHair.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
               if(!mContext.setNativeCrossHair(b)){
                    mSwitchButtonCrossHair.setChecked(!b);

            }
        });
        }*/
        mSwitchCacheTextDraw.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                mContext.setNativeCacheTextDraw(b);
            }
        });

        mSwitchKeyboard.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                mContext.setNativeKeyboardSettings(b);
            }
        });

        mSwitchRadarrect.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                mContext.setNativeRadarrect(b);
            }
        });

        mSwitchPCMoney.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                mContext.setNativePcMoney(b);
            }
        });

        mSwitchOutfit.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                mContext.setNativeOutfitGunsSettings(b);
            }
        });

        mSwitchHpArmour.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                mContext.setNativeHpArmourText(b);
            }
        });

        mSwitchFPSCounter.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                mContext.setNativeFpsCounterSettings(b);
            }
        });

        return mRootView;
    }

    private void setSeekBarListeners()
    {
        mListenerSeekBars = new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                if(bChangeAllowed)
                {
                    passValuesToNative();
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar)
            {
                mContext.onSettingsWindowSave();
            }
        };

        for(int i = DialogClientSettings.mSettingsComonStart; i < DialogClientSettings.mSettingsComonEnd; i++)
        {
            String widgetX = "hud_element_pos_x_" + i;
            String widgetY = "hud_element_pos_y_" + i;

            int resIDX = mContext.getResources().getIdentifier(widgetX, "id", mContext.getPackageName());
            int resIDY = mContext.getResources().getIdentifier(widgetY, "id", mContext.getPackageName());

            SeekBar barX = mRootView.findViewById(resIDX);
            SeekBar barY = mRootView.findViewById(resIDY);

            if(barX != null)
            {
                barX.setOnSeekBarChangeListener(mListenerSeekBars);
            }
            if(barY != null)
            {
                barY.setOnSeekBarChangeListener(mListenerSeekBars);
            }
        }

        for(int i = DialogClientSettings.mSettingsComonStart; i < DialogClientSettings.mSettingsComonEnd; i++)
        {
            String widgetX = "hud_element_scale_x_" + i;
            String widgetY = "hud_element_scale_y_" + i;

            int resIDX = mContext.getResources().getIdentifier(widgetX, "id", mContext.getPackageName());
            int resIDY = mContext.getResources().getIdentifier(widgetY, "id", mContext.getPackageName());

            SeekBar barX = mRootView.findViewById(resIDX);
            SeekBar barY = mRootView.findViewById(resIDY);

            if(barX != null)
            {
                barX.setOnSeekBarChangeListener(mListenerSeekBars);
            }
            if(barY != null)
            {
                barY.setOnSeekBarChangeListener(mListenerSeekBars);
            }
        }
    }

    @Override
    public void save() {

    }

    @Override
    public void getValues() {
        mSwitchKeyboard.setChecked(mContext.getNativeKeyboardSettings());
        mSwitchCutout.setChecked(mContext.getNativeCutoutSettings());
        mSwitchFPSCounter.setChecked(mContext.getNativeFpsCounterSettings());
        mSwitchHpArmour.setChecked(mContext.getNativeHpArmourText());
        mSwitchOutfit.setChecked(mContext.getNativeOutfitGunsSettings());
        mSwitchPCMoney.setChecked(mContext.getNativePcMoney());
        mSwitchRadarrect.setChecked(mContext.getNativeRadarrect());
  //      mSwitchSkyBox.setChecked(mContext.getNativeSkyBox());
        mSwitchNameTag.setChecked(mContext.getNativeNameTag());
        mSwitchTexdD.setChecked(mContext.getNative3DText());
        mSwitchVoiceChat.setChecked(mContext.getNativeVoice());
        mSwitchSky.setChecked(mContext.getNativeSkyBox());
        mSwitchDialogNew.setChecked(mContext.getNativeDialogNew());
        //mSwitchButtonAttackNew.setChecked(mContext.getNativeButtonAttackNew());
        mSwitchCacheTextDraw.setChecked(mContext.getNativeCacheTextDraw());
       // mSwitchButtonCrossHair.setChecked(mContext.getNativeCrossHair());


        bChangeAllowed = false;
        for(int i = DialogClientSettings.mSettingsComonStart; i < DialogClientSettings.mSettingsComonEnd; i++)
        {
            String widgetX = "hud_element_pos_x_" + i;
            String widgetY = "hud_element_pos_y_" + i;
            String widgetScale = "hud_element_scale_x_" + i;

            int resIDX = mContext.getResources().getIdentifier(widgetX, "id", mContext.getPackageName());
            int resIDY = mContext.getResources().getIdentifier(widgetY, "id", mContext.getPackageName());

            SeekBar barX = mRootView.findViewById(resIDX);
            SeekBar barY = mRootView.findViewById(resIDY);

            int[] pos = mContext.getNativeHudElementPosition(i);

            if(pos[0] == -1)
            {
                pos[0] = 1;
            }
            if(pos[1] == -1)
            {
                pos[1] = 1;
            }

            if(barX != null)
            {
                barX.setProgress(pos[0]);
            }
            if(barY != null)
            {
                barY.setProgress(pos[1]);
            }
        }

        for(int i = DialogClientSettings.mSettingsComonStart; i < DialogClientSettings.mSettingsComonEnd; i++)
        {
            String widgetX = "hud_element_scale_x_" + i;
            String widgetY = "hud_element_scale_y_" + i;

            int resIDX = mContext.getResources().getIdentifier(widgetX, "id", mContext.getPackageName());
            int resIDY = mContext.getResources().getIdentifier(widgetY, "id", mContext.getPackageName());

            SeekBar barX = mRootView.findViewById(resIDX);
            SeekBar barY = mRootView.findViewById(resIDY);

            int[] pos = mContext.getNativeHudElementScale(i);

            if(pos[0] == -1)
            {
                pos[0] = 1;
            }
            if(pos[1] == -1)
            {
                pos[1] = 1;
            }

            if(barX != null && pos[0] != -1)
            {
                barX.setProgress(pos[0]);
            }
            if(barY != null && pos[1] != -1)
            {
                barY.setProgress(pos[1]);
            }
        }

        bChangeAllowed = true;
    }
    public void passValuesToNative() {
        for (int i = DialogClientSettings.mSettingsComonStart; i < DialogClientSettings.mSettingsComonEnd; i++) {
            String widgetX = "hud_element_pos_x_" + i;
            String widgetY = "hud_element_pos_y_" + i;

            int resIDX = mContext.getResources().getIdentifier(widgetX, "id", mContext.getPackageName());
            int resIDY = mContext.getResources().getIdentifier(widgetY, "id", mContext.getPackageName());

            SeekBar barX = mRootView.findViewById(resIDX);
            SeekBar barY = mRootView.findViewById(resIDY);
            int x = -1;
            int y = -1;
            if (barX != null) {
                x = barX.getProgress();
            }
            if (barY != null) {
                y = barY.getProgress();
            }

            mContext.setNativeHudElementPosition(i, x, y);
        }

        for (int i = DialogClientSettings.mSettingsComonStart; i < DialogClientSettings.mSettingsComonEnd; i++) {
            String widgetX = "hud_element_scale_x_" + i;
            String widgetY = "hud_element_scale_y_" + i;

            int resIDX = mContext.getResources().getIdentifier(widgetX, "id", mContext.getPackageName());
            int resIDY = mContext.getResources().getIdentifier(widgetY, "id", mContext.getPackageName());

            SeekBar barX = mRootView.findViewById(resIDX);
            SeekBar barY = mRootView.findViewById(resIDY);
            int x = -1;
            int y = -1;
            if (barX != null) {
                x = barX.getProgress();
            }
            if (barY != null) {
                y = barY.getProgress();
            }

            mContext.setNativeHudElementScale(i, x, y);
        }
    }
}
