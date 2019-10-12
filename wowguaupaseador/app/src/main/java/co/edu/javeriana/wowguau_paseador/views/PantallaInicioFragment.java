package co.edu.javeriana.wowguau_paseador.views;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import co.edu.javeriana.wowguau_paseador.R;
import co.edu.javeriana.wowguau_paseador.model.Paseador;
import co.edu.javeriana.wowguau_paseador.utils.FirebaseUtils;

public class PantallaInicioFragment extends Fragment {
    ConstraintLayout cl_confirmar_paseo;
    ConstraintLayout cl_crear_paseo;
    ConstraintLayout cl_cancelar_paseo;
    ConstraintLayout cl_empezar_paseo;

    private FirebaseAuth mAuth;

    public PantallaInicioFragment() {
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_pantalla_inicio, container, false);
        ((MenuActivity) getActivity()).getSupportActionBar().setTitle("Paseador");
        cl_confirmar_paseo = view.findViewById(R.id.cl_confirmar_paseo);
        cl_crear_paseo = view.findViewById(R.id.cl_crear_paseo);
        cl_cancelar_paseo = view.findViewById(R.id.cl_cancelar_paseo);
        cl_empezar_paseo = view.findViewById(R.id.cl_empezar_paseo);

        mAuth = FirebaseAuth.getInstance();

        cl_empezar_paseo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Intent i= new Intent(getApplicationContext(), EmpezarPaseo.class);
                //startActivity(i);
            }
        });

        cl_crear_paseo.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Intent i= new Intent(getActivity(), CrearPaseoActivity.class);
                startActivity(i);
            }
        });

        cl_confirmar_paseo.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Intent i= new Intent(getActivity(), SeleccionMascotaActivity.class);
                i.putExtra("texto", "darpaseo");
                startActivity(i);
            }
        });

        cl_cancelar_paseo.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Intent i= new Intent(getActivity(), SeleccionMascotaActivity.class);
                i.putExtra("texto", "cancelar");
                startActivity(i);
            }
        });

        return view;
    }
}
