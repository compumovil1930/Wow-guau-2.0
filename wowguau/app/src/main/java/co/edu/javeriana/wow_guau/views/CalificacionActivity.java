package co.edu.javeriana.wow_guau.views;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Toast;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

import co.edu.javeriana.wow_guau.R;
import co.edu.javeriana.wow_guau.adapters.PaseoClassAdapter;

public class CalificacionActivity extends DialogFragment {

    FirebaseFirestore db = FirebaseFirestore.getInstance();


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater  = requireActivity().getLayoutInflater();

        final View mView = inflater.inflate(R.layout.activity_calificacion, null);

        builder.setView(mView);

        builder.setMessage("Califica este paseo")
                .setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // FIRE ZE MISSILES!
                        String mId = getArguments().getString("uidPaseo");
                        EditText comentario = mView.findViewById(R.id.et_comentarios);
                        RatingBar rb = mView.findViewById(R.id.ratingBar);

                        Map<String,Object> vals = new HashMap<>();
                        vals.put("calificado", true);
                        vals.put("comentarioCalificacion", comentario.getText().toString());
                        vals.put("calificacion", rb.getRating());


                        Log.i("Info", mId + " " + comentario.getText() + " " + rb.getRating());
                        //db.collection("Paseos").document(mId).update("calificado",true, "calificacion", rb.getRating(), "comentarioCalificacion", comentario.getText().toString());
                        db.collection("Paseos").document(mId).update(vals);
                        Toast.makeText(getContext(), "Gracias por tu calificacacion", Toast.LENGTH_LONG).show();
                    }
                })
                .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User cancelled the dialog
                        Toast.makeText(getContext(), "Califica despues", Toast.LENGTH_LONG).show();
                    }
                });
        // Create the AlertDialog object and return it
        return builder.create();
    }


}
