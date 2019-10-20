package co.edu.javeriana.wow_guau.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.widget.ImageView;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;

import co.edu.javeriana.wow_guau.model.Perro;

public class FirebaseUtils {

    private static FirebaseFirestore db = FirebaseFirestore.getInstance();
    private static FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder()
            .build();

    private static StorageReference mStorageRef = FirebaseStorage.getInstance().getReference();

    public static void guardarPerro(Perro perro, String uid, Bitmap photo)
    {
        db.setFirestoreSettings(settings);
        // certificados
        subirFoto(perro.getDireccionFoto(), photo);
        db.collection("Perro").document(uid).set(perro);
    }

    public static void subirFoto(String ruta, Bitmap photo)
    {
        db.setFirestoreSettings(settings);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        photo.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();
        UploadTask uploadTask = mStorageRef.child("images").child(ruta).putBytes(data);
        uploadTask.addOnFailureListener(new OnFailureListener()
        {
            @Override
            public void onFailure(@NonNull Exception exception)
            {
                Log.i("ERROR", "No se pudo subir la foto");
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Log.i("INFO", taskSnapshot.getMetadata().toString());
            }
        });
    }

    public static void descargarFotoImageView(String ruta, final ImageView imagen)
    {
        db.setFirestoreSettings(settings);

        StorageReference photoRef = mStorageRef.child("images").child(ruta);

        final long ONE_MEGABYTE = 1024 * 1024;
        photoRef.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>()
        {
            @Override
            public void onSuccess(byte[] bytes)
            {
                imagen.setImageBitmap(BitmapFactory.decodeByteArray(bytes, 0, bytes.length));
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle any errors
            }
        });
    }




}
