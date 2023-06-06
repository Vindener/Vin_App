package com.example.vin.server;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class GetCityDataTask extends AsyncTask<Void, Void, List<City>> {

    private Context context;

    public GetCityDataTask(Context context) {
        this.context = context;
    }

    public void GetCityDataTask(Context context) {
        this.context = context;
    }

    String API_URL = ApiConstants.API_URL+"place";

    @Override
    protected List<City> doInBackground(Void... voids) {
        List<City> cityList = new ArrayList<>();

        try {
            // Створення URL-адресу для запиту
            URL url = new URL(API_URL);

            // Створення з'єднання HTTP
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            // Отримання коду відповіді сервера
            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                // Отримання вхідного потоку даних
                InputStream inputStream = connection.getInputStream();

                // дані JSON
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

                    // Створення об'єкту City і витягніть значення поля city
                    City city = new City();
                    city.setCityIndex(jsonObject.getInt("index_place"));
                    city.setCityName(jsonObject.getString("city"));

                    // Витягування об'єкту координат
                    JSONObject coordinatsObject = jsonObject.getJSONObject("coordinats");

                    // Поля координат у масив класу City
                    double[] coordinates = new double[20];
                    coordinates[0] = coordinatsObject.getDouble("corX_1");
                    coordinates[1] = coordinatsObject.getDouble("corY_1");
                    coordinates[2] = coordinatsObject.getDouble("corX_2");
                    coordinates[3] = coordinatsObject.getDouble("corY_2");
                    coordinates[4] = coordinatsObject.getDouble("corX_3");
                    coordinates[5] = coordinatsObject.getDouble("corY_3");
                    coordinates[6] = coordinatsObject.getDouble("corX_4");
                    coordinates[7] = coordinatsObject.getDouble("corY_4");
                    coordinates[8] = coordinatsObject.getDouble("corX_5");
                    coordinates[9] = coordinatsObject.getDouble("corY_5");
                    coordinates[10] = coordinatsObject.getDouble("corX_6");
                    coordinates[11] = coordinatsObject.getDouble("corY_6");
                    coordinates[12] = coordinatsObject.getDouble("corX_7");
                    coordinates[13] = coordinatsObject.getDouble("corY_7");
                    coordinates[14] = coordinatsObject.getDouble("corX_8");
                    coordinates[15] = coordinatsObject.getDouble("corY_8");
                    coordinates[16] = coordinatsObject.getDouble("corX_9");
                    coordinates[17] = coordinatsObject.getDouble("corY_9");
                    coordinates[18] = coordinatsObject.getDouble("corX_10");
                    coordinates[19] = coordinatsObject.getDouble("corY_10");

                    city.setCoordinates(coordinates);

                    // Додавання об'єкту City до списку
                    cityList.add(city);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return cityList;
    }

    @Override
    protected void onPostExecute(List<City> cityList) {
        if (cityList != null && !cityList.isEmpty()) {
            // Обробка отриманих даних
//            for (City city : cityList) {
//                Toast.makeText(context, "City Index: " + city.getCityIndex(), Toast.LENGTH_SHORT).show();
//                Toast.makeText(context, "City Name: " + city.getCityName(), Toast.LENGTH_SHORT).show();
//                Toast.makeText(context, "City coordinates: " + Arrays.toString(city.getCoordinates()), Toast.LENGTH_SHORT).show();
//            }
        } else {
//            Toast.makeText(context, "Ошибка при получении данных с сервера", Toast.LENGTH_SHORT).show();
        }
    }
}
