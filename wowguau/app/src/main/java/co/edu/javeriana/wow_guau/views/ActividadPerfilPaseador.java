package co.edu.javeriana.wow_guau.views;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import co.edu.javeriana.wow_guau.R;

public class ActividadPerfilPaseador extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_actividad_perfil_paseador);

        Button btn = findViewById(R.id.ver_calificaciones_btn);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), ComentariosPaseadorActivity.class);
                startActivity(i);
            }
        });
    }
}
