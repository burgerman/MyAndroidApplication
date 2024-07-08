package com.example.myapplication;

import static androidx.test.espresso.intent.Intents.init;
import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.intent.Intents.release;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasAction;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasData;
import static org.hamcrest.CoreMatchers.notNullValue;

import android.content.Intent;
import android.provider.MediaStore;

import androidx.test.espresso.Espresso;
import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.assertion.ViewAssertions;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class ListItemsActivityInstrumentedTest {

    @Rule
    public ActivityScenarioRule<ListItemsActivity> activityScenarioRule =
            new ActivityScenarioRule<>(ListItemsActivity.class);


    @Test
    public void testImageButtonClick() {
        init();
        Espresso.onView(ViewMatchers.withId(R.id.imageButton))
                .perform(ViewActions.click());
        intended(hasAction(Intent.ACTION_PICK));
        intended(hasData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI));
        release();
    }

    @Test
    public void testSwitchToggle() {
        Espresso.onView(ViewMatchers.withId(R.id.switch1))
                .perform(ViewActions.click())
                .check(ViewAssertions.matches(ViewMatchers.isChecked()));
    }

    @Test
    public void testCheckBoxClick() {
        Espresso.onView(ViewMatchers.withId(R.id.checkBox))
                .perform(ViewActions.click());
        Espresso.onView(ViewMatchers.withText(R.string.dialog_title))
                .check(ViewAssertions.matches(notNullValue()));
    }

    @Test
    public void testUIElements() {
        Espresso.onView(ViewMatchers.withId(R.id.imageButton))
                .check(ViewAssertions.matches(ViewMatchers.isDisplayed()));
        Espresso.onView(ViewMatchers.withId(R.id.switch1))
                .check(ViewAssertions.matches(ViewMatchers.isDisplayed()));
        Espresso.onView(ViewMatchers.withId(R.id.checkBox))
                .check(ViewAssertions.matches(ViewMatchers.isDisplayed()));
    }
}
