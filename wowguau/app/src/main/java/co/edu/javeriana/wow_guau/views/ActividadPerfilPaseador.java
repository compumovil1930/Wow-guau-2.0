package co.edu.javeriana.wow_guau.views;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import org.w3c.dom.Text;

import java.util.Map;

import co.edu.javeriana.wow_guau.R;
import co.edu.javeriana.wow_guau.adapters.PaseadorClassAdapter;
import co.edu.javeriana.wow_guau.adapters.PaseadorSerializable;
import co.edu.javeriana.wow_guau.utils.FirebaseUtils;

public class ActividadPerfilPaseador extends AppCompatActivity {
    PaseadorSerializable paseador;
    ImageView iv;
    TextView nombre, experiencia, distancia, disponibilidad, descripcion, titulo;

    FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_actividad_perfil_paseador);

        nombre = findViewById(R.id.tvNombrePaseadorPerfil);
        experiencia = findViewById(R.id.tvExperienciaPaseadorPerfil);
        distancia = findViewById(R.id.tvDistanciaPaseadorPerfil);
        disponibilidad = findViewById(R.id.tvDisponibilidadPaseadorPerfil);
        descripcion = findViewById(R.id.tvDescripcionPaseadorPerfil);
        iv = findViewById(R.id.ivPerfilPaseador);


        titulo = findViewById(R.id.tvTituloPaseadorPerfil);


        paseador = (PaseadorSerializable) this.getIntent().getSerializableExtra("paseador");

        if(paseador.getmImage() == null){
            paseador.setmImage( FirebaseUtils.descargarFotoImageViewOther( paseador.getUriPhoto(),iv) );
        } else {
            iv.setImageURI(Uri.fromFile(paseador.getmImage()));
        }
        nombre.setText(paseador.getNombre());
        titulo.setText(paseador.getNombre());
        distancia.setText(paseador.getDist().toString() + " km");


        db.collection("Paseadores").document(paseador.getUidPaseador()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                Map<String,Object> doc = documentSnapshot.getData();
                long xp = (long) doc.get("experiencia");

                experiencia.setText( String.valueOf(xp) + " años");
                descripcion.setText( (String) doc.get("descripcion"));
                String dis = (Boolean) doc.get("estado") ? "Disponible" : "No Disponible";
                disponibilidad.setText(dis);

            }
        });

        Button btn = findViewById(R.id.btnVerCalificacionesPaseador);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), ComentariosPaseadorActivity.class);
                startActivity(i);
            }
        });
    }
}
