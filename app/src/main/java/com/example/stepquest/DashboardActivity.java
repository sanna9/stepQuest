package com.example.stepquest;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class DashboardActivity extends AppCompatActivity {
    // Button references
    Button walkBtn, runBtn, cycleBtn, startBtn, historyBtn, logoutBtn;
    TextView greetingText;
    // Stores the selected activity
    String selectedActivity = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Connect Java file to XML layout
        setContentView(R.layout.activity_dashboard);
        // Connect XML buttons to Java variables
        greetingText = findViewById(R.id.greetingText);
        walkBtn = findViewById(R.id.walkBtn);
        runBtn = findViewById(R.id.runBtn);
        cycleBtn = findViewById(R.id.cycleBtn);
        startBtn = findViewById(R.id.startBtn);
        historyBtn = findViewById(R.id.historyBtn);
        logoutBtn = findViewById(R.id.logoutBtn);

        String email =
                getIntent().getStringExtra("userEmail");

        if(email != null && email.contains("@")) {

            String userName =
                    email.substring(0, email.indexOf("@"));

            greetingText.setText(
                    "Hello, " + userName + " 👋");
        }

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

        // Activity selection buttons
        walkBtn.setOnClickListener(v ->
                selectActivity(walkBtn, "Walking"));

        runBtn.setOnClickListener(v ->
                selectActivity(runBtn, "Running"));

        cycleBtn.setOnClickListener(v ->
                selectActivity(cycleBtn, "Cycling"));

        // Start Tracking Screen
        startBtn.setOnClickListener(v -> {

            if (selectedActivity.isEmpty()) {

                Toast.makeText(
                        DashboardActivity.this,
                        "Please select an activity first",
                        Toast.LENGTH_SHORT
                ).show();

            } else {

                Intent intent = new Intent(
                        DashboardActivity.this,
                        TrackingActivity.class);

                // Send selected activity to TrackingActivity
                intent.putExtra(
                        "activityType",
                        selectedActivity);

                startActivity(intent);
            }
        });

        // Open History Screen
        historyBtn.setOnClickListener(v -> {

            Intent intent = new Intent(
                    DashboardActivity.this,
                    HistoryActivity.class);

            startActivity(intent);
        });

        // Logout Button
        logoutBtn.setOnClickListener(v -> {

            Intent intent = new Intent(
                    DashboardActivity.this,
                    MainActivity.class);

            startActivity(intent);
            finish();
        });
    }

    //activity selection and button color changes
    private void selectActivity(Button selectedButton, String activityName) {

        // Save selected activity
        selectedActivity = activityName;
        // Reset all buttons back to purple
        walkBtn.setBackgroundTintList(
                getColorStateList(R.color.purple_500));
        runBtn.setBackgroundTintList(
                getColorStateList(R.color.purple_500));
        cycleBtn.setBackgroundTintList(
                getColorStateList(R.color.purple_500));
        // Highlighting selected button in red
        selectedButton.setBackgroundTintList(
                getColorStateList(android.R.color.holo_red_dark));
    }

    @Override
    protected void onResume() {
        super.onResume();
        // undo selected activity
        selectedActivity = "";
        // Reset all buttons to purple
        walkBtn.setBackgroundTintList(
                getColorStateList(R.color.purple_500));
        runBtn.setBackgroundTintList(
                getColorStateList(R.color.purple_500));
        cycleBtn.setBackgroundTintList(
                getColorStateList(R.color.purple_500));
    }
}