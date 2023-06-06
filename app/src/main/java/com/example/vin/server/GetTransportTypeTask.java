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
import java.util.ArrayList;
import java.util.List;

public class GetTransportTypeTask extends AsyncTask<Void, Void,  List<Trafic>> {

    String API_URL = ApiConstants.API_URL+"typetransport";
    @Override
    protected List<Trafic> doInBackground(Void... voids) {
        List<Trafic> traficList = new ArrayList<>();
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

                // Розбір отриманої JSON-відповіді і створення об'єкту Trafic
                JSONArray jsonArray = new JSONArray(response.toString());
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject typeTransportData = jsonArray.getJSONObject(i);

                    String typeName = typeTransportData.getString("type_transport_name");
                    double priceOf1 = typeTransportData.getDouble("price_of_1");

                    Trafic trafic = new Trafic();
                    trafic.setTypeName(typeName);
                    trafic.setPriceOf1(priceOf1);

                    traficList.add(trafic);
                }
            }

            connection.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
            Log.d("TransportTypeInfo", "Помилка під час отримання даних із сервера.");
        }

        return traficList;
    }

    @Override
    protected void onPostExecute(List<Trafic> traficList) {
        if (traficList != null && !traficList.isEmpty()) {
            // Використання даних об'єктів Trafic в активіті
//            for (Trafic trafic : traficList) {
//                Log.d("TransportTypeInfo", "Type Name: " + trafic.getTypeName());
//                Log.d("TransportTypeInfo", "Price of 1: " + trafic.getPriceOf1());
//            }
        } else {
            Log.d("TransportTypeInfo", "Помилка під час отримання даних із сервера");
        }
    }
}