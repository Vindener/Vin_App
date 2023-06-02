package com.example.vin.list;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.vin.R;

import java.util.ArrayList;
import java.util.List;


public class ListFragment extends Fragment {

    private RecyclerView recyclerView;
    private MyAdapter adapter;
    private List<Item> itemList = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list, container, false);

        recyclerView = view.findViewById(R.id.listView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        itemList = createItemList();
        adapter = new MyAdapter(itemList);
        recyclerView.setAdapter(adapter);

        return view;
    }

    private List<Item> createItemList() {
        createItemList_();
        return itemList;
    }

    private void createItemList_() {
        SharedPreferences sharedPreferences = getContext().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        int userIndex = sharedPreferences.getInt("userIndex", 1);

        GetTripsByUserIndexDataTask task = new GetTripsByUserIndexDataTask(new GetTripsByUserIndexDataTask.OnTripsReceivedListener() {
            @Override
            public void onTripsReceived(List<Trips> tripsList) {
                if (tripsList != null) {
                    // Обробка отриманого списку поїздок
                    for (Trips trips : tripsList) {
                        int transportId = trips.getTransportIndex();
                        int typeId = trips.getTypeId();
                        double cost = trips.getCost();
                        String duration = trips.getDuration();
                        String timeStart = trips.getTimeStart();
                        String timeEnd = trips.getTimeEnd();

                        itemList.add(new Item(typeId, cost + " UAH", "00"+String.valueOf(transportId), timeStart, timeEnd, duration));
                    }

                    updateUI();
                } else {
                    Toast.makeText(getActivity(), "Помилка отримання даних про поїздки!", Toast.LENGTH_SHORT).show();
                }
            }
        });
        task.execute(String.valueOf(userIndex)); // Запуск задачі
    }

    private void updateUI() {
        // Оновлення списку
        RecyclerView.Adapter adapter = new MyAdapter(itemList);
        recyclerView.setAdapter(adapter);
    }
}