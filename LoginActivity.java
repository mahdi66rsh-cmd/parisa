package com.example.nativex;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {

    // 10 کاربر ثابت
    private static final String[][] USERS = new String[][]{
            {"user1", "pass1"}, {"user2", "pass2"}, {"user3", "pass3"},
            {"user4", "pass4"}, {"user5", "pass5"}, {"user6", "pass6"},
            {"user7", "pass7"}, {"user8", "pass8"}, {"user9", "pass9"},
            {"user10", "pass10"}
    };

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        EditText etUser = findViewById(R.id.etUser);
        EditText etPass = findViewById(R.id.etPass);
        Button btnLogin = findViewById(R.id.btnLogin);

        btnLogin.setOnClickListener(v -> {
            String u = etUser.getText().toString().trim();
            String p = etPass.getText().toString().trim();
            boolean ok = false;
            for (String[] pair : USERS)
                if (pair[0].equals(u) && pair[1].equals(p)) { ok = true; break; }

            if (ok) {
                startActivity(new Intent(this, HomeActivity.class));
                finish();
            } else {
                Toast.makeText(this, "نام کاربری/رمز اشتباه است", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
