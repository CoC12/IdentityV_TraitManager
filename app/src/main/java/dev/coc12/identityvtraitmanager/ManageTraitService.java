package dev.coc12.identityvtraitmanager;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Build;
import android.os.IBinder;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;

import androidx.annotation.RequiresApi;
import androidx.core.content.ContextCompat;

import java.util.ArrayList;
import java.util.List;

public class ManageTraitService extends Service {

    public static boolean isActive = false;
    private Context context;
    private LinearLayout serviceLinearLayout;
    private WindowManager windowManager;
    private final List<CountdownCoolTime> countdownCoolTimes = new ArrayList<>();

    public ManageTraitService() {
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        context = getApplicationContext();
        isActive = true;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            makeForeground();
        }
        buildView();
        return super.onStartCommand(intent, flags, startId);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void makeForeground() {
        String channelId = "default";
        String title = context.getString(R.string.app_name);

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        if (notificationManager == null) {
            return;
        }
        NotificationChannel notificationChannel = new NotificationChannel(
                channelId, title, NotificationManager.IMPORTANCE_DEFAULT);
        notificationManager.createNotificationChannel(notificationChannel);
        Notification notification = new Notification.Builder(context, channelId)
                .setContentTitle(title)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentText(getString(R.string.runningService))
                .setAutoCancel(true)
                .build();
        startForeground(1, notification);
    }

    private void buildView() {
        final int[] iconDrawables = Constants.TRAIT_ICONS;
        final int[] openingTimes = Constants.TRAIT_OPENING_TIMES;
        final int[] coolTimes = Constants.TRAIT_COOL_TIMES;
        int typeLayer;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            typeLayer = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
        } else {
            typeLayer = WindowManager.LayoutParams.TYPE_PHONE;
        }

        serviceLinearLayout = new LinearLayout(context);
        serviceLinearLayout.setOrientation(LinearLayout.HORIZONTAL);

        View startIcon = buildIconView(R.drawable.ic_start_count, 0, 0);
        startIcon.setOnClickListener(view -> startAllCountdown(countdownCoolTimes));
        serviceLinearLayout.addView(startIcon);
        for (int i = 0; i < iconDrawables.length; i++) {
            serviceLinearLayout.addView(buildIconView(iconDrawables[i], openingTimes[i], coolTimes[i]));
        }
        View closeIcon = buildIconView(R.drawable.ic_close_service, 0, 0);
        closeIcon.setOnClickListener(view -> stopSelf());
        serviceLinearLayout.addView(closeIcon);

        windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        WindowManager.LayoutParams params = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                typeLayer,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                        | WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL,
                PixelFormat.TRANSLUCENT);
        params.gravity=  Gravity.TOP | Gravity.START;
        windowManager.addView(serviceLinearLayout, params);
    }

    private View buildIconView(int traitIcon, int openingTime, int coolTime) {
        final ViewGroup nullParent = null;

        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View iconView = layoutInflater.inflate(R.layout.service_manage_trait, nullParent);
        iconView.findViewById(R.id.icon)
                .setBackground(ContextCompat.getDrawable(context, traitIcon));
        CountdownCoolTime countdownCoolTime = new CountdownCoolTime(openingTime, iconView.findViewById(R.id.countdownText));
        countdownCoolTimes.add(countdownCoolTime);
        iconView.setOnClickListener(view -> countdownCoolTime.startCountdown(coolTime));
        return iconView;
    }

    private void startAllCountdown(List<CountdownCoolTime> countdownCoolTimes) {
        for (CountdownCoolTime countdownCoolTime : countdownCoolTimes) {
            countdownCoolTime.startOpeningCountdown();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        windowManager.removeView(serviceLinearLayout);
        isActive = false;
        Intent broadcastIntent = new Intent();
        broadcastIntent.setAction(Constants.SERVICE_STOPPED_ACTION);
        getBaseContext().sendBroadcast(broadcastIntent);
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}