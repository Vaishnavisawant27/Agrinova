package com.example.agreenova;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.InputType;
import android.view.MotionEvent;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Calendar;

public class RegistrationActivity extends AppCompatActivity {

    EditText etDob, etPassword, etUsername;
    RadioGroup radioGroup;
    String selectedGender = "";
    SharedPreferences sp;
    private String username;
    private String password;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(
                LocaleHelper.setLocale(
                        newBase,
                        LocaleHelper.getSavedLanguage(newBase)
                )
        );
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        initViews();
        setupGenderSelection();
        setupDatePicker();
        setupPasswordToggle();
        setupRegisterButton();
    }

    private void setupRegisterButton() {
    }

    // =============================
    // Initialize Views
    // =============================
    private void initViews() {
        etDob = findViewById(R.id.etDob);
        etPassword = findViewById(R.id.etPassword);
        etUsername = findViewById(R.id.etUsername);
        radioGroup = findViewById(R.id.radioGender);
        Button btnRegister = findViewById(R.id.btnRegister);

        sp = getSharedPreferences("UserData", MODE_PRIVATE);

        btnRegister.setOnClickListener(v -> registerUser());

        SharedPreferences sp = getSharedPreferences("UserData", MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("username", username);
        editor.putString("password", password);
        editor.apply();
    }

    // =============================
    // Gender Selection
    // =============================
    private void setupGenderSelection() {
        radioGroup.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId != -1) {
                RadioButton selected = findViewById(checkedId);
                selectedGender = selected.getText().toString();
            }
        });
    }

    // =============================
    // Date Picker
    // =============================
    private void setupDatePicker() {
        etDob.setOnClickListener(v -> {
            Calendar calendar = Calendar.getInstance();

            DatePickerDialog datePicker = new DatePickerDialog(
                    RegistrationActivity.this,
                    (view, year, month, dayOfMonth) -> {
                        String date = dayOfMonth + "/" + (month + 1) + "/" + year;
                        etDob.setText(date);
                    },
                    calendar.get(Calendar.YEAR),
                    calendar.get(Calendar.MONTH),
                    calendar.get(Calendar.DAY_OF_MONTH)
            );

            datePicker.show();
        });
    }

    // =============================
    // Password Show / Hide
    // =============================
    @SuppressLint("ClickableViewAccessibility")
    private void setupPasswordToggle() {
        etPassword.setOnTouchListener((v, event) -> {
            if (event.getAction() == MotionEvent.ACTION_UP) {

                if (etPassword.getCompoundDrawables()[2] != null &&
                        event.getRawX() >=
                                (etPassword.getRight() -
                                        etPassword.getCompoundDrawables()[2]
                                                .getBounds().width())) {

                    if ((etPassword.getInputType() &
                            InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD)
                            == InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD) {

                        etPassword.setInputType(
                                InputType.TYPE_CLASS_TEXT |
                                        InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    } else {

                        etPassword.setInputType(
                                InputType.TYPE_CLASS_TEXT |
                                        InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                    }

                    etPassword.setSelection(etPassword.getText().length());
                    return true;
                }
            }
            return false;
        });
    }

    // =============================
    // Register Logic
    // =============================
    private void registerUser() {

        String username = etUsername.getText().toString().trim();
        String password = etPassword.getText().toString().trim();
        String dob = etDob.getText().toString().trim();

        if (username.isEmpty()) {
            etUsername.setError("Enter username");
            etUsername.requestFocus();
        }
        else if (password.length() < 6) {
            etPassword.setError("Password must be at least 6 characters");
            etPassword.requestFocus();
        }
        else if (dob.isEmpty()) {
            etDob.setError("Select Date of Birth");
            etDob.requestFocus();
        }
        else if (selectedGender.isEmpty()) {
            Toast.makeText(this,
                    "Please select gender",
                    Toast.LENGTH_SHORT).show();
        }
        else {

            SharedPreferences.Editor editor = sp.edit();
            editor.putString("username", username);
            editor.putString("password", password);
            editor.putString("dob", dob);
            editor.putString("gender", selectedGender);
            editor.putBoolean("isLoggedIn", false);
            editor.apply();

            Toast.makeText(this,
                    "Registration Successful",
                    Toast.LENGTH_LONG).show();

            startActivity(new Intent(this, LoginActivity.class));
            finish();
        }
    }
}