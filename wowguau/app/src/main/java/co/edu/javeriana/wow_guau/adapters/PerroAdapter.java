package co.edu.javeriana.wow_guau.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import co.edu.javeriana.wow_guau.R;
import co.edu.javeriana.wow_guau.model.Perro;
import co.edu.javeriana.wow_guau.utils.FirebaseUtils;

public class PerroAdapter extends RecyclerView.Adapter<PerroAdapter.MyViewHolder>
{
    private ArrayList<Perro> mListaPerros;
    private OnPerroListener mOnPerroListener;

    public PerroAdapter(ArrayList<Perro> listaPerros,OnPerroListener onPerroListener)
    {
        this.mListaPerros = listaPerros;
        this.mOnPerroListener=onPerroListener;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
    {
        public TextView txtViewDogName;
        public ImageView imgViewDogPic;

        OnPerroListener onPerroListener;

        public MyViewHolder(View view,OnPerroListener onPerroListener)
        {
            super(view);

            txtViewDogName = view.findViewById(R.id.dog_info);
            imgViewDogPic = view.findViewById(R.id.imgViewDog);
            this.onPerroListener = onPerroListener;

            view.setOnClickListener(this);

        }

        @Override
        public void onClick(View view)
        {
            onPerroListener.onPerroClick(getAdapterPosition());
        }
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View itemview = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_dog,parent,false);

        return new MyViewHolder(itemview,mOnPerroListener);
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


    public interface OnPerroListener
    {
        void onPerroClick(int posicion);
    }
}



