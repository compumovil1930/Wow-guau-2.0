package co.edu.javeriana.wow_guau.views;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import co.edu.javeriana.wow_guau.R;

public class ActividadBusquedaPaseadores extends AppCompatActivity {

    RecyclerView recyclerViewPaseadores;
    ProgressBar progressBarLista;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_actividad_busqueda_paseadores);

        recyclerViewPaseadores = findViewById(R.id.rvListaPaseadores);
        progressBarLista = findViewById(R.id.pbBusquedaPasedores);

        //al momento de mostrar resultados hacer .setVisibility(View.GONE) al progress bar
        //el progress bar podría extenderse más en altura al mostrarse

        View.OnClickListener on = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), ActividadPerfilPaseador.class);
                startActivity(i);
            }
        };


    }
}
