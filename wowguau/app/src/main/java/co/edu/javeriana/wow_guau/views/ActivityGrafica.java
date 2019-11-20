package co.edu.javeriana.wow_guau.views;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import co.edu.javeriana.wow_guau.R;

public class ActivityGrafica extends AppCompatActivity {

    GraphView graphDuracion;
    GraphView graphDistancia;

    FirebaseFirestore db;
    FirebaseAuth mAuth;
    FirebaseUser currentUser;

    String uidPerro;

    int numeroPaseos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grafica);

        graphDuracion = (GraphView) findViewById(R.id.graphDuracion);
        graphDistancia = (GraphView) findViewById(R.id.graphDistancia);
        //Button button = findViewById(R.id.addButton);

        uidPerro = getIntent().getStringExtra("idPerro");

        Log.i("Nada", uidPerro);

        db = FirebaseFirestore.getInstance();

        numeroPaseosRealizados();

        /*DataPoint[] dataPoints = new DataPoint[3];
        dataPoints[0] = new DataPoint(0, 1);
        dataPoints[1] = new DataPoint(1, 20);
        dataPoints[2] = new DataPoint(2, 45);*/

        /*try {
            LineGraphSeries <DataPoint> series = new LineGraphSeries< >(new DataPoint[] {
                    new DataPoint(0, 1),
                    new DataPoint(Integer.valueOf(1), Integer.valueOf(12)),
                    new DataPoint(Integer.valueOf(2), Integer.valueOf(13)),
                    new DataPoint(Integer.valueOf(3), Integer.valueOf(4)),
                    new DataPoint(Integer.valueOf(4), Integer.valueOf(7))
            });
            graph.addSeries(series);
        } catch (IllegalArgumentException e) {
            Toast.makeText(this.getApplicationContext(),
                    "Hubo un problema con la gráfica!",
                    Toast.LENGTH_LONG).show();
        }*/

        /*try {
            LineGraphSeries <DataPoint> series = new LineGraphSeries< >( dataPoints );
            graph.addSeries(series);
        } catch (IllegalArgumentException e) {
            //Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
        }*/
    }

    private void numeroPaseosRealizados(){
        db.collection("Paseos")
                .whereEqualTo("estado",false)
                .whereEqualTo("uidPerro", uidPerro)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>()
                {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task)
                    {
                        if(task.isSuccessful()){

                            int contador = 0;
                            for(QueryDocumentSnapshot document : task.getResult()) {
                                contador = contador +1 ;
                                Log.i("Nada", "prueba");
                            }
                            numeroPaseos = contador;
                            buscarPaseosRealizados();
                        } else{
                            Log.d("Errror Perron", "Error al buscar ejercicios ", task.getException());
                        }
                    }
                });
    }


    private void buscarPaseosRealizados(){

        if (numeroPaseos > 0) {

            db.collection("Paseos")
                    .whereEqualTo("estado", false)
                    .whereEqualTo("uidPerro", uidPerro)
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                long distanciaAcumulada = 0;
                                long tiempoAcumulado = 0;
                                boolean b = false;

                                DataPoint[] dataPointsDuracion = new DataPoint[numeroPaseos];
                                DataPoint[] dataPointsDistanciaRecorrida = new DataPoint[numeroPaseos];
                                int contador = 0;
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    dataPointsDuracion[contador] = new DataPoint(contador, document.getLong("duracion").intValue());
                                    dataPointsDistanciaRecorrida[contador] = new DataPoint(contador, document.getLong("distanciaRecorrida").intValue());
                                    contador = contador+1;
                                }
                                Log.i("Jmmm", String.valueOf(dataPointsDuracion.length));
                                try {
                                    LineGraphSeries<DataPoint> series = new LineGraphSeries<>(dataPointsDuracion);
                                    graphDuracion.addSeries(series);

                                    graphDuracion.getViewport().setXAxisBoundsManual(true);
                                    graphDuracion.getViewport().setMinX(0.0);
                                    graphDuracion.getViewport().setMaxX(5.0);

                                    // activate horizontal zooming and scrolling
                                    graphDuracion.getViewport().setScalable(true);

// activate horizontal scrolling
                                    graphDuracion.getViewport().setScrollable(true);

// activate horizontal and vertical zooming and scrolling
                                    graphDuracion.getViewport().setScalableY(true);

// activate vertical scrolling
                                    graphDuracion.getViewport().setScrollableY(true);


                                    LineGraphSeries<DataPoint> series1 = new LineGraphSeries<>(dataPointsDistanciaRecorrida);
                                    graphDistancia.addSeries(series1);

                                    // activate horizontal zooming and scrolling
                                    graphDistancia.getViewport().setScalable(true);

// activate horizontal scrolling
                                    graphDistancia.getViewport().setScrollable(true);

// activate horizontal and vertical zooming and scrolling
                                    graphDistancia.getViewport().setScalableY(true);

// activate vertical scrolling
                                    graphDistancia.getViewport().setScrollableY(true);

                                    graphDistancia.getViewport().setXAxisBoundsManual(true);
                                    graphDistancia.getViewport().setMinX(0.0);
                                    graphDistancia.getViewport().setMaxX(5.0);

                                } catch (IllegalArgumentException e) {
                                    //Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                                }
                            } else {
                                Log.d("Errror Perron", "Error al buscar ejercicios ", task.getException());
                            }
                        }
                    });
        }else{
            Toast.makeText(this.getApplicationContext(),
                    "No hay paseos realizados!",
                    Toast.LENGTH_LONG).show();
        }

    }
}
