package com.example.nativex;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.net.VpnService;
import android.os.Build;
import android.os.ParcelFileDescriptor;

import androidx.core.app.NotificationCompat;

import java.io.File;

public class SimpleVpnService extends VpnService {

    private static final String CH_ID = "vpn";
    private ParcelFileDescriptor tun;
    private Process xrayProc, tun2Proc;

    @Override public int onStartCommand(Intent intent, int flags, int startId) {
        startForegroundNoti();

        String vless = intent.getStringExtra("vless");
        try {
            // 1) آماده‌کردن باینری‌ها از assets/bin → files/bin و chmod 700
            File binDir = new File(getFilesDir(), "bin");
            ProcessUtils.copyAssetDir(this, "bin", binDir);
            ProcessUtils.chmodExecutable(new File(binDir, "xray"));
            ProcessUtils.chmodExecutable(new File(binDir, "tun2socks"));

            // 2) ساخت کانفیگ Xray از VLESS
            File cfg = new File(getFilesDir(), "xray.json");
            XrayConfigGenerator.writeConfigFromVless(vless, cfg);

            // 3) ساخت TUN
            Builder b = new Builder();
            b.addAddress("10.10.0.1", 24);
            b.addDnsServer("1.1.1.1");
            b.addRoute("0.0.0.0", 0);
            b.setSession("NativeHello");
            tun = b.establish();

            // 4) اجرای xray
            xrayProc = new ProcessBuilder(binDir.getAbsolutePath()+"/xray", "-config", cfg.getAbsolutePath())
                    .redirectErrorStream(true).start();

            // 5) اجرای tun2socks (ساده؛ ورودی/خروجی: 10808 -> TUN)
            int tunFd = tun.getFd();
            tun2Proc = new ProcessBuilder(
                    binDir.getAbsolutePath()+"/tun2socks",
                    "--netif-ipaddr", "10.10.0.2",
                    "--socks-server-addr", "127.0.0.1:10808",
                    "--tunfd", String.valueOf(tunFd),
                    "--tunmtu", "1500",
                    "--loglevel", "info")
                    .redirectErrorStream(true).start();

        } catch (Exception e) {
            e.printStackTrace();
            stopSelf();
        }
        return START_STICKY;
    }

    private void startForegroundNoti() {
        if (Build.VERSION.SDK_INT >= 26) {
            NotificationChannel ch = new NotificationChannel(CH_ID, "VPN", NotificationManager.IMPORTANCE_LOW);
            ((NotificationManager)getSystemService(NOTIFICATION_SERVICE)).createNotificationChannel(ch);
        }
        Notification n = new NotificationCompat.Builder(this, CH_ID)
                .setContentTitle("NativeHello VPN")
                .setContentText("Running…")
                .setSmallIcon(android.R.drawable.stat_sys_download_done)
                .build();
        startForeground(1, n);
    }

    @Override public void onDestroy() {
        super.onDestroy();
        try { if (tun2Proc != null) tun2Proc.destroy(); } catch (Exception ignored) {}
        try { if (xrayProc != null) xrayProc.destroy(); } catch (Exception ignored) {}
        try { if (tun != null) tun.close(); } catch (Exception ignored) {}
    }
}
