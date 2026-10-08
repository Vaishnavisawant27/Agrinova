package com.example.agreenova;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(
                LocaleHelper.setLocale(
                        newBase,
                        LocaleHelper.getSavedLanguage(newBase)
                )
        );
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.btnEnglish).setOnClickListener(v -> {
            changeLanguage("en");
        });

        findViewById(R.id.btnMarathi).setOnClickListener(v -> {
            changeLanguage("mr");
        });

        findViewById(R.id.btnHindi).setOnClickListener(v -> {
            changeLanguage("hi");
        });
    }

    private void changeLanguage(String langCode) {

        // Apply Language
        LocaleHelper.setLocale(this, langCode);

        // Go to Login (clear back stack)
        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
}