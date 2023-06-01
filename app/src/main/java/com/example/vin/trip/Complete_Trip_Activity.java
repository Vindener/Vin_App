package com.example.vin.trip;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.vin.MainActivity;
import com.example.vin.R;
import com.example.vin.maps.MapsFragment;
import com.example.vin.server.Trafic;

public class Complete_Trip_Activity extends AppCompatActivity {

    Button bthGoToMap;

    TextView EndCost,EndTarifTextView,EndDuration,EndTripCost,EndDurationTrip;
    ImageView EndTripImage;

    private int selectedTransportType;
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

        EndTripImage = findViewById(R.id.EndTripImage);

        GetInfo();
        ShowPicture();
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

        double perSec = 4;

        perSec = Trafic.getTrafic(selectedTransportType);

        EndTarifTextView.setText(perSec+ " ГРН/хв ");

        selectedTransportType = sharedPreferences.getInt("TransportType",1);
    }

    private void EndTrip(){
        SharedPreferences sharedPreferences1 = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        Float balance_ = sharedPreferences1.getFloat("balance",0);

        SharedPreferences sharedPreferences = getSharedPreferences("CurrentTrip", MODE_PRIVATE);
        String costString = sharedPreferences.getString("costTrip", "");

        costString = costString.replace(",", ".");

        Double cost = Double.parseDouble(costString);
        Double userBalance = Double.parseDouble(String.valueOf(balance_));

        double new_Balance = userBalance - cost;

        SharedPreferences.Editor editor1 = sharedPreferences1.edit();

        editor1.putFloat("balance", (float) new_Balance);
        editor1.apply();

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("TripStart", false);
        editor.putString("TransportNumber", "");
        editor.putString("selected_marker_title", "");
        editor.putString("durationTrip", "");
        editor.putString("costTrip", "");

        editor.apply();

        MapsFragment.TripStarted();
    }

    private void ShowPicture() {
        if (selectedTransportType == 1) {
            Drawable drawable = getResources().getDrawable(R.drawable.ic_electric_scooter);
            EndTripImage.setImageDrawable(drawable);
        } else if (selectedTransportType == 2) {
            Drawable drawable = getResources().getDrawable(R.drawable.ic_electric_bike);
            EndTripImage.setImageDrawable(drawable);
        }
    }

    private void GoToMap(){
        Intent myIntent = new Intent(Complete_Trip_Activity.this, MainActivity.class);
        Complete_Trip_Activity.this.startActivity(myIntent);
        finish();
    }
}