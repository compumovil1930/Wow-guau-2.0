package co.edu.javeriana.wow_guau.adapters;

import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.w3c.dom.Text;

import java.lang.reflect.Array;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import co.edu.javeriana.wow_guau.R;
import co.edu.javeriana.wow_guau.model.Ejercicio;

public class EjercicioAdapter extends RecyclerView.Adapter<EjercicioAdapter.MyViewHolder>
{
    private ArrayList<Ejercicio> mListaEjercicios;
    private OnEjercicioListener mOnEjercicioListener;

    public EjercicioAdapter(ArrayList<Ejercicio> listaEjercicios,OnEjercicioListener onEjercicioListener)
    {
        this.mListaEjercicios=listaEjercicios;
        this.mOnEjercicioListener= onEjercicioListener;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
    {
        OnEjercicioListener onEjercicioListener;
        TextView txtViewDistancia;
        TextView txtViewEstado;
        TextView txtViewFecha;
        TextView txtViewTiempo;

        public MyViewHolder(View view, OnEjercicioListener onEjercicioListener)
        {
            super(view);

            txtViewDistancia = view.findViewById(R.id.txtViewDistancia);
            txtViewEstado = view.findViewById(R.id.txtViewEstado);
            txtViewFecha = view.findViewById(R.id.txtViewFechaEjercicio);
            txtViewTiempo = view.findViewById(R.id.txtViewTiempo);

            this.onEjercicioListener = onEjercicioListener;

            view.setOnClickListener(this);

        }

        @Override
        public void onClick(View view)
        {
            onEjercicioListener.OnEjercicioClick(getAdapterPosition());
        }
    }

    @NonNull
    @Override
    public EjercicioAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_ejercicio,parent,false);

        return new MyViewHolder(itemView,mOnEjercicioListener);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position)
    {
        final Ejercicio ejercicio = mListaEjercicios.get(position);


        holder.txtViewDistancia.setText("Distancia : " + ejercicio.getDistancia());
        if(ejercicio.isEstado())
        {
            holder.txtViewEstado.setText("Estado : No Completado");
            holder.txtViewEstado.
                    setCompoundDrawablesRelativeWithIntrinsicBounds(0,0,R.drawable.img_objetivo_no_completado,0);
        }
        else
        {
            holder.txtViewEstado.setText("Estado : Completado");
            holder.txtViewEstado.
                    setCompoundDrawablesRelativeWithIntrinsicBounds(0,0,R.drawable.img_objetivo_completado,0);

        }

        Date date = ejercicio.getFecha();
        DateFormat dateFormat = new SimpleDateFormat("dd-mm-yyyy");
        String strDate = dateFormat.format(date);
        holder.txtViewFecha.setText("Fecha: " + strDate);

        if (ejercicio.getTiempo() > 0)
        {
            holder.txtViewTiempo.setText("Tiempo : " + ejercicio.getTiempo());
        }
        else
        {
            holder.txtViewTiempo.setText("Tiempo: No Especificado");
        }

    }


    @Override
    public int getItemCount()
    {
        return mListaEjercicios.size();
    }

    public interface OnEjercicioListener
    {
        void OnEjercicioClick(int posicion);
    }
}
