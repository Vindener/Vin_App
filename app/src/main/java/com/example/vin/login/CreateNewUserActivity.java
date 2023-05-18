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
        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("name", name);
        editor.putString("phone", phone);
        editor.putBoolean("isFirstRun", false);
        editor.apply();

        Toast.makeText(CreateNewUserActivity.this, "Все добре!", Toast.LENGTH_SHORT).show();

        Intent myIntent = new Intent(CreateNewUserActivity.this, MainActivity.class);
        CreateNewUserActivity.this.startActivity(myIntent);
        finish();

    }
}