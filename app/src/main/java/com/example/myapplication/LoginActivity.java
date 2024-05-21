package com.example.myapplication;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import androidx.appcompat.app.AppCompatActivity;


public class LoginActivity extends AppCompatActivity {
    private static final String TAG = "LoginActivity";

    private static final String PREFS_NAME = "LocalPrefs";
    private EditText editTextLoginName;
    private EditText editTextPassword;
    private Button login_button;
    private EditText emailAdd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(TAG, "onCreate");
        setTitle("Login Activity");
        setContentView(R.layout.activity_login);
        editTextLoginName = findViewById(R.id.edit_login);
        editTextPassword = findViewById(R.id.edit_password);
        login_button = findViewById(R.id.login_button);
        SharedPreferences sharedPref = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        String email = sharedPref.getString("DefaultEmail", "email@domain.com");
        emailAdd.setText(email);
        login_button.setOnClickListener(v -> {
            saveLoginEmail();
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);});
    }

    private void saveLoginEmail() {
        String email = emailAdd.getText().toString();
        SharedPreferences sharedPreferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("DefaultEmail", email);
        editor.commit();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i(TAG, "onResume");
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.i(TAG, "onStart");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.i(TAG, "onPause");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.i(TAG, "onStop");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i(TAG, "onDestroy");
    }
}
