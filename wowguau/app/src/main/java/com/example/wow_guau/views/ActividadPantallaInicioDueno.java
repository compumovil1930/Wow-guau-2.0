package com.example.wow_guau.views;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.wow_guau.R;

public class ActividadPantallaInicioDueno extends AppCompatActivity
{
    ConstraintLayout btn_mis_consentidos;
    ConstraintLayout btn_buscar_paseos;
    ConstraintLayout btn_buscar_paseadores;
    ConstraintLayout btn_actualizar_info;
    ConstraintLayout btn_cerrar_sesion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_actividad_pantalla_inicio_dueno);

        btn_buscar_paseadores = findViewById(R.id.cl_buscar_paseadores);
        btn_buscar_paseos = findViewById(R.id.cl_buscar_paseos);
        btn_mis_consentidos = findViewById(R.id.cl_mis_consentidos);
        btn_cerrar_sesion = findViewById(R.id.cl_cerrar_sesion);
        btn_actualizar_info = findViewById(R.id.cl_actualizar_mis_datos);

        btn_buscar_paseadores.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), ActividadBusquedaPaseadores.class);
                startActivity(i);
            }
        });

        btn_buscar_paseos.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Intent i= new Intent(getApplicationContext(), ActividadBusquedaPaseos.class);
                startActivity(i);
            }
        });

        btn_mis_consentidos.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Intent i= new Intent(getApplicationContext(), ListaMascotasActivity.class);
                startActivity(i);
            }
        });

        btn_cerrar_sesion.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Toast.makeText(ActividadPantallaInicioDueno.this, "Se ha cerrado la sesión", Toast.LENGTH_LONG).show();
                Intent i= new Intent(getApplicationContext(), Login.class);
                startActivity(i);
            }
        });

        btn_actualizar_info.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Intent i= new Intent(getApplicationContext(), update_my_info.class);
                i.putExtra("texto", "actualizar");
                startActivity(i);
            }
        });
    }
}
