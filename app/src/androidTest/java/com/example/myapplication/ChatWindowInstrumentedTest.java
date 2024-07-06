package com.example.myapplication;

import static androidx.test.espresso.matcher.ViewMatchers.isAssignableFrom;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import androidx.test.core.app.ApplicationProvider;
import androidx.test.espresso.Espresso;
import androidx.test.espresso.PerformException;
import androidx.test.espresso.UiController;
import androidx.test.espresso.ViewAction;
import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.assertion.ViewAssertions;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.espresso.util.HumanReadables;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.hamcrest.Matcher;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import java.util.concurrent.TimeoutException;

@RunWith(AndroidJUnit4.class)
public class ChatWindowInstrumentedTest {

    @Rule
    public ActivityScenarioRule<ChatWindow> activityScenarioRule =
            new ActivityScenarioRule<>(ChatWindow.class);

    private ChatDatabaseHelper chatDatabaseHelper;
    private SQLiteDatabase sqLiteDatabase;
    private Context context;

    @Before
    public void setUp() {
        context = ApplicationProvider.getApplicationContext();
        chatDatabaseHelper = new ChatDatabaseHelper(context);
        sqLiteDatabase = chatDatabaseHelper.getWritableDatabase();
    }

    @After
    public void shutDown() {
        sqLiteDatabase.close();
        chatDatabaseHelper.close();
    }

    @Test
    public void testShowMessage() {
        sqLiteDatabase.execSQL("INSERT INTO " + ChatDatabaseHelper.TABLE_NAME + " (" + ChatDatabaseHelper.KEY_MESSAGE + ") VALUES ('Test Message')");
        sqLiteDatabase.close();
        activityScenarioRule.getScenario().recreate();
        Espresso.onView(ViewMatchers.withId(R.id.chat_window_list_view))
                .check(ViewAssertions.matches(ViewMatchers.isDisplayed()));
        Espresso.onView(ViewMatchers.withId(R.id.chat_window_list_view))
                .perform(withTextInListView("Test Message"));
    }

    @Test
    public void testSendMessageWithDB() {
        // Initially get the count of messages
        int initialCount = getListViewCount();

        Espresso.onView(ViewMatchers.withId(R.id.chat_window_edit_text))
                .perform(ViewActions.typeText("Test Message"), ViewActions.closeSoftKeyboard());
        Espresso.onView(ViewMatchers.withId(R.id.send_button))
                .perform(ViewActions.click());
        initialCount++;
        // Verify that the message count has changed
        int finalCount = getListViewCount();
        assertEquals(initialCount, finalCount);

        Cursor cursor = sqLiteDatabase.query(ChatDatabaseHelper.TABLE_NAME,null, null, null, null, null, null);
        assertTrue(cursor.moveToFirst());
        int messageIndex = cursor.getColumnIndex(ChatDatabaseHelper.KEY_MESSAGE);
        assertTrue(messageIndex!=-1);
        assertEquals("Test Message", cursor.getString(messageIndex));
        cursor.close();
    }

    @Test
    public void testSendMessage() {
        // Initially get the count of messages
        int initialCount = getListViewCount();

        Espresso.onView(ViewMatchers.withId(R.id.chat_window_edit_text))
                .perform(ViewActions.typeText("Test Message"), ViewActions.closeSoftKeyboard());
        Espresso.onView(ViewMatchers.withId(R.id.send_button))
                .perform(ViewActions.click());
        initialCount++;
        // Verify that the message count has changed
        int finalCount = getListViewCount();
        assertEquals(initialCount, finalCount);
    }

    @Test
    public void testEmptyMessageNotSent() {
        // Initially get the count of messages
        int initialCount = getListViewCount();

        // Attempt to send an empty message
        Espresso.onView(withId(R.id.chat_window_edit_text))
                .perform(ViewActions.typeText(""), ViewActions.closeSoftKeyboard());
        Espresso.onView(withId(R.id.send_button))
                .perform(ViewActions.click());

        // Verify that the message count has not changed
        int finalCount = getListViewCount();
        assertEquals(initialCount, finalCount);
    }

    @Test
    public void testUIElements() {
        Espresso.onView(ViewMatchers.withId(R.id.chat_window_edit_text))
                .check(ViewAssertions.matches(ViewMatchers.isDisplayed()));
        Espresso.onView(ViewMatchers.withId(R.id.send_button))
                .check(ViewAssertions.matches(ViewMatchers.isDisplayed()));
        Espresso.onView(ViewMatchers.withId(R.id.chat_window_list_view))
                .check(ViewAssertions.matches(ViewMatchers.isDisplayed()));
    }

    private int getListViewCount() {
        final int[] count = new int[1];
        activityScenarioRule.getScenario().onActivity(activity -> {
            ListView listView = activity.findViewById(R.id.chat_window_list_view);
            count[0] = listView.getAdapter().getCount();
        });
        return count[0];
    }

    public static ViewAction withTextInListView(final String text) {
        return new ViewAction() {
            @Override
            public Matcher<View> getConstraints() {
                return isAssignableFrom(ListView.class);
            }

            @Override
            public String getDescription() {
                return "Check ListView has item with text: " + text;
            }

            @Override
            public void perform(UiController uiController, View view) {
                ListView listView = (ListView) view;

                for (int i = 0; i < listView.getCount(); i++) {
                    View listItem = listView.getChildAt(i);
                    if (listItem != null) {
                        TextView textView = listItem.findViewById(R.id.message_text);
                        if (textView != null && textView.getText().toString().equals(text)) {
                            return;
                        }
                    }
                }

                throw new PerformException.Builder()
                        .withActionDescription(this.getDescription())
                        .withViewDescription(HumanReadables.describe(view))
                        .withCause(new TimeoutException())
                        .build();
            }
        };
    }
}
