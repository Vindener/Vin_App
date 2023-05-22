package com.example.vin.trip;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.example.vin.R;

public class CurrentTripFragment extends Fragment {

    boolean TripStart = false;

    Button EndTrip;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_current_trip, container, false);
        Context context = getContext();
        SharedPreferences sharedPreferences = getContext().getSharedPreferences("CurrentTrip", Context.MODE_PRIVATE);
        TripStart = sharedPreferences.getBoolean("TripStart",false);
        String TripNum = sharedPreferences.getString("TripNumber","");

        ConstraintLayout startedContainer = view.findViewById(R.id.TripNotStartedContainer);
        ConstraintLayout notStartedContainer = view.findViewById(R.id.TripStartedContainer);

        if (TripStart){
            Toast.makeText(getActivity(), "Поїздка іде!"+TripNum, Toast.LENGTH_SHORT).show();

            startedContainer.setVisibility(View.GONE);

            // Показать startedContainer
            notStartedContainer.setVisibility(View.VISIBLE);
        }
        else{
            Toast.makeText(getActivity(), "Поїздка не іде!"+TripNum, Toast.LENGTH_SHORT).show();
            // Скрыть startedContainer
            notStartedContainer.setVisibility(View.GONE);

            // Показать notStartedContainer
            startedContainer.setVisibility(View.VISIBLE);
        }

        EndTrip = view.findViewById(R.id.EndTrip);

        EndTrip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { EndTrip();  }
        });

        
        return view;
    }

    private void EndTrip(){
        SharedPreferences sharedPreferences = getContext().getSharedPreferences("CurrentTrip", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("TripStart", false);
        editor.putString("TripNumber", "");

        editor.apply();
    }
}