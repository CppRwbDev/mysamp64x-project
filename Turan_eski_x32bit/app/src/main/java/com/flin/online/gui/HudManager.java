package com.flin.online.gui;

import android.app.Activity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ToggleButton;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.flin.online.Preferences;
import com.flin.online.gui.util.SeekArc;
import com.flin.online.gui.util.Utils;
import com.flinc.core.R;
import com.nvidia.devtech.NvEventQueueActivity;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Formatter;
import java.util.Timer;
import java.util.TimerTask;

public class HudManager {
    public Activity activity;

    public Animation animation, hideanim, showanim;
    public ConstraintLayout hud_layout, hud_online, brp_hud_date, brp_hud_time;
    public LinearLayout hud_ammo_layout, brp_hud_logo;

    //public ProgressBar hud_health, hud_hunger, hud_armour;
    public SeekArc hud_health, hud_hunger, hud_armour;

    public TextView hud_health_t, hud_hunger_t, hud_armour_t, hud_money;
    public TextView hud_time, hud_date, hud_online_text;

    public ArrayList<ImageView> hud_wanted;
    public ImageView hud_weapon;

    public ImageView hud_alt, hud_y, hud_n, hud_h, hud_x2, hud_seat;
    public ToggleButton hud_sr;

    public static Boolean hud_hide_data_time = false;
    public static Boolean hud_b_sr = false;
    public static Boolean hud_hide_logo = false;
    public static Boolean bt_is_ctrl = false;

    public HudManager(Activity aactivity) {
        activity = aactivity;

        Animation animation = AnimationUtils.loadAnimation(aactivity, R.anim.button_click);
        Animation hideanim = AnimationUtils.loadAnimation(aactivity, R.anim.popup_hide_notification);
        Animation showanim = AnimationUtils.loadAnimation(aactivity, R.anim.popup_show_notification);

        hud_layout = aactivity.findViewById(R.id.bhud_main);
        hud_layout.setVisibility(View.GONE);
        hud_online = aactivity.findViewById(R.id.brp_hud_online);

        brp_hud_logo = aactivity.findViewById(R.id.brp_hud_logo);
        brp_hud_time = aactivity.findViewById(R.id.brp_hud_time);
        brp_hud_date = aactivity.findViewById(R.id.brp_hud_date);

        hud_x2 = aactivity.findViewById(R.id.imageView);
        if(Preferences.GetX2Status() == true){
            hud_x2.setVisibility(View.VISIBLE);
        } else {
           // hud_x2.setVisibility(View.GONE);
        }

        hud_health = aactivity.findViewById(R.id.hud_health_pb);
        hud_hunger = aactivity.findViewById(R.id.hud_eat_pb);
        hud_armour = aactivity.findViewById(R.id.hud_armour_pb);

        hud_health_t = aactivity.findViewById(R.id.hud_health_text);
        hud_hunger_t = aactivity.findViewById(R.id.hud_eat_text);
        hud_armour_t = aactivity.findViewById(R.id.hud_armour_text);

        hud_money = aactivity.findViewById(R.id.hud_balance_text);
        hud_weapon = aactivity.findViewById(R.id.hud_fist_icon);
        hud_ammo_layout = aactivity.findViewById(R.id.hud_ammo_layout);

        hud_time = aactivity.findViewById(R.id.hud_time_text);
        hud_date = aactivity.findViewById(R.id.hud_date_text);
        hud_online_text = aactivity.findViewById(R.id.hud_online_text);

        hud_wanted = new ArrayList<>();
        hud_wanted.add(activity.findViewById(R.id.hud_star_1));
        hud_wanted.add(activity.findViewById(R.id.hud_star_2));
        hud_wanted.add(activity.findViewById(R.id.hud_star_3));
        hud_wanted.add(activity.findViewById(R.id.hud_star_4));
        hud_wanted.add(activity.findViewById(R.id.hud_star_5));

        hud_sr = aactivity.findViewById(R.id.toggleButton);

        hud_seat = aactivity.findViewById(R.id.imageView2);
        hud_seat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view.startAnimation(animation);
                NvEventQueueActivity.getInstance().sendG();
            }
        });

        hud_y = aactivity.findViewById(R.id.imageView6);
        hud_y.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view.startAnimation(animation);
                hideAllButt();
                NvEventQueueActivity.getInstance().sendY();
            }
        });

        hud_n = aactivity.findViewById(R.id.imageView8);
        hud_n.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view.startAnimation(animation);
                hideAllButt();
                NvEventQueueActivity.getInstance().sendN();
            }
        });

        hud_h = aactivity.findViewById(R.id.imageView9);
        hud_h.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view.startAnimation(animation);
                hideAllButt();
                NvEventQueueActivity.getInstance().sendH();
            }
        });

        hud_alt = aactivity.findViewById(R.id.imageView4);
        hud_alt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view.startAnimation(animation);

                if(bt_is_ctrl == false) {
                    NvEventQueueActivity.getInstance().sendALT();
                } else NvEventQueueActivity.getInstance().sendCTRL();
            }
        });

        hud_online.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view.startAnimation(animation);
                openTab();
            }
        });
    }

    public void UpdateHudInfo(int health, int armour, int hunger, int weaponid, int ammo, int playerid, int money, int wanted)
    {
        hud_health.setProgress(health);
        hud_armour.setProgress(armour);

        // fix random eat progress
        if(hunger > 100) {
            hud_hunger.setProgress(100);
            hud_hunger_t.setText("100");
        } else if(hunger < 0) {
            hud_hunger.setProgress(0);
            hud_hunger_t.setText("0");
        } else {
            hud_hunger.setProgress(hunger);
            hud_hunger_t.setText(Integer.toString(hunger));
        }

        // fix 0/0 showing
        if(weaponid == 0) {
            hud_ammo_layout.setVisibility(View.INVISIBLE);
        } else hud_ammo_layout.setVisibility(View.VISIBLE);

        hud_health_t.setText(Integer.toString(health));
        hud_armour_t.setText(Integer.toString(armour));

        hud_online_text.setText(Integer.toString(playerid));

        DecimalFormat formatter = new DecimalFormat();
        DecimalFormatSymbols symbols = DecimalFormatSymbols.getInstance();
        symbols.setGroupingSeparator(' ');
        formatter.setDecimalFormatSymbols(symbols);
        String s = formatter.format(money).toString();
        hud_money.setText(String.valueOf(s));

        int id = activity.getResources().getIdentifier(new Formatter().format("weapon_%d", Integer.valueOf(weaponid)).toString(), "drawable", activity.getPackageName());
        hud_weapon.setImageResource(id);

        hud_weapon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NvEventQueueActivity.getInstance().onWeaponChanged();
            }
        });
        if(wanted > 5) wanted = 5;
        for (int i2 = 0; i2 < wanted; i2++) {
            hud_wanted.get(i2).setBackgroundResource(R.drawable.ic_y_star);
        }
        hud_time.setText(new SimpleDateFormat("HH:mm").format(Calendar.getInstance().getTime()));
        hud_date.setText(new SimpleDateFormat("dd.MM.yyyy").format(Calendar.getInstance().getTime()));
    }

    private void openTab()
    {
        Timer t = new Timer();
        t.schedule(new TimerTask(){
            @Override
            public void run() {
                NvEventQueueActivity.getInstance().showTab();
            }
        }, 200L);
    }

    public void showAllButt()
    {
            Utils.ShowLayout(hud_y, true);
            Utils.ShowLayout(hud_n, true);
            Utils.ShowLayout(hud_h, true);
    }

    public void hideAllButt()
    {
            hud_sr.setChecked(false);
            Utils.HideLayout(hud_y, true);
            Utils.HideLayout(hud_n, true);
            Utils.HideLayout(hud_h, true);
    }

    public void ShowHud() {
        HideVehButtonG();
        hideAllButt();
        hud_sr.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (buttonView.isChecked()) {
                    hud_b_sr = true;
                    showAllButt();
                } else {
                    hideAllButt();
                    hud_b_sr = false;
                }
            }
        });
        Utils.ShowLayout(hud_layout, true);
    }

    public void ShowVehButton() {
        hud_alt.setImageResource(R.drawable.b_ctrl);
        bt_is_ctrl = true;
    }

    public void HideVehButton() {
        bt_is_ctrl = false;
        hud_alt.setImageResource(R.drawable.b_alt);
    }

    public void ShowTabButton() {
        ShowHud();
        Utils.ShowLayout(hud_online, true);
    }

    public void HideTabButton() {
        HideHud();
        Utils.HideLayout(hud_online, true);
    }

    public void ShowVehButtonG() {
        hud_seat.setVisibility(View.VISIBLE);
    }

    public void HideVehButtonG() {
        hud_seat.setVisibility(View.GONE);
    }

    public void ShowLogo(boolean isShow) {
        if(isShow == true) {
            hud_hide_logo = false;
            Utils.ShowLayout(hud_x2, true);
            Utils.ShowLayout(hud_online, true);
            Utils.ShowLayout(brp_hud_logo, true);
        } else HideLogo();
    }

    public void ShowTimeData() {
        hud_hide_data_time = true;
        Utils.ShowLayout(brp_hud_date, true);
        Utils.ShowLayout(brp_hud_time, true);
    }

    public void HideTimeData() {
        hud_hide_data_time = false;
        Utils.HideLayout(brp_hud_date, true);
        Utils.HideLayout(brp_hud_time, true);
    }

    public void HideLogo() {
        hud_hide_logo = false;
        Utils.HideLayout(hud_x2, true);
        Utils.HideLayout(hud_online, true);
        Utils.HideLayout(brp_hud_logo, true);
    }

    public void HideHud() {
        Utils.HideLayout(hud_layout, true);
    }
}
