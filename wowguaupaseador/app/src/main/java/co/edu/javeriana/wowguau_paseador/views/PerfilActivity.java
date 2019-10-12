package co.edu.javeriana.wowguau_paseador.views;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import co.edu.javeriana.wowguau_paseador.R;

public class PerfilActivity extends AppCompatActivity {
    Button btn_calificaciones;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil);

        btn_calificaciones = findViewById(R.id.ver_calificaciones_btn);

        btn_calificaciones.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), ComentariosActivity.class);
                startActivity(i);
            }
        });

        /*
        // Old:
        java.util.Date date = snapshot.getDate("created_at");
        // New:
        Timestamp timestamp = snapshot.getTimestamp("created_at");
        java.util.Date date = timestamp.toDate();
         */
    }
}
