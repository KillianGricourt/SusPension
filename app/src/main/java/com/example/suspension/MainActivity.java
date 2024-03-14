package com.example.suspension;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private TextView textView;
    private float offsetX, offsetY;
    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textView = findViewById(R.id.textView);

        // Afficher la boîte de dialogue au démarrage de l'application
        showInputDialog();

        textView.setOnTouchListener((v, event) -> {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    // Calculer la différence entre la position du doigt et la position du texte
                    offsetX = event.getRawX() - textView.getX();
                    offsetY = event.getRawY() - textView.getY();
                    break;
                case MotionEvent.ACTION_MOVE:
                    // Mettre à jour la position du texte en fonction du mouvement du doigt
                    textView.setX(event.getRawX() - offsetX);
                    textView.setY(event.getRawY() - offsetY);
                    break;
                default:
                    return false;
            }
            return true;
        });
    }

    private void showInputDialog() {
        // Créer une boîte de dialogue personnalisée avec la mise en page définie dans dialog_input.xml
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_input, null);
        builder.setView(dialogView);

        // Remplir les menus déroulants avec les valeurs de poids et de taille
        Spinner weightSpinner = dialogView.findViewById(R.id.weightSpinner);
        Spinner heightSpinner = dialogView.findViewById(R.id.heightSpinner);

        List<String> weightList = new ArrayList<>();
        for (int i = 50; i <= 120; i += 5) {
            weightList.add(String.valueOf(i) + " kg");
        }

        List<String> heightList = new ArrayList<>();
        for (double i = 1.45; i <= 1.95; i += 0.05) {
            heightList.add(String.format("%.2f m", i));
        }

        ArrayAdapter<String> weightAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, weightList);
        weightAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        weightSpinner.setAdapter(weightAdapter);

        ArrayAdapter<String> heightAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, heightList);
        heightAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        heightSpinner.setAdapter(heightAdapter);

        // Ajouter un écouteur pour le bouton "Fermer"
        Button closeButton = dialogView.findViewById(R.id.closeButton);
        closeButton.setOnClickListener(v -> {
            // Fermer la boîte de dialogue lorsque le bouton est cliqué
            AlertDialog dialog = (AlertDialog) builder.create();
            dialog.dismiss();
        });

        // Afficher la boîte de dialogue
        AlertDialog dialog = builder.create();
        dialog.show();
    }
}