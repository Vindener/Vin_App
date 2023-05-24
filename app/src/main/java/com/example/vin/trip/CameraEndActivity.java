package com.example.vin.trip;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import java.text.SimpleDateFormat;

import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.vin.R;
import com.example.vin.maps.MapsFragment;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Date;
import java.util.Locale;

public class CameraEndActivity extends AppCompatActivity {

    private static final int REQUEST_CAMERA_PERMISSION = 200;
    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private static final int REQUEST_IMAGE_PICK = 2;

    private Button bthOpenCamera,bthGoToComplete;
    private ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera_end);

        // Включение кнопки возврата на предыдущую активность
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        bthOpenCamera = findViewById(R.id.bthOpenCamera);
        imageView = findViewById(R.id.photoendView);

        bthOpenCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openCamera();
            }
        });

        bthGoToComplete = findViewById(R.id.bthGoToComplete);

        bthGoToComplete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GoToComplete();
            }
        });

        openCamera();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            // Здесь определите действия при нажатии на кнопку возврата
            onBackPressed(); // Возврат на предыдущую активность
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        Intent resultIntent = new Intent();
        setResult(Activity.RESULT_OK, resultIntent);
        finish();
    }

    private void openCamera() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, REQUEST_CAMERA_PERMISSION);
        } else {
            Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            if (extras != null) {
                Bitmap imageBitmap = (Bitmap) extras.get("data");
                imageView.setImageBitmap(imageBitmap);
            }
        } else if (requestCode == REQUEST_IMAGE_PICK && resultCode == RESULT_OK) {
            try {
                Uri imageUri = data.getData();
                InputStream imageStream = getContentResolver().openInputStream(imageUri);
                Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                imageView.setImageBitmap(selectedImage);
            } catch (FileNotFoundException e) {
                Log.e("MainActivity", "Error selecting image from gallery", e);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CAMERA_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openCamera();
            } else {
                Toast.makeText(this, "Доступ к камере отклонен", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void GoToComplete(){
        if(isImageViewEmpty()){
            Toast.makeText(this, "Перезніміть фотографію", Toast.LENGTH_SHORT).show();
        }
        else{
            Toast.makeText(this, "Переход на другую активити", Toast.LENGTH_SHORT).show();

            saveImageToGallery();

            Intent myIntent = new Intent(CameraEndActivity.this, Complete_Trip_Activity.class);
            CameraEndActivity.this.startActivity(myIntent);
            finish();

        }
    }

    public boolean isImageViewEmpty(){
        return imageView.getDrawable() == null;
    }

    private void saveImageToGallery() {
        Bitmap imageBitmap = ((BitmapDrawable) imageView.getDrawable()).getBitmap();

        // Увеличение размера изображения до 800x600 пикселей
        int newWidth = 800;
        int newHeight = 600;
        Bitmap scaledBitmap = Bitmap.createScaledBitmap(imageBitmap, newWidth, newHeight, true);

        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
        String imageFileName = "IMG_" + timeStamp + ".jpg";
        File storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);

        try {
            File imageFile = new File(storageDir, imageFileName);
            OutputStream outputStream = new FileOutputStream(imageFile);

            // Установите желаемое качество сжатия (от 0 до 100)
            int quality = 90;

            // Используйте JPEG для сохранения изображения
            scaledBitmap.compress(Bitmap.CompressFormat.JPEG, quality, outputStream);

            outputStream.flush();
            outputStream.close();

            // Просканируйте новый файл, чтобы он стал видимым в галерее
            MediaScannerConnection.scanFile(this, new String[]{imageFile.getAbsolutePath()}, null, null);

            Toast.makeText(this, "Изображение сохранено", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            Log.e("MainActivity", "Error saving image to gallery", e);
            Toast.makeText(this, "Ошибка сохранения изображения", Toast.LENGTH_SHORT).show();
        }
    }


}