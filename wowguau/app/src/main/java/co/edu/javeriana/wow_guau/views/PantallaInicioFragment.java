package co.edu.javeriana.wow_guau.views;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import co.edu.javeriana.wow_guau.R;

public class PantallaInicioFragment extends Fragment
{
    ConstraintLayout btn_mis_consentidos;
    ConstraintLayout btn_buscar_paseos;
    ConstraintLayout btn_buscar_paseadores;

    public PantallaInicioFragment() {
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                                ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_pantalla_inicio, container, false);
        ((MenuActivity) getActivity()).getSupportActionBar().setTitle("Dueño");
        
        btn_buscar_paseadores = view.findViewById(R.id.cl_buscar_paseadores);
        btn_buscar_paseos = view.findViewById(R.id.cl_buscar_paseos);
        btn_mis_consentidos = view.findViewById(R.id.cl_mis_consentidos);

        btn_buscar_paseadores.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getActivity(), ActividadBusquedaPaseadores.class);
                startActivity(i);
            }
        });

        btn_buscar_paseos.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Intent i= new Intent(getActivity(), PaseosActivosActivity.class);
                startActivity(i);
            }
        });

        btn_mis_consentidos.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Intent i= new Intent(getActivity(), ListaMascotasActivity.class);
                startActivity(i);
            }
        });
        return view;
    }
}
