package com.example.wow_guau;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class ActividadRegistroUsuario extends AppCompatActivity {

    ConstraintLayout cl_soy_dueno_mascota;
    ConstraintLayout cl_soy_paseador;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_actividad_registro_usuario);

        cl_soy_dueno_mascota = (ConstraintLayout)findViewById(R.id.cl_soy_dueno_mascota);
        cl_soy_paseador = (ConstraintLayout)findViewById(R.id.cl_soy_paseador);

        cl_soy_dueno_mascota.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Intent i= new Intent(getApplicationContext(), update_my_info.class);
                //startActivity(i);
            }
        });

        cl_soy_paseador.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i= new Intent(getApplicationContext(), Signup_w.class);
                startActivity(i);
            }
        });
    }
}
