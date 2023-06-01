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
            // Создайте URL-адрес для запроса
            URL url = new URL(API_URL + indexWallet);

            // Создайте соединение HTTP
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("PUT");
            connection.setRequestProperty("Content-Type", "application/json");

            // Создайте JSON-объект для передачи нового значения баланса
            String jsonInputString = "{\"balance\":" + newBalance + "}";

            // Установите флаг для разрешения вывода данных в запрос
            connection.setDoOutput(true);

            // Получите выходной поток данных для отправки данных на сервер
            OutputStream outputStream = connection.getOutputStream();
            outputStream.write(jsonInputString.getBytes());
            outputStream.flush();
            outputStream.close();

            // Проверьте код ответа сервера
            responseCode = connection.getResponseCode();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return responseCode;
    }

    @Override
    protected void onPostExecute(Integer responseCode) {
        // Вывод промежуточной информации в консоль
        System.out.println("Response Code: " + responseCode);

        if (responseCode == HttpURLConnection.HTTP_OK) {
            Toast.makeText(context, "Баланс обновлен успешно", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, "Ошибка при обновлении баланса", Toast.LENGTH_SHORT).show();
        }
    }
}
