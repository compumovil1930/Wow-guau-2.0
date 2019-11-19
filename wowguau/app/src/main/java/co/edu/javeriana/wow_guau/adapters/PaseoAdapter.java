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

public class PaseoAdapter extends ArrayAdapter<PaseoClassAdapter> {

    private StorageReference mStorageRef;

    public PaseoAdapter(@NonNull Context context, ArrayList<PaseoClassAdapter> resource) {
        super(context, 0,resource);
        mStorageRef = FirebaseStorage.getInstance().getReference();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        PaseoClassAdapter paseo = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_paseo_adapter, parent, false);
        }
        // Lookup view for data population

        TextView tvNombre = convertView.findViewById(R.id.item_paseo_name);
        TextView tvEstado = convertView.findViewById(R.id.item_paseo_estado);
        TextView tvTiempo = convertView.findViewById(R.id.item_paseo_duracion);
        TextView tvCosto = convertView.findViewById(R.id.item_paseo_costo);
        ImageView ivPerro = convertView.findViewById(R.id.iv_item_paseo_imagen);

        // Populate the data into the template view using the data object
        tvNombre.setText(paseo.getNomPerro());
        tvEstado.setText( paseo.isEstado() ? "En curso" : "Finalizado");
        tvTiempo.setText( paseo.getDuracion() + " Minutos");
        tvCosto.setText( paseo.getCosto() + " Monedas");


        if(paseo.getmImage() == null){
            paseo.setmImage(FirebaseUtils.descargarFotoImageViewOther( paseo.getUriPerro(),ivPerro));
        } else {
            ivPerro.setImageURI(Uri.fromFile(paseo.getmImage()));
        }

        // Return the completed view to render on screen
        return convertView;
    }


}
