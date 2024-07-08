package com.example.myapplication;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.MenuItem;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.Switch;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NavUtils;

import java.io.IOException;

public class ListItemsActivity extends AppCompatActivity {
    private static final String ACTIVITY_NAME = "ListItemsActivity";
    private static final int REQUEST_IMAGE = 1;
    private ImageButton imageButton;
    private Switch mySwitch;
    private CheckBox checkBox;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(ACTIVITY_NAME, "onCreate");
        print("Call onCreate");
        setContentView(R.layout.activity_list_items);
        ActionBar actionBar = getSupportActionBar();
        if(getSupportActionBar() != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        setTitle("List Items Activity");
        imageButton = findViewById(R.id.imageButton);
        mySwitch = findViewById(R.id.switch1);
        checkBox = findViewById(R.id.checkBox);
//        imageButton.setOnClickListener(v-> {
//            Intent picIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//            if(picIntent.resolveActivity(getPackageManager()) != null) {
//                startActivityForResult(picIntent, REQUEST_IMAGE);
//            } else {
//                Log.i(ACTIVITY_NAME, "No Camera Found");
//            }
//        });
        imageButton.setOnClickListener(v-> openImagePicker());
        mySwitch.setOnCheckedChangeListener((compoundButton, isChecked) -> setOnCheckedChanged(isChecked));
        checkBox.setOnCheckedChangeListener((compoundButton, isChecked) -> {
            if(isChecked) {
                showFinishDialog();
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
            default:
                return super.onOptionsItemSelected(item);

        }
    }

    private void openImagePicker() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, REQUEST_IMAGE);
    }

    private void setOnCheckedChanged(boolean isChecked) {
        CharSequence text = isChecked? "Switch is On" : "Switch is Off";
        int duration = isChecked ? Toast.LENGTH_SHORT : Toast.LENGTH_LONG;

        Toast toast = Toast.makeText(this, text, duration);
        toast.show();
    }

    private void print(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    private void showFinishDialog() {
        AlertDialog.Builder builder= new AlertDialog.Builder(ListItemsActivity.this);
        builder.setMessage(R.string.dialog_message)
                .setTitle(R.string.dialog_title)
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User clicked OK button
                        Intent resultIntent = new Intent(  );
                        resultIntent.putExtra("Response", "my information to share");
                        setResult(Activity.RESULT_OK, resultIntent);
                        Log.i(ACTIVITY_NAME, "onFinish() called");
                        finish();
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User cancelled the dialog
                        checkBox.setChecked(false);
                    }
                })
                .show();
    }

//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if (requestCode == REQUEST_IMAGE && resultCode == RESULT_OK) {
//            Bundle extras = data.getExtras();
//            Bitmap imageBitmap = (Bitmap) extras.get("data");
//            imageButton.setImageBitmap(imageBitmap);
//        }
//    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri imageUri = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri);
                imageButton.setImageBitmap(bitmap);
                Toast.makeText(this, "Image updated", Toast.LENGTH_SHORT).show();
            } catch (IOException e) {
                Toast.makeText(this, "Failed to load image", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i(ACTIVITY_NAME, "onResume");
        print("Call onResume");
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.i(ACTIVITY_NAME, "onStart");
        print("Call onStart");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.i(ACTIVITY_NAME, "onPause");
        print("Call onPause");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.i(ACTIVITY_NAME, "onStop");
        print("Call onStop");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i(ACTIVITY_NAME, "onDestroy");
        print("Call onDestroy");
    }
}