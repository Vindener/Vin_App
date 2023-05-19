package com.example.vin.login;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.example.vin.LoadActivity;
import com.example.vin.R;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import com.example.vin.addition.CreateUser;

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

                    //ЗАПРОС ДО СЕРВЕР
//                    CreateUser task = new CreateUser();
//                    task.execute("testuser3","0955598","l23h");

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

    public void ConnectToServer(){
        try {
            URL url = new URL("@string/ip_server"+"/createUser");

            // Открытие соединения
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            // Установка метода запроса
            connection.setRequestMethod("POST");

            // Разрешение вывода данных
            connection.setDoOutput(true);

            // Параметры запроса (если необходимо)
            String parameters = "kod=2&email=testuser&phone=09598&name=loh&wallet_id=2";
            DataOutputStream outputStream = new DataOutputStream(connection.getOutputStream());
            outputStream.writeBytes(parameters);
            outputStream.flush();
            outputStream.close();

            // Получение ответа от сервера
            int responseCode = connection.getResponseCode();
            System.out.println("Response Code: " + responseCode);

            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String line;
            StringBuilder response = new StringBuilder();

            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
            reader.close();

            // Вывод ответа
            System.out.println("Response: " + response.toString());

            // Закрытие соединения
            connection.disconnect();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void emailValidator( String emailToText){
        if (!emailToText.isEmpty() && Patterns.EMAIL_ADDRESS.matcher(emailToText).matches()) {
            Toast.makeText(LoginActivity.this, "Result: "+email, Toast.LENGTH_SHORT).show();

            SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("email", email);
            editor.apply();

            Intent myIntent = new Intent(LoginActivity.this, VerficationEmailActivity.class);
            LoginActivity.this.startActivity(myIntent);
            finish();
        } else {
            Toast.makeText(this, "Помилка: Ви не правильно ввели email!", Toast.LENGTH_SHORT).show();
        }
    }
}