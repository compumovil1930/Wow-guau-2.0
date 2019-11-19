package co.edu.javeriana.wow_guau.views;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.StrictMode;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

import co.edu.javeriana.wow_guau.R;
import co.edu.javeriana.wow_guau.adapters.PerroAdapter;
import co.edu.javeriana.wow_guau.model.Perro;

public class ActividadSeleccionMascotaMonitoreo extends AppCompatActivity
{
    FirebaseFirestore db;
    FirebaseAuth mAuth;
    FirebaseUser currentUser;
    ArrayList<Perro> perrosOwner;
    RecyclerView recyclerViewPerros;

    private PerroAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_actividad_seleccion_mascota_monitoreo);

        recyclerViewPerros = recyclerViewPerros.findViewById(R.id.recycledViewPerros);

        ConnectivityManager cm = (ConnectivityManager)ActividadSeleccionMascotaMonitoreo.this.getSystemService
                (CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();

        if(!isConnected)
        {
            Toast.makeText(this, "No hay conexión a internet",
                    Toast.LENGTH_SHORT).show();
        }

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitNetwork().build();
        StrictMode.setThreadPolicy(policy);

        db = FirebaseFirestore.getInstance();

        mAuth = FirebaseAuth.getInstance();

        currentUser = mAuth.getCurrentUser();

        perrosOwner = new ArrayList<Perro>();
    }
}

