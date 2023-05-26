package com.example.vin.server;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class GetTransportTypeTask extends AsyncTask<Void, Void, Trafic> {

    private static final String API_URL = "http://192.168.0.11:5000/getTransportType";

    @Override
    protected Trafic doInBackground(Void... voids) {
        Trafic trafic = null;
        try {
            URL url = new URL(API_URL);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                InputStream inputStream = connection.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
                StringBuilder response = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }
                reader.close();

                // Разберите полученный JSON-ответ и создайте объект Trafic
                JSONArray jsonArray = new JSONArray(response.toString());
                JSONObject typeTransportData = jsonArray.getJSONObject(0); // Первый объект в массиве

                String typeName = typeTransportData.getString("type_transport_name");
                double priceOf1 = typeTransportData.getDouble("price_of_1");

                trafic = new Trafic();
                trafic.setTypeName(typeName);
                trafic.setPriceOf1(priceOf1);
            }

            connection.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return trafic;
    }

    @Override
    protected void onPostExecute(Trafic trafic) {
        if (trafic != null) {
            // Вывод информации о полученных данных объекта Trafic
            Log.d("TransportTypeInfo", "Type Name: " + trafic.getTypeName());
            Log.d("TransportTypeInfo", "Price of 1: " + String.valueOf(trafic.getPriceOf1()));

            // Другие операции с данными объекта Trafic...
        } else {
            Log.d("TransportTypeInfo", "Ошибка при получении данных с сервера");
        }
    }
}