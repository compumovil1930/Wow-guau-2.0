package co.edu.javeriana.wow_guau.views;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import co.edu.javeriana.wow_guau.R;

public class UpdateInfoFragment extends Fragment {
    public UpdateInfoFragment() {
    }

    public View onCreate(@NonNull LayoutInflater inflater,
                         ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_update_info, container, false);
        ((MenuActivity) getActivity()).getSupportActionBar().setTitle("Actualizar");
        return view;
    }
}
