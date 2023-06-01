package com.example.vin.login;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.vin.MainActivity;
import com.example.vin.R;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

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
                    Toast.makeText(LoginActivity.this, "Помилка: поле для email пусте!", Toast.LENGTH_SHORT).show();
                }
                else{
                    emailValidator(email);

                    //ЗАПит ДО СЕРВЕР

                }
            }
        });
    }

    public void emailValidator( String emailToText){
        if (!emailToText.isEmpty() && Patterns.EMAIL_ADDRESS.matcher(emailToText).matches()) {
            SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("email", email);
            editor.apply();
            Toast.makeText(LoginActivity.this, "Result: "+email, Toast.LENGTH_SHORT).show();
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
                    // Указанный email уже существует
                    Toast.makeText(LoginActivity.this, "Існує такий емайл: ", Toast.LENGTH_SHORT).show();

                    editor.putString("email", email);
                    editor.putBoolean("registered",true);
                    editor.apply();

                    Intent myIntent = new Intent(LoginActivity.this, VerficationEmailActivity.class);
                    LoginActivity.this.startActivity(myIntent);
                    finish();
                } else {
                    // Указанный email не существует
                    Toast.makeText(LoginActivity.this, "Не Існує такий емайл: ", Toast.LENGTH_SHORT).show();

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
}