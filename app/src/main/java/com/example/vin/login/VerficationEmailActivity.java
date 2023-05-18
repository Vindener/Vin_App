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
    private Button bth_verfication_email;
    private String verication_code;

    private String email;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verfication_email);

        EditText editText = findViewById(R.id.Verfication_Email_Code);
        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        email = sharedPreferences.getString("email","");

        bth_verfication_email = findViewById(R.id.bth_verfication_email);
        bth_verfication_email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 verication_code = editText.getText().toString().trim();
                if(verication_code.length() == 0){
                    Toast.makeText(VerficationEmailActivity.this, "Помилка: поле пусте!", Toast.LENGTH_SHORT).show();
                }
                else if(verication_code.equals("123456") ){
                    //якщо такого користувача ще немає
                    if(email.equals("user@user.com")){
                        Toast.makeText(VerficationEmailActivity.this, "User", Toast.LENGTH_SHORT).show();
                        Intent myIntent = new Intent(VerficationEmailActivity.this, CreateNewUserActivity.class);
                        VerficationEmailActivity.this.startActivity(myIntent);
                        finish();
                    }
                    else{
                        Toast.makeText(VerficationEmailActivity.this, "Not User", Toast.LENGTH_SHORT).show();
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putBoolean("isFirstRun", false);
                        editor.apply();

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
}