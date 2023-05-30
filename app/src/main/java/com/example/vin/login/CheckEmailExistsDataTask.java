package com.example.vin.login;


import android.os.AsyncTask;

import com.example.vin.server.ApiConstants;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class CheckEmailExistsDataTask extends AsyncTask<String, Void, String> {
    String API_URL = ApiConstants.API_URL+"users/email/";

    private OnEmailExistsListener listener;

    public CheckEmailExistsDataTask(OnEmailExistsListener listener) {
        this.listener = listener;
    }

    @Override
    protected String doInBackground(String... emails) {
        if (emails.length == 0) {
            return "false";
        }

        String email = emails[0];
        try {
            // Создайте URL-адрес для запроса с параметром email
            String urlString = API_URL + URLEncoder.encode(email, "UTF-8");
            URL url = new URL(urlString);

            // Создайте соединение HTTP
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            // Проверьте код ответа сервера
            int responseCode = connection.getResponseCode();
            System.out.println("Response Code: " + responseCode);

            // Прочитайте ответ от сервера
            InputStream inputStream = connection.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            StringBuilder stringBuilder = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                stringBuilder.append(line);
            }
            reader.close();

            return stringBuilder.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return "false";
        }
    }

    @Override
    protected void onPostExecute(String response) {
        boolean exists = response.equalsIgnoreCase("true");
        if (listener != null) {
            listener.onEmailExists(exists);
        }
    }

    public interface OnEmailExistsListener {
        void onEmailExists(boolean exists);
    }
}
