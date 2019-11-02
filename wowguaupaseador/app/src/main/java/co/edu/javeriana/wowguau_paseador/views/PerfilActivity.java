package co.edu.javeriana.wowguau_paseador.views;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import co.edu.javeriana.wowguau_paseador.R;
import co.edu.javeriana.wowguau_paseador.model.Paseador;

public class PerfilActivity extends AppCompatActivity {
    TextView tv_nombre;
    TextView tv_nombre1;
    ImageView iv_perfil;
    TextView tv_ciudad;
    TextView tv_experiencia;
    Button btn_calificaciones;

    Paseador paseador;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil);
        tv_nombre = findViewById(R.id.tv_nombre);
        tv_nombre1 = findViewById(R.id.tv_nombre1);
        iv_perfil = findViewById(R.id.iv_perfil);
        tv_ciudad = findViewById(R.id.tv_ciudad);
        tv_experiencia = findViewById(R.id.tv_experiencia);
        btn_calificaciones = findViewById(R.id.ver_calificaciones_btn);

        paseador = (Paseador) getIntent().getSerializableExtra("user");

        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        List<Address> addresses = null;
        try {
            addresses = geocoder.getFromLocation(paseador.getDireccion().getLatitud(), paseador.getDireccion().getLongitud(), 1);
        } catch (IOException e) {
            e.printStackTrace();
        }

        tv_nombre.setText(paseador.getNombre());
        tv_nombre1.setText(paseador.getNombre());
        tv_ciudad.setText(addresses.get(0).getAddressLine(0));
        tv_experiencia.setText("Años: "+paseador.getExperiencia()+"\n"+paseador.getDescripcion());

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
