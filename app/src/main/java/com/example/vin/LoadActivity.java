package com.example.vin;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;

import com.example.vin.login.LoginActivity;

public class LoadActivity extends AppCompatActivity {

    final Handler handler = new Handler();
    final Runnable r = new Runnable() {
        public void run() {
           // setContentView(R.layout.activity_main);
            SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
            boolean isFirstRun = sharedPreferences.getBoolean("isFirstRun", true);
            if (isFirstRun) {
                Intent myIntent = new Intent(LoadActivity.this, LoginActivity.class);
                LoadActivity.this.startActivity(myIntent);
                finish();
            }
            else{
                Intent myIntent = new Intent(LoadActivity.this, MainActivity.class);
                LoadActivity.this.startActivity(myIntent);
                finish();
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_load);
        handler.postDelayed(r, 1500);


    }
}