package com.example.stepquest;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.os.SystemClock;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.Priority;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class TrackingActivity extends AppCompatActivity
        implements OnMapReadyCallback {

    private GoogleMap mMap;

    private FusedLocationProviderClient fusedLocationClient;
    private Location previousLocation;
    private LocationCallback locationCallback;

    TextView activityTitle;
    TextView distanceText;
    TextView caloriesText;

    Chronometer chronometer;
    Button stopBtn;

    private double distance = 0.0;
    private int calories = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tracking);

        activityTitle = findViewById(R.id.activityTitle);
        distanceText = findViewById(R.id.distanceText);
        caloriesText = findViewById(R.id.caloriesText);
        chronometer = findViewById(R.id.chronometer);
        stopBtn = findViewById(R.id.stopBtn);

        SupportMapFragment mapFragment =
                (SupportMapFragment)
                        getSupportFragmentManager()
                                .findFragmentById(R.id.map);

        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }

        String activity =
                getIntent().getStringExtra("activityType");

        activityTitle.setText(activity);

        chronometer.setBase(
                SystemClock.elapsedRealtime());

        chronometer.start();

        fusedLocationClient =
                LocationServices.getFusedLocationProviderClient(this);

        LocationRequest locationRequest =
                new LocationRequest.Builder(
                        Priority.PRIORITY_HIGH_ACCURACY,
                        3000)
                        .build();

        locationCallback = new LocationCallback() {

            @Override
            public void onLocationResult(
                    LocationResult locationResult) {

                if (locationResult == null) {
                    return;
                }

                Location currentLocation =
                        locationResult.getLastLocation();

                if (currentLocation == null) {
                    return;
                }

                // Update Google Map
                if (mMap != null) {

                    LatLng currentPosition =
                            new LatLng(
                                    currentLocation.getLatitude(),
                                    currentLocation.getLongitude());

                    mMap.clear();

                    mMap.addMarker(
                            new MarkerOptions()
                                    .position(currentPosition)
                                    .title("Current Location"));

                    mMap.moveCamera(
                            CameraUpdateFactory.newLatLngZoom(
                                    currentPosition,
                                    17));
                }

                // Calculate distance
                if (previousLocation != null) {

                    float meters =
                            previousLocation.distanceTo(
                                    currentLocation);

                    distance += meters / 1000.0;

                    if (activity.equals("Walking")) {

                        calories = (int) (distance * 50);

                    } else if (activity.equals("Running")) {

                        calories = (int) (distance * 70);

                    } else {

                        calories = (int) (distance * 30);
                    }

                    distanceText.setText(
                            "Distance: "
                                    + String.format("%.2f",
                                    distance)
                                    + " km");

                    caloriesText.setText(
                            "Calories: "
                                    + calories
                                    + " kcal");
                }

                previousLocation = currentLocation;
            }
        };

        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {

            fusedLocationClient.requestLocationUpdates(
                    locationRequest,
                    locationCallback,
                    getMainLooper());
        }

        stopBtn.setOnClickListener(v -> {

            chronometer.stop();

            fusedLocationClient.removeLocationUpdates(
                    locationCallback);

            AlertDialog.Builder builder =
                    new AlertDialog.Builder(
                            TrackingActivity.this);

            builder.setTitle("Workout Complete");

            builder.setMessage(
                    "Activity: " + activity
                            + "\nDistance: "
                            + String.format("%.2f",
                            distance)
                            + " km"
                            + "\nCalories: "
                            + calories
                            + " kcal"
            );

            builder.setPositiveButton(
                    "OK",
                    (dialog, which) -> {

                        Workout workout =
                                new Workout(
                                        activity,
                                        distance,
                                        calories);

                        WorkoutData.workoutList.add(
                                workout);

                        finish();
                    });

            builder.show();
        });
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        mMap = googleMap;
    }
}