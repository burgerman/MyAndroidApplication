package com.example.myapplication;

import static androidx.test.espresso.matcher.ViewMatchers.withId;
import android.widget.ListView;
import androidx.test.espresso.Espresso;
import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.assertion.ViewAssertions;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.junit.Assert.assertEquals;

@RunWith(AndroidJUnit4.class)
public class ChatWindowInstrumentedTest {

    @Rule
    public ActivityScenarioRule<ChatWindow> activityScenarioRule =
            new ActivityScenarioRule<>(ChatWindow.class);

    @Test
    public void testSendMessage() {
        // Initially get the count of messages
        int initialCount = getListViewCount();

        // Attempt to send an empty message
        Espresso.onView(withId(R.id.chat_window_edit_text))
                .perform(ViewActions.typeText("Hello"), ViewActions.closeSoftKeyboard());
        Espresso.onView(withId(R.id.send_button))
                .perform(ViewActions.click());
        initialCount++;
        // Verify that the message count has not changed
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
}
