package com.example.nativex;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {

    private static final Map<String,String> USERS = new HashMap<>();
    static {
        USERS.put("user1", "pass1");
        USERS.put("user2", "pass2");
        USERS.put("user3", "pass3");
        USERS.put("user4", "pass4");
        USERS.put("user5", "pass5");
        USERS.put("user6", "pass6");
        USERS.put("user7", "pass7");
        USERS.put("user8", "pass8");
        USERS.put("user9", "pass9");
        USERS.put("user10", "pass10");
    }

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        EditText etUser = findViewById(R.id.etUser);
        EditText etPass = findViewById(R.id.etPass);
        Button btnLogin = findViewById(R.id.btnLogin);

        btnLogin.setOnClickListener(v -> {
            String u = etUser.getText().toString().trim();
            String p = etPass.getText().toString().trim();
            if (USERS.containsKey(u) && p.equals(USERS.get(u))) {
                startActivity(new Intent(this, HomeActivity.class));
                finish();
            } else {
                Toast.makeText(this, "نام کاربری/رمز نادرست است", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
