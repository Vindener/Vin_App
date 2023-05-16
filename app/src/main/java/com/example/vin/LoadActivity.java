package com.example.vin;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

public class LoadActivity extends AppCompatActivity {

    final Handler handler = new Handler();
    final Runnable r = new Runnable() {
        public void run() {
           // setContentView(R.layout.activity_main);
            Intent myIntent = new Intent(LoadActivity.this, MainActivity.class);
            LoadActivity.this.startActivity(myIntent);
            finish();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_load);
        handler.postDelayed(r, 1500);


    }
}