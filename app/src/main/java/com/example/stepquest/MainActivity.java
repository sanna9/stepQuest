package com.example.stepquest;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    Button loginBtn;
    EditText emailInput;
    EditText passwordInput;

    private SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        loginBtn = findViewById(R.id.loginBtn);
        emailInput = findViewById(R.id.emailInput);
        passwordInput = findViewById(R.id.passwordInput);

        prefs = getSharedPreferences(
                "StepQuestUsers",
                MODE_PRIVATE);

        // Auto Login
        boolean loggedIn =
                prefs.getBoolean(
                        "loggedIn",
                        false);

        if (loggedIn) {

            String email =
                    prefs.getString(
                            "currentUser",
                            "");

            Intent intent =
                    new Intent(
                            MainActivity.this,
                            DashboardActivity.class);

            intent.putExtra(
                    "userEmail",
                    email);

            startActivity(intent);
            finish();

            return;
        }

        loginBtn.setOnClickListener(view -> {

            String email =
                    emailInput.getText()
                            .toString()
                            .trim();

            String password =
                    passwordInput.getText()
                            .toString()
                            .trim();

            // Email Required
            if (email.isEmpty()) {

                Toast.makeText(
                        MainActivity.this,
                        "Please enter an email",
                        Toast.LENGTH_SHORT
                ).show();

                return;
            }

            // Password Required
            if (password.isEmpty()) {

                Toast.makeText(
                        MainActivity.this,
                        "Please enter a password",
                        Toast.LENGTH_SHORT
                ).show();

                return;
            }

            // Password Length Check
            if (password.length() < 5) {

                Toast.makeText(
                        MainActivity.this,
                        "Password must be at least 5 characters",
                        Toast.LENGTH_SHORT
                ).show();

                return;
            }

            String savedPassword =
                    prefs.getString(
                            email,
                            null);

            // First Time User
            if (savedPassword == null) {

                prefs.edit()
                        .putString(email, password)
                        .apply();

                Toast.makeText(
                        MainActivity.this,
                        "Account Created",
                        Toast.LENGTH_SHORT
                ).show();

                openDashboard(email);
            }

            // Existing User
            else {

                if (savedPassword.equals(password)) {

                    Toast.makeText(
                            MainActivity.this,
                            "Login Successful",
                            Toast.LENGTH_SHORT
                    ).show();

                    openDashboard(email);

                } else {

                    Toast.makeText(
                            MainActivity.this,
                            "Incorrect Password",
                            Toast.LENGTH_SHORT
                    ).show();
                }
            }
        });
    }

    private void openDashboard(String email) {

        prefs.edit()
                .putBoolean("loggedIn", true)
                .putString("currentUser", email)
                .apply();

        Intent intent =
                new Intent(
                        MainActivity.this,
                        DashboardActivity.class);

        intent.putExtra(
                "userEmail",
                email);

        startActivity(intent);
        finish();
    }
}