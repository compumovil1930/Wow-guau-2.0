package co.edu.javeriana.wow_guau.views;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Transaction;

import co.edu.javeriana.wow_guau.R;

public class CargarFragment extends Fragment {

    FirebaseUser mUser;
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    EditText monto;
    Button retirar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_money, container, false);

        mUser = mAuth.getCurrentUser();

        monto = view.findViewById(R.id.edit_text_monton);
        retirar = view.findViewById(R.id.buttonRetirar);

        String uid = mUser.getUid();

        final DocumentReference sfDocRef = db.collection("Clientes").document(uid);

        retirar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                db.runTransaction(new Transaction.Function<Void>() {
                    @Override
                    public Void apply(Transaction transaction) throws FirebaseFirestoreException {
                        DocumentSnapshot snapshot = transaction.get(sfDocRef);

                        // Note: this could be done without a transaction
                        //       by updating the population using FieldValue.increment()
                        long saldo = snapshot.getLong("saldo");
                        long retirar = Long.valueOf(monto.getText().toString());


                        saldo+=retirar;

                        //double newPopulation = snapshot.getDouble("population") + 1;
                        transaction.update(sfDocRef, "saldo", saldo);
                        // Success
                        return null;
                    }
                }).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d("", "Transaction success!");
                        Toast.makeText(getContext(), "Transacción exitosa", Toast.LENGTH_LONG ).show();
                    }
                })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.w("", "Transaction failure.", e);
                                Toast.makeText(getContext(), "Error Transacción", Toast.LENGTH_LONG ).show();
                            }
                        });
            }
        });





        // bla bla


        return view;
    }

}
