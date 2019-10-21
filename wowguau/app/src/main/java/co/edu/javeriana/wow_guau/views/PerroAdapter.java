package co.edu.javeriana.wow_guau.views;

import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;

import co.edu.javeriana.wow_guau.R;
import co.edu.javeriana.wow_guau.model.Perro;
import co.edu.javeriana.wow_guau.utils.FirebaseUtils;

public class PerroAdapter extends RecyclerView.Adapter<PerroAdapter.MyViewHolder>
{
    private ArrayList<Perro> mListaPerros;

    public PerroAdapter(ArrayList<Perro> listaPerros)
    {
        mListaPerros = listaPerros;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder
    {
        public TextView txtViewDogName;
        public ImageView imgViewDogPic;

        public MyViewHolder(View view)
        {
            super(view);

            txtViewDogName = view.findViewById(R.id.dog_info);
            imgViewDogPic = view.findViewById(R.id.imgViewDog);

            imgViewDogPic.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view)
                {
                    Toast.makeText(view.getContext(),"Me hicieron click",Toast.LENGTH_LONG).show();

                }
            });

        }
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View itemview = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_dog,parent,false);

        return new MyViewHolder(itemview);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position)
    {
        final Perro perro = mListaPerros.get(position);

        holder.txtViewDogName.setText(perro.getNombre());

        FirebaseUtils.descargarFotoImageView(perro.getDireccionFoto(),holder.imgViewDogPic);
    }
    @Override
    public int getItemCount()
    {
        return mListaPerros.size();
    }
}



