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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import androidx.annotation.RequiresApi;

public class ManageTraitService extends Service {

    public static boolean isActive = false;
    private Context context;
    private View traitView;
    private WindowManager windowManager;

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
        final ViewGroup nullParent = null;
        int typeLayer;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            typeLayer = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
        } else {
            typeLayer = WindowManager.LayoutParams.TYPE_PHONE;
        }

        LayoutInflater layoutInflater = LayoutInflater.from(context);
        traitView = layoutInflater.inflate(R.layout.service_manage_trait, nullParent);
        traitView.setOnClickListener(view -> stopSelf());

        windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        WindowManager.LayoutParams params = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                typeLayer,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                        | WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL,
                PixelFormat.TRANSLUCENT);
        windowManager.addView(traitView, params);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        windowManager.removeView(traitView);
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