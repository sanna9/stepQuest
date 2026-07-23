package com.example.stepquest;

import android.os.Bundle;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

public class HistoryActivity extends AppCompatActivity {

    Button backBtn;
    LinearLayout historyContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        backBtn = findViewById(R.id.backBtn);
        historyContainer = findViewById(R.id.historyContainer);

        backBtn.setOnClickListener(v -> finish());

        loadHistory();
    }

    private void loadHistory() {

        for (Workout workout : WorkoutData.workoutList) {

            CardView card = new CardView(this);
            card.setRadius(12);
            card.setCardElevation(6);

            LinearLayout layout = new LinearLayout(this);
            layout.setOrientation(LinearLayout.VERTICAL);
            layout.setPadding(30,30,30,30);

            TextView activity = new TextView(this);
            activity.setText(workout.getActivityType());

            TextView distance = new TextView(this);
            distance.setText("Distance: "
                    + String.format("%.2f",
                    workout.getDistance()) + " km");

            TextView calories = new TextView(this);
            calories.setText("Calories: "
                    + workout.getCalories()
                    + " kcal");

            layout.addView(activity);
            layout.addView(distance);
            layout.addView(calories);

            card.addView(layout);

            historyContainer.addView(card);
        }
    }
}