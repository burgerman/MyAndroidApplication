package com.example.myapplication;

import android.graphics.Bitmap;

import androidx.test.espresso.Espresso;
import androidx.test.espresso.assertion.ViewAssertions;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.rule.ActivityTestRule;

import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class WeatherForcastInstrumentedTest {

    @Rule
    public ActivityTestRule<WeatherForecast> activityRule =
            new ActivityTestRule<>(WeatherForecast.class);

    @Test
    public void testWeatherForecastUI() {
        Espresso.onView(ViewMatchers.withId(R.id.progress_bar)).check(ViewAssertions.matches(ViewMatchers.withEffectiveVisibility(ViewMatchers.Visibility.INVISIBLE)));
        WeatherForecast.ForecastQuery forecastQuery = activityRule.getActivity().new ForecastQuery();
        forecastQuery.execute("");
        Espresso.onView(ViewMatchers.withId(R.id.current_temperature)).check(ViewAssertions.matches(ViewMatchers.withText("Current Temperature:null°C")));
        Espresso.onView(ViewMatchers.withId(R.id.min_temperature)).check(ViewAssertions.matches(ViewMatchers.withText("Min Temperature:null°C")));
        Espresso.onView(ViewMatchers.withId(R.id.max_temperature)).check(ViewAssertions.matches(ViewMatchers.withText("Max Temperature:null°C")));
        Espresso.onView(ViewMatchers.withId(R.id.current_weather_image)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()));
    }

    @Test
    public void testDownloadImage() {
        WeatherForecast weatherForecastActivity = activityRule.getActivity();
        WeatherForecast.ForecastQuery forecastQuery = weatherForecastActivity.new ForecastQuery();
        String iconName = "03n";
        Bitmap bitmap = forecastQuery.downloadImage("http://openweathermap.org/img/w/" + iconName + ".png", iconName);
        Assert.assertNotNull(bitmap);
        Assert.assertTrue(forecastQuery.fileExistance(iconName + ".png"));
    }
}
