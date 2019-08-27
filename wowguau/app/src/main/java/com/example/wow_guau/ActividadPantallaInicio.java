package com.example.wow_guau;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class ActividadPantallaInicio extends AppCompatActivity
{
    Button btnBuscarPaseos;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_actividad_pantalla_inicio);

        Button btn = findViewById(R.id.buscar_paseadores_btn);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), ActividadBusquedaPaseadores.class);
                startActivity(i);
            }
        });

        btnBuscarPaseos = findViewById(R.id.buscar_paseo_btn);
        btnBuscarPaseos.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Intent i= new Intent(getApplicationContext(), ActividadListaCaminatas.class);
                startActivity(i);
            }
        });



    }
}
