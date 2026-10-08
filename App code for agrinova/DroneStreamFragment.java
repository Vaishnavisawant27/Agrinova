package com.example.agreenova;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebSettings;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

public class DroneStreamFragment extends Fragment {

    WebView webDrone;
    TextView batteryText, statusText;

    // Raspberry Pi Live Stream URL
    String streamURL = "http://10.26.113.163:8000/video_feed";

    @SuppressLint("MissingInflatedId")
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_drone_stream, container, false);

        webDrone = view.findViewById(R.id.webDrone);
        batteryText = view.findViewById(R.id.batteryText);
        statusText = view.findViewById(R.id.statusText);

        startStream();

        return view;
    }

    private void startStream(){

        WebSettings webSettings = webDrone.getSettings();
        webSettings.setJavaScriptEnabled(true);

        webDrone.loadUrl(streamURL);

        batteryText.setText("Drone Battery : 85%");
        statusText.setText("Drone Status : Live Streaming 🚁");

    }
}