package co.edu.javeriana.wowguau_paseador.views;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Map;

import co.edu.javeriana.wowguau_paseador.R;
import co.edu.javeriana.wowguau_paseador.adapters.ComentarioAdapter;
import co.edu.javeriana.wowguau_paseador.model.Comentario;

public class ComentariosActivity extends AppCompatActivity {

    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    FirebaseUser mUser;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    ComentarioAdapter mAdapter;
    ArrayList<Comentario> mComentarios;
    ListView mListView;
    TextView tvNomPaseador;
    Float prom = 0.0f;
    int cant = 0;
    RatingBar cali;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comentarios);
        mUser = mAuth.getCurrentUser();
        mComentarios = new ArrayList<>();
        mListView = findViewById(R.id.lv_comentarios);

        mAdapter = new ComentarioAdapter(getApplicationContext(), mComentarios);
        mListView.setAdapter(mAdapter);

        //tv_nom_paseador
         tvNomPaseador = findViewById(R.id.tv_nom_paseador);

        db.collection("Paseadores").document(mUser.getUid()).get().addOnSuccessListener(
                new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        tvNomPaseador.setText((String) documentSnapshot.get("nombre"));
                    }
                }
        );

        cali = findViewById(R.id.rating_bar_cali);
        if(cant!=0){
            cali.setRating(prom/(cant*1.0f));
        } else {
            cali.setRating(0);
        }

        String uidUser = mUser.getUid();

        db.collection("Paseos").whereEqualTo("uidPaseador",uidUser)
                .whereEqualTo("calificado", true).addSnapshotListener(new EventListener<QuerySnapshot>() {

            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                for (  DocumentChange dc: queryDocumentSnapshots.getDocumentChanges() ) {
                    switch (dc.getType()){
                        case ADDED: {

                            final Map<String,Object> vals = dc.getDocument().getData();
                            String uidDueno = (String) vals.get("uidDueno");

                            prom+=Float.valueOf(String.valueOf(vals.get("calificacion")));
                            cant++;
                            if(cant!=0){
                                cali.setRating(prom/(cant*1.0f));
                            } else {
                                cali.setRating(0);
                            }


                            db.collection("Clientes").document(uidDueno).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                @Override
                                public void onSuccess(DocumentSnapshot documentSnapshot) {
                                    Comentario com = new Comentario((String) vals.get("comentarioCalificacion"),
                                            Float.valueOf(String.valueOf(vals.get("calificacion"))),
                                            (String) documentSnapshot.get("nombre"));
                                    mAdapter.add(com);
                                }
                            });

                            break;
                        }
                    }
                }
            }
        });
    }
}
