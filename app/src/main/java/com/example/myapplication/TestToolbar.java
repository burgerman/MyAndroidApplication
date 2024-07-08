package com.example.myapplication;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

public class TestToolbar extends AppCompatActivity {

    private static final String TAG = "TestToolbar";
    private Toolbar toolbar;
    private FloatingActionButton fab;

    private String snackbarMsg = "You selected item 1";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_toolbar);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        fab = findViewById(R.id.fab);
        fab.setOnClickListener(v-> {
            snackbarMsg = "My custom snackbar message";
            Snackbar.make(v, snackbarMsg, Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
        });
    }

    @Override
    public boolean onCreateOptionsMenu (Menu m) {
        getMenuInflater().inflate(R.menu.toolbar_menu, m );
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem mi) {
        int itemID = mi.getItemId();
        if (itemID == R.id.option1) {
            Log.d(TAG, "Option 1 selected");
            View view = findViewById(R.id.toolbar);
            Snackbar.make(view, snackbarMsg, Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
            return true;
        } else if (itemID == R.id.option2) {
            Log.d(TAG, "Option 2 selected");
            goDialogs();
            return true;
        } else if (itemID == R.id.option3) {
            Log.d(TAG, "Option 3 selected");
            goCustomDialog();
            return true;
        } else if (itemID ==R.id.option4) {
            Toast.makeText(this, "Version 1.0, by Menghao", Toast.LENGTH_SHORT).show();
            return true;
        } else {
            return super.onOptionsItemSelected(mi);
        }
    }

    private void goDialogs() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.go_back)
                .setPositiveButton(R.string.positive_button, (dialog, id) -> finish())
                .setNegativeButton(R.string.negative_button, (dialog, id) -> dialog.dismiss());
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void goCustomDialog() {
        LayoutInflater inflater = LayoutInflater.from(this);
        View dialogView = inflater.inflate(R.layout.custom_dialog, null);
        EditText editText = dialogView.findViewById(R.id.new_message);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(dialogView)
                .setTitle(R.string.go_back)
                .setPositiveButton(R.string.positive_button, (dialog, id) -> {
                    String newMessage = editText.getText().toString().trim();
                    if (!newMessage.isEmpty()) {
                        snackbarMsg = newMessage;
                        View view = findViewById(R.id.toolbar);
                        Snackbar.make(view, snackbarMsg, Snackbar.LENGTH_LONG)
                                .setAction("Action", null).show();
                    }
                })
                .setNegativeButton(R.string.negative_button, (dialog, id) -> {
                    dialog.dismiss();
                });
        AlertDialog dialog = builder.create();
        dialog.show();
    }
}