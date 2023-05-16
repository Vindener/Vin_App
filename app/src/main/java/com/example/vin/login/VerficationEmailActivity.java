package com.example.vin.login;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.vin.MainActivity;
import com.example.vin.R;

public class VerficationEmailActivity extends AppCompatActivity {
    private Button bth_verfication_email;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verfication_email);

        bth_verfication_email = findViewById(R.id.bth_verfication_email);

        bth_verfication_email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putBoolean("isFirstRun", false);
                editor.apply();

                Intent myIntent = new Intent(VerficationEmailActivity.this, MainActivity.class);
                VerficationEmailActivity.this.startActivity(myIntent);
                finish();
//                SharedPreferences login = context.getSharedPreferences("MyPrefs", MODE_PRIVATE);
//                SharedPreferences.Editor edit = login.edit();
//                edit.putString("email", email);
//                edit.apply();

                // Обработка нажатия кнопки
                // Вы можете выполнить здесь необходимые действия при нажатии кнопки
            }
        });


    }
}