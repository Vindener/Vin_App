package com.example.vin.list;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.vin.R;

import java.util.List;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder>{
    private List<Item> items;

    public MyAdapter(List<Item> items) {
        this.items = items;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Item item = items.get(position);

        holder.imageView.setImageResource(item.getImage());
        holder.numberTextView.setText(item.getNumber());
        holder.nameTextView.setText(item.getName());
        holder.dateTextView.setText(item.getDate());
        holder.timeTextView.setText(item.getTime());
        holder.dirucationTextView.setText(item.getdirucation());

    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView imageView;
        public TextView numberTextView;
        public TextView nameTextView;
        public TextView dateTextView;
        public TextView timeTextView;
        public TextView dirucationTextView;

        public ViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.photoendView);
            numberTextView = itemView.findViewById(R.id.numberTextView);
            nameTextView = itemView.findViewById(R.id.nameTextView);
            dateTextView = itemView.findViewById(R.id.dateTextView);
            timeTextView = itemView.findViewById(R.id.timeTextView);
            dirucationTextView = itemView.findViewById(R.id.dirucationTextView);
        }
    }
}
