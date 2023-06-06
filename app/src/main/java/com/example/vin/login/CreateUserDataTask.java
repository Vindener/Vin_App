package com.example.vin.login;

import android.os.AsyncTask;

import com.example.vin.server.ApiConstants;

import java.io.BufferedWriter;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class CreateUserDataTask extends AsyncTask<Void, Void, Boolean> {
    String API_URL = ApiConstants.API_URL+"users";

    private String email;
    private String phone;
    private String name;
    private OnUserCreatedListener listener;

    public CreateUserDataTask(String email, String phone, String name, OnUserCreatedListener listener) {
        this.email = email;
        this.phone = phone;
        this.name = name;
        this.listener = listener;
    }

    @Override
    protected Boolean doInBackground(Void... voids) {
        try {
            // Створення URL-адресу для запиту
            URL url = new URL(API_URL);

            // Створення з'єднання HTTP
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setDoOutput(true);

            // Встановлення параметри запиту
            String postData = "email=" + URLEncoder.encode(email, "UTF-8") +
                    "&phone=" + URLEncoder.encode(phone, "UTF-8") +
                    "&name=" + URLEncoder.encode(name, "UTF-8");

            // Параметри запиту в тіло запиту
            OutputStream outputStream = connection.getOutputStream();
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
            writer.write(postData);
            writer.flush();
            writer.close();
            outputStream.close();

            // Отримання коду відповіді сервера
            int responseCode = connection.getResponseCode();
            System.out.println("Response Code: " + responseCode);

            return responseCode == HttpURLConnection.HTTP_CREATED;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    protected void onPostExecute(Boolean created) {
        if (listener != null) {
            listener.onUserCreated(created);
        }
    }

    public interface OnUserCreatedListener {
        void onUserCreated(boolean created);
    }
}
