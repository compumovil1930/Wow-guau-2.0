package co.edu.javeriana.wow_guau.views;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

import co.edu.javeriana.wow_guau.R;
import co.edu.javeriana.wow_guau.adapters.EjercicioAdapter;
import co.edu.javeriana.wow_guau.model.Ejercicio;

public class ActividadHistorialEjercicios extends AppCompatActivity implements EjercicioAdapter.OnEjercicioListener
{
    RecyclerView mRecyclerView;

    FirebaseFirestore db;
    FirebaseAuth mAuth;
    FirebaseUser currentUser;

    private ArrayList<Ejercicio> listaEjercicios;
    private EjercicioAdapter mEjercicioAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_actividad_historial_ejercicios);

        listaEjercicios = new ArrayList<Ejercicio>();
        mRecyclerView = findViewById(R.id.recycleViewListEjercicios);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        mRecyclerView.setLayoutManager(mLayoutManager);

        mRecyclerView.setItemAnimator(new DefaultItemAnimator());

        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();

        fillEjercicios();
    }

    private void fillEjercicios()
    {
        db.collection("Ejercicios")
                .whereEqualTo("idPerro",getIntent().getStringExtra("idPerro"))
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task)
                    {
                        if(task.isSuccessful())
                        {
                            for(QueryDocumentSnapshot document : task.getResult())
                            {
                                Ejercicio ejercicio = new Ejercicio();
                                ejercicio.setDistancia(document.getDouble("distancia"));
                                ejercicio.setEstado(document.getBoolean("estado"));
                                ejercicio.setFecha(document.getDate("fecha"));
                                double tiempo = document.getDouble("tiempo");
                                ejercicio.setTiempo((int)tiempo);

                                listaEjercicios.add(ejercicio);
                            }
                            mostrarEjercicios();
                        }
                    }
                });
    }

    private void mostrarEjercicios()
    {
        if(listaEjercicios.size() > 0)
        {
            mEjercicioAdapter = new EjercicioAdapter(listaEjercicios,this);
            mRecyclerView.setAdapter(mEjercicioAdapter);
        }
        else
        {
            Toast.makeText(this.getApplicationContext(),
                    "Tu consentido no tiene ejercicios!",
                    Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void OnEjercicioClick(int posicion)
    {
        Toast.makeText(this.getApplicationContext(),
                "Este es un ejercicio de tu consentido",
                Toast.LENGTH_LONG).show();
    }
}
