package com.example.wow_guau;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class ActividadSolicitudCaminata extends AppCompatActivity
{
    Button btnSolicitud;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_actividad_solicitud_caminata);

        btnSolicitud=findViewById(R.id.btnSolicitar);
        btnSolicitud.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
            Intent i = new Intent(getApplicationContext(), ActividadSeleccionMascota.class);
            i.putExtra("texto", "paseo");
            startActivity(i);
            }
        });

    }
}
