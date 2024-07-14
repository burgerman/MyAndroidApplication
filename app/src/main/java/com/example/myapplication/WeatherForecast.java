package com.example.myapplication;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NavUtils;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class WeatherForecast extends AppCompatActivity {
    private static final String ACTIVITY_NAME = "WeatherForecast";

    public final static String WEATHER_URL = "http://api.openweathermap.org/data/2.5/weather?q=ottawa,ca&APPID=b36baad209276042028cbbed5832f60c&mode=xml&units=metric";
    private ImageView weatherImage;
    private TextView currentTemperature;
    private TextView minTemperature;
    private TextView maxTemperature;

    private ProgressBar progressBar;

    private ForecastQuery forecastQuery;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(ACTIVITY_NAME, "onCreate");
        setContentView(R.layout.activity_weather_forecast);
        ActionBar actionBar = getSupportActionBar();
        if(getSupportActionBar() != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        weatherImage = findViewById(R.id.current_weather_image);
        currentTemperature = findViewById(R.id.current_temperature);
        minTemperature = findViewById(R.id.min_temperature);
        maxTemperature = findViewById(R.id.max_temperature);
        progressBar = findViewById(R.id.progress_bar);
        progressBar.setVisibility(View.VISIBLE);
        forecastQuery = new ForecastQuery();
        forecastQuery.execute(WEATHER_URL);
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

    @Override
    protected void onResume() {
        super.onResume();
        Log.i(ACTIVITY_NAME, "onResume");
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.i(ACTIVITY_NAME, "onStart");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.i(ACTIVITY_NAME, "onPause");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.i(ACTIVITY_NAME, "onStop");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i(ACTIVITY_NAME, "onDestroy");
    }


    public class ForecastQuery extends AsyncTask<String, Integer, String> {
        private String currentTemp;
        private String minTemp;
        private String maxTemp;
        private Bitmap weatherIcon;

        public String getCurrentTemp() {
            return currentTemp;
        }

        public String getMinTemp() {
            return minTemp;
        }

        public String getMaxTemp() {
            return maxTemp;
        }

        @Override
        protected String doInBackground(String... args) {
            HttpURLConnection urlConnection = null;
            try {
                URL url = new URL(args[0]);
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.connect();
                Log.i(ACTIVITY_NAME, "HTTP Response Code: "+urlConnection.getResponseCode());
                InputStream inputStream = urlConnection.getInputStream();
                XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
                XmlPullParser parser = factory.newPullParser();
                parser.setInput(inputStream, null);
                int eventType = parser.getEventType();
                while (eventType != XmlPullParser.END_DOCUMENT) {
                    if (eventType == XmlPullParser.START_TAG) {
                        if (parser.getName().equals("temperature")) {
                            currentTemp = parser.getAttributeValue(null, "value");
                            publishProgress(25);
                            minTemp = parser.getAttributeValue(null, "min");
                            publishProgress(50);
                            maxTemp = parser.getAttributeValue(null, "max");
                            publishProgress(75);
                        } else if (parser.getName().equals("weather")) {
                            String iconName = parser.getAttributeValue(null, "icon");
                            if(iconName!=null) {
                                weatherIcon = downloadImage("http://openweathermap.org/img/w/" + iconName + ".png", iconName);
                                publishProgress(100);
                            }
                            return "Success";
                        }
                    }
                    eventType = parser.next();
                }


            } catch (Exception e) {
                e.printStackTrace();
                Log.e(ACTIVITY_NAME, "Failed to fetch XML via URL");
            } finally {
                if(urlConnection!=null) urlConnection.disconnect();
            }
            return null;
        }

        protected Bitmap downloadImage(String imageUrl, String iconName) {
            Bitmap image = null;
            if (fileExistance(iconName + ".png")) {
                Log.i(ACTIVITY_NAME, "Image Found: " + iconName + ".png");
                try (FileInputStream fis = openFileInput(iconName + ".png")) {
                    image = BitmapFactory.decodeStream(fis);
                } catch (IOException e) {
                    Log.e(ACTIVITY_NAME, "Failed to load image: " + iconName + ".png");
                }
            } else {
                Log.i(ACTIVITY_NAME, "Downloading image: " + imageUrl);
                HttpURLConnection connection = null;
                try {
                    URL url = new URL(imageUrl);
                    connection = (HttpURLConnection) url.openConnection();
                    connection.setDoInput(true);
                    connection.connect();
                    InputStream input = connection.getInputStream();
                    image = BitmapFactory.decodeStream(input);
                    FileOutputStream outputStream = null;
                    try {
                        outputStream = openFileOutput(iconName + ".png", Context.MODE_PRIVATE);
                        image.compress(Bitmap.CompressFormat.PNG, 80, outputStream);
                    } catch (Exception e) {
                        Log.e(ACTIVITY_NAME, "Failed to save image: " + iconName + ".png");
                    } finally {
                        if(outputStream!=null) outputStream.close();
                    }
                } catch (Exception e) {
                    Log.e(ACTIVITY_NAME, "Failed to fetch remote image: " + iconName + ".png");
                } finally {
                    if(connection!=null) connection.disconnect();
                }
            }
            return image;
        }

        public boolean fileExistance(String fname) {
            File file = getBaseContext().getFileStreamPath(fname);
            return file.exists();
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            progressBar.setVisibility(View.VISIBLE);
            progressBar.setProgress(values[0]);
        }

        @Override
        protected void onPostExecute(String result) {
            progressBar.setVisibility(View.INVISIBLE);
            currentTemperature.setText(getString(R.string.current_temp) + currentTemp + "°C");
            minTemperature.setText(getString(R.string.min_temp) + minTemp + "°C");
            maxTemperature.setText(getString(R.string.max_temp) + maxTemp + "°C");
            if (weatherIcon != null) {
                weatherImage.setImageBitmap(weatherIcon);
            }
        }
    }
}