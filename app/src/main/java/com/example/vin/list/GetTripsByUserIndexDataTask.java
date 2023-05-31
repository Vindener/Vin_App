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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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
            // Создайте URL-адрес для запроса с параметром user_index
            String urlString = API_URL + userIndex;
            URL url = new URL(urlString);

            // Создайте соединение HTTP
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            // Проверьте код ответа сервера
            int responseCode = connection.getResponseCode();

            if (responseCode == HttpURLConnection.HTTP_OK) {
                // Получите входной поток данных
                InputStream inputStream = connection.getInputStream();

                // Прочтите данные JSON
                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
                StringBuilder stringBuilder = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    stringBuilder.append(line);
                }
                reader.close();

                // Обработайте полученные данные JSON
                JSONArray jsonArray = new JSONArray(stringBuilder.toString());
                List<Trips> tripsList = new ArrayList<>();
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);

                    // Создайте объект Trips и извлеките значения полей
                    Trips trips = new Trips();
                    trips.setTransportIndex(jsonObject.getInt("transport_id"));
                    trips.setCost(jsonObject.getDouble("cost"));
                    trips.setDuration(jsonObject.getDouble("duration"));

                    // Обработка даты и времени
                    String timeStartStr = jsonObject.optString("time_start");
                    String timeEndStr = jsonObject.optString("time_end");
                    // Преобразование строкового представления в объект типа Date
                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
                    if (!timeStartStr.isEmpty()) {
                        Date timeStart = dateFormat.parse(timeStartStr);
                        trips.setTimeStart(timeStart);
                    }
                    if (!timeEndStr.isEmpty()) {
                        Date timeEnd = dateFormat.parse(timeEndStr);
                        trips.setTimeEnd(timeEnd);
                    }

                    // Получите объект transport
                    JSONObject transportObject = jsonObject.getJSONObject("transport");

                    // Извлеките значение type_id из объекта transport
                    int typeId = transportObject.getInt("type_id");
                    trips.setTypeId(typeId);

                    // Добавьте объект Trips в список
                    tripsList.add(trips);
                }

                return tripsList;
            } else {
                // Вывод промежуточной информации в консоль
                publishProgress(responseCode);
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Ошибка при получении данных с сервера: " + e.getMessage());
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
        // Вывод промежуточной информации в консоль
        int responseCode = progress[0];
        System.out.println("Response Code: " + responseCode);
    }

    public interface OnTripsReceivedListener {
        void onTripsReceived(List<Trips> tripsList);
    }
}
