package co.edu.javeriana.wowguau_paseador.views;


import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import co.edu.javeriana.wowguau_paseador.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class ActualizarInfoFragment extends Fragment {


    public ActualizarInfoFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_actualizar_info, container, false);
        // igual que en activity pero se hace view.findById...
        return view;
    }

}
