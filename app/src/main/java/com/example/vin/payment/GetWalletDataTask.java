package com.example.vin.payment;

import android.content.Context;
import android.os.AsyncTask;

import com.example.vin.payment.Wallet;
import com.example.vin.server.ApiConstants;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class GetWalletDataTask extends AsyncTask<String, Void, Wallet> {
    String API_URL = ApiConstants.API_URL+"wallet/";

    private Context context;
    private OnWalletDataReceivedListener listener;

    public GetWalletDataTask(Context context, OnWalletDataReceivedListener listener) {
        this.context = context;
        this.listener = listener;
    }

    @Override
    protected Wallet doInBackground(String... params) {
        String indexWallet = params[0];
        String apiUrl = API_URL + indexWallet;

        try {
            URL url = new URL(apiUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                InputStream inputStream = connection.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
                StringBuilder stringBuilder = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    stringBuilder.append(line);
                }
                reader.close();

                JSONObject jsonObject = new JSONObject(stringBuilder.toString());
                double balance = jsonObject.getDouble("balance");

                Wallet wallet = new Wallet();
                wallet.setBalance(balance);

                return wallet;
            } else {
                // Ошибка при получении данных с сервера
                return null;
            }
        } catch (IOException | JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    protected void onPostExecute(Wallet wallet) {
        if (wallet != null) {
            if (listener != null) {
                listener.onWalletDataReceived(wallet);
            }
        } else {
            // Ошибка при получении данных с сервера
            if (listener != null) {
                listener.onWalletDataError();
            }
        }
    }

    public interface OnWalletDataReceivedListener {
        void onWalletDataReceived(Wallet wallet);
        void onWalletDataError();
    }
}