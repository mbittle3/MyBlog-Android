package com.demo.myblog;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.demo.myblog.profile.UserProfile;

public class Dashboard extends AppCompatActivity {
    Button btn_Profile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        btn_Profile = findViewById(R.id.btn_Profile);

        btn_Profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), UserProfile.class);
                startActivity(intent);
            }
        });

    }
}