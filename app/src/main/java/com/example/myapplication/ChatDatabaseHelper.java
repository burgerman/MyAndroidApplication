package com.example.myapplication;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class ChatDatabaseHelper extends SQLiteOpenHelper {
    private static final String CLASS_NAME = "ChatDatabaseHelper";
    private static final String DATABASE_NAME = "Messages.db";
    private static final int VERSION_NUM = 1;

    // Table name and column names
    public static final String TABLE_NAME = "chat_messages";
    public static final String KEY_ID = "MESSAGE_ID";
    public static final String KEY_MESSAGE = "MESSAGE";



    public ChatDatabaseHelper(Context ctx) {
        super(ctx, DATABASE_NAME, null, VERSION_NUM);
    }
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("CREATE TABLE " + TABLE_NAME + " (" +
                KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                KEY_MESSAGE + " TEXT)");
        Log.i(CLASS_NAME, "Calling onCreate");

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        Log.i(CLASS_NAME, "Calling onUpgrade, oldVersion="+oldVersion+"newVersion="+newVersion);
        onCreate(sqLiteDatabase);
    }
}
