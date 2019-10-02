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
    ConstraintLayout btnBuscarPaseos;
    ConstraintLayout btn_buscar_paseadores;
    ConstraintLayout btn_monitorear;
    ConstraintLayout btn_actualizar_mascotas;
    ConstraintLayout btn_cerrar_sesion;
    ConstraintLayout btn_actualizar_info;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_actividad_pantalla_inicio_dueno);

        btn_buscar_paseadores = findViewById(R.id.cl_buscar_paseadores);
        btn_monitorear = findViewById(R.id.cl_monitorear);
        btnBuscarPaseos = findViewById(R.id.cl_buscar_paseos);
        btn_actualizar_mascotas = findViewById(R.id.cl_actualizar_info_mascota);
        btn_cerrar_sesion = findViewById(R.id.cl_cerrar_sesion);
        btn_actualizar_info = findViewById(R.id.cl_actualizar_mis_datos);

        btn_monitorear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), ActividadSeleccionMascota.class);
                i.putExtra("texto", "monitorear");
                startActivity(i);
            }
        });

        btn_buscar_paseadores.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), ActividadBusquedaPaseadores.class);
                startActivity(i);
            }
        });

        btnBuscarPaseos.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Intent i= new Intent(getApplicationContext(), ActividadListaCaminatas.class);
                startActivity(i);
            }
        });

        btn_actualizar_mascotas.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Intent i= new Intent(getApplicationContext(), ActividadSeleccionMascota.class);
                i.putExtra("texto", "actualizar");
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
                Intent i= new Intent(getApplicationContext(), Login.class);
                i.putExtra("texto", "actualizar");
                startActivity(i);
            }
        });
    }
}
