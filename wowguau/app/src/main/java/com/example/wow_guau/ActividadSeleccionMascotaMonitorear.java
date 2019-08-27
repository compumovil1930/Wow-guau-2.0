package com.example.wow_guau;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class ActividadSeleccionMascotaMonitorear extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_actividad_seleccion_mascota_monitorear);

        CardView cv1 = findViewById(R.id.card_1);
        CardView cv2 = findViewById(R.id.card_2);
        View.OnClickListener vl = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), ActividadMonitorearMascota.class);
                startActivity(i);
            }
        };
        cv1.setOnClickListener(vl);
        cv2.setOnClickListener(vl);
    }
}

