package com.example.wow_guau;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class ActividadBusquedaPaseadores extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_actividad_busqueda_paseadores);

        TextView tv = findViewById(R.id.textView17);
        TextView tv1 = findViewById(R.id.textView18);
        TextView tv2 = findViewById(R.id.textView19);

        View.OnClickListener on = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), ActividadPerfilPaseador.class);
                startActivity(i);
            }
        };

        tv.setOnClickListener(on);
        tv1.setOnClickListener(on);
        tv2.setOnClickListener(on);

    }
}
