package com.example.myapplication;

import android.os.IBinder;
import android.view.WindowManager;

import androidx.test.espresso.Espresso;
import androidx.test.espresso.Root;
import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.assertion.ViewAssertions;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;

import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class ToolbarInstrumentedTest {

    @Rule
    public ActivityScenarioRule<TestToolbar> activityScenarioRule =
            new ActivityScenarioRule<>(TestToolbar.class);

    @Test
    public void testDialogs() {
        Espresso.onView(ViewMatchers.withId(R.id.toolbar))
                .perform(ViewActions.click());
        Espresso.onView(ViewMatchers.withId(R.id.fab))
                .perform(ViewActions.click());
    }

    @Test
    public void testFab() {
        Espresso.onView(ViewMatchers.withId(R.id.fab))
                .perform(ViewActions.click());
        Espresso.onView(ViewMatchers.withId(com.google.android.material.R.id.snackbar_text))
                .check(ViewAssertions.matches(ViewMatchers.withText("My custom snackbar message")));
    }

    @Test
    public void testUIElements() {
        Espresso.onView(ViewMatchers.withId(R.id.toolbar))
                .check(ViewAssertions.matches(ViewMatchers.isDisplayed()));
        Espresso.onView(ViewMatchers.withId(R.id.fab))
                .check(ViewAssertions.matches(ViewMatchers.isDisplayed()));
    }

    @Test
    public void testItemOption1() {
        Espresso.onView(ViewMatchers.withId(R.id.option1)).perform(ViewActions.click());
        Espresso.onView(ViewMatchers.withId(com.google.android.material.R.id.snackbar_text))
                .check(ViewAssertions.matches(ViewMatchers.withText("You selected item 1")));
    }

    @Test
    public void testItemOption2() {
        Espresso.onView(ViewMatchers.withId(R.id.option2)).perform(ViewActions.click());
        Espresso.onView(ViewMatchers.withText(R.string.go_back)).perform(ViewActions.click());
        Espresso.onView(ViewMatchers.withText(R.string.go_back)).check(ViewAssertions.matches(ViewMatchers.withText(R.string.go_back)));
    }

    @Test
    public void testItemOption3() {
        Espresso.onView(ViewMatchers.withId(R.id.option3)).perform(ViewActions.click());
        Espresso.onView(ViewMatchers.withText(R.string.go_back)).check(ViewAssertions.matches(ViewMatchers.withText(R.string.go_back)));
    }

    @Test
    public void testItemOption4() {
        Espresso.openActionBarOverflowOrOptionsMenu(InstrumentationRegistry.getInstrumentation().getTargetContext());
        Espresso.onView(ViewMatchers.withText("About")).perform(ViewActions.click());
        Espresso.onView(ViewMatchers.withText("Version 1.0, by Menghao")).inRoot(new ToastMatcher())
                .check(ViewAssertions.matches(ViewMatchers.withText("Version 1.0, by Menghao")));
    }


    public static class ToastMatcher extends TypeSafeMatcher<Root> {

        @Override
        public void describeTo(Description description) {
            description.appendText("is toast");
        }

        @Override
        public boolean matchesSafely(Root root) {
            int type = root.getWindowLayoutParams().get().type;
            if ((type == WindowManager.LayoutParams.TYPE_TOAST)) {
                IBinder windowToken = root.getDecorView().getWindowToken();
                IBinder appToken = root.getDecorView().getApplicationWindowToken();
                return windowToken == appToken;
            }
            return false;
        }
    }
}
