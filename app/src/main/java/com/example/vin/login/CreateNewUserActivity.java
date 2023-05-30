package com.example.vin.login;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.vin.MainActivity;
import com.example.vin.R;

public class CreateNewUserActivity extends AppCompatActivity {

    private Button bth_CreateNewUser;

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

        bth_CreateNewUser = findViewById(R.id.bth_CreateNewUser);
        EditText editText1 = findViewById(R.id.RegisterUserNameTextView);
        EditText editText2 = findViewById(R.id.RegisterPhoneTextView);
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
                else if (phone.length() < 10) {
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
                    // Данные пользователя успешно созданы на сервере
                    Toast.makeText(CreateNewUserActivity.this, "Данные пользователя успешно созданы", Toast.LENGTH_SHORT).show();
                } else {
                    // Ошибка при создании данных пользователя на сервере
                    Toast.makeText(CreateNewUserActivity.this, "Ошибка при создании данных пользователя", Toast.LENGTH_SHORT).show();
                }
            }
        });
        task.execute();

        GetProfileInfo();

        Toast.makeText(CreateNewUserActivity.this, "Все добре!", Toast.LENGTH_SHORT).show();

        Intent myIntent = new Intent(CreateNewUserActivity.this, MainActivity.class);
        CreateNewUserActivity.this.startActivity(myIntent);
        finish();
    }

    private void GetProfileInfo(){
        GetUserByEmailDataTask task = new GetUserByEmailDataTask(new GetUserByEmailDataTask.OnUserByEmailListener() {
            @Override
            public void onUserByEmail(User user) {
                if (user != null) {
                    // Используйте значения переменных userIndex, phone и name
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
                    // Далее выполните необходимые действия с полученными данными
                } else {
                    // Обработка случая, когда пользователь не найден
                }
            }
        });
        task.execute(email);
    }
}