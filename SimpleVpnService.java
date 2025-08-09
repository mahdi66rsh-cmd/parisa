package com.example.nativex;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.os.Build;

import androidx.core.app.NotificationCompat;

import android.net.VpnService;

public class SimpleVpnService extends VpnService {

    @Override public void onCreate() {
        super.onCreate();
        startAsForeground();
    }

    private void startAsForeground() {
        String chId = "vpn";
        if (Build.VERSION.SDK_INT >= 26) {
            NotificationChannel ch = new NotificationChannel(
                    chId, "VPN", NotificationManager.IMPORTANCE_LOW);
            getSystemService(NotificationManager.class).createNotificationChannel(ch);
        }
        Notification n = new NotificationCompat.Builder(this, chId)
                .setContentTitle("VPN running")
                .setSmallIcon(R.mipmap.ic_launcher)
                .build();
        startForeground(1, n);
    }

    @Override public int onStartCommand(Intent intent, int flags, int startId) {
        // اینجا بعداً اجرای xray/tun2socks رو اضافه می‌کنیم
        return START_STICKY;
    }

    @Override public void onDestroy() {
        stopForeground(true);
        super.onDestroy();
    }
}
