package com.example.suspension;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.MotionEvent;
import android.widget.TextView;
import android.Manifest;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;

public class MainActivity extends AppCompatActivity {
    private static final int REQUEST_CODE_LOCATION_PERMISSION = 1001;

    private TextView textView;
    private ConstraintLayout screen;
    private float dPosX, dPosY, offsetX, offsetY, textX, textY;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textView = findViewById(R.id.textView);
        screen = findViewById(R.id.screen);
        textX = textView.getX();
        textY = textView.getY();

        screen.setOnTouchListener((v, event) -> {

            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    dPosX = event.getX();
                    dPosY = event.getY();
                    break;
                case MotionEvent.ACTION_MOVE:
                    offsetX = event.getX() - dPosX;
                    offsetY = event.getY() - dPosY;
                    textView.setX(textX + offsetX);
                    textView.setY(textY + offsetY);
                    break;
                case MotionEvent.ACTION_UP:
                    textX += offsetX;
                    textY += offsetY;
                    break;
                default:
                    return true;
            }

            return true;
        });

        LocationListener locationListener = location -> {
            // Mettre à jour les coordonnées GPS
            double latitude = location.getLatitude();
            double longitude = location.getLongitude();
            // Faites quelque chose avec les coordonnées GPS
            textView.setText(String.format("%s\n%s", latitude, longitude));
        };

        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE_LOCATION_PERMISSION);
            return;
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 0, locationListener);

    }
}