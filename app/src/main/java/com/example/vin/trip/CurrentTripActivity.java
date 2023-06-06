package com.example.vin.trip;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.vin.R;
import com.example.vin.server.Trafic;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class CurrentTripActivity extends AppCompatActivity {

    boolean TripStart = false;
    boolean isInPolygon = true;
    boolean TimerOver = false;
    private String duration;
    private TextView costTextView;
    private TextView TripDiructionContainer;
    private TextView TransportNumber;
    private ImageView CurrentTripImage;
    private  String StartTime;
    private int selectedTransportType;
    private Handler handler;
    private Runnable runnable;
    private float cost;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_current_trip);

        // Увімкнення кнопки повернення на попередню активність
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        SharedPreferences sharedPreferences = getSharedPreferences("CurrentTrip", MODE_PRIVATE);
        TripStart = sharedPreferences.getBoolean("TripStart",false);
        isInPolygon = sharedPreferences.getBoolean("isInPolygon",true);
        String TransportNum = sharedPreferences.getString("TransportNumber","");

        StartTime = sharedPreferences.getString("CurrentDateTrip","");

        TransportNumber = findViewById(R.id.TransportNumber);
        TextView timeStartCurrentTrip = findViewById(R.id.timeStartCurrentTrip);
        TripDiructionContainer = findViewById(R.id.TripDiructionContainer);
        costTextView = findViewById(R.id.costTextView);

        timeStartCurrentTrip.setText(StartTime);
        TransportNumber.setText(TransportNum);

        Button endTrip = findViewById(R.id.EndTrip);

        selectedTransportType = sharedPreferences.getInt("TransportType",1);

        CurrentTripImage = findViewById(R.id.CurrentTripImage);

        ShowPicture();

        endTrip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isInPolygon){
                    EndTrip();
                }
                else{
                    Toast.makeText(CurrentTripActivity.this, "Ви не можете завершити поїздку! Поверніться в дозволену зону!", Toast.LENGTH_SHORT).show();
                }
            }
        });


        // Створення Handler та Runnable для оновлення значення timeDifference
        handler = new Handler();
        runnable = new Runnable() {
            @Override
            public void run() {
                if (!TimerOver){
                // Обчислити різницю часу і встановити текст у TextView
                String timeDifference = calculateTimeDifference();
                TripDiructionContainer.setText(timeDifference);

                // Обчислити cost та встановити текст у TextView
                double cost = calculateCost();
                DecimalFormat decimalFormat = new DecimalFormat("#0.00");
                String formattedCost = decimalFormat.format(cost);
                costTextView.setText(formattedCost + "грн.");

                // Планувати повторне виконання через 1 секунду
                handler.postDelayed(this, 1000);
                }
                else{
                    handler.removeCallbacks(runnable);
                }
            }
        };

        // Запустити оновлення значення timeDifference
        handler.post(runnable);
    }

    private void ShowPicture() {
        if (selectedTransportType == 1) {
            Drawable drawable = getResources().getDrawable(R.drawable.ic_electric_scooter);
            CurrentTripImage.setImageDrawable(drawable);
        } else if (selectedTransportType == 2) {
            Drawable drawable = getResources().getDrawable(R.drawable.ic_electric_bike);
            CurrentTripImage.setImageDrawable(drawable);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Зупинити оновлення значення timeDifference та cost при знищенні активіті
        handler.removeCallbacks(runnable);
    }

    private String calculateTimeDifference() {
        // Отримати поточну дату та час
        Calendar currentCalendar = Calendar.getInstance();
        Date currentDate = currentCalendar.getTime();

        // Отримати дату та час початку таймера
        String startDateTime = StartTime;
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        Date startDate = null;

        try {
            startDate = dateFormat.parse(startDateTime);
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Обчислити різницю в мілісекундах між початком таймера та поточним часом
        long timeDifferenceInMillis = currentDate.getTime() - startDate.getTime();

        // Перетворити різницю часу на бажаний формат (години:хвилини:секунди)
        long seconds = timeDifferenceInMillis / 1000;
        long minutes = seconds / 60;
        long hours = minutes / 60;
        seconds = seconds % 60;
        minutes = minutes % 60;

        duration = String.format("%02d:%02d:%02d", hours, minutes, seconds);

        return duration;
    }

    private float calculateCost() {
        // Отримати поточну дату та час
        Calendar currentCalendar = Calendar.getInstance();
        Date currentDate = currentCalendar.getTime();

        // Отримати дату та час початку таймера
        String startDateTime = StartTime;
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        Date startDate = null;

        try {
            startDate = dateFormat.parse(startDateTime);
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Обчислити різницю в мілісекундах між початком таймера та поточним часом
        float timeDifferenceInMillis = currentDate.getTime() - startDate.getTime();

        // Обчислити кількість секунд
        float seconds = timeDifferenceInMillis / 1000;

        //перевірка на тип
        double addperces = Trafic.getTrafic(selectedTransportType)/60.;

        // Помножити кількість секунд на потрібне
        cost = (float) (seconds * addperces);

        return cost;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed(); // Повернення на попередню активність
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

    private String EndTripString;
    private void EndTrip(){
        TimerOver = true;

        // Округлити значення cost до двох знаків після точки
        DecimalFormat decimalFormat = new DecimalFormat("#0.00");
        String formattedCost = decimalFormat.format(cost);

        // Обчислити підсумкове значення часу закінчення з додавання duration до StartTime
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        Date startDate = null;
        try {
            startDate = dateFormat.parse(StartTime);
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Обчислити дату і час закінчення
        Calendar endCalendar = Calendar.getInstance();
        endCalendar.setTime(startDate);

        // Розділити duration на години, хвилини та секунди
        String[] timeParts = duration.split(":");
        int hours = Integer.parseInt(timeParts[0]);
        int minutes = Integer.parseInt(timeParts[1]);
        int seconds = Integer.parseInt(timeParts[2]);

        // Додати години, хвилини та секунди до календаря
        endCalendar.add(Calendar.HOUR_OF_DAY, hours);
        endCalendar.add(Calendar.MINUTE, minutes);
        endCalendar.add(Calendar.SECOND, seconds);
        Date endDate = endCalendar.getTime();

        // Перетворити дату і час закінчення в рядок
        String endTime = dateFormat.format(endDate);

        // Зберегти значення endTime у змінній EndTrip
        EndTripString = endTime;

        SharedPreferences sharedPreferences = getSharedPreferences("CurrentTrip", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("costTrip", formattedCost);
        editor.putString("durationTrip", duration);
        editor.putString("EndTime", EndTripString);
        editor.apply();

        TripStart = false;

        Intent myIntent = new Intent(CurrentTripActivity.this, Complete_Trip_Activity.class);
        CurrentTripActivity.this.startActivity(myIntent);
        finish();
    }
}