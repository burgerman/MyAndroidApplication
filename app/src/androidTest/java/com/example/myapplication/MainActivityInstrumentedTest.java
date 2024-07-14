package com.example.myapplication;

import static androidx.test.espresso.intent.Intents.init;
import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.intent.Intents.release;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent;

import androidx.test.core.app.ApplicationProvider;
import androidx.test.espresso.Espresso;
import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.assertion.ViewAssertions;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class MainActivityInstrumentedTest {
    @Rule
    public ActivityScenarioRule<MainActivity> activityScenarioRule = new ActivityScenarioRule<>(MainActivity.class);

    @Before
    public void setUp() {
        init();
    }

    @After
    public void shutDown() {
        release();
    }

    @Test
    public void testButton2() {
        Espresso.onView(ViewMatchers.withId(R.id.button2)).perform(ViewActions.click());
        intended(hasComponent(ListItemsActivity.class.getName()));
    }

    @Test
    public void testButton3() {
        Espresso.onView(ViewMatchers.withId(R.id.button3)).perform(ViewActions.click());
        intended(hasComponent(ChatWindow.class.getName()));
    }

    @Test
    public void testToolbarButton() {
        Espresso.onView(ViewMatchers.withId(R.id.test_toolbar_button)).perform(ViewActions.click());
        intended(hasComponent(TestToolbar.class.getName()));
    }

    @Test
    public void testWeatherButton() {
        Espresso.onView(ViewMatchers.withId(R.id.weather_forecast_button)).perform(ViewActions.click());
        intended(hasComponent(TestToolbar.class.getName()));
    }

    @Test
    public void testUIElements() {
        Espresso.onView(ViewMatchers.withId(R.id.button2))
                .check(ViewAssertions.matches(ViewMatchers.isDisplayed()));
        Espresso.onView(ViewMatchers.withId(R.id.button3))
                .check(ViewAssertions.matches(ViewMatchers.isDisplayed()));
        Espresso.onView(ViewMatchers.withId(R.id.test_toolbar_button))
                .check(ViewAssertions.matches(ViewMatchers.isDisplayed()));
        Espresso.onView(ViewMatchers.withId(R.id.weather_forecast_button))
                .check(ViewAssertions.matches(ViewMatchers.isDisplayed()));
    }
}
