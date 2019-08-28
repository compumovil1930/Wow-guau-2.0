package com.example.wow_guau;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class ActividadSeleccionMascota extends AppCompatActivity {
    TextView descripcion;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_actividad_seleccion_mascota);

        descripcion = findViewById(R.id.descripcion);
        CardView cv1 = findViewById(R.id.card_1);
        CardView cv2 = findViewById(R.id.card_2);

        if(getIntent().getStringExtra("texto").equals("monitorear")) {
            descripcion.setText(getString(R.string.selecciona_a_qui_n_quieres_monitorear));
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
        else if(getIntent().getStringExtra("texto").equals("paseo")){
            descripcion.setText(getString(R.string.selecciona_a_qui_n_quieres_pasear));
            View.OnClickListener vl = new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i = new Intent(getApplicationContext(), ActividadSolicitudCaminataExitosa.class);
                    startActivity(i);
                }
            };
            cv1.setOnClickListener(vl);
            cv2.setOnClickListener(vl);
        }
        else if(getIntent().getStringExtra("texto").equals("actualizar")){
            descripcion.setText(getString(R.string.selecciona_a_qui_n_quieres_actualizar));
            View.OnClickListener vl = new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // actualizar perro
                }
            };
            cv1.setOnClickListener(vl);
            cv2.setOnClickListener(vl);
        }
        else if(getIntent().getStringExtra("texto").equals("darpaseo")){
            descripcion.setText(getString(R.string.selecciona_a_qui_n_quieres_darpaseo));
            View.OnClickListener vl = new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // dar paseo
                }
            };
            cv1.setOnClickListener(vl);
            cv2.setOnClickListener(vl);
        }
        else if(getIntent().getStringExtra("texto").equals("cancelar")){
            descripcion.setText(getString(R.string.selecciona_a_qui_n_quieres_cancelar));
            View.OnClickListener vl = new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // cancelar
                }
            };
            cv1.setOnClickListener(vl);
            cv2.setOnClickListener(vl);
        }
    }
}

