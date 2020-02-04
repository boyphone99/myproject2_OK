package com.example.myproject2;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myproject2.Address.MapsActivity;
import com.example.myproject2.Home.HomeActivity;
import com.example.myproject2.Profile.InsertProfileActivity;
import com.example.myproject2.Profile.ProfileActivity;
import com.example.myproject2.Session.SessionHandler;
import com.example.myproject2.Session.User;

public class DashboardActivity extends AppCompatActivity {
    private SessionHandler session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        session = new SessionHandler(getApplicationContext());
        User user = session.getUserDetails();
        TextView welcomeText = findViewById(R.id.welcomeText);

        welcomeText.setText("Welcome "+user.getUsername()+", your session will expire on "+user.getSessionExpiryDate());

        Button profileBut = findViewById(R.id.butProfile);

        profileBut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(DashboardActivity.this, ProfileActivity.class);
                startActivity(i);
                finish();
            }
        });

        Button insertBut = findViewById(R.id.butInsert_address);
        insertBut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(DashboardActivity.this, InsertProfileActivity.class);
                startActivity(i);
                finish();
            }
        });

        Button butMap = findViewById(R.id.butMap);
        butMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(DashboardActivity.this, MapsActivity.class);
                startActivity(i);
                finish();
            }
        });

        Button butHome = findViewById(R.id.buthome);
        butHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(DashboardActivity.this, HomeActivity.class);
                startActivity(i);
                finish();
            }
        });
    }
}
