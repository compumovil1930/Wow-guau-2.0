package co.edu.javeriana.wow_guau.views;

import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;

import co.edu.javeriana.wow_guau.R;
import co.edu.javeriana.wow_guau.model.Perro;

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
        }
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View itemview = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.activity_actividad_seleccion_mascota_monitoreo,parent,false);

        return new MyViewHolder(itemview);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position)
    {
        final Perro perro = mListaPerros.get(position);
        holder.txtViewDogName.setText(perro.getNombre());

        //Hilo para poner la imagen y que no se trabe.
        new Thread(new Runnable() {
            public void run() {
                // a potentially time consuming task

                if(!perro.getDireccionFoto().equals("") ) {
                    try {
                        String imageUrl = perro.getDireccionFoto();
                        InputStream URLcontent = (InputStream) new URL(imageUrl).getContent();
                        final Drawable image = Drawable.createFromStream(URLcontent, "your source link");

                        holder.imgViewDogPic.post(new Runnable() {
                            public void run() {
                                holder.imgViewDogPic.setImageDrawable(image);
                            }
                        });

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

            }
        }).start();

    }
    @Override
    public int getItemCount() {
        return mListaPerros.size();
    }
}



