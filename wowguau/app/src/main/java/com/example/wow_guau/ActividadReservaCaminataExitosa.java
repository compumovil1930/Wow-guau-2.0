package com.example.wow_guau;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import org.w3c.dom.Text;

public class ActividadReservaCaminataExitosa extends AppCompatActivity {
    TextView localidad;
    TextView ciudad;
    TextView nombre_responsable;
    TextView tiempo;
    Button boton_solicitar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_actividad_reserva_caminata_exitosa);
    }
}
