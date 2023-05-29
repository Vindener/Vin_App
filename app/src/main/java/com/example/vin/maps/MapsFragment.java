package com.example.vin.maps;

import static android.content.Context.MODE_PRIVATE;

import androidx.activity.result.ActivityResultLauncher;

import android.Manifest;

import android.content.Context;
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
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.util.Log;
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
import com.example.vin.qrcode.scanner.QrCodeScanner;
import com.example.vin.server.City;
import com.example.vin.server.CreateTransport;
import com.example.vin.server.GetCityDataTask;
import com.example.vin.server.GetTransportDataTask;
import com.example.vin.server.GetTransportTypeTask;
import com.example.vin.server.Trafic;
import com.example.vin.trip.CameraEndActivity;
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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

public class MapsFragment extends Fragment implements OnMapReadyCallback {

    private Button testbutton;


    private GoogleMap mMap;
    private Polygon polygonMap;
    private Button btnShowCurrentLocation;
    private Button bthQRCodeScanner;
    private static Button bthGoToTrip;

    private Button bthShowCurrentTrip;

    private List<Transport> transports = new ArrayList<>();
    private Marker selectedMarker; // Выбранный маркер

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

    public static boolean isLocationEnabled(Context context) {
        LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        return locationManager != null && locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
    }

    public static void openLocationSettings(Context context) {
        Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
        context.startActivity(intent);
    }

    public void DrawPolygon(){
        if(polygonMap!=null){
            polygonMap.remove();
        }
            for (City city : cityList) {

                double[] coordinates = city.getCoordinates();

                // Создаем объект PolygonOptions для текущего объекта City
                PolygonOptions polygonOptions = new PolygonOptions();
                polygonOptions.strokeWidth(5);
                polygonOptions.strokeColor(Color.argb(160, 255, 0, 0));
                polygonOptions.fillColor(Color.argb(50, 0, 255, 0)); // Задаем полупрозрачный зеленый цвет (128 - уровень прозрачности)

                // Добавляем координаты в PolygonOptions
                for (int i = 0; i < coordinates.length; i += 2) {
                    double latitude = coordinates[i];
                    double longitude = coordinates[i + 1];
                    LatLng coordinate = new LatLng(latitude, longitude);
                    polygonOptions.add(coordinate);
                }

                // Добавляем полигон на карту
                mMap.addPolygon(polygonOptions);
                }


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_maps, container, false);
        WorkServer();

        if (isLocationEnabled(requireActivity())) {
            // Местоположение включено
        } else {
            openLocationSettings(requireActivity());
        }

        bthQRCodeScanner = view.findViewById(R.id.bthQRCodeScanner);
        bthQRCodeScanner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { scanCode();  }
        });

        bthGoToTrip =  view.findViewById(R.id.GoToTrip);


        btnShowCurrentLocation = view.findViewById(R.id.btnShowCurrentLocation);
        btnShowCurrentLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showCurrentLocation();
            }
        });

        testbutton = view.findViewById(R.id.testbutton);
        testbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                test();
            }
        });

        bthShowCurrentTrip = view.findViewById(R.id.GoToTrip);
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

        addMarker("11",true,49.8926838, 28.5903351,1);
        addMarker("12",true,49.892613, 28.590552,2);
        addMarker("13",false,49.886918, 28.594652,1);
        addMarker("14",true,49.894497, 28.582444,2);
        addMarker("15",false,49.894579, 28.582607,1);
        addMarker("16",false,49.895602, 28.583382,1);
        addMarker("17",true,49.895748, 28.583480,1);

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
    //Замінити Фрагмент

        Intent myIntent = new Intent(getActivity(), CurrentTripActivity.class);
        getActivity().startActivity(myIntent);
//
//        FragmentManager fragmentManager = requireParentFragment().getChildFragmentManager(); // Используйте правильный метод получения FragmentManager
//        FragmentTransaction transaction = fragmentManager.beginTransaction();
//        transaction.replace(R.id.nav_host_fragment_content_main, new CurrentTripFragment());
//
//        //transaction.replace(R.id.nav_host_fragment_content_main, new CurrentTripFragment());
//        transaction.commit();

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

        TripStarted();
        TripStared_1();

        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                // Обработка клика по маркеру
                String markerId = marker.getTitle(); // Получение id маркера
                selectedMarker = marker;

                // Отображение нижнего окна активити с текстом и кнопками
                showBottomSheet(markerId);
                return true;
            }
        });


    }

    private void showBottomSheet(String markerId) {
        if (!isBottomSheetAllowed) {
            // Булева переменная указывает, что отображение нижнего диалогового окна запрещено
            return;
        }
        // Создание нижнего окна активити
        Context context = requireContext();
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(context);

        bottomSheetDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        bottomSheetDialog.setContentView(R.layout.bottom_sheet_layout);

        TextView textView = bottomSheetDialog.findViewById(R.id.textView);

        TextView bottomSheetTrafic = bottomSheetDialog.findViewById(R.id.bottomSheetTrafic);
        Button button1 = bottomSheetDialog.findViewById(R.id.StartTrip);
        Button button2 = bottomSheetDialog.findViewById(R.id.button2);

        SharedPreferences sharedPreferences = context.getSharedPreferences("MyPrefs", MODE_PRIVATE);

        ImageView bottomSheetImage = bottomSheetDialog.findViewById(R.id.bottomSheetImage);

        double traficPrice = 3.;

        for (Transport transport : transports) {
            if (transport.getTitle().equals(markerId)) {
                Marker marker = transport.getMarker();
                if (marker != null) {
                    if (transport.getType() == 1) {
                        Drawable drawable = getResources().getDrawable(R.drawable.ic_electric_scooter);
                        bottomSheetImage.setImageDrawable(drawable);
                        Trafic firstTrafic = traficList.get(0);
                        traficPrice = firstTrafic.getPriceOf1();
                    } else if (transport.getType() == 2) {
                        Drawable drawable = getResources().getDrawable(R.drawable.ic_electric_bike);
                        bottomSheetImage.setImageDrawable(drawable);
                        Trafic firstTrafic = traficList.get(1);
                        traficPrice = firstTrafic.getPriceOf1();
                    }
                }
                break;
            }
        }

        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                StartTrip();
                bottomSheetDialog.hide();
            }
        });

        textView.setText(markerId);
        bottomSheetTrafic.setText(String.valueOf(traficPrice));

        // Настройка текста и обработчиков кнопок
        // Используйте markerId для получения информации о маркере

        bottomSheetDialog.show();
    }

    //StartTrip

    private void StartTrip(){
        if(userBalance >= 100) {
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
        else{
            Toast.makeText(getActivity(), "Для початку поїздку ви повинні мати як мінімум на балнасі 100 грн!", Toast.LENGTH_SHORT).show();
        }
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
        SharedPreferences sharedPreferences = context.getSharedPreferences("CurrentTrip", Context.MODE_PRIVATE);

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

    public void addMarker(String title, boolean isFree, double latitde,double longitude,int type) {
        Transport transport = new Transport(title, isFree, latitde, longitude,type);
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

        TextView balance =  getActivity().findViewById(R.id.BalanceText);
        balance.setText(balance_.toString());

        userBalance = Double.parseDouble(String.valueOf(balance_));
    }

    private void GetCurrentDate(){
        // Получаем текущую дату и время
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

        // Форматируем текущую дату и время в строку
        currentDate = dateFormat.format(calendar.getTime());
    }


    // Добавьте поле для списка объектов Trafic
    private List<Trafic> traficList;

    //Міста
    private List<City> cityList;

    private void WorkServer(){
        fetchCityData();
        fetchTransportData();
    }

    // Создайте метод для выполнения запроса и получения данных
    private void fetchTransportData() {
        GetTransportTypeTask task = new GetTransportTypeTask() {
            @Override
            protected void onPostExecute(List<Trafic> result) {
                // Получите данные и обновите список объектов Trafic
                traficList = result;

                // Обновите пользовательский интерфейс вашего фрагмента с использованием полученных данных
                //updateUI();
            }
        };
        task.execute();
    }

    // Создайте метод для выполнения запроса и получения данных
    private void fetchCityData() {
        GetCityDataTask task = new GetCityDataTask(getContext()) {
            @Override
            protected void onPostExecute(List<City> result) {
                // Получите данные и обновите список объектов City
                cityList = result;

                // Обновите пользовательский интерфейс вашего фрагмента или активности с использованием полученных данных
                updateUI();
                DrawPolygon();
            }
        };
        task.execute();
    }

    // Метод для обновления пользовательского интерфейса фрагмента с использованием полученных данных
    private void updateUI() {
        if (traficList != null && !traficList.isEmpty()) {
            // Используйте данные объектов Trafic для обновления пользовательского интерфейса
            Toast.makeText(getActivity(), "Go2! ", Toast.LENGTH_SHORT).show();
            // Пример: Вывод данных в Log
            for (Trafic trafic : traficList) {
                Log.d("TransportTypeInfo", "1Type Name: " + trafic.getTypeName());
                Log.d("TransportTypeInfo", "2Price of 1: " + trafic.getPriceOf1());
            }

            // Пример: Обновление TextView с данными объектов Trafic
//            TextView typeNameTextView = getView().findViewById(R.id.typeNameTextView);

            // Предполагая, что у вас есть соответствующие TextView в макете фрагмента
            Trafic firstTrafic = traficList.get(0);
//            typeNameTextView.setText(firstTrafic.getTypeName());
//            priceOf1TextView.setText(String.valueOf(firstTrafic.getPriceOf1()));

            // Другие операции с данными объектов Trafic и обновление интерфейса...
        } else {
            Log.d("TransportTypeInfo", "Ошибка при получении данных с сервера");
        }


        // Проверка, есть ли данные для отображения
        if (cityList != null && !cityList.isEmpty()) {
            Toast.makeText(getActivity(), "Go3! ", Toast.LENGTH_SHORT).show();
            // Вывод данных
            for (City city : cityList) {
                String cityName = city.getCityName();
                double[] coordinates = city.getCoordinates();

                // Вывод данных в лог
                Log.d("City", "City Name: " + cityName);
                Log.d("City", "Coordinates: " + Arrays.toString(coordinates));

                // Другие операции с данными, например, обновление пользовательского интерфейса
                // ...
            }
        } else {
            // Данные не найдены или пустой список
            Log.d("City", "No data available.");
        }
    }

    public void test(){

        Toast.makeText(getActivity(), "Go! ", Toast.LENGTH_SHORT).show();

        //Create
//            CreateTransport task = new CreateTransport();
//            task.execute("10.0", "20.0", "1", "2", "ABC123");

     //   fetchTransportData();
        fetchCityData();


    }

}