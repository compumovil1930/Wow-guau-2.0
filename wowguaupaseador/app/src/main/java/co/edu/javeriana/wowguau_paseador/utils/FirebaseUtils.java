package co.edu.javeriana.wowguau_paseador.utils;

import android.graphics.Bitmap;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;

import co.edu.javeriana.wowguau_paseador.model.Paseador;

public class FirebaseUtils {
    private static FirebaseFirestore db = FirebaseFirestore.getInstance();
    private static FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder()
            .setTimestampsInSnapshotsEnabled(true)
            .build();
    private static StorageReference mStorageRef = FirebaseStorage.getInstance().getReference();

    public static void guardarUsuario(Paseador paseador, String uid, Bitmap photo){
        // certificados
        subirFoto("photo_"+uid, photo);
        db.setFirestoreSettings(settings);
        db.collection("Paseadores").document(uid).set(paseador);
    }
    public static Paseador buscarUsuario(String uid){
        final Paseador[] paseador = new Paseador[1];
        db.collection("Paseadores").document(uid)
                .get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    paseador[0] = documentSnapshot.toObject(Paseador.class);
                }
        });
        return paseador[0];
    }
    public static void subirFoto(String ruta, Bitmap photo){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        photo.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();
        UploadTask uploadTask = mStorageRef.child("images").child(ruta).putBytes(data);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                Log.i("ERROR", "No se pudo subir el archivo");
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Log.i("INFO", taskSnapshot.getMetadata().toString());
            }
        });
    }
}
