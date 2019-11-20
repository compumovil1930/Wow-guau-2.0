package co.edu.javeriana.wow_guau.views;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Calendar;
import java.util.Date;

import co.edu.javeriana.wow_guau.R;
import co.edu.javeriana.wow_guau.model.Ejercicio;

public class ActivityEstablecerObjetivo extends AppCompatActivity {

    private String uidPerro;

    FirebaseFirestore db;
    FirebaseAuth mAuth;
    FirebaseUser currentUser;

    Button buttonRegistrarObjetivo;
    EditText editTextTiempo;
    EditText editTextDistancia;

    String TAG = "Establecer objetivo";

    boolean cambioObjetivo = false;

    String idUltimoObjetivo;
    double distanciaAnterior;
    int tiempoAnterior;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_establecer_objetivo);

        buttonRegistrarObjetivo = findViewById(R.id.btn_establecer_objetivo);
        editTextTiempo = findViewById(R.id.et_establecer_tiempo);
        editTextDistancia = findViewById(R.id.et_establecer_distancia);

        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();

        uidPerro = getIntent().getStringExtra("idPerro");

        buttonRegistrarObjetivo.setEnabled(false);
        buscarUltimoEjercicio();

        buttonRegistrarObjetivo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean porEncimaCero = true;
                int c = 0;
                if (!editTextTiempo.getText().toString().isEmpty()){
                    c = c+1;
                    if ( Integer.valueOf(editTextTiempo.getText().toString()) <= 0 ){
                        porEncimaCero = false;
                    }
                }
                if (!editTextDistancia.getText().toString().isEmpty()){
                    c = c+1;
                    if ( Double.valueOf(editTextDistancia.getText().toString()) <= 0 ){
                        porEncimaCero = false;
                    }
                }
                if (porEncimaCero) {
                    switch (c) {
                        case 0:
                            Toast.makeText(getApplicationContext(), "Debes ingresar un tiempo o " +
                                    "una distancia", Toast.LENGTH_LONG).show();
                            break;
                        case 1:

                            if (cambioObjetivo == false) {
                                registrarEjercicio();
                            } else {

                                boolean b = true;
                                if (!editTextDistancia.getText().toString().isEmpty()) {
                                    if (Double.valueOf(editTextDistancia.getText().toString()) == distanciaAnterior) {
                                        b = false;
                                    }
                                }
                                if (!editTextTiempo.getText().toString().isEmpty()) {
                                    if (Integer.valueOf(editTextTiempo.getText().toString()) == tiempoAnterior) {
                                        b = false;
                                    }
                                }
                                if (b) {
                                    actualizarObjetivo();
                                } else {
                                    Toast.makeText(getApplicationContext(), "No realizaste un " +
                                            "cambio al objetivo", Toast.LENGTH_LONG).show();
                                }
                            }

                            break;
                        case 2:
                            Toast.makeText(getApplicationContext(), "Debes seleccionar tiempo o " +
                                    "distancia, una sola", Toast.LENGTH_LONG).show();
                            break;
                    }
                }else{
                    Toast.makeText(getApplicationContext(), "Los número deben ser mayores a cero"
                            , Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void registrarEjercicio(){

        Ejercicio ejercicio = new Ejercicio();

        ejercicio.setIdPerro(uidPerro);
        ejercicio.setEstado(true);
        Date dateObj = Calendar.getInstance().getTime();
        ejercicio.setFecha(dateObj);
        if(!editTextDistancia.getText().toString().isEmpty()){
            ejercicio.setDistancia( Double.valueOf(editTextDistancia.getText().toString()) );
        }
        if(!editTextTiempo.getText().toString().isEmpty()) {
            ejercicio.setTiempo(Integer.valueOf(editTextTiempo.getText().toString()));
        }
        DocumentReference referencia = db.collection("Ejercicios").document();

        referencia
                .set(ejercicio)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "DocumentSnapshot successfully written!");
                        Toast.makeText(ActivityEstablecerObjetivo.this, "Objetivo registrado con éxito",
                                Toast.LENGTH_SHORT).show();

                        Intent intent = new Intent(ActivityEstablecerObjetivo.this, ActividadPerfilPerro.class);
                        intent.putExtra("idPerro",uidPerro);
                        startActivity(intent);
                        finish();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error writing document", e);
                        Toast.makeText(ActivityEstablecerObjetivo.this, "Falló el registro de objetivo",
                                Toast.LENGTH_SHORT).show();
                        //btn_hacer_solicitud.setEnabled(true);
                    }
                });
    }

    private void actualizarObjetivo(){

        DocumentReference referencia = db.collection("Ejercicios").document( idUltimoObjetivo );

        referencia
                .update("estado", false)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "DocumentSnapshot successfully updated!");
                        /*Toast.makeText(ActivityEstablecerObjetivo.this, "Solicitud rechazada" +
                                "con éxito", Toast.LENGTH_SHORT).show();*/
                        registrarEjercicio();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error updating document", e);
                        Toast.makeText(ActivityEstablecerObjetivo.this, "Ocurrió un problema" +
                                "intentando rechazar la solicitud. Intentalo de nuevo", Toast.LENGTH_SHORT).show();
                    }
                });

    }

    private void buscarUltimoEjercicio() {

        Query query = db.collection("Ejercicios")
                .whereEqualTo("estado", true)
                .whereEqualTo("idPerro", uidPerro);

        query
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            int contadorDocumentos = 0;
                            long tiempo = 0;
                            double distancia = 0;
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                contadorDocumentos = contadorDocumentos+1;
                                Log.d(TAG, "Encontrado documento: "+document.getData().toString());
                                tiempo = document.getLong("tiempo");
                                distancia = document.getDouble("distancia");
                                idUltimoObjetivo = document.getId();
                            }
                            if(contadorDocumentos == 0){
                                buttonRegistrarObjetivo.setEnabled(true);
                            }else{
                                distanciaAnterior = distancia;
                                tiempoAnterior = (int)tiempo;
                                if(tiempo != 0){
                                    editTextTiempo.setText( String.valueOf(tiempo) );
                                }
                                if(distancia != 0) {
                                    editTextDistancia.setText( String.valueOf(distancia) );
                                }
                                buttonRegistrarObjetivo.setText("Cambiar objetivo actual");
                                buttonRegistrarObjetivo.setEnabled(true);
                                cambioObjetivo = true;
                            }
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });

    }
}
