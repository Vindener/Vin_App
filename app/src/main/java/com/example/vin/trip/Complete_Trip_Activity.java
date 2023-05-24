package com.example.vin.trip;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.vin.MainActivity;
import com.example.vin.R;
import com.example.vin.maps.MapsFragment;

public class Complete_Trip_Activity extends AppCompatActivity {

    Button bthGoToMap;

    TextView EndCost,EndTarifTextView,EndDuration,EndTripCost,EndDurationTrip;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_complete_trip);

        bthGoToMap = findViewById(R.id.bthGoToMap);

        EndCost = findViewById(R.id.EndCost);
        EndTarifTextView = findViewById(R.id.EndTarifTextView);
        EndDuration = findViewById(R.id.EndDuration);
        EndTripCost = findViewById(R.id.EndTripCost);
        EndDurationTrip = findViewById(R.id.EndDurationTrip);


        EndTarifTextView.setText("4.0 ГРН/хв ");
        GetInfo();
        EndTrip();

        bthGoToMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GoToMap();
            }
        });
    }

    private void GetInfo() {
        SharedPreferences sharedPreferences = getSharedPreferences("CurrentTrip", MODE_PRIVATE);
        String costString = sharedPreferences.getString("costTrip", "");
        String duration = sharedPreferences.getString("durationTrip", "");

        EndCost.setText(costString + "грн.");
        EndDuration.setText("* "+duration + "хв.");
        EndTripCost.setText("= "+ costString);

        EndDurationTrip.setText(duration + "хв.");

    }

    private void EndTrip(){
        SharedPreferences sharedPreferences1 = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        Float balance_ = sharedPreferences1.getFloat("balance",0);

        SharedPreferences sharedPreferences = getSharedPreferences("CurrentTrip", MODE_PRIVATE);
        String costString = sharedPreferences.getString("costTrip", "");

        Double cost = Double.parseDouble(costString);
        Double userBalance = Double.parseDouble(String.valueOf(balance_));

        double new_Balance = userBalance - cost;

        SharedPreferences.Editor editor1 = sharedPreferences1.edit();

        editor1.putFloat("balance", (float) new_Balance);
        editor1.apply();

        Toast.makeText(this, " New balance - "+ new_Balance + " cost - "+cost, Toast.LENGTH_SHORT).show();


        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("TripStart", false);
        editor.putString("TransportNumber", "");
        editor.putString("selected_marker_title", "");
        editor.putString("durationTrip", "");
        editor.putString("costTrip", "");



        editor.apply();

        MapsFragment.TripStarted();
    }

    private void GoToMap(){
        Intent myIntent = new Intent(Complete_Trip_Activity.this, MainActivity.class);
        Complete_Trip_Activity.this.startActivity(myIntent);
        finish();
    }
}