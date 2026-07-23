package com.example.stepquest;

public class Workout {

    private String activityType;
    private double distance;
    private int calories;

    public Workout(String activityType,
                   double distance,
                   int calories) {

        this.activityType = activityType;
        this.distance = distance;
        this.calories = calories;
    }

    public String getActivityType() {
        return activityType;
    }

    public double getDistance() {
        return distance;
    }

    public int getCalories() {
        return calories;
    }
}