package com.example.vin.server;

import android.os.AsyncTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class GetTransportDataTask extends AsyncTask<Void, Void, String> {
    private static final String SERVER_URL = "http://192.168.0.103:5000/transport";

    public String transport_index;
    public String corX;
    public String corY;
    public String stanId;
    public String typeId;
    public String qrCode;

    @Override
    protected String doInBackground(Void... params) {
        String result = "";

        try {
            URL url = new URL(SERVER_URL);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String line;
                StringBuilder response = new StringBuilder();

                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }
                reader.close();

                result = response.toString();
                System.out.println("все ок: " + result);
            } else {
                result = "Error: " + responseCode;
                System.out.println("десь помилка: " + result);
            }

            connection.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
            result = "Error: " + e.getMessage();
            System.out.println("Не підключилось: " + result);
        }

        return result;
    }

    @Override
    protected void onPostExecute(String result) {
        // Обработка результата запроса
        if (!result.startsWith("Error")) {
            // Разбор ответа и сохранение данных в переменные
            try {
                JSONArray jsonArray = new JSONArray(result);
                JSONObject transportData = jsonArray.getJSONObject(0); // Первый объект в массиве

                transport_index = transportData.getString("transport_index");
                corX = transportData.getString("corX");
                corY = transportData.getString("corY");
                stanId = transportData.getString("stan_id");
                typeId = transportData.getString("type_id");
                qrCode = transportData.getString("qr_code");

                // Теперь у вас есть доступ к данным и можете использовать их в вашем коде
                // Например:
                // myVariable = index;
                // myOtherVariable = corX;
                // ...
                System.out.println("все ок з присвоїнням: " + result);
            } catch (JSONException e) {
                e.printStackTrace();
                result = "Error: " + e.getMessage();
                System.out.println(" помилка присвоїння: " + result);
            }
        }

        // Продолжайте обрабатывать результат запроса или ошибку здесь
    }
}
