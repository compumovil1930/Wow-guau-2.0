package co.edu.javeriana.wowguau_paseador.adapters;

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

import co.edu.javeriana.wowguau_paseador.R;
import co.edu.javeriana.wowguau_paseador.model.Paseo;
import co.edu.javeriana.wowguau_paseador.utils.FirebaseUtils;

public class PaseoAdapter extends ArrayAdapter<Paseo> {

    private StorageReference mStorageRef;

    public PaseoAdapter(@NonNull Context context, @NonNull ArrayList<Paseo> objects) {
        super(context, 0, objects);
        mStorageRef = FirebaseStorage.getInstance().getReference();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        Paseo paseo = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_consulta_paseo, parent, false);
        }
        // Lookup view for data population
        TextView tvNombre = (TextView) convertView.findViewById(R.id.tv_nomperro_paseo);
        TextView tvDuracion = (TextView) convertView.findViewById(R.id.tv_duraccion_paseo);
        TextView tvCosto = (TextView) convertView.findViewById(R.id.tv_costo_paseo);
        TextView tvDireccion = (TextView) convertView.findViewById(R.id.tv_direccion_paseo);
        ImageView ivPerrito = (ImageView) convertView.findViewById(R.id.iv_paseo);

        // Populate the data into the template view using the data object
        tvNombre.setText(paseo.getNomPerro());
        tvDuracion.setText(String.valueOf(paseo.getDuracionMinutos()));
        tvCosto.setText(String.valueOf(paseo.getCosto()));
        tvDireccion.setText(paseo.getDireccion());

        FirebaseUtils.descargarFotoImageView( paseo.getUriPerrito(),ivPerrito);



        // Return the completed view to render on screen
        return convertView;
    }
    /*
    private static final int NAME_INDEX = 0;
    private static final int DURACION_INDEX = 1;
    private static final int COSTO_INDEX = 2;
    private static final int DIRECCION_INDEX = 3;
    private static final int IMAGE_INDEX = 4;
    private StorageReference mStorageRef;

    public PaseoAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);
        mStorageRef = FirebaseStorage.getInstance().getReference();
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context)
                .inflate(R.layout.item_consulta_paseo, parent, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        TextView tvNombre = (TextView) view.findViewById(R.id.tv_nomperro_paseo);
        TextView tvDuracion = (TextView) view.findViewById(R.id.tv_duraccion_paseo);
        TextView tvCosto = (TextView) view.findViewById(R.id.tv_costo_paseo);
        TextView tvDireccion = (TextView) view.findViewById(R.id.tv_direccion_paseo);
        final ImageView ivPerrito = (ImageView) view.findViewById(R.id.iv_paseo);


        String nombre = cursor.getString(NAME_INDEX);
        int duracion = cursor.getInt(DURACION_INDEX);
        long costo = cursor.getLong(COSTO_INDEX);
        String direccion = cursor.getString(DIRECCION_INDEX);
        String uri = cursor.getString(IMAGE_INDEX);

        tvNombre.setText(nombre);
        tvDuracion.setText(String.valueOf(duracion));
        tvCosto.setText(String.valueOf(costo));
        tvDireccion.setText(direccion);

        StorageReference myImage = mStorageRef.child(uri);

        Glide.with(view.getContext() )
                .load(myImage)
                .into(ivPerrito);
    }
    */
}
