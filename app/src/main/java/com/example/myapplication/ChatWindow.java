package com.example.myapplication;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
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
import android.widget.FrameLayout;
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
    private Cursor cursor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_window);
        ActionBar actionBar = getSupportActionBar();
        if(getSupportActionBar() != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        FrameLayout frameLayout;
        boolean frameExist = findViewById(R.id.frame_layout)!=null;
        if(frameExist) {
            frameLayout = findViewById(R.id.frame_layout);
            Log.i(ACTIVITY_NAME, "frame layout loaded");
        }
        listView = findViewById(R.id.chat_window_list_view);
        textInput = findViewById(R.id.chat_window_edit_text);
        sendButton = findViewById(R.id.send_button);
        msgs = new ArrayList<>();

        chatDatabaseHelper = new ChatDatabaseHelper(this);
        sqLiteDatabase = chatDatabaseHelper.getWritableDatabase();

        cursor = sqLiteDatabase.query(ChatDatabaseHelper.TABLE_NAME, null,null,null,null,null, null);
        chatAdapter = new ChatAdapter(this, cursor);
        listView.setAdapter(chatAdapter);

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

        sendButton.setOnClickListener(v->{
            String msg = textInput.getText().toString().trim();
            if(!msg.isEmpty()) {
                msgs.add(msg);
                chatAdapter.notifyDataSetChanged();
                textInput.setText("");

                ContentValues contentValues = new ContentValues();
                contentValues.put(ChatDatabaseHelper.KEY_MESSAGE, msg);
                sqLiteDatabase.insert(ChatDatabaseHelper.TABLE_NAME, null, contentValues);
                cursor.requery();
            }

        });

        listView.setOnItemClickListener((parent, view, position, id) -> {
            if (frameExist) {
                MessageFragment messageFragment = new MessageFragment();
                Bundle bundle = new Bundle();
                bundle.putString("message", msgs.get(position));
                bundle.putLong("id", id);
                messageFragment.setArguments(bundle);
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.frame_layout, messageFragment)
                        .addToBackStack(null)
                        .commit();
            } else {
                Intent intent = new Intent(ChatWindow.this, MessageDetails.class);
                intent.putExtra("message", msgs.get(position));
                intent.putExtra("id", id);
                startActivityForResult(intent, 1);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK) {
            long idToDelete = data.getLongExtra("id", -1);
            if (idToDelete != -1) {
                sqLiteDatabase.delete(ChatDatabaseHelper.TABLE_NAME, ChatDatabaseHelper.KEY_ID + " = ?", new String[]{String.valueOf(idToDelete)});
                cursor.requery();
                msgs.clear();
                int messageIndex = cursor.getColumnIndex(ChatDatabaseHelper.KEY_MESSAGE);
                if (messageIndex != -1 && cursor.moveToFirst()) {
                    do {
                        String message = cursor.getString(messageIndex);
                        msgs.add(message);
                    } while (cursor.moveToNext());
                }
                chatAdapter.notifyDataSetChanged();
            }
        }
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

        private View resultView;
        private Context context;
        private ArrayList<String> messages;
        private Cursor cursor;

        public ChatAdapter(Context ctx, Cursor cursor) {
            super(ctx, 0, msgs);
            context = ctx;
            messages = msgs;
            this.cursor = cursor;
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
        public long getItemId(int position) {
            if (cursor.moveToPosition(position)) {
                return cursor.getLong(cursor.getColumnIndexOrThrow(ChatDatabaseHelper.KEY_ID));
            }
            return -1;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = ChatWindow.this.getLayoutInflater();
            if (position % 2 == 0) {
                resultView = inflater.inflate(R.layout.chat_row_incoming, null);
            } else {
                resultView = inflater.inflate(R.layout.chat_row_outgoing, null);
            }
            TextView msg = resultView.findViewById(R.id.message_text);
            msg.setText(getItem(position));
            return resultView;
        }
    }
}