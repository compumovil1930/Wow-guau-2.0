package com.example.wow_guau.views;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.wow_guau.R;

public class ActividadPantallaInicioPaseador extends AppCompatActivity {
    ConstraintLayout cl_confirmar_paseo;
    ConstraintLayout cl_crear_paseo;
    ConstraintLayout cl_cancelar_paseo;
    ConstraintLayout cl_cerrar_sesion;
    ConstraintLayout cl_actualizar_mis_datos;
    ConstraintLayout cl_empezar_paseo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_actividad_pantalla_inicio_paseador);
        cl_confirmar_paseo = findViewById(R.id.cl_confirmar_paseo);
        cl_crear_paseo = findViewById(R.id.cl_crear_paseo);
        cl_cancelar_paseo = findViewById(R.id.cl_cancelar_paseo);
        cl_cerrar_sesion = findViewById(R.id.cl_cerrar_sesion);
        cl_actualizar_mis_datos = findViewById(R.id.cl_actualizar_mis_datos);
        cl_empezar_paseo = (ConstraintLayout)findViewById(R.id.cl_empezar_paseo);

        cl_empezar_paseo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Intent i= new Intent(getApplicationContext(), EmpezarPaseo.class);
                //startActivity(i);
            }
        });

        cl_crear_paseo.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Intent i= new Intent(getApplicationContext(), ActivityCrearCaminata.class);
                startActivity(i);
            }
        });

        cl_confirmar_paseo.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Intent i= new Intent(getApplicationContext(), ActividadSeleccionMascota.class);
                i.putExtra("texto", "darpaseo");
                startActivity(i);
            }
        });

        cl_cancelar_paseo.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Intent i= new Intent(getApplicationContext(), ActividadSeleccionMascota.class);
                i.putExtra("texto", "cancelar");
                startActivity(i);
            }
        });

        cl_actualizar_mis_datos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Intent i= new Intent(getApplicationContext(), ActualizarDatosPaseador.class);
                //startActivity(i);
            }
        });

        cl_cerrar_sesion.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Toast.makeText(ActividadPantallaInicioPaseador.this, "Se ha cerrado la sesión", Toast.LENGTH_LONG).show();
                Intent i= new Intent(getApplicationContext(), Login.class);
                startActivity(i);
            }
        });
    }
}
