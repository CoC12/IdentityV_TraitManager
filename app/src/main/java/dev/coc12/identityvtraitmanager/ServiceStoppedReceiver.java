package dev.coc12.identityvtraitmanager;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class ServiceStoppedReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        MainActivity.startServiceSwitch.setChecked(false);
    }
}