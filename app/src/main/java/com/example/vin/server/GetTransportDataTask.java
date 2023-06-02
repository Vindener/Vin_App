package com.example.vin.server;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import com.example.vin.maps.Transport;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class GetTransportDataTask extends AsyncTask<Void, Void,  List<Transport>> {
    String API_URL = ApiConstants.API_URL+"transport";

    private Context context;

    public GetTransportDataTask(Context context) {
        this.context = context;
    }

    @Override
    protected List<Transport> doInBackground(Void... voids) {
        List<Transport> transportList = new ArrayList<>();

        try {
            // Створення URL-адресу для запиту
            URL url = new URL(API_URL);

            // Створення з'єднання HTTP
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            // Перевірка коду відповіді сервера
            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                InputStream inputStream = connection.getInputStream();

                // Читання даних JSON
                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
                StringBuilder stringBuilder = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    stringBuilder.append(line);
                }
                reader.close();

                // Обробка отриманих даних JSON
                JSONArray jsonArray = new JSONArray(stringBuilder.toString());
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);

                    // Створнггя об'єкту Transport і витягніть значення полів
                    Transport transport = new Transport();
                    transport.setTitle(String.valueOf(jsonObject.getInt("transport_index")));
                    transport.setLatitude(jsonObject.getDouble("corX"));
                    transport.setLongitude(jsonObject.getDouble("corY"));
                    transport.setBattery(jsonObject.getInt("battery"));
                    transport.setStan(jsonObject.getInt("stan_id"));
                    transport.setType(jsonObject.getInt("type_id"));

                    // Додавання об'єкту Transport до списку
                    transportList.add(transport);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return transportList;
    }

    @Override
    protected void onPostExecute(List<Transport> transportList) {
        if (transportList != null && !transportList.isEmpty()) {
            // Обробка отриманих даних
//            for (Transport transport : transportList) {
//                Toast.makeText(context, "Transport Index: " + transport.getTitle(), Toast.LENGTH_SHORT).show();
//                Toast.makeText(context, "CorX: " + transport.getLatitude(), Toast.LENGTH_SHORT).show();
//                Toast.makeText(context, "CorY: " + transport.getLongitude(), Toast.LENGTH_SHORT).show();
//                Toast.makeText(context, "Battery: " + transport.getBattery(), Toast.LENGTH_SHORT).show();
//                Toast.makeText(context, "Stan ID: " + transport.isFree(), Toast.LENGTH_SHORT).show();
//                Toast.makeText(context, "Type ID: " + transport.getType(), Toast.LENGTH_SHORT).show();
//            }
        } else {
//            Toast.makeText(context, "Помилка під час отримання даних із сервера", Toast.LENGTH_SHORT).show();
        }
    }

}
