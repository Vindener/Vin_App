package com.example.vin.payment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.vin.R;


public class PaymentFragment extends Fragment {

    private Button pay30, pay50, pay70, pay100,bthPay;
    private EditText payCount;
    private TextView balance;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_payment, container, false);

        payCount = view.findViewById(R.id.payCountText);

        pay30 = view.findViewById(R.id.pay30);
        pay50 = view.findViewById(R.id.pay50);
        pay70 = view.findViewById(R.id.pay70);
        pay100 = view.findViewById(R.id.pay100);
        bthPay = view.findViewById(R.id.bthPay);


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

        balance = getActivity().findViewById(R.id.BalanceText);
        balance.setText(payCount.getText());
    }
}