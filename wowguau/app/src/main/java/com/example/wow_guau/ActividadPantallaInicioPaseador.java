package com.example.wow_guau;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class ActividadPantallaInicioPaseador extends AppCompatActivity {
    ConstraintLayout cl_dar_paseo;
    ConstraintLayout cl_crear_paseo;
    ConstraintLayout cl_cancelar_paseo;
    ConstraintLayout cl_cerrar_sesion;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_actividad_pantalla_inicio_paseador);
        cl_dar_paseo = findViewById(R.id.cl_dar_paseo);
        cl_crear_paseo = findViewById(R.id.cl_crear_paseo);
        cl_cancelar_paseo = findViewById(R.id.cl_cancelar_paseo);
        cl_cerrar_sesion = findViewById(R.id.cl_cerrar_sesion);

        cl_crear_paseo.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Intent i= new Intent(getApplicationContext(), ActivityCrearCaminata.class);
                startActivity(i);
            }
        });

        cl_dar_paseo.setOnClickListener(new View.OnClickListener()
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
