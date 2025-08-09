package com.example.nativex;

import android.content.Intent;
import android.net.VpnService;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class HomeActivity extends AppCompatActivity {

    private TextView tvStatus;

    // لینک VLESS که دادی:
    private static final String VLESS_URI =
            "vless://e3075864-27df-4d15-b181-9c94b0e6a53c@216.9.224.59:443"
            + "?path=%2FvTlFuPRGGIPbezDZVgy1yX&security=tls&alpn=http%2F1.1"
            + "&encryption=none&host=onlinebazikon.ir&fp=chrome&type=httpupgrade"
            + "&sni=onlinebazikon.ir#onlinebazikon.ir%20tls%20httpupgrade%20direct%20vless";

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        tvStatus = findViewById(R.id.tvStatus);
        Button btnConnect = findViewById(R.id.btnConnect);
        Button btnDisconnect = findViewById(R.id.btnDisconnect);

        btnConnect.setOnClickListener(v -> startVpn());
        btnDisconnect.setOnClickListener(v -> stopService(new Intent(this, SimpleVpnService.class)));
    }

    private void startVpn() {
        Intent prepare = VpnService.prepare(this);
        if (prepare != null) {
            startActivityForResult(prepare, 1001);
        } else {
            onActivityResult(1001, RESULT_OK, null);
        }
    }

    @Override protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1001 && resultCode == RESULT_OK) {
            Intent i = new Intent(this, SimpleVpnService.class);
            i.putExtra("vless", VLESS_URI);
            startService(i);
            tvStatus.setText("Connecting…");
        }
    }
}
