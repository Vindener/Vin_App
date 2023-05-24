package com.example.vin.trip;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.vin.MainActivity;
import com.example.vin.R;
import com.example.vin.maps.MapsFragment;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class CurrentTripActivity extends AppCompatActivity {

    boolean TripStart = false;
    boolean TimerOver = false;

    private Button EndTrip;

    private String duration;


    ConstraintLayout startedContainer,notStartedContainer;
    private  TextView timeStartCurrentTrip,costTextView,TripDiructionContainer,TransportNumber;
    private  String StartTime;

    private Handler handler;
    private Runnable runnable;

    private float cost;

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
        String TransportNum = sharedPreferences.getString("TransportNumber","");

        StartTime = sharedPreferences.getString("CurrentDateTrip","");

        startedContainer = findViewById(R.id.TripNotStartedContainer);
        notStartedContainer = findViewById(R.id.TripStartedContainer);

        TransportNumber = findViewById(R.id.TransportNumber);
        timeStartCurrentTrip = findViewById(R.id.timeStartCurrentTrip);
        TripDiructionContainer = findViewById(R.id.TripDiructionContainer);
        costTextView = findViewById(R.id.costTextView);

        timeStartCurrentTrip.setText(StartTime);
        TransportNumber.setText(TransportNum);

        ShowContainer();

        EndTrip = findViewById(R.id.EndTrip);

        EndTrip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { EndTrip();  }
        });


        // Создать Handler и Runnable для обновления значения timeDifference
        handler = new Handler();
        runnable = new Runnable() {
            @Override
            public void run() {
                if (!TimerOver){
                // Вычислить разницу времени и установить текст в TextView
                String timeDifference = calculateTimeDifference();
                TripDiructionContainer.setText(timeDifference);

                // Вычислить cost и установить текст в TextView
                double cost = calculateCost();
                DecimalFormat decimalFormat = new DecimalFormat("#0.00");
                String formattedCost = decimalFormat.format(cost);
                costTextView.setText(formattedCost + "грн.");

                // Планировать повторное выполнение через 1 секунду
                handler.postDelayed(this, 1000);
                }
                else{
                    handler.removeCallbacks(runnable);
                }
            }
        };

        // Запустить обновление значения timeDifference
        handler.post(runnable);

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();

        // Остановить обновление значения timeDifference и cost при уничтожении активити
        handler.removeCallbacks(runnable);
    }

    private String calculateTimeDifference() {
        // Получить текущую дату и время
        Calendar currentCalendar = Calendar.getInstance();
        Date currentDate = currentCalendar.getTime();

        // Получить дату и время начала таймера
        String startDateTime = StartTime;
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        Date startDate = null;

        try {
            startDate = dateFormat.parse(startDateTime);
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Вычислить разницу в миллисекундах между началом таймера и текущим временем
        long timeDifferenceInMillis = currentDate.getTime() - startDate.getTime();

        // Преобразовать разницу времени в желаемый формат (часы:минуты:секунды)
        long seconds = timeDifferenceInMillis / 1000;
        long minutes = seconds / 60;
        long hours = minutes / 60;
        seconds = seconds % 60;
        minutes = minutes % 60;

        duration = String.format("%02d:%02d:%02d", hours, minutes, seconds);

        return duration;
    }

    private float calculateCost() {
        // Получить текущую дату и время
        Calendar currentCalendar = Calendar.getInstance();
        Date currentDate = currentCalendar.getTime();

        // Получить дату и время начала таймера
        String startDateTime = StartTime;
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        Date startDate = null;

        try {
            startDate = dateFormat.parse(startDateTime);
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Вычислить разницу в миллисекундах между началом таймера и текущим временем
        float timeDifferenceInMillis = currentDate.getTime() - startDate.getTime();

        // Вычислить количество секунд
        float seconds = timeDifferenceInMillis / 1000;

        // Умножить количество секунд на 0.6
        cost = (float) (seconds * 0.0667);

        return cost;
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
        TimerOver = true;

        // Округлить значение cost до двух знаков после точки
        DecimalFormat decimalFormat = new DecimalFormat("#0.00");
        String formattedCost = decimalFormat.format(cost);

        SharedPreferences sharedPreferences = getSharedPreferences("CurrentTrip", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("costTrip", formattedCost);
        editor.putString("durationTrip", duration);
        editor.apply();

        Toast.makeText(this, "Зараз треба буде зробити фотографію транспорту", Toast.LENGTH_SHORT).show();

        TripStart = false;

        Intent myIntent = new Intent(CurrentTripActivity.this, CameraEndActivity.class);
        CurrentTripActivity.this.startActivity(myIntent);
        finish();
        //ShowContainer();
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