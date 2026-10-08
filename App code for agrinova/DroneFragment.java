package com.example.agreenova;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

public class DroneFragment extends Fragment {

    CardView cardDrone, cardStream, cardGallery;

    public DroneFragment() {
        // Required empty constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_drone, container, false);

        // CardView IDs
        cardDrone = view.findViewById(R.id.cardDrone);
        cardStream = view.findViewById(R.id.cardStream);
        cardGallery = view.findViewById(R.id.cardGallery);

        // Drone Capture Click
        cardDrone.setOnClickListener(v -> {

            getParentFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, new DroneCaptureFragment())
                    .addToBackStack(null)
                    .commit();

        });

        // Live Stream Click
        cardStream.setOnClickListener(v -> {

            getParentFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, new DroneStreamFragment())
                    .addToBackStack(null)
                    .commit();

        });

        // Gallery Click
        cardGallery.setOnClickListener(v -> {

            getParentFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, new DroneGalleryFragment())
                    .addToBackStack(null)
                    .commit();

        });

        return view;
    }
}