package com.example.myapplication;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;


public class LoginActivity extends AppCompatActivity {
    private static final String ACTIVITY_NAME = "LoginActivity";

    private static final String PREFS_NAME = "LocalPrefs";
    private EditText editTextLoginName;
    private EditText editTextPassword;
    private Button login_button;
    private EditText emailAdd;
    private TextView viewError;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("Login Activity");
        setContentView(R.layout.activity_login);
        editTextLoginName = findViewById(R.id.edit_login);
        editTextPassword = findViewById(R.id.edit_password);
        login_button = findViewById(R.id.login_button);
        viewError = findViewById(R.id.textViewError);
        emailAdd = findViewById(R.id.edit_login);
        SharedPreferences sharedPref = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        String email = sharedPref.getString("DefaultEmail", "email@mylaurier.ca");
        emailAdd.setText(email);
        login_button.setOnClickListener(v -> {
            handleLogin();
        });
        Log.i(ACTIVITY_NAME, "onCreate");
    }

    private void handleLogin() {
        String username = editTextLoginName.getText().toString();
        String password = editTextPassword.getText().toString();

        if (username.isEmpty() || password.isEmpty() || username == null ||password == null ) {
            viewError.setText("Either username or password can't be empty/null");
            viewError.setVisibility(View.VISIBLE);
        } else if (!Patterns.EMAIL_ADDRESS.matcher(username).matches()) {
            viewError.setText("Invalid email address");
            viewError.setVisibility(View.VISIBLE);
        } else {
            saveLoginEmail();
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }
    }

    private void saveLoginEmail() {
        String email = editTextLoginName.getText().toString();
        SharedPreferences sharedPreferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("DefaultEmail", email);
        editor.apply();
        Log.i(ACTIVITY_NAME, "Saved email: " + email + " to SharedPreferences");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i(ACTIVITY_NAME, "onResume");
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.i(ACTIVITY_NAME, "onStart");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.i(ACTIVITY_NAME, "onPause");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.i(ACTIVITY_NAME, "onStop");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i(ACTIVITY_NAME, "onDestroy");
    }
}
