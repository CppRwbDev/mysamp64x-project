package com.flin.online;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import com.yandex.metrica.push.YandexMetricaPush;
public class SilentPushReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        // Extract push message payload from your push message.
        String payload = intent.getStringExtra(YandexMetricaPush.EXTRA_PAYLOAD);
    }
}