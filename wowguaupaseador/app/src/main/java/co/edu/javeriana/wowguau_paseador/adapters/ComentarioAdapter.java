package co.edu.javeriana.wowguau_paseador.adapters;

import android.content.Context;
import android.media.Rating;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.google.firebase.storage.FirebaseStorage;

import java.util.ArrayList;

import co.edu.javeriana.wowguau_paseador.R;
import co.edu.javeriana.wowguau_paseador.model.Comentario;

public class ComentarioAdapter extends ArrayAdapter<Comentario> {

    public ComentarioAdapter(@NonNull Context context, ArrayList<Comentario> resource) {
        super(context, 0,resource);
        //mStorageRef = FirebaseStorage.getInstance().getReference();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Comentario comentario = getItem(position);

        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_comentario_adapter, parent, false);
        }

        TextView tvNombre = convertView.findViewById(R.id.tv_nom_dueno_com);
        TextView tvComentario = convertView.findViewById(R.id.tv_comen_comen);
        RatingBar ratingBar = convertView.findViewById(R.id.rating_bar_comen);

        // los sets
        tvNombre.setText(comentario.getNomDueno());
        tvComentario.setText(comentario.getComentario());
        ratingBar.setRating(comentario.getCalificacion());

        return convertView;
    }

}
