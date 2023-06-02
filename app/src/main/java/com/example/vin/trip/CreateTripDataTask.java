package com.example.vin.trip;

import android.os.AsyncTask;

import com.example.vin.login.CreateUserDataTask;
import com.example.vin.server.ApiConstants;

import org.json.JSONObject;

import java.io.BufferedWriter;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

public class CreateTripDataTask extends AsyncTask<String, Void, Boolean> {
    private String API_URL = ApiConstants.API_URL + "trip";
    private OnTripCreatedListener listener;
    public CreateTripDataTask(OnTripCreatedListener listener) {
        this.listener = listener;
    }

    @Override
    protected Boolean doInBackground(String... params) {
        if (params.length < 7) {
            return false;
        }

        String transportId = params[0];
        String userId = params[1];
        String placeId = params[2];
        String timeStart = params[3];
        String timeEnd = params[4];
        String duration = params[5];
        String cost = params[6];

        try {
            // Створення URL-адресу для запиту
            URL url = new URL(API_URL);

            // Створення з'єднання HTTP
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setDoOutput(true);

            // Встановлення заголовку запиту
            connection.setRequestProperty("Content-Type", "application/json");

            // Створіть JSON-об'єкт для передачі даних
            JSONObject tripJson = new JSONObject();
            tripJson.put("transport_id", transportId);
            tripJson.put("user_id", userId);
            tripJson.put("place_id", placeId);
            tripJson.put("time_start", timeStart);
            tripJson.put("time_end", timeEnd);
            tripJson.put("duration", duration);
            tripJson.put("cost", cost);

            // Пперетворення JSON-об'єкт у рядок
            String requestBody = tripJson.toString();

            // Дані в тіло запиту
            OutputStream outputStream = connection.getOutputStream();
            outputStream.write(requestBody.getBytes(StandardCharsets.UTF_8));
            outputStream.close();

            // Перевірте код відповіді сервера
            int responseCode = connection.getResponseCode();
            System.out.println("Response Code: " + responseCode);

            return responseCode == HttpURLConnection.HTTP_CREATED;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    protected void onPostExecute(Boolean success) {
        if (listener != null) {
            listener.onTripCreated(success);
        }
    }

    public interface OnTripCreatedListener {
        void onTripCreated(boolean success);
    }
}