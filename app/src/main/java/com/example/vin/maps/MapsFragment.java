package com.example.vin.maps;

import static android.content.Context.MODE_PRIVATE;

import androidx.activity.result.ActivityResultLauncher;

import android.Manifest;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.example.vin.R;
import com.example.vin.addition.PermissionDialogFragment;
import com.example.vin.qrcode.scanner.QrCodeScanner;
import com.example.vin.server.Trafic;
import com.example.vin.trip.CurrentTripActivity;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.PolygonOptions;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.journeyapps.barcodescanner.ScanContract;
import com.journeyapps.barcodescanner.ScanOptions;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MapsFragment extends Fragment implements OnMapReadyCallback {
    private GoogleMap mMap;
    private Polygon polygonMap;
    private static Button bthGoToTrip;
    private List<Transport> transports = new ArrayList<>();
    private Marker selectedMarker; // Вибраний маркер
    private String  selectedMarkerTitle = "";
    private int selectedTransportType = 1;
    private String currentDate= null;
    private Double userBalance = 0.0;
    private boolean isBottomSheetAllowed = true;
    final Handler handler = new Handler();
    final Runnable r = new Runnable() {
        public void run() {
            ShowProfileiInfo();
        }
    };

    static Context context;

    //Location
    public static boolean isLocationEnabled(Context context) {
        LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        return locationManager != null && locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
    }

    @Override
    public void onResume() {
        super.onResume();
        //Перевірка на доступність місцеположення
        CheckPermissions();
        CheckBalance();
    }


    //Internet
    private boolean isInternetAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected();
    }
    private void CheckPermissions(){
        boolean isInternetEnabled = isInternetAvailable();
        boolean isLocationEnabled = isLocationEnabled(requireContext());

        if (!isInternetEnabled || !isLocationEnabled) {
            String message = !isInternetEnabled ? "Интернет недоступен" : "Местоположение не включено";
            PermissionDialogFragment dialogFragment = PermissionDialogFragment.newInstance(message);
            dialogFragment.show(getFragmentManager(), "ConfirmationDialogFragment");
        }
    }

    private void CheckBalance(){
        if(userBalance<0.){
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            String value = "У вас існує заборговоність в сумі "+ userBalance +". Поповніть свій баланс!";

            builder.setMessage(value);

            builder.setPositiveButton("Добре", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    // Закрытие диалогового окна
                    dialog.dismiss();
                }
            });

            AlertDialog dialog = builder.create();
            dialog.show();
        }
    }


    public void DrawPolygon(){
        if(polygonMap!=null){
            polygonMap.remove();
        }
        // Створення массив з координатами
        LatLng[] coordinates = new LatLng[10];
        coordinates[0] = new LatLng(49.944961, 28.611455);
        coordinates[1] = new LatLng(49.939776, 28.617922);
        coordinates[2] = new LatLng(49.930024, 28.627584);
        coordinates[3] = new LatLng(49.900735, 28.632276);
        coordinates[4] = new LatLng(49.860025, 28.615820);
        coordinates[5] = new LatLng(49.873121, 28.568991);
        coordinates[6] = new LatLng(49.876661, 28.554013);
        coordinates[7] = new LatLng(49.907011, 28.556739);
        coordinates[8] = new LatLng(49.933163, 28.586319);
        coordinates[9] = new LatLng(49.942839, 28.603362);

        // Створення об'єкта PolygonOptions
        PolygonOptions polygonOptions = new PolygonOptions();
        polygonOptions.strokeWidth(5);
        polygonOptions.strokeColor(Color.argb(160, 255, 0, 0));
        polygonOptions.fillColor(Color.argb(50, 0, 255, 0)); // Задаем полупрозрачный зеленый цвет (128 - уровень прозрачности)

        // Додавання координати в PolygonOptions
        for (LatLng coordinate : coordinates) {
            polygonOptions.add(coordinate);
        }

        // Додаємо полігон на карту
        polygonMap = mMap.addPolygon(polygonOptions);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_maps, container, false);

        Button bthQRCodeScanner = view.findViewById(R.id.bthQRCodeScanner);
        bthQRCodeScanner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { scanCode();  }
        });

        bthGoToTrip =  view.findViewById(R.id.GoToTrip);

        Button btnShowCurrentLocation = view.findViewById(R.id.btnShowCurrentLocation);
        btnShowCurrentLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showCurrentLocation();
            }
        });

        Button bthShowCurrentTrip = view.findViewById(R.id.GoToTrip);
        bthShowCurrentTrip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showCurrentTrip();
            }
        });

        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);

        }

        context = getContext();

        //Прогрузка інформації користувача
        handler.postDelayed(r, 200);
        //ShowProfileiInfo();

        addMarker("11",true,49.8926838, 28.5903351,1,50);
        addMarker("12",true,49.892613, 28.590552,2,30);
        addMarker("1",false,49.886918, 28.594652,1,40);
        addMarker("14",true,49.894497, 28.582444,2,50);
        addMarker("15",false,49.894579, 28.582607,1,70);
        addMarker("16",false,49.895602, 28.583382,1,80);
        addMarker("17",true,49.895748, 28.583480,1,100);

        return view;
    }

    //Qr Code Scanner
    public void scanCode(){
        ScanOptions options = new ScanOptions();
        options.setPrompt("Кнопку гучності вверх - включити ліхтар \n Кнопку гучності вниз - виключити");
        options.setBeepEnabled(true);
        options.setOrientationLocked(true);
        options.setCaptureActivity(QrCodeScanner.class);
        barLauncer.launch(options);
    }

    //Go To Curennt Trip
    public void showCurrentTrip(){
        Intent myIntent = new Intent(getActivity(), CurrentTripActivity.class);
        getActivity().startActivity(myIntent);
    }

    ActivityResultLauncher<ScanOptions> barLauncer = registerForActivityResult(new ScanContract(),result ->{
        if(result.getContents() != null){
            String resultQr = result.getContents();

            Pattern pattern = Pattern.compile("<vinmetz>(\\d+)<vinmetz>");
            Matcher matcher = pattern.matcher(resultQr);

            boolean found = false;

            if (matcher.find()) {
                String value = matcher.group(1);
                for (Transport transport : transports) {
                    if (transport.getTitle().equals(value) && transport.isFree()) {
                        Marker marker = transport.getMarker();
                        if (marker != null) {
                            selectedMarkerTitle = value;
                            selectedMarker = marker;
                            showBottomSheet(value);
                        }
                        found = true;
                        break;
                    }
                }
                if (!found) {
                    Toast.makeText(getActivity(), "Цей транспорт вже зайнято!", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(getActivity(), "Ви просканували не те!", Toast.LENGTH_SHORT).show();
            }

        }
    });

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        showCurrentLocation();

        DrawPolygon();

        TripStarted();
        TripStared_1();

        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                // Обробка кліку по маркеру
                String markerId = marker.getTitle(); // Отримання id маркера
                selectedMarker = marker;

                // Показ нижнього вікна активити з текстом і кнопками
                showBottomSheet(markerId);
                return true;
            }
        });
    }

    private void showBottomSheet(String markerId) {
        if (!isBottomSheetAllowed) {
            // Те що відображення нижнього вікна заборонено
            return;
        }
        // Створення нижнього вікна активити
        Context context = requireContext();
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(context);

        bottomSheetDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        bottomSheetDialog.setContentView(R.layout.bottom_sheet_layout);

        TextView textView = bottomSheetDialog.findViewById(R.id.textView);

        TextView bottomSheetTrafic = bottomSheetDialog.findViewById(R.id.bottomSheetTrafic);
        Button button1 = bottomSheetDialog.findViewById(R.id.StartTrip);

        TextView batteryLevel = bottomSheetDialog.findViewById(R.id.batteryLevel);
        ImageView batteryImage = bottomSheetDialog.findViewById(R.id.baterryImage);

        ImageView bottomSheetImage = bottomSheetDialog.findViewById(R.id.bottomSheetImage);
        int batteryValueLevel;

        for (Transport transport : transports) {
            if (transport.getTitle().equals(markerId)) {
                Marker marker = transport.getMarker();
                if (marker != null) {
                    if (transport.getType() == 1) {
                        Drawable drawable = getResources().getDrawable(R.drawable.ic_electric_scooter);
                        bottomSheetImage.setImageDrawable(drawable);
                        selectedTransportType = 1;
                    } else if (transport.getType() == 2) {
                        Drawable drawable = getResources().getDrawable(R.drawable.ic_electric_bike);
                        bottomSheetImage.setImageDrawable(drawable);
                        selectedTransportType = 2;
                    }
                    batteryValueLevel = transport.getBatteryLevel();
                    batteryLevel.setText(String.valueOf(batteryValueLevel)+"%");
                    if(batteryValueLevel == 100){
                        Drawable drawable = getResources().getDrawable(R.drawable.battery_full);
                        batteryImage.setImageDrawable(drawable);
                        break;
                    }else if(batteryValueLevel > 70){
                        Drawable drawable = getResources().getDrawable(R.drawable.battery_6_bar);
                        batteryImage.setImageDrawable(drawable);
                        break;
                    }else if (batteryValueLevel >50) {
                        Drawable drawable = getResources().getDrawable(R.drawable.battery_5_bar);
                        batteryImage.setImageDrawable(drawable);
                        break;
                    }else if (batteryValueLevel >40) {
                        Drawable drawable = getResources().getDrawable(R.drawable.battery_4_bar);
                        batteryImage.setImageDrawable(drawable);
                        break;
                    }
                    else if (batteryValueLevel >25) {
                        Drawable drawable = getResources().getDrawable(R.drawable.battery_3_bar);
                        batteryImage.setImageDrawable(drawable);
                        break;
                    }
                    else if (batteryValueLevel >10) {
                        Drawable drawable = getResources().getDrawable(R.drawable.battery_2_bar);
                        batteryImage.setImageDrawable(drawable);
                        break;
                    }
                    else if (batteryValueLevel >10) {
                        Drawable drawable = getResources().getDrawable(R.drawable.battery_1_bar);
                        batteryImage.setImageDrawable(drawable);
                        break;
                    }
                }
                break;
            }
        }

        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ShowDialogBeforeStart();
                bottomSheetDialog.hide();
            }
        });

        textView.setText(markerId);
        bottomSheetTrafic.setText(String.valueOf(Trafic.getTrafic(selectedTransportType)));

        bottomSheetDialog.show();
    }

    private void ShowDialogBeforeStart(){
        if(userBalance >= 100) {
            // Створення діалогового вікна
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setMessage("Перевірте, чи все добре з транспортом. ");
            // Встановлення кнопки "Так все, добре"
            builder.setPositiveButton("Так все, добре.", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    // Початок поїздки
                    StartTrip();
                }
            });
            // Встановлення кнопки "Ні"
            builder.setNegativeButton("Ні", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    // Закриття діалогового вікна
                    dialog.dismiss();
                }
            });

        // Створення та відображення діалогового вікна.
            AlertDialog dialog = builder.create();
            dialog.show();
        }
        else{
            Toast.makeText(getActivity(), "Для початку поїздку ви повинні мати як мінімум на балнасі 100 грн!", Toast.LENGTH_SHORT).show();
        }
    }

    //Початок поїздки
    private void StartTrip(){
        SharedPreferences sharedPreferences = getContext().getSharedPreferences("CurrentTrip", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("TripStart", true);
        selectedMarkerTitle = selectedMarker.getTitle();

        editor.putString("TransportNumber", selectedMarkerTitle);
        editor.putString("selected_marker_title", selectedMarkerTitle);

        editor.putInt("TransportType", selectedTransportType);

        GetCurrentDate();
        editor.putString("CurrentDateTrip", currentDate);

        editor.apply();

        Toast.makeText(getActivity(), "Поїздка почалась!", Toast.LENGTH_SHORT).show();

        HideTransport();
        TripStarted();
        TripStared_1();
        showMarkerByTitle();
    }

    public static void TripStarted(){
        SharedPreferences sharedPreferences = context.getSharedPreferences("CurrentTrip", Context.MODE_PRIVATE);
        boolean TripStarted = sharedPreferences.getBoolean("TripStart",false);

        if (TripStarted) {
            bthGoToTrip.setVisibility(View.VISIBLE);
        }
        else{
            bthGoToTrip.setVisibility(View.GONE);
        }
    }

    public void TripStared_1(){
        SharedPreferences sharedPreferences = context.getSharedPreferences("CurrentTrip", Context.MODE_PRIVATE);
        boolean TripStarted = sharedPreferences.getBoolean("TripStart",false);
        selectedMarkerTitle = sharedPreferences.getString("selected_marker_title", "");
        isBottomSheetAllowed = !TripStarted;

        if (TripStarted) {
            ShowTransport();
            HideTransport();
            showMarkerByTitle();
        }else{
            HideTransport();
            ShowTransport();
        }
    }


    private static final int REQUEST_LOCATION_PERMISSION = 123;
    private void showCurrentLocation() {
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // Запит разрешения на доступ до місцеположення, якщо їх не було
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, REQUEST_LOCATION_PERMISSION);
            return;
        }

        // Отримання доступу до провайдера місцеположення
        mMap.setMyLocationEnabled(true);
        LocationManager locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        Location location = locationManager.getLastKnownLocation(LocationManager.PASSIVE_PROVIDER);

        if (location != null) {
            if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                mMap.setMyLocationEnabled(true);
                return;
            }
            mMap.setMyLocationEnabled(true);
            // Отримання широти и довготи поточного місцеположення
            double latitude = location.getLatitude();
            double longitude = location.getLongitude();

            // Створення маркера для поточного місцеположення
            LatLng currentLocation = new LatLng(latitude, longitude);

            // Переміщенян камери на поточне місцеположення
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLocation, 15));
        } else {
            Toast.makeText(getActivity(), "Не вдалось отримати поточне місцеположення!", Toast.LENGTH_SHORT).show();
        }
    }

    private BitmapDescriptor bitmapDescriptorVector(Context context,int vectorResId){
        Drawable vectorDrawable = ContextCompat.getDrawable(context,vectorResId);
        vectorDrawable.setBounds(0,0,vectorDrawable.getIntrinsicWidth(),vectorDrawable.getIntrinsicHeight());
        Bitmap bitmap = Bitmap.createBitmap(vectorDrawable.getIntrinsicWidth(),
                vectorDrawable.getIntrinsicHeight(),Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        vectorDrawable.draw(canvas);
        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }

    public void ShowTransport() {
        for (Transport transport : transports) {
            if (transport.isFree()) {
                LatLng position = new LatLng(transport.getLatitude(), transport.getLongitude());
                MarkerOptions markerOptions = new MarkerOptions().position(position).title(transport.getTitle());

                if (transport.getType() == 1) {
                    markerOptions.icon(bitmapDescriptorVector(getActivity(),R.drawable.ic_electric_scooter));
                } else if (transport.getType() == 2) {
                    markerOptions.icon(bitmapDescriptorVector(getActivity(),R.drawable.ic_electric_bike));
                }
                Marker marker = mMap.addMarker(markerOptions);
                transport.setMarker(marker);
            }
        }
    }

    public void addMarker(String title, boolean isFree, double latitde,double longitude,int type, int battery) {
        Transport transport = new Transport(title, isFree, latitde, longitude,type,battery);
        transports.add(transport);
    }

    public void HideTransport() {
        for (Transport transport : transports) {
            Marker marker = transport.getMarker();
            if (marker != selectedMarker && marker!= null) {
                marker.setVisible(false);
            }
        }
    }

    private void showMarkerByTitle() {
        if (!selectedMarkerTitle.isEmpty()) {
            for (Transport transport : transports) {
                if (transport.getTitle().equals(selectedMarkerTitle)) {
                    Marker marker = transport.getMarker();
                    if (marker != null) {
                        marker.setVisible(true);
                        mMap.animateCamera(CameraUpdateFactory.newLatLng(marker.getPosition()));
                    }
                    break;
                }
            }
        }
    }

    public void ShowProfileiInfo(){
        SharedPreferences sharedPreferences = getContext().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        String phone_ = sharedPreferences.getString("phone","");
        String name_ = sharedPreferences.getString("name","");
        Float balance_ = sharedPreferences.getFloat("balance",0);

        TextView name = getActivity().findViewById(R.id.headerName);
        name.setText(name_);

        TextView phone =  getActivity().findViewById(R.id.headerPhone);
        phone.setText(phone_);

        userBalance = Double.parseDouble(String.valueOf(balance_));

        TextView balance =  getActivity().findViewById(R.id.BalanceText);

        DecimalFormat decimalFormat = new DecimalFormat("#.##");
        String formattedBalance = decimalFormat.format(userBalance);
        balance.setText(formattedBalance.toString());
    }

    private void GetCurrentDate(){
        // Отримуємо дату і час зараз
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

        // Форматуємо дату і час в String
        currentDate = dateFormat.format(calendar.getTime());
    }

}