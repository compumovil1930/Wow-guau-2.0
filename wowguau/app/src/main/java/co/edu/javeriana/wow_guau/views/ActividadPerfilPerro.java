package co.edu.javeriana.wow_guau.views;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import co.edu.javeriana.wow_guau.R;
import co.edu.javeriana.wow_guau.model.Perro;
import co.edu.javeriana.wow_guau.utils.FirebaseUtils;

public class ActividadPerfilPerro extends AppCompatActivity
{
    FirebaseFirestore db;
    FirebaseAuth mAuth;
    FirebaseUser currentUser;

    ImageView imgViewFotoPerfilPerro;
    TextView textViewNombre;
    TextView textViewRaza;
    TextView textViewFechaNacimiento;
    TextView textViewTamano;
    TextView textViewSexo;
    TextView textViewObs;
    Button btnActualizar;
    Button btnMonitorear;
    Button btnHistorial;
    String uidPerro;

    Button buttonEstablecerObjetivos;

    TextView textViewCompletitudObjetivo;
    ImageView imageViewCompletitudObjetivo;

    String nombrePerro, fotoPerro;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_actividad_perfil_perro);

        //Inflates
        imgViewFotoPerfilPerro = findViewById(R.id.imgViewPerfilPerro);
        textViewNombre = findViewById(R.id.txtViewNombre);
        textViewRaza = findViewById(R.id.txtViewRaza);
        textViewFechaNacimiento = findViewById(R.id.txtViewFechaNacimiento);
        textViewTamano = findViewById(R.id.txtViewTamano);
        textViewSexo = findViewById(R.id.txtViewSexo);
        textViewObs = findViewById(R.id.txtViewObs);
        btnActualizar = findViewById(R.id.btnActualizar);
        btnMonitorear = findViewById(R.id.btnMonitorear);
        buttonEstablecerObjetivos = findViewById(R.id.btn_ir_establecer_objetivos);
        btnHistorial = findViewById(R.id.btn_ver_historial_ejercicio);

        textViewCompletitudObjetivo = findViewById(R.id.tv_completitud_objetivo);
        imageViewCompletitudObjetivo = findViewById(R.id.imageView);

        btnActualizar.setEnabled(false);

        uidPerro = getIntent().getStringExtra("idPerro");

        db = FirebaseFirestore.getInstance();


        fillData();

        determinarEstadoObjetivo();

        btnActualizar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                /*Toast.makeText(getApplicationContext(),"Funcionalidad a Implementar",Toast.LENGTH_LONG).show();*/
                Intent intent = new Intent(view.getContext(),ActivitySolicitarPaseo.class);
                intent.putExtra("idPerro",uidPerro);
                intent.putExtra("nombrePerro",nombrePerro);
                intent.putExtra("fotoPerro",fotoPerro);
                startActivity(intent);
            }
        });

        buttonEstablecerObjetivos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(),ActivityEstablecerObjetivo.class);
                intent.putExtra("idPerro",uidPerro);
                startActivity(intent);
            }
        });

        btnHistorial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                Intent i = new Intent(getApplicationContext(),ActividadHistorialEjercicios.class);
                i.putExtra("idPerro",uidPerro);
                startActivity(i);
            }
        });


    }

    public void fillData()
    {
        db.collection("Mascotas")
                .whereEqualTo("perroID",uidPerro)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>()
                {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task)
                    {
                        if(task.isSuccessful())
                        {
                            for(QueryDocumentSnapshot document : task.getResult())
                            {
                                FirebaseUtils.descargarFotoImageView(document
                                        .getString("direccionFoto"),imgViewFotoPerfilPerro);

                                textViewNombre.setText("Nombre: " + document.getString("nombre"));
                                textViewRaza.setText("Raza: " + document.getString("raza"));

                                Date date = document.getDate("fechaNacimiento");
                                DateFormat dateFormat = new SimpleDateFormat("dd-mm-yyyy");
                                String strDate = dateFormat.format(date);

                                textViewFechaNacimiento.setText("Fecha de Nacimiento: " + strDate);

                                textViewTamano.setText("Tamano: "+ document.getString("tamano"));
                                textViewSexo.setText("Sexo: "+document.getString("sexo"));
                                textViewObs.setText("Observaciones: "+ document.getString("observaciones"));

                                if(!document.getBoolean("estado"))
                                {
                                    btnMonitorear.setText("No Activo");
                                    btnMonitorear.setTextColor(Color.GRAY);
                                    btnMonitorear.setClickable(false);
                                }

                                nombrePerro = document.getString("nombre");
                                fotoPerro = document.getString("direccionFoto");
                                Log.d("Nombre perro:", nombrePerro);
                                Log.d("Foto perro:", fotoPerro);

                                btnActualizar.setEnabled(true);

                            }
                        }
                        else
                        {
                            Log.d("Errror Perron", "Error al buscar perritos ", task.getException());
                        }
                    }
                });
    }

    private void determinarEstadoObjetivo()
    {

        textViewCompletitudObjetivo.setText("No ha completado el objetivo diario");
        imageViewCompletitudObjetivo.setImageResource(R.drawable.img_objetivo_completado);
        //imageViewCompletitudObjetivo.setImageResource(R.drawable.img_objetivo_no_completado);

    }
}
