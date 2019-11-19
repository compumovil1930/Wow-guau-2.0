package co.edu.javeriana.wowguau_paseador.utils;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
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
import java.util.Calendar;

import co.edu.javeriana.wowguau_paseador.model.Mensaje;
import co.edu.javeriana.wowguau_paseador.model.Paseador;
import co.edu.javeriana.wowguau_paseador.model.Paseo;
import co.edu.javeriana.wowguau_paseador.model.Perro;
import co.edu.javeriana.wowguau_paseador.views.MenuActivity;
import co.edu.javeriana.wowguau_paseador.views.InfoPaseoActivity;
import co.edu.javeriana.wowguau_paseador.views.WalkToDogActivity;

public class FirebaseUtils {
    private static FirebaseFirestore db = FirebaseFirestore.getInstance();
    private static FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder()
            .setTimestampsInSnapshotsEnabled(true)
            .build();
    private static StorageReference mStorageRef = FirebaseStorage.getInstance().getReference();

    public static void guardarUsuario(Paseador paseador, String uid, Bitmap photo){
        db.setFirestoreSettings(settings);
        // certificados
        if(photo!=null)
            subirFoto(paseador.getDireccionFoto(), photo);
        db.collection("Paseadores").document(uid).set(paseador);
    }
    public static void buscarUsuario(final String uid, final Activity activity){
        db.setFirestoreSettings(settings);
        db.collection("Paseadores").document(uid).get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        Intent i = new Intent(activity, MenuActivity.class);
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
    public static void getPerro(final Paseo paseo, final Activity activity){
        db.setFirestoreSettings(settings);
        db.collection("Mascotas").document(paseo.getUidPerro()).get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        Intent i = new Intent(activity, InfoPaseoActivity.class);
                        i.putExtra("perro", documentSnapshot.toObject(Perro.class));
                        i.putExtra("uid", paseo.getUidPerro());
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
    public static void checkPaseo(final String uid, final Activity activity, final Perro perro){
        db.setFirestoreSettings(settings);
        db.collection("Paseos").document(uid).get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if(documentSnapshot.get("uidPaseador").equals("")) {
                            Intent i = new Intent(activity, WalkToDogActivity.class);
                            i.putExtra("perro", perro);
                            i.putExtra("uidPaseo", uid);
                            i.putExtra("uidDueno", documentSnapshot.get("uidDueno").toString());
                            activity.startActivity(i);
                            activity.finish();
                        }
                        else{
                            Toast.makeText(activity, "El paseo ya fue tomado", Toast.LENGTH_LONG).show();
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(activity, "No existe paseo", Toast.LENGTH_LONG).show();
                    }
                });
    }
    public static void subirFoto(String ruta, Bitmap photo) {
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
    public static void sendMessage(Mensaje mensaje, String uidChat){
        mensaje.setTime(Calendar.getInstance().getTime());
        mensaje.setId(uidChat);
        db.setFirestoreSettings(settings);
        db.collection("Chats").add(mensaje);
    }
}
