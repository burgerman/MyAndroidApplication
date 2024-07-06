package com.example.myapplication;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NavUtils;

import java.util.ArrayList;

public class ChatWindow extends AppCompatActivity {
    private static final String ACTIVITY_NAME = "ChatWindow";
    private ListView listView;
    private EditText textInput;
    private Button sendButton;
    private ArrayList<String> msgs;
    private ChatAdapter chatAdapter;
    private ChatDatabaseHelper chatDatabaseHelper;
    private SQLiteDatabase sqLiteDatabase;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_window);
        ActionBar actionBar = getSupportActionBar();
        if(getSupportActionBar() != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        listView = findViewById(R.id.chat_window_list_view);
        textInput = findViewById(R.id.chat_window_edit_text);
        sendButton = findViewById(R.id.send_button);
        msgs = new ArrayList<>();
        chatAdapter = new ChatAdapter(this);
        listView.setAdapter(chatAdapter);

        chatDatabaseHelper = new ChatDatabaseHelper(this);
        sqLiteDatabase = chatDatabaseHelper.getWritableDatabase();

        Cursor cursor = sqLiteDatabase.query(ChatDatabaseHelper.TABLE_NAME, null,null,null,null,null, null);


        int messageIndex = cursor.getColumnIndex(ChatDatabaseHelper.KEY_MESSAGE);
        if (messageIndex != -1 && cursor.moveToFirst()) {
            do {
                String message = cursor.getString(messageIndex);
                Log.i(ACTIVITY_NAME, "SQL MESSAGE: " + message);
                msgs.add(message);
            } while (cursor.moveToNext());
        }
        Log.i(ACTIVITY_NAME, "Cursor’s column count =" + cursor.getColumnCount());
        for (int columnIndex = 0; columnIndex < cursor.getColumnCount(); columnIndex++) {
            Log.i(ACTIVITY_NAME, "Column Name: " + cursor.getColumnName(columnIndex));
        }

        cursor.close();
        sendButton.setOnClickListener(v->{
            String msg = textInput.getText().toString().trim();
            if(!msg.isEmpty()) {
                msgs.add(msg);
                chatAdapter.notifyDataSetChanged();
                textInput.setText("");

                ContentValues contentValues = new ContentValues();
                contentValues.put(ChatDatabaseHelper.KEY_MESSAGE, msg);
                sqLiteDatabase.insert(ChatDatabaseHelper.TABLE_NAME, null, contentValues);
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

    private void print(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
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
        sqLiteDatabase.close();
        Log.i(ACTIVITY_NAME, "onDestroy");
        print("Call onDestroy and DB Close");
    }

    private class ChatAdapter extends ArrayAdapter<String> {
        private Context context;
        private ArrayList<String> messages;
        public ChatAdapter(Context ctx) {
            super(ctx, 0, msgs);
            context = ctx;
            messages = msgs;
        }

        @Override
        public int getCount() {
            return messages.size();
        }

        @Override
        public String getItem(int position) {
            return messages.get(position);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = ChatWindow.this.getLayoutInflater();
            View result = null;
            if (position % 2 == 0) {
                result = inflater.inflate(R.layout.chat_row_incoming, null);
            } else {
                result = inflater.inflate(R.layout.chat_row_outgoing, null);
            }
            TextView msg = result.findViewById(R.id.message_text);
            msg.setText(getItem(position));
            return result;
        }
    }
}