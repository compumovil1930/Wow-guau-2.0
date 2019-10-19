package co.edu.javeriana.wow_guau.views;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
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

        //usar item_paseador para adiocionar al recycler view

        //al momento de mostrar resultados hacer .setVisibility(View.GONE) al progress bar
        //el progress bar podría extenderse más en altura al mostrarse

        //usar onOptionsItemSelected para manejar acción para filtrar (ya pusé algo)

        View.OnClickListener on = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), ActividadPerfilPaseador.class);
                startActivity(i);
            }
        };

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_filtro, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.cerrarSesionMenu:
                cerrarSesion();
                return true;
            case R.id.mostrarOpcionesFiltroMenu:
                /*if (activacionFiltro) {
                    Intent intent = new Intent(ActividadBusquedaPaseadores.this, ActivityFiltro.class);
                    startActivityForResult(intent, FILTRO_REQUEST);
                }else{
                    Toast toast = Toast.makeText(ActivityBuscarAnimales.this, "No se puede " +
                            "filtrar porque no hay paseadores disponibles", Toast.LENGTH_LONG);
                    toast.show();
                }*/
                return true;
            default:
                return true;
        }
    }

    private void cerrarSesion(){

    }
}
