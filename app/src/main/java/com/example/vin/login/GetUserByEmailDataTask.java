package com.example.vin.login;

import android.os.AsyncTask;

import com.example.vin.server.ApiConstants;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class GetUserByEmailDataTask extends AsyncTask<String, Integer, User> {
    String API_URL = ApiConstants.API_URL+"users/email2/";
    private OnUserByEmailListener listener;
    public GetUserByEmailDataTask(OnUserByEmailListener listener) {
        this.listener = listener;
    }

    @Override
    protected User doInBackground(String... emails) {
        if (emails.length == 0) {
            return null;
        }

        String email = emails[0];
        try {
            // Створення URL-адресу для запиту з параметром email
            String urlString = API_URL + email;
            URL url = new URL(urlString);

            // Створення з'єднання HTTP
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            // Перевірка код відповіді сервера
            int responseCode = connection.getResponseCode();

            // Виведення проміжної інформації в консоль
            publishProgress(responseCode);

            if (responseCode == HttpURLConnection.HTTP_OK) {
                // Отримайте вхідний потік даних
                InputStream inputStream = connection.getInputStream();

                // Отримання даних JSON
                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
                StringBuilder stringBuilder = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    stringBuilder.append(line);
                }
                reader.close();

                JSONObject jsonObject = new JSONObject(stringBuilder.toString());

                // Створення об'єкт User і витягніть значення полів
                User user = new User();
                user.setUserIndex(jsonObject.getInt("user_index"));
                user.setPhone(jsonObject.getString("phone"));
                user.setName(jsonObject.getString("name"));

                return user;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    protected void onPostExecute(User user) {
        if (listener != null) {
            listener.onUserByEmail(user);
        }
    }

    @Override
    protected void onProgressUpdate(Integer... progress) {
        // Виведення проміжної інформації в консоль
        int responseCode = progress[0];
        System.out.println("Response Code: " + responseCode);
    }

    public interface OnUserByEmailListener {
        void onUserByEmail(User user);
    }
}
