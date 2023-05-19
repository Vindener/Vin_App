package com.example.vin.server;
import android.content.Intent;
import android.os.AsyncTask;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class CreateUser extends AsyncTask<String, Void, String> {
    private static final String SERVER_URL = "http://192.168.0.51:5000/users";

    @Override
    protected String doInBackground(String... params) {
        int kod = 3;
        String email = params[0];
        String phone = params[1];
        String name = params[2];
        int wallet_id =5;
        String result = "";

        try {
            URL url = new URL(SERVER_URL);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setDoOutput(true);

            String parameters = "kod=" + kod + "&email=" + email+ "&phone=" + phone+ "&name=" + name+ "&wallet_id=" + wallet_id;
            DataOutputStream outputStream = new DataOutputStream(connection.getOutputStream());
            outputStream.writeBytes(parameters);
            outputStream.flush();
            outputStream.close();

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
        // result содержит ответ от сервера или сообщение об ошибке
    }
}
