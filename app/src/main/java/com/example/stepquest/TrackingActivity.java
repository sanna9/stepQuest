package com.example.stepquest;

import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class TrackingActivity extends AppCompatActivity {
    TextView activityTitle, distanceText, caloriesText;
    Chronometer chronometer;
    Button stopBtn;
    private double distance = 0.0;
    private int calories = 0;
    private Handler handler;
    private Runnable runnable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tracking);

        // Connect UI
        activityTitle = findViewById(R.id.activityTitle);
        distanceText = findViewById(R.id.distanceText);
        caloriesText = findViewById(R.id.caloriesText);
        chronometer = findViewById(R.id.chronometer);
        stopBtn = findViewById(R.id.stopBtn);

        // Receive activity from Dashboard
        String activity = getIntent().getStringExtra("activityType");

        // Display activity name
        activityTitle.setText(activity);

        // Start timer
        chronometer.setBase(SystemClock.elapsedRealtime());
        chronometer.start();

        // Start distance and calorie updates
        handler = new Handler();

        runnable = new Runnable() {
            @Override
            public void run() {
                distance += 0.05;
                calories += 2;

                distanceText.setText(
                        "Distance: " +
                                String.format("%.2f", distance) +
                                " km");

                caloriesText.setText(
                        "Calories: " +
                                calories +
                                " kcal");

                handler.postDelayed(this, 1000);
            }
        };

        handler.post(runnable);

        // Stop Button
        stopBtn.setOnClickListener(v -> {
            // Stop timer
            chronometer.stop();
            // Stop distance/calorie updates
            handler.removeCallbacks(runnable);
            // Show summary
            AlertDialog.Builder builder =
                    new AlertDialog.Builder(TrackingActivity.this);

            builder.setTitle("Workout Complete");
            builder.setMessage(
                    "Activity: " + activity +
                            "\nDistance: " + String.format("%.2f", distance) + " km" +
                            "\nCalories: " + calories + " kcal"
            );
            builder.setPositiveButton("OK", (dialog, which) -> {
                // Return to Dashboard
                finish();
            });
            builder.show();
        });
    }
}