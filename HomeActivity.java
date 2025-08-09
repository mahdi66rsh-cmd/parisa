package com.example.nativex;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

public class HomeActivity extends AppCompatActivity {
    private TextView tvStatus;

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        tvStatus = findViewById(R.id.tvStatus);
        Button btnConnect = findViewById(R.id.btnConnect);

        btnConnect.setOnClickListener(v -> {
            try {
                // باینری‌ها را از assets کپی و executable کن

                // سرویس را به صورت Foreground استارت کن (فعلاً دمو)
                Intent i = new Intent(this, SimpleVpnService.class);
                ContextCompat.startForegroundService(this, i);

                tvStatus.setText("Service started (demo)");
            } catch (Throwable t) {
                Log.e("VPN", "start failed", t);
                tvStatus.setText("خطا: " + t.getMessage());
                Toast.makeText(this, "خطا: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }
}
