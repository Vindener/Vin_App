package com.example.vin.list;

import android.os.AsyncTask;

import com.example.vin.server.ApiConstants;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class GetTripsByUserIndexDataTask extends AsyncTask<String , Integer, List<Trips>> {
    private String  API_URL = ApiConstants.API_URL+"trip/";

    private OnTripsReceivedListener listener;

    public GetTripsByUserIndexDataTask(OnTripsReceivedListener listener) {
        this.listener = listener;
    }

    @Override
    protected List<Trips> doInBackground(String ... params) {
        if (params.length == 0) {
            return null;
        }

        String userIndex = params[0];

        try {
            // Створіть URL-адресу для запиту з параметром user_index
            String urlString = API_URL + userIndex;
            URL url = new URL(urlString);

            // Створіть з'єднання HTTP
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            // Перевірте код відповіді сервера
            int responseCode = connection.getResponseCode();

            if (responseCode == HttpURLConnection.HTTP_OK) {
                // Отримати вхідний потік даних
                InputStream inputStream = connection.getInputStream();

                // Прочоитати дані JSON
                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
                StringBuilder stringBuilder = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    stringBuilder.append(line);
                }
                reader.close();

                // Опрацювати отримані дані JSON
                JSONArray jsonArray = new JSONArray(stringBuilder.toString());
                List<Trips> tripsList = new ArrayList<>();
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);

                    Trips trips = new Trips();
                    trips.setTransportIndex(jsonObject.getInt("transport_id"));
                    trips.setCost(jsonObject.getDouble("cost"));
                    trips.setDuration(jsonObject.getString("duration"));
                    trips.setTimeStart(jsonObject.getString("time_start"));
                    trips.setTimeEnd(jsonObject.getString("time_end"));

                    // Отримання об'єкту type_id з транспорту
                    JSONObject transportObject = jsonObject.getJSONObject("transport");
                    int typeId = transportObject.getInt("type_id");
                    trips.setTypeId(typeId);

                    //Додавання об'єкту Trips до списку
                    tripsList.add(trips);
                }

                return tripsList;
            } else {
                // Виведення проміжної інформації в консоль
                publishProgress(responseCode);
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Помилка під час отримання даних із сервера: " + e.getMessage());
            return null;
        }
    }

    @Override
    protected void onPostExecute(List<Trips> tripsList) {
        if (listener != null) {
            listener.onTripsReceived(tripsList);
        }
    }

    @Override
    protected void onProgressUpdate(Integer... progress) {
        //Виведення проміжної інформації в консоль
        int responseCode = progress[0];
        System.out.println("Response Code: " + responseCode);
    }

    public interface OnTripsReceivedListener {
        void onTripsReceived(List<Trips> tripsList);
    }
}
