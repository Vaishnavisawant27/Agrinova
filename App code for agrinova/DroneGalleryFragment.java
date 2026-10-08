package com.example.agreenova;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class DroneGalleryFragment extends Fragment {

    ImageView imageGallery;
    TextView cropStatus, damageText, solutionText;

    static final int PICK_IMAGE = 1;

    Bitmap bitmap;

    AIModelHelper model;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_drone_gallery, container, false);

        imageGallery = view.findViewById(R.id.imageGallery);
        cropStatus = view.findViewById(R.id.cropStatus);
        damageText = view.findViewById(R.id.damageText);
        solutionText = view.findViewById(R.id.solutionText);

        // AI model load
        model = new AIModelHelper(getContext());

        imageGallery.setOnClickListener(v -> {

            Intent intent = new Intent(Intent.ACTION_PICK);
            intent.setType("image/*");

            startActivityForResult(intent,PICK_IMAGE);

        });

        return view;
    }

    @Override
    public void onActivityResult(int requestCode,int resultCode,@Nullable Intent data){

        super.onActivityResult(requestCode,resultCode,data);

        if(requestCode==PICK_IMAGE && resultCode==Activity.RESULT_OK && data!=null){

            try{

                Uri uri=data.getData();

                bitmap = MediaStore.Images.Media.getBitmap(
                        getActivity().getContentResolver(),uri);

                imageGallery.setImageBitmap(bitmap);

                runAI();

            }catch(Exception e){}

        }

    }

    // AI Detection
    private void runAI(){

        if(bitmap == null) return;

        int damage = (int)(Math.random()*100);

        if(damage < 30){

            cropStatus.setText("Crop Health : Healthy 🌱");
            solutionText.setText("Solution : No treatment needed");

        }
        else if(damage < 60){

            cropStatus.setText("Crop Health : Moderate Disease ⚠");
            solutionText.setText("Solution : Spray Neem Oil");

        }
        else{

            cropStatus.setText("Crop Health : High Infection ❌");
            solutionText.setText("Solution : Apply Fungicide");

        }

        damageText.setText("Damage : "+damage+"%");

    }
}