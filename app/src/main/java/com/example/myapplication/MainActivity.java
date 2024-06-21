package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    private static final String ACTIVITY_NAME = "MainActivity";
    private static final int REQUEST_CODE = 10;
    private Intent intent;
    private Button button;
    private Button button2;

    private Button button3;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(ACTIVITY_NAME, "onCreate");
        setContentView(R.layout.activity_main);
        button = findViewById(R.id.button2);
        button2 = findViewById(R.id.button3);
        button3 = findViewById(R.id.test_toolbar_button);
        button.setOnClickListener(v->{
            Log.i(ACTIVITY_NAME, "User clicked Start List Item");
            intent = new Intent(MainActivity.this, ListItemsActivity.class);
            startActivityForResult(intent, REQUEST_CODE);
        });

        button2.setOnClickListener(v->{
            Log.i(ACTIVITY_NAME, "User clicked Start Chat");
            intent = new Intent(MainActivity.this, ChatWindow.class);
            startActivityForResult(intent, REQUEST_CODE);
        });

        button3.setOnClickListener(v->{
            Log.i(ACTIVITY_NAME, "User clicked Start Test Toolbar");
            intent = new Intent(MainActivity.this, TestToolbar.class);
            startActivityForResult(intent, REQUEST_CODE);
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                String messagePassed = data.getStringExtra("Response");
                if (messagePassed != null) {
                    String toastMessage = "ListItemsActivity passed: " + messagePassed;
                    Toast.makeText(this, toastMessage, Toast.LENGTH_SHORT).show();
                }
            }
            Log.i(ACTIVITY_NAME, "Returned to MainActivity.onActivityResult");
        }
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