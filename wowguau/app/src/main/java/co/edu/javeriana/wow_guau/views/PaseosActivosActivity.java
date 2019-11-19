package co.edu.javeriana.wow_guau.views;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Map;

import co.edu.javeriana.wow_guau.R;
import co.edu.javeriana.wow_guau.adapters.PaseadorClassAdapter;
import co.edu.javeriana.wow_guau.adapters.PaseadorSerializable;
import co.edu.javeriana.wow_guau.adapters.PaseoAdapter;
import co.edu.javeriana.wow_guau.adapters.PaseoClassAdapter;

import static androidx.constraintlayout.widget.Constraints.TAG;

public class PaseosActivosActivity extends AppCompatActivity {

    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseUser mUser = mAuth.getCurrentUser();
    ArrayList<PaseoClassAdapter> mPaseos;
    PaseoAdapter mAdapter;
    ListView mLvPaseos;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_paseos_activos);

        mLvPaseos= findViewById(R.id.lv_paseos_activos);

        mPaseos = new ArrayList<>();
        mAdapter = new PaseoAdapter(this, mPaseos);

        mLvPaseos.setAdapter(mAdapter);

        db.collection("Paseos").whereEqualTo("uidDueno",mUser.getUid()).whereEqualTo("aceptado",true).whereEqualTo("calificado",false).addSnapshotListener(new EventListener<QuerySnapshot>() {

            private PaseoClassAdapter newPaseo(Map<String, Object> vals){
                PaseoClassAdapter mPaseo = new PaseoClassAdapter(
                        (String) vals.get("nomPerro"),
                        (boolean) vals.get("aceptado"),
                        (long) vals.get("costo"),
                        (long) vals.get("duracion"),
                        (boolean) vals.get("estado"),
                        (String) vals.get("uidDueno"),
                        (String) vals.get("uidPaseador"),
                        (String) vals.get("uidPerro"),
                        (String) vals.get("uriPerro"),
                        (boolean) vals.get("calificado"),
                        Double.parseDouble(String.valueOf(vals.get("calificacion"))),
                        (String) vals.get("comentarioCalificacion"),
                        (String) vals.get("uidPaseo")
                        );
                return mPaseo;
            }


            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                if (e != null) {
                    Log.w("error", "listen:error", e);
                    return;
                }

                for (  DocumentChange dc: queryDocumentSnapshots.getDocumentChanges() ) {
                    Log.d("Paseador ", "Add: " + dc.getDocument().getData());
                    switch (dc.getType()){

                        case ADDED: {
                            //mPaseos.add();

                            Map<String,Object> vals = dc.getDocument().getData();
                            vals.put("uidPaseo",dc.getDocument().getId());

                            PaseoClassAdapter mPaseo = newPaseo(vals);


                            mAdapter.add(mPaseo);
                            break;
                        }

                        case REMOVED: {
                            for(int i=0;i<mAdapter.getCount();++i){
                                PaseoClassAdapter temp = mAdapter.getItem(i);
                                if(temp.getUidPaseo().equals(dc.getDocument().getId())){
                                    mAdapter.remove(temp);
                                }

                            }
                            //mPaseos.remove();
                            break;
                        }
                        case MODIFIED: {
                            Map<String,Object> vals = dc.getDocument().getData();
                            vals.put("uidPaseo",dc.getDocument().getId());

                            PaseoClassAdapter mPaseo = newPaseo(vals);

                            for(int i=0;i<mAdapter.getCount();++i){
                                PaseoClassAdapter temp = mAdapter.getItem(i);
                                if(temp.getUidPaseo().equals(dc.getDocument().getId())){
                                    mAdapter.remove(temp);
                                }
                            }

                            mAdapter.add(mPaseo);

                            break;
                        }
                    }
                }
            }
        });

        mLvPaseos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                PaseoClassAdapter p = mAdapter.getItem(position);
                Log.i(TAG, p.toString());

                if(p.isEstado()){ // sigue en progreso
                    Intent i = new Intent(getApplicationContext(), paseoMapaActivity.class);
                    i.putExtra("paseo",p);
                    startActivity(i);
                } else {
                    /*
                    Intent i = new Intent(getApplicationContext(), CalificacionActivity.class);
                    i.putExtra("paseo",p);
                    startActivity(i);

                     */
                    //(new CalificacionActivity()).setArguments();
                    CalificacionActivity mDialogo =(new CalificacionActivity());
                    Bundle b = new Bundle();
                    b.putString("uidPaseo",p.getUidPaseo());
                    mDialogo.setArguments(b);
                    mDialogo.show(getSupportFragmentManager(),"Tag");
                }
                //PaseadorSerializable my_p = new PaseadorSerializable(p);
            }
        });
    }
}
