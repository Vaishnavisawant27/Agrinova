package com.example.agreenova;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class DroneCaptureFragment extends Fragment {

    ImageView cropImage;
    Button captureBtn;

    TextView locationText, weatherText, statusText, damageText, solutionText;

    FusedLocationProviderClient fusedLocationClient;

    // Raspberry Pi Camera URL
    String cameraURL = "http://10.26.113.163:8080/shot.jpg";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_drone_capture, container, false);

        cropImage = view.findViewById(R.id.cropImage);
        captureBtn = view.findViewById(R.id.captureBtn);

        locationText = view.findViewById(R.id.locationText);
        weatherText = view.findViewById(R.id.weatherText);
        statusText = view.findViewById(R.id.statusText);
        damageText = view.findViewById(R.id.damageText);
        solutionText = view.findViewById(R.id.solutionText);

        fusedLocationClient =
                LocationServices.getFusedLocationProviderClient(requireActivity());

        getLocation();

        captureBtn.setOnClickListener(v -> captureImage());

        return view;
    }

    // Capture Image From Raspberry Pi
    private void captureImage() {

        new Thread(() -> {

            try {

                URL url = new URL(cameraURL);

                HttpURLConnection connection =
                        (HttpURLConnection) url.openConnection();

                connection.connect();

                InputStream input = connection.getInputStream();

                Bitmap bitmap = BitmapFactory.decodeStream(input);

                requireActivity().runOnUiThread(() -> {

                    cropImage.setImageBitmap(bitmap);

                    runAI(bitmap);

                });

            } catch (Exception e) {

                e.printStackTrace();

            }

        }).start();
    }

    // Fake AI Detection (temporary)
    private void runAI(Bitmap bitmap){

        int damage = (int)(Math.random()*100);

        if(damage < 30){

            statusText.setText("Healthy 🌱");
            solutionText.setText("No treatment needed");

        }
        else if(damage < 60){

            statusText.setText("Moderate Infection ⚠");
            solutionText.setText("Spray Neem Oil");

        }
        else{

            statusText.setText("High Infection ❌");
            solutionText.setText("Apply Fungicide");

        }

        damageText.setText("Damage : "+damage+"%");

    }

    // Get GPS Location
    private void getLocation() {

        if (ActivityCompat.checkSelfPermission(requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            return;
        }

        fusedLocationClient.getLastLocation()
                .addOnSuccessListener(location -> {

                    if(location != null){

                        double lat = location.getLatitude();
                        double lon = location.getLongitude();

                        locationText.setText("Location : "+lat+" , "+lon);

                        weatherText.setText("Weather : 28°C");

                    }

                });

    }
}