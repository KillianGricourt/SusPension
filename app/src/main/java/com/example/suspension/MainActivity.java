package com.example.suspension;

import static androidx.core.content.ContextCompat.getSystemService;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.view.MotionEvent;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.Manifest;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;

import java.io.IOException;
import java.io.InputStream;

public class MainActivity extends AppCompatActivity {
    private static final int REQUEST_CODE_LOCATION_PERMISSION = 1001;

    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        checkPermissions();

        setContentView(R.layout.activity_main);
        textView = findViewById(R.id.textView);


        AssetManager assetManager = getAssets();
        InputStream data;
        try {
            data = assetManager.open("areas.json");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        PixelBoxView pixelBoxView = new PixelBoxView(this);
        MapPosView mapPosView = new MapPosView(this);

        // Obtenez le conteneur dans lequel vous souhaitez ajouter PixelBoxView
        FrameLayout container = findViewById(R.id.container);
        FrameLayout position = findViewById(R.id.position);

        // Ajoutez PixelBoxView au conteneur
        container.addView(pixelBoxView);
        position.addView(mapPosView);

        Controller.getInstance(data, textView, locationManager, mapPosView);


    }

    private void checkPermissions() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE_LOCATION_PERMISSION);
        }
    }
}