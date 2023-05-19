package com.example.vin.list;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.vin.R;

import java.util.ArrayList;
import java.util.List;


public class ListFragment extends Fragment {

    private RecyclerView recyclerView;
    private MyAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list, container, false);

        recyclerView = view.findViewById(R.id.listView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        List<Item> itemList = createItemList(); // Создание списка элементов
        adapter = new MyAdapter(itemList);
        recyclerView.setAdapter(adapter);

        return view;
    }

    private List<Item> createItemList() {
        // Создайте и заполните список элементов, содержащих данные для отображения
        List<Item> itemList = new ArrayList<>();

        // Добавьте элементы в список
        itemList.add(new Item(1, "20 UAH", "0002", "2023-05-18 10:30", "2023-05-18 10:35","5 хв"));
        itemList.add(new Item(2, "21 UAH", "0001", "2023-05-19 11:45", "2023-05-19 11:50","7 хв"));
        // ...

        return itemList;
    }
}