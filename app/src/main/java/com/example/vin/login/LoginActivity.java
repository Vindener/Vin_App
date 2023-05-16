package com.example.vin.login;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.example.vin.LoadActivity;
import com.example.vin.R;

public class LoginActivity extends AppCompatActivity {
    private Button bth_login;
    private String email;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        EditText editText = findViewById(R.id.TextEmailAddress);

        bth_login = findViewById(R.id.bth_login);
        bth_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                email = editText.getText().toString().trim();
                if(email.length() == 0){
                    Toast.makeText(LoginActivity.this, "Помилка: поле пусте!", Toast.LENGTH_SHORT).show();
                }
                else{
                    Toast.makeText(LoginActivity.this, "Result: "+email, Toast.LENGTH_SHORT).show();
                    Intent myIntent = new Intent(LoginActivity.this, VerficationEmailActivity.class);
                    LoginActivity.this.startActivity(myIntent);
                    finish();
                }
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