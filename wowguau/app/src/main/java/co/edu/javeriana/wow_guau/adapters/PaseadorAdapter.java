package co.edu.javeriana.wow_guau.adapters;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

import co.edu.javeriana.wow_guau.R;
import co.edu.javeriana.wow_guau.utils.FirebaseUtils;

public class PaseadorAdapter extends ArrayAdapter<PaseadorClassAdapter> {

    private StorageReference mStorageRef;

    public PaseadorAdapter(@NonNull Context context, ArrayList<PaseadorClassAdapter> resource) {
        super(context, 0,resource);
        mStorageRef = FirebaseStorage.getInstance().getReference();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        PaseadorClassAdapter paseador = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_paseador_adapter, parent, false);
        }
        // Lookup view for data population
        TextView tvNombre = (TextView) convertView.findViewById(R.id.tv_item_paseador_nombre);
        TextView tvDistancia = convertView.findViewById(R.id.tv_item_paseador_distancia);
        ImageView ivPerrito = (ImageView) convertView.findViewById(R.id.iv_item_paseador_imagen);

        // Populate the data into the template view using the data object
        tvNombre.setText(paseador.getNombre());

        tvDistancia.setText(String.valueOf(paseador.getDist()) + " km");

        if(paseador.getmImage() == null){
            paseador.setmImage(FirebaseUtils.descargarFotoImageViewOther( paseador.getUriPhoto(),ivPerrito));
        } else {
            ivPerrito.setImageURI(Uri.fromFile(paseador.getmImage()));
        }
        paseador.calcDist();
        if(paseador.getDist() <= -0.5 || paseador.getDist() > 5 ){
            convertView.setVisibility(View.INVISIBLE);
        } else {
            convertView.setVisibility(View.VISIBLE);
        }
        // Return the completed view to render on screen
        return convertView;
    }

}
