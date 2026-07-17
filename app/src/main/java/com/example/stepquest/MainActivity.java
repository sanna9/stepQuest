package com.example.stepquest;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    Button loginBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        loginBtn = findViewById(R.id.loginBtn);
        EditText emailInput = findViewById(R.id.emailInput);
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println("Login clicked");
                String email = emailInput.getText().toString();

                // using Explicit Intent moving to Dashboard
                Intent intent = new Intent(
                        MainActivity.this,
                        DashboardActivity.class);

                intent.putExtra("userEmail", email);

                startActivity(intent);
            }
        });
    }
}