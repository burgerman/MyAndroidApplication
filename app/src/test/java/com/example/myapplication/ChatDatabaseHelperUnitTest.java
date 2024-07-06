package com.example.myapplication;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class ChatDatabaseHelperUnitTest {

    private ChatDatabaseHelper chatDatabaseHelper;
    @Mock
    SQLiteDatabase sqLiteDatabase;
    @Mock
    Context context;

    @Before
    public void setUp() {
        chatDatabaseHelper = new ChatDatabaseHelper(context);
    }

    @Test
    public void testOnCreate() {
        try (MockedStatic<Log> logMock = Mockito.mockStatic(Log.class)) {
            logMock.when(() -> Log.i(Mockito.anyString(), Mockito.anyString())).thenReturn(0);
            chatDatabaseHelper.onCreate(sqLiteDatabase);
            Mockito.verify(sqLiteDatabase).execSQL("CREATE TABLE " + ChatDatabaseHelper.TABLE_NAME + " (" +
                    ChatDatabaseHelper.KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    ChatDatabaseHelper.KEY_MESSAGE + " TEXT)");
        }
    }

    @Test
    public void testOnUpgrade() {
        try (MockedStatic<Log> logMock = Mockito.mockStatic(Log.class)) {
            logMock.when(() -> Log.i(Mockito.anyString(), Mockito.anyString())).thenReturn(0);
            chatDatabaseHelper.onUpgrade(sqLiteDatabase, 1, 2);
            Mockito.verify(sqLiteDatabase).execSQL("DROP TABLE IF EXISTS " + ChatDatabaseHelper.TABLE_NAME);
            Mockito.verify(sqLiteDatabase).execSQL("CREATE TABLE " + ChatDatabaseHelper.TABLE_NAME + " (" +
                    ChatDatabaseHelper.KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    ChatDatabaseHelper.KEY_MESSAGE + " TEXT)");
        }
    }
}
