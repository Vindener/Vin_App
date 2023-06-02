package com.example.vin.login;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.vin.MainActivity;
import com.example.vin.R;

public class CreateNewUserActivity extends AppCompatActivity {

    private String email;
    private String name;
    private String phone;
    private int userIndex;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_new_user);

        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        email = sharedPreferences.getString("email","");

        Button bth_CreateNewUser = findViewById(R.id.bth_CreateNewUser);
        EditText editText1 = findViewById(R.id.RegisterUserNameTextView);
        EditText editText2 = findViewById(R.id.RegisterPhoneTextView);

        editText2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                //Не використовується
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //Не використовується
            }

            @Override
            public void afterTextChanged(Editable s) {
                String text = s.toString();

                if (!text.startsWith("380")) {
                    // Видалення символів, які не відповідають потрібному початку "380"
                    s.replace(0, s.length(), "380");
                }
            }
        });
        bth_CreateNewUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                name = editText1.getText().toString().trim();
                phone = editText2.getText().toString().trim();
                if(name.length() == 0){
                    Toast.makeText(CreateNewUserActivity.this, "Помилка: поле ім'я пусте!", Toast.LENGTH_SHORT).show();
                }
                else if(name.length() < 3 ){
                    Toast.makeText(CreateNewUserActivity.this, "Помилка: Ви ввели коротке ім'я!", Toast.LENGTH_SHORT).show();
                }
                else if (phone.length() == 0) {
                    Toast.makeText(CreateNewUserActivity.this, "Помилка: поле телефону пусте!", Toast.LENGTH_SHORT).show();
                }
                else if (phone.length() < 12) {
                    Toast.makeText(CreateNewUserActivity.this, "Помилка: Ви ввели короткий номер телефону!", Toast.LENGTH_SHORT).show();
                }
                else{
                    CreateUser();
                }

            }
        });
    }

    public void CreateUser(){
        CreateUserDataTask task = new CreateUserDataTask(email, phone, name, new CreateUserDataTask.OnUserCreatedListener() {
            @Override
            public void onUserCreated(boolean created) {
                if (created) {
                    Toast.makeText(CreateNewUserActivity.this, "Користувач успішно зареєстровано!", Toast.LENGTH_SHORT).show();

                    //Сюди переставив
                    GetProfileInfo();

                    Intent myIntent = new Intent(CreateNewUserActivity.this, MainActivity.class);
                    CreateNewUserActivity.this.startActivity(myIntent);
                    finish();
                } else {
                    // Ошибка при создании данных пользователя на сервере
                    Toast.makeText(CreateNewUserActivity.this, "Помилка при створенні даних користувача! Підключіться до серверу!", Toast.LENGTH_SHORT).show();
                }
            }
        });
        task.execute();
    }

    private void GetProfileInfo(){
        GetUserByEmailDataTask task = new GetUserByEmailDataTask(new GetUserByEmailDataTask.OnUserByEmailListener() {
            @Override
            public void onUserByEmail(User user) {
                if (user != null) {
                    userIndex = user.getUserIndex();
                    phone = user.getPhone();
                    name = user.getName();

                    SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putInt("userIndex", userIndex);
                    editor.putString("name", name);
                    editor.putString("phone", phone);
                    editor.putFloat("balance",0 );
                    editor.putBoolean("isFirstRun", false);
                    editor.apply();
                } else {
                    // Обробка випадку, коли користувач не знайдений
                }
            }
        });
        task.execute(email);
    }
}