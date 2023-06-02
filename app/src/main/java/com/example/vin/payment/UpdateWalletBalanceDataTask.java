package com.example.vin.payment;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import com.example.vin.server.ApiConstants;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class UpdateWalletBalanceDataTask extends AsyncTask<Void, Void, Integer> {
    String API_URL = ApiConstants.API_URL+"wallet/";

    private Context context;
    private String indexWallet;
    private double newBalance;

    public UpdateWalletBalanceDataTask(Context context, String indexWallet, double newBalance ) {
        this.context = context;
        this.indexWallet = indexWallet;
        this.newBalance = newBalance;
    }

    @Override
    protected Integer doInBackground(Void... voids) {
        int responseCode = -1;

        try {
            // Створення URL-адресу для запиту
            URL url = new URL(API_URL + indexWallet);

            // З'єднання HTTP
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("PUT");
            connection.setRequestProperty("Content-Type", "application/json");

            // Створіть JSON-об'єкт для передачі нового значення балансу
            String jsonInputString = "{\"balance\":" + newBalance + "}";

            // Встановіть прапор для дозволу виведення даних у запит
            connection.setDoOutput(true);

            // Отримайте вихідний потік даних для надсилання даних на сервер
            OutputStream outputStream = connection.getOutputStream();
            outputStream.write(jsonInputString.getBytes());
            outputStream.flush();
            outputStream.close();

            responseCode = connection.getResponseCode();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return responseCode;
    }

    @Override
    protected void onPostExecute(Integer responseCode) {
        // Виведення проміжної інформації в консоль
        System.out.println("Response Code: " + responseCode);

        if (responseCode == HttpURLConnection.HTTP_OK) {
            Toast.makeText(context, "Баланс обновлен успешно", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, "Помилка при оновленні баланса", Toast.LENGTH_SHORT).show();
        }
    }
}
