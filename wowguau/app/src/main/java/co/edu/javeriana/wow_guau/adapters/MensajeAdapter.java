package co.edu.javeriana.wow_guau.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import co.edu.javeriana.wow_guau.R;
import co.edu.javeriana.wow_guau.model.Mensaje;
import co.edu.javeriana.wow_guau.utils.FirebaseUtils;


public class MensajeAdapter extends RecyclerView.Adapter<MensajeAdapter.MensajeViewHolder> {
    private static final int SENT = 0;
    private static final int RECEIVED = 1;

    private String userId;
    private List<Mensaje> mensajes;

    public MensajeAdapter(String userId, List<Mensaje> mensajes) {
        this.userId = userId;
        this.mensajes = mensajes;
    }

    @NonNull
    @Override
    public MensajeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        if(viewType == SENT){
            view = LayoutInflater.from(parent.getContext()).inflate(
                    R.layout.item_chat_sent,
                    parent,
                    false);
        } else {
            view = LayoutInflater.from(parent.getContext()).inflate(
                    R.layout.item_chat_received,
                    parent,
                    false);
        }
        return new MensajeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MensajeViewHolder holder, int position) {
        holder.bind(mensajes.get(position));
    }

    @Override
    public int getItemViewType(int position){
        if(mensajes.get(position).getSenderId().contentEquals(userId)){
            return SENT;
        } else {
            return RECEIVED;
        }
    }

    @Override
    public int getItemCount() {
        return mensajes.size();
    }

    class MensajeViewHolder extends RecyclerView.ViewHolder {
        TextView mensaje;
        ImageView imagen;

        public MensajeViewHolder(View itemView) {
            super(itemView);
            mensaje = itemView.findViewById(R.id.chat_message);
            imagen = itemView.findViewById(R.id.chat_image);
        }

        public void bind(Mensaje mensaje) {
            if(mensaje.getText()!=null) {
                this.mensaje.setText(mensaje.getText());
                this.mensaje.setVisibility(TextView.VISIBLE);
                this.imagen.setVisibility(ImageView.INVISIBLE);
                this.imagen.setMaxHeight(0);
            }else if(mensaje.getImageUrl()!=null){
                this.mensaje.setVisibility(TextView.INVISIBLE);
                this.mensaje.setHeight(0);
                this.imagen.setVisibility(ImageView.VISIBLE);
                this.imagen.setMaxWidth(500);
                FirebaseUtils.descargarFotoImageView(mensaje.getImageUrl(), this.imagen);
            }
        }
    }
}
