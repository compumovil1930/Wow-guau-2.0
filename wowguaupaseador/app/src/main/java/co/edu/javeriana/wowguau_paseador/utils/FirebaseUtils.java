package co.edu.javeriana.wowguau_paseador.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

import co.edu.javeriana.wowguau_paseador.model.Paseador;
import co.edu.javeriana.wowguau_paseador.views.MenuActivity;

public class FirebaseUtils {
    private static FirebaseFirestore db = FirebaseFirestore.getInstance();
    private static FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder()
            .setTimestampsInSnapshotsEnabled(true)
            .build();
    private static StorageReference mStorageRef = FirebaseStorage.getInstance().getReference();

    public static void guardarUsuario(Paseador paseador, String uid, Bitmap photo){
        db.setFirestoreSettings(settings);
        // certificados
        subirFoto(paseador.getDireccionFoto(), photo);
        db.collection("Paseadores").document(uid).set(paseador);
    }
    public static void buscarUsuario(final String uid, final Activity activity){
        db.setFirestoreSettings(settings);
        final Paseador[] paseador = new Paseador[1];
        db.collection("Paseadores").document(uid).get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        Paseador paseador = documentSnapshot.toObject(Paseador.class);
                        Intent i = new Intent(activity, MenuActivity.class);
                        i.putExtra("user", paseador);
                        i.putExtra("uid", uid);
                        activity.startActivity(i);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(activity, "El usuario no existe como paseador", Toast.LENGTH_LONG).show();
                    }
                });
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
            public void onFailure(@NonNull Exception exception) {
                Log.i("ERROR", "No se pudo subir la foto");
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>()
        {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Log.i("INFO", taskSnapshot.getMetadata().toString());
            }
        });
    }
    public static File descargarFotoImageView(String ruta, final ImageView perfil){
        db.setFirestoreSettings(settings);
        StorageReference photoRef = mStorageRef.child("images").child(ruta);
        Log.i("PATH" , photoRef.toString());
        /*
        final long ONE_MEGABYTE = 1024 * 1024;
        photoRef.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                perfil.setImageBitmap(BitmapFactory.decodeByteArray(bytes, 0, bytes.length));
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle any errors
            }
        });

         */

        try {
            final File localFile = File.createTempFile("images", "jpg");
        photoRef.getFile(localFile)
                .addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                        // Successfully downloaded data to local file
                        // ...
                        perfil.setImageURI(Uri.fromFile(localFile));

                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle failed download
                // ...
            }
        });
        return localFile;

        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
        /*
        Glide.with( context)
                .load(photoRef)
                .into(perfil);

         */
    }
}
