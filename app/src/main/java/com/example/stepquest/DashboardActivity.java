package com.example.stepquest;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;

import org.json.JSONArray;
import org.json.JSONObject;

public class DashboardActivity extends AppCompatActivity {

    private static final String API_KEY =
            BuildConfig.WEATHER_API_KEY;

    private FusedLocationProviderClient fusedLocationClient;

    Button walkBtn, runBtn, cycleBtn, startBtn, historyBtn, logoutBtn;

    TextView greetingText, weatherText;

    String selectedActivity = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        // Connect Views
        greetingText = findViewById(R.id.greetingText);
        weatherText = findViewById(R.id.weatherText);

        walkBtn = findViewById(R.id.walkBtn);
        runBtn = findViewById(R.id.runBtn);
        cycleBtn = findViewById(R.id.cycleBtn);

        startBtn = findViewById(R.id.startBtn);
        historyBtn = findViewById(R.id.historyBtn);
        logoutBtn = findViewById(R.id.logoutBtn);

        // Greeting
        String email =
                getIntent().getStringExtra("userEmail");

        if (email != null &&
                !email.isEmpty() &&
                email.contains("@")) {

            String userName =
                    email.substring(
                            0,
                            email.indexOf("@"));

            greetingText.setText(
                    "Hello, " + userName + " 👋");

        } else {

            greetingText.setText("Hello 👋");
        }

        // GPS Setup
        fusedLocationClient =
                LocationServices
                        .getFusedLocationProviderClient(this);

        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(
                    this,
                    new String[]{
                            Manifest.permission.ACCESS_FINE_LOCATION
                    },
                    100);

        } else {

            getCurrentLocation();
        }

        // Activity Selection
        walkBtn.setOnClickListener(v ->
                selectActivity(walkBtn, "Walking"));

        runBtn.setOnClickListener(v ->
                selectActivity(runBtn, "Running"));

        cycleBtn.setOnClickListener(v ->
                selectActivity(cycleBtn, "Cycling"));

        // Start Tracking
        startBtn.setOnClickListener(v -> {

            if (selectedActivity.isEmpty()) {

                Toast.makeText(
                        DashboardActivity.this,
                        "Please select an activity first",
                        Toast.LENGTH_SHORT
                ).show();

            } else {

                Intent intent =
                        new Intent(
                                DashboardActivity.this,
                                TrackingActivity.class);

                intent.putExtra(
                        "activityType",
                        selectedActivity);

                startActivity(intent);
            }
        });

        // History
        historyBtn.setOnClickListener(v -> {

            Intent intent =
                    new Intent(
                            DashboardActivity.this,
                            HistoryActivity.class);

            startActivity(intent);
        });

        // Logout
        logoutBtn.setOnClickListener(v -> {

            getSharedPreferences(
                    "StepQuestUsers",
                    MODE_PRIVATE)
                    .edit()
                    .putBoolean("loggedIn", false)
                    .apply();

            Intent intent =
                    new Intent(
                            DashboardActivity.this,
                            MainActivity.class);

            startActivity(intent);
            finish();
        });
    }

    private void getCurrentLocation() {

        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        fusedLocationClient.getLastLocation()
                .addOnSuccessListener(location -> {

                    if (location != null) {

                        double latitude =
                                location.getLatitude();

                        double longitude =
                                location.getLongitude();

                        getWeather(latitude, longitude);

                    } else {

                        weatherText.setText(
                                "Location unavailable");
                    }
                });
    }

    private void getWeather(
            double latitude,
            double longitude) {

        String url =
                "https://api.openweathermap.org/data/2.5/weather"
                        + "?lat=" + latitude
                        + "&lon=" + longitude
                        + "&units=metric"
                        + "&appid=" + API_KEY;

        JsonObjectRequest request =
                new JsonObjectRequest(
                        Request.Method.GET,
                        url,
                        null,

                        response -> {

                            try {

                                String city =
                                        response.getString("name");

                                JSONObject main =
                                        response.getJSONObject("main");

                                double temp =
                                        main.getDouble("temp");

                                String formattedTemp =
                                        String.format("%.0f", temp);

                                JSONArray weatherArray =
                                        response.getJSONArray("weather");

                                JSONObject weatherObject =
                                        weatherArray.getJSONObject(0);

                                String condition =
                                        weatherObject.getString("main");

                                String recommendation;

                                if (condition.equalsIgnoreCase("Rain")
                                        || condition.equalsIgnoreCase("Thunderstorm")
                                        || condition.equalsIgnoreCase("Snow")
                                        || temp < 5) {

                                    recommendation =
                                            "❌ Not Recommended";

                                } else {

                                    recommendation =
                                            "✅ Good To Go";
                                }

                                weatherText.setText(
                                        "📍 " + city +
                                                "\n🌡 " + formattedTemp + "°C" +
                                                "\n☁ " + condition +
                                                "\n\n" +
                                                recommendation);

                            } catch (Exception e) {

                                weatherText.setText(
                                        "Weather unavailable");
                            }

                        },

                        error -> {

                            if (error.networkResponse != null) {

                                weatherText.setText(
                                        "Weather Error: "
                                                + error.networkResponse.statusCode);

                            } else {

                                weatherText.setText(
                                        "Network Error");
                            }
                        });

        Volley.newRequestQueue(this)
                .add(request);
    }
    private void selectActivity(
            Button selectedButton,
            String activityName) {

        selectedActivity = activityName;

        walkBtn.setBackgroundTintList(
                getColorStateList(R.color.purple_500));

        runBtn.setBackgroundTintList(
                getColorStateList(R.color.purple_500));

        cycleBtn.setBackgroundTintList(
                getColorStateList(R.color.purple_500));

        selectedButton.setBackgroundTintList(
                getColorStateList(
                        android.R.color.holo_red_dark));
    }

    @Override
    protected void onResume() {
        super.onResume();

        selectedActivity = "";

        walkBtn.setBackgroundTintList(
                getColorStateList(R.color.purple_500));

        runBtn.setBackgroundTintList(
                getColorStateList(R.color.purple_500));

        cycleBtn.setBackgroundTintList(
                getColorStateList(R.color.purple_500));
    }

    @Override
    public void onRequestPermissionsResult(
            int requestCode,
            @NonNull String[] permissions,
            @NonNull int[] grantResults) {

        super.onRequestPermissionsResult(
                requestCode,
                permissions,
                grantResults);

        if (requestCode == 100
                && grantResults.length > 0
                && grantResults[0]
                == PackageManager.PERMISSION_GRANTED) {

            getCurrentLocation();
        }
    }
}