package com.example.wow_guau;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class ActividadPantallaInicioDueno extends AppCompatActivity
{
    Button btnBuscarPaseos;
    Button btn_buscar_paseadores;
    Button btn_monitorear;
    Button btn_actualizar_mascotas;
    Button btn_cerrar_sesion;
    Button btn_actualizar_info;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_actividad_pantalla_inicio_dueno);

        btn_buscar_paseadores = findViewById(R.id.buscar_paseadores_btn);
        btn_monitorear = findViewById(R.id.monitoriar_perro_btn);
        btnBuscarPaseos = findViewById(R.id.buscar_paseo_btn);
        btn_actualizar_mascotas = findViewById(R.id.btn_actualizar_mascota);
        btn_cerrar_sesion = findViewById(R.id.btn_cerrar_sesion);
        btn_actualizar_info = findViewById(R.id.btn_actualizar_info);

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