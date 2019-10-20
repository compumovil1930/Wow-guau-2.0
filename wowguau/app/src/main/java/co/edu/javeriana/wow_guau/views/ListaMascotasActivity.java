package co.edu.javeriana.wow_guau.views;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import co.edu.javeriana.wow_guau.R;

public class ListaMascotasActivity extends AppCompatActivity {
    Button btn_add;
    ListView listView;
    /*ConstraintLayout btn_monitorear;
    ConstraintLayout btn_actualizar_mascotas;*/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_mascotas);

        btn_add = findViewById(R.id.btn_add);
        listView = findViewById(R.id.listView);

        fillPets();

        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ListaMascotasActivity.this, Signup_dog.class);
                startActivity(i);
            }
        });
        /*btn_monitorear = findViewById(R.id.cl_monitorear);

        btn_monitorear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), ActividadSeleccionMascotaMonitoreo.class);
                i.putExtra("texto", "monitorear");
                startActivity(i);
            }
        });

        btn_actualizar_mascotas.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Intent i= new Intent(getApplicationContext(), ActividadSeleccionMascotaMonitoreo.class);
                i.putExtra("texto", "actualizar");
                startActivity(i);
            }
        });*/
    }
    public void fillPets(){
        //recuperar todos los mascotas y recorrerlos

    }
}
