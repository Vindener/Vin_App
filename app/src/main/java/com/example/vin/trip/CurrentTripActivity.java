package com.example.vin.trip;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.vin.LoadActivity;
import com.example.vin.MainActivity;
import com.example.vin.R;
import com.example.vin.maps.MapsFragment;

public class CurrentTripActivity extends AppCompatActivity {

    boolean TripStart = false;

    Button EndTrip;

    ConstraintLayout startedContainer,notStartedContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_current_trip);

        // Включение кнопки возврата на предыдущую активность
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        SharedPreferences sharedPreferences = getSharedPreferences("CurrentTrip", MODE_PRIVATE);
        TripStart = sharedPreferences.getBoolean("TripStart",false);
        String TripNum = sharedPreferences.getString("TripNumber","");

        startedContainer = findViewById(R.id.TripNotStartedContainer);
        notStartedContainer = findViewById(R.id.TripStartedContainer);

        ShowContainer();

        EndTrip = findViewById(R.id.EndTrip);

        EndTrip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { EndTrip();  }
        });



    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            // Здесь определите действия при нажатии на кнопку возврата
            onBackPressed(); // Возврат на предыдущую активность


            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        Intent resultIntent = new Intent();
        setResult(Activity.RESULT_OK, resultIntent);
        finish();
    }


    private void EndTrip(){
        SharedPreferences sharedPreferences = getSharedPreferences("CurrentTrip", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("TripStart", false);
        editor.putString("TripNumber", "");
        editor.putString("selected_marker_title", "");

        editor.apply();

        TripStart = false;
        ShowContainer();

        MapsFragment.TripStarted();

        Intent myIntent = new Intent(CurrentTripActivity.this, CameraEndActivity.class);
        CurrentTripActivity.this.startActivity(myIntent);
    }

    private void ShowContainer(){
        if (TripStart){
            Toast.makeText(this, "Поїздка іде!", Toast.LENGTH_SHORT).show();

            startedContainer.setVisibility(View.GONE);

            // Показать startedContainer
            notStartedContainer.setVisibility(View.VISIBLE);
        }
        else{
            Toast.makeText(this, "Поїздка не іде!", Toast.LENGTH_SHORT).show();
            // Скрыть startedContainer
            notStartedContainer.setVisibility(View.GONE);

            // Показать notStartedContainer
            startedContainer.setVisibility(View.VISIBLE);
        }
    }
}