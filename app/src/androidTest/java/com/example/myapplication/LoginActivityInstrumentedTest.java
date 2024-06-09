package com.example.myapplication;

import static org.junit.Assert.assertEquals;
import android.content.Context;
import android.content.SharedPreferences;
import android.view.View;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
@RunWith(AndroidJUnit4.class)
public class LoginActivityInstrumentedTest {
    @Rule
    public ActivityScenarioRule<LoginActivity> activityScenarioRule =
            new ActivityScenarioRule<>(LoginActivity.class);

    @Test
    public void testHandleLogin() {
        activityScenarioRule.getScenario().onActivity(activity -> {

            activity.setEditTextLoginName("test@example.com");
            activity.setEditTextPassword("password");
            activity.handleLogin();
            assertEquals(View.GONE, activity.getViewError().getVisibility());
        });
    }

    @Test
    public void testHandleEmptyLogin() {
        activityScenarioRule.getScenario().onActivity(activity -> {

            activity.setEditTextLoginName("");
            activity.setEditTextPassword("");
            activity.handleLogin();
            assertEquals(View.VISIBLE, activity.getViewError().getVisibility());
            assertEquals("Either username or password can't be empty", activity.getViewError().getText().toString());
        });
    }

    @Test
    public void testHandleInvalidLogin() {
        activityScenarioRule.getScenario().onActivity(activity -> {

            activity.setEditTextLoginName("invalid");
            activity.setEditTextPassword("pwd");
            activity.handleLogin();
            assertEquals(View.VISIBLE, activity.getViewError().getVisibility());
            assertEquals("Invalid email address", activity.getViewError().getText().toString());
        });
    }

    @Test
    public void testSaveLoginEmail() {
            activityScenarioRule.getScenario().onActivity(activity -> {
            // Set the EditText fields
            activity.setEditTextLoginName("my@domain.com");

            // Call saveLoginEmail function
            activity.saveLoginEmail();

            // Check if the email is saved correctly
            SharedPreferences sharedPreferences = activity.getSharedPreferences("LocalPrefs", Context.MODE_PRIVATE);
            String email = sharedPreferences.getString("DefaultEmail", "");
            assertEquals("my@domain.com", email);
        });
    }

}
