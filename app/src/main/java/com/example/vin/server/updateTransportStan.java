package com.example.vin.server;

import android.os.AsyncTask;

import org.json.JSONObject;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class updateTransportStan extends AsyncTask<Void, Void, Boolean> {
    private int transportIndex;
    private double corX;
    private double corY;
    private int battery;
    private int stanId;

    public updateTransportStan(int transportIndex, double corX, double corY, int battery, int stanId) {
        this.transportIndex = transportIndex;
        this.corX = corX;
        this.corY = corY;
        this.battery = battery;
        this.stanId = stanId;
    }

    @Override
    protected Boolean doInBackground(Void... voids) {
        try {
            // Создаем URL для запроса
            String apiUrl = ApiConstants.API_URL+"transport/"+transportIndex;
            URL url = new URL(apiUrl);

            // Создаем соединение HTTP
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("PUT");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setDoOutput(true);

            // Создаем JSON-объект с обновленными значениями
            JSONObject requestBody = new JSONObject();
            requestBody.put("corX", corX);
            requestBody.put("corY", corY);
            requestBody.put("battery", battery);
            requestBody.put("stan_id", stanId);

            // Записываем JSON-объект в тело запроса
            OutputStream outputStream = connection.getOutputStream();
            outputStream.write(requestBody.toString().getBytes());
            outputStream.flush();
            outputStream.close();

            // Проверяем код ответа сервера
            int responseCode = connection.getResponseCode();
            connection.disconnect();

            // Возвращаем true, если код ответа HTTP 200 (OK)
            return responseCode == HttpURLConnection.HTTP_OK;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    protected void onPostExecute(Boolean success) {
        if (success) {
            // Обновление успешно выполнено
            System.out.println("Transport updated successfully.");
        } else {
            // Обработка ошибки
            System.out.println("Failed to update transport.");
        }
    }
}
