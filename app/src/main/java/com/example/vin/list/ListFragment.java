package com.example.vin.list;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.vin.R;

import java.util.ArrayList;
import java.util.Date;
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

        itemList = createItemList(); // Создание списка элементов
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
        Toast.makeText(getActivity(), "user "+ userIndex, Toast.LENGTH_SHORT).show();

        // Создаем и выполняем задачу для получения списка поездок
        GetTripsByUserIndexDataTask task = new GetTripsByUserIndexDataTask(new GetTripsByUserIndexDataTask.OnTripsReceivedListener() {
            @Override
            public void onTripsReceived(List<Trips> tripsList) {
                if (tripsList != null) {
                    // Обработка полученного списка поездок
                    for (Trips trips : tripsList) {
                        int transportId = trips.getTransportIndex();
                        int typeId = trips.getTypeId();
                        double cost = trips.getCost();
                        String duration = trips.getDuration();
                        String  timeStart = trips.getTimeStart();
                        String timeEnd = trips.getTimeEnd();

                        // Другие действия с данными поездки
                        itemList.add(new Item(typeId, cost + " UAH", "00"+String.valueOf(transportId), timeStart, timeEnd, duration));
                    }

                    // Здесь вы можете продолжить выполнение других действий с itemList,
                    // так как данные о поездках уже были добавлены в список

                    updateUI(); // Вызываем метод для обновления UI или выполнения другой логики
                } else {
                    // Обработка случая, когда список поездок пустой или произошла ошибка
                    Toast.makeText(getActivity(), "Ошибка получения данных о поездках", Toast.LENGTH_SHORT).show();
                }
            }
        });

        task.execute(String.valueOf(userIndex)); // Запускаем выполнение задачи
    }

    private void updateUI() {
        // Здесь вы можете обновить пользовательский интерфейс или выполнить другую логику
        // с использованием заполненного списка itemList

        // Пример обновления RecyclerView с помощью адаптера
        RecyclerView.Adapter adapter = new MyAdapter(itemList);
        recyclerView.setAdapter(adapter);
    }


}