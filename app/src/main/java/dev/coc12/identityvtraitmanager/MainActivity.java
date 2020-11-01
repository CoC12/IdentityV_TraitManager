package dev.coc12.identityvtraitmanager;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;

import com.google.android.material.switchmaterial.SwitchMaterial;

public class MainActivity extends AppCompatActivity {

    private final Context context = this;
    public static SwitchMaterial startServiceSwitch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setView();
        registerReceiver();
    }

    private void setView() {
        startServiceSwitch = findViewById(R.id.startServiceSwitch);
        startServiceSwitch.setChecked(ManageTraitService.isActive);
        startServiceSwitch.setOnClickListener(startServiceSwitchListener);
    }

    final private View.OnClickListener startServiceSwitchListener = view -> {
        Intent serviceIntent = new Intent(getApplication(), ManageTraitService.class);
        if (!startServiceSwitch.isChecked()) {
            stopService(serviceIntent);
            return;
        }
        if (!hasPermission()) {
            startServiceSwitch.setChecked(false);
            Intent systemSettingIntent = new Intent(
                    Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                    Uri.parse(String.format(Constants.URI_PACKAGE_FORMAT, getPackageName()))
            );
            startActivity(systemSettingIntent);
            return;
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startForegroundService(serviceIntent);
            return;
        }
        startService(serviceIntent);
    };

    private boolean hasPermission() {
        return Settings.canDrawOverlays(context);
    }

    private void registerReceiver() {
        ServiceStoppedReceiver receiver = new ServiceStoppedReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Constants.SERVICE_STOPPED_ACTION);
        registerReceiver(receiver, intentFilter);
    }
}