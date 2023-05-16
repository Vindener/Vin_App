package com.example.vin;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import com.example.vin.qrcode.scanner.QrCodeScanner;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.journeyapps.barcodescanner.ScanContract;
import com.journeyapps.barcodescanner.ScanOptions;


import java.security.Permission;

public class MapsFragment extends Fragment implements OnMapReadyCallback {
    private GoogleMap mMap;
    private Button btnShowCurrentLocation;

    private Button bthQRCodeScanner;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_maps, container, false);

        bthQRCodeScanner = view.findViewById(R.id.bthQRCodeScanner);
        bthQRCodeScanner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { scanCode();  }
        });

        btnShowCurrentLocation = view.findViewById(R.id.btnShowCurrentLocation);
        btnShowCurrentLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showCurrentLocation();
            }
        });

        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }

        return view;
    }


    public void scanCode(){
        ScanOptions options = new ScanOptions();
        options.setPrompt("Кнопку гучності вверх - включити ліхтар \n Кнопку гучності вниз - виключити");
        options.setBeepEnabled(true);
        options.setOrientationLocked(true);
        options.setCaptureActivity(QrCodeScanner.class);
        barLauncer.launch(options);
    }

    ActivityResultLauncher<ScanOptions> barLauncer = registerForActivityResult(new ScanContract(),result ->{
        if(result.getContents() != null){
            Toast.makeText(getActivity(), "Result: "+ result.getContents(), Toast.LENGTH_SHORT).show();
        }
    });

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        showCurrentLocation();
    }
    private static final int REQUEST_LOCATION_PERMISSION = 123;
    private void showCurrentLocation() {


        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // Запросите разрешения на доступ к местоположению, если они не были предоставлены
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, REQUEST_LOCATION_PERMISSION);
            return;
        }


        // Получите доступ к провайдеру местоположения

        mMap.setMyLocationEnabled(true);
        LocationManager locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        Location location = locationManager.getLastKnownLocation(LocationManager.PASSIVE_PROVIDER);

        //Passive_Provider or GpS

        if (location != null) {
            if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                mMap.setMyLocationEnabled(true);
                return;
            }
            mMap.setMyLocationEnabled(true);
            // Получите широту и долготу текущего местоположения
            double latitude = location.getLatitude();
            double longitude = location.getLongitude();

            // Создайте маркер для текущего местоположения
            LatLng currentLocation = new LatLng(latitude, longitude);
          //  MarkerOptions markerOptions = new MarkerOptions().position(currentLocation).title("Мое местоположение");

            // Добавьте маркер на карту
           // mMap.addMarker(markerOptions);

            // Переместите камеру на текущее местоположение
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLocation, 15));
        } else {
            Toast.makeText(getActivity(), "Не удалось определить текущее местоположение", Toast.LENGTH_SHORT).show();
        }
    }
}