package com.example.agreenova;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.*;

import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {

    EditText etLoginUsername, etLoginPassword;
    Button btnLogin;
    TextView txtRegister;
    SharedPreferences sp;

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
        setContentView(R.layout.activity_login);

        etLoginUsername = findViewById(R.id.etLoginUsername);
        etLoginPassword = findViewById(R.id.etLoginPassword);
        btnLogin = findViewById(R.id.btnLogin);
        txtRegister = findViewById(R.id.txtRegister);

        sp = getSharedPreferences("UserData", MODE_PRIVATE);

        btnLogin.setOnClickListener(v -> {

            String username = etLoginUsername.getText().toString().trim();
            String password = etLoginPassword.getText().toString().trim();

            if (username.isEmpty() || password.isEmpty()) {
                Toast.makeText(LoginActivity.this,
                        "Enter username & password",
                        Toast.LENGTH_SHORT).show();
                return;
            }

            String savedUsername = sp.getString("username", null);
            String savedPassword = sp.getString("password", null);

            if (savedPassword != null &&
                    username.equals(savedUsername) &&
                    password.equals(savedPassword)) {

                Toast.makeText(LoginActivity.this,
                        "Login Successful",
                        Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                startActivity(intent);
                finish();

            } else {
                Toast.makeText(LoginActivity.this,
                        "Invalid Username or Password",
                        Toast.LENGTH_SHORT).show();
            }
        });

        txtRegister.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, RegistrationActivity.class);
            startActivity(intent);
        });
    }
}