package com.example.vin.trip;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.vin.MainActivity;
import com.example.vin.R;
import com.example.vin.maps.MapsFragment;
import com.example.vin.payment.UpdateWalletBalanceDataTask;
import com.example.vin.server.Trafic;
import com.example.vin.server.updateTransportStan;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.LatLng;

public class Complete_Trip_Activity extends AppCompatActivity {

    private Button bthGoToMap;

    private TextView EndCost, EndTarifTextView, EndDuration, EndTripCost, EndDurationTrip;
    private ImageView EndTripImage;
    private double new_Balance;
    private int userIndex,transportIndex;
    private double costTrip;
    private String durationTrip,StartTime,EndTime="2023-05-30T12:00:00Z";
    private double TransportX= 49.8926838;
    private double TransportY= 28.5903351;
    private int selectedTransportType;

    private static final int REQUEST_LOCATION_PERMISSION = 1;

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

        // Проверка разрешений
        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this,
                        Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    REQUEST_LOCATION_PERMISSION);
        } else {
            getCurrentLocation();
        }

        GetInfo();
        EndTrip();
        EndTripServer();
        ShowPicture();


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
        durationTrip = sharedPreferences.getString("durationTrip", "");
        StartTime = sharedPreferences.getString("CurrentDateTrip","");
        EndTime = sharedPreferences.getString("EndTime","");

        String selectedMarkerTitle = sharedPreferences.getString("TransportNumber", "");
        transportIndex= Integer.parseInt(selectedMarkerTitle);

        EndCost.setText(costString + "грн.");
        EndDuration.setText("* " + durationTrip + "хв.");
        EndTripCost.setText("= " + costString);

        EndDurationTrip.setText(durationTrip + "хв.");

        double perSec = 4;

        perSec = Trafic.getTrafic(selectedTransportType);

        EndTarifTextView.setText(perSec + " ГРН/хв ");

        selectedTransportType = sharedPreferences.getInt("TransportType", 1);

    }

    private void EndTrip() {
        SharedPreferences sharedPreferences1 = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        Float balance_ = sharedPreferences1.getFloat("balance", 0);
        userIndex = sharedPreferences1.getInt("userIndex", 1);

        SharedPreferences sharedPreferences = getSharedPreferences("CurrentTrip", MODE_PRIVATE);
        String costString = sharedPreferences.getString("costTrip", "");

        costString = costString.replace(",", ".");

        costTrip = Double.parseDouble(costString);
        double userBalance = Double.parseDouble(String.valueOf(balance_));

        new_Balance = userBalance - costTrip;

        SharedPreferences.Editor editor1 = sharedPreferences1.edit();

        editor1.putFloat("balance", (float) new_Balance);
        editor1.apply();

        Toast.makeText(this, " New balance - " + new_Balance + " cost - " + costTrip, Toast.LENGTH_SHORT).show();

        updateTransportStan(transportIndex, TransportX, TransportY, 50, 1);

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("TripStart", false);
        editor.putString("TransportNumber", "");
        editor.putString("selected_marker_title", "");
        editor.putString("durationTrip", "");
        editor.putString("costTrip", "");

        editor.apply();

        UpdateWalletBalance();

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

    private void GoToMap() {
        Intent myIntent = new Intent(Complete_Trip_Activity.this, MainActivity.class);
        Complete_Trip_Activity.this.startActivity(myIntent);
        finish();
    }

    public void updateTransportStan(int index, double corX_, double corY_, int battery_, int stan) {
        int transportIndex = index;
        double corX = corX_;
        double corY = corY_;
        int battery = battery_;
        int stanId = stan;

        updateTransportStan updateTransportStanTask = new updateTransportStan(transportIndex, corX, corY, battery, stanId);
        updateTransportStanTask.execute();
    }

    private void UpdateWalletBalance() {
        UpdateWalletBalanceDataTask updateTask = new UpdateWalletBalanceDataTask(Complete_Trip_Activity.this, String.valueOf(userIndex), new_Balance);
        updateTask.execute();
    }

    private void EndTripServer() {
        String placeId = "2";

        CreateTripDataTask createTripTask = new CreateTripDataTask(new CreateTripDataTask.OnTripCreatedListener() {
            @Override
            public void onTripCreated(boolean success) {
                if (success) {
                    // Поездка успешно создана
                    Toast.makeText(Complete_Trip_Activity.this, "поїздка  успешно созданы", Toast.LENGTH_SHORT).show();
                } else {
                    // Ошибка при создании поездки
                    Toast.makeText(Complete_Trip_Activity.this, "Ошибка при создании данных поїздка", Toast.LENGTH_SHORT).show();
                }
            }
        });
        createTripTask.execute(String.valueOf(transportIndex), String.valueOf(userIndex), placeId, StartTime, EndTime, durationTrip, String.valueOf(costTrip));

    }

    private void getCurrentLocation() {
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        Location location = locationManager.getLastKnownLocation(LocationManager.PASSIVE_PROVIDER);

        if (location != null) {
            TransportX = location.getLatitude();
            TransportY = location.getLongitude();

            // Используйте полученные координаты по вашему усмотрению
            // Например, отобразите их в текстовом поле или выполните другие операции
        }
    }

    @Override
    public void onRequestPermissionsResult(
            int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_LOCATION_PERMISSION) {
            if (grantResults.length > 0 &&
                    grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getCurrentLocation();
            }
        }
    }
}