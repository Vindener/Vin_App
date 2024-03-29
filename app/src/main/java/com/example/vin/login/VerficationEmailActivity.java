package com.example.vin.login;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.vin.MainActivity;
import com.example.vin.R;

public class VerficationEmailActivity extends AppCompatActivity {
    private String verication_code;
    private String email;
    private boolean registered = false;
    private int userIndex;
    private String phone,name;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verfication_email);

        EditText editText = findViewById(R.id.Verfication_Email_Code);
        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        email = sharedPreferences.getString("email","");
        registered = sharedPreferences.getBoolean("registered",false);

        Button bth_verfication_email = findViewById(R.id.bth_verfication_email);
        bth_verfication_email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 verication_code = editText.getText().toString().trim();
                if(verication_code.length() == 0){
                    Toast.makeText(VerficationEmailActivity.this, "Помилка: поле пусте!", Toast.LENGTH_SHORT).show();
                }
                else if(verication_code.equals("123456") ){
                    if(!registered){
                        //якщо такого користувача ще немає

                        Toast.makeText(VerficationEmailActivity.this, "Перехід на реєстрацію", Toast.LENGTH_SHORT).show();
                        Intent myIntent = new Intent(VerficationEmailActivity.this, CreateNewUserActivity.class);
                        VerficationEmailActivity.this.startActivity(myIntent);
                        finish();
                    }
                    else{
                        // якщо такий користувач існує
                        GetProfileInfo();

                        Intent myIntent = new Intent(VerficationEmailActivity.this, MainActivity.class);
                        VerficationEmailActivity.this.startActivity(myIntent);
                        finish();
                    }
                }
                else {
                    Toast.makeText(VerficationEmailActivity.this, "Помилка: Ви ввели не те!", Toast.LENGTH_SHORT).show();
                }
            }
        });
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

                    Toast.makeText(VerficationEmailActivity.this, "Все вийшло!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(VerficationEmailActivity.this, "Користувач із зазначеним email не знайдений!", Toast.LENGTH_SHORT).show();
                }
            }
        });
        task.execute(email);
    }
}