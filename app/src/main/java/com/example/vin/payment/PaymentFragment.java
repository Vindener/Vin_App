package com.example.vin.payment;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.vin.R;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;


public class PaymentFragment extends Fragment {

    private Button pay30, pay50, pay70, pay100,bthPay;
    private EditText payCount;
    private TextView balance;
    private ImageView pay_qr;

    private Float new_balance;
    private int userIndex;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_payment, container, false);

        payCount = view.findViewById(R.id.payCountText);
        payCount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // Не використовується
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // Не використовується
            }

            @Override
            public void afterTextChanged(Editable s) {
                String text = s.toString();
                int decimalIndex = text.indexOf(".");

                if (decimalIndex != -1 && text.length() - decimalIndex - 1 > 2) {
                    // Видаляємо зайві символи після точки
                    s.delete(decimalIndex + 3, s.length());
                }
            }
        });

        pay30 = view.findViewById(R.id.pay30);
        pay50 = view.findViewById(R.id.pay50);
        pay70 = view.findViewById(R.id.pay70);
        pay100 = view.findViewById(R.id.pay100);
        bthPay = view.findViewById(R.id.bthPay);
        pay_qr = view.findViewById(R.id.pay_qr);

        pay30.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { pay("30"); }
        });
        pay50.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { pay("50"); }
        });
        pay70.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { pay("70"); }
        });
        pay100.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { pay("100"); }
        });

        bthPay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { Checkpay();}
        });

        return view;
    }

    private void pay(String pay){
        payCount.setText(pay);
    }

    private void Checkpay(){
        if( payCount.getText().toString().trim().isEmpty()){
            Toast.makeText(getActivity(), "Ви залишили поле пустим!", Toast.LENGTH_SHORT).show();
        }
        else{
            SharedPreferences sharedPreferences = getContext().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
            userIndex = sharedPreferences.getInt("userIndex", 1);
            SharedPreferences.Editor editor = sharedPreferences.edit();

            Float balance_ = sharedPreferences.getFloat("balance",0);

            balance = getActivity().findViewById(R.id.BalanceText);

            new_balance = balance_ + Float.valueOf(payCount.getText().toString());

            editor.putFloat("balance",new_balance);
            editor.apply();

            Float new_balance_ = sharedPreferences.getFloat("balance",0);

            balance.setText(new_balance_.toString());

            //Відправка значень на сервер
            UpdateWalletBalance();

            generateQR();
            Toast.makeText(getActivity(), "Ви успішно поповнили баланс на: "+ payCount.getText().toString()+" грн.", Toast.LENGTH_SHORT).show();
        }
    }

    private void generateQR(){
        String text = "Pay : "+payCount.getText().toString().trim()+" UAH";
        MultiFormatWriter writer = new MultiFormatWriter();
        try {
            BitMatrix matrix = writer.encode(text, BarcodeFormat.QR_CODE,800,800);

            BarcodeEncoder encoder = new BarcodeEncoder();
            Bitmap bitmap = encoder.createBitmap(matrix);

            pay_qr.setImageBitmap(bitmap);

        } catch (WriterException e) {
            throw new RuntimeException(e);
        }
    }

    private void UpdateWalletBalance(){
        UpdateWalletBalanceDataTask updateTask = new UpdateWalletBalanceDataTask(getContext(), String.valueOf(userIndex), new_balance);
        updateTask.execute();
    }
}