package com.example.vin.login;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.vin.R;
import com.example.vin.server.ApiConstants;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.UnknownHostException;

public class LoginActivity extends AppCompatActivity {
    private String email;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        EditText editText = findViewById(R.id.TextEmailAddress);

        Button bth_login = findViewById(R.id.bth_login);
        bth_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                email = editText.getText().toString().trim();
                if(email.length() == 0){
                    Toast.makeText(LoginActivity.this, "Помилка: поле для email пусте!", Toast.LENGTH_SHORT).show();
                }
                else{
                    checkServerConnection();
                }
            }
        });
    }

    public void emailValidator(String emailToText){
        if (!emailToText.isEmpty() && Patterns.EMAIL_ADDRESS.matcher(emailToText).matches()) {
            SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("email", email);
            editor.apply();
            CheckEmail();
        } else {
            Toast.makeText(LoginActivity.this, "Помилка: Ви не правильно ввели email!", Toast.LENGTH_SHORT).show();
        }
    }

    private void CheckEmail(){
        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        CheckEmailExistsDataTask task = new CheckEmailExistsDataTask(new CheckEmailExistsDataTask.OnEmailExistsListener() {
            @Override
            public void onEmailExists(boolean exists) {
                if (exists) {
                    // Зазначений email вже існує
                    Toast.makeText(LoginActivity.this, "Існує вже такий емайл: ", Toast.LENGTH_SHORT).show();

                    editor.putString("email", email);
                    editor.putBoolean("registered",true);
                    editor.apply();

                    Intent myIntent = new Intent(LoginActivity.this, VerficationEmailActivity.class);
                    LoginActivity.this.startActivity(myIntent);
                    finish();
                } else {
                    // Зазначений email не існує
                    Toast.makeText(LoginActivity.this, "Не існує ще такий емайл: ", Toast.LENGTH_SHORT).show();

                    editor.putString("email", email);
                    editor.putBoolean("registered",false);
                    editor.apply();

                    Intent myIntent = new Intent(LoginActivity.this, VerficationEmailActivity.class);
                    LoginActivity.this.startActivity(myIntent);
                    finish();
                }
            }
        });
        task.execute(email);
    }

    private void checkServerConnection() {
        String serverUrl = ApiConstants.API_URL;
        int timeout = 3000; // Таймаут підключення у мілісекундах

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    URL url = new URL(serverUrl);
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.setConnectTimeout(timeout);
                    connection.connect();

                    // Підключення успішно
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            emailValidator(email);
                        }
                    });

                    // Закриваємо з'єднання після перевірки
                    connection.disconnect();

                } catch (UnknownHostException e) {
                    // Помилка DNS-дозвілу
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(LoginActivity.this, "Помилка DNS-дозвілу!", Toast.LENGTH_SHORT).show();
                        }
                    });
                } catch (IOException e) {
                    // Помилка підключення до сервера
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(LoginActivity.this, "Помилка підключення до сервера!", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        }).start();
    }
}