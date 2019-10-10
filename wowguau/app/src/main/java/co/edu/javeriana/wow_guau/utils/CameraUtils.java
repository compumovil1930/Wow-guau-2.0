package co.edu.javeriana.wow_guau.utils;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.provider.MediaStore;

import androidx.core.content.ContextCompat;

public class CameraUtils {
    public static void pickImage(Activity context){
        Intent pickImage = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        pickImage.setType("image/*");
        context.startActivityForResult(pickImage, Permisos.IMAGE_PICKER_REQUEST);
    }

    public static void takePicture(Activity context) {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(context.getPackageManager()) != null) {
            context.startActivityForResult(takePictureIntent, Permisos.REQUEST_IMAGE_CAPTURE);
        }
    }
    public static void startDialog(final Activity context) {
        AlertDialog.Builder myAlertDialog = new AlertDialog.Builder(context);
        myAlertDialog.setTitle("Cargar foto");
        myAlertDialog.setMessage("De dónde quieres cargar la imagen?");

        myAlertDialog.setPositiveButton("Gallery",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface arg0, int arg1) {
                        Permisos.requestPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE, "I need to read the storage because I want to spy you", Permisos.MY_PERMISSIONS_REQUEST_READ_STORAGE);
                        if (ContextCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                            CameraUtils.pickImage(context);
                        }
                    }
                });

        myAlertDialog.setNegativeButton("Camera",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface arg0, int arg1) {
                        Permisos.requestPermission(context, Manifest.permission.CAMERA, "I need your camera because I want to spy you", Permisos.MY_PERMISSIONS_REQUEST_CAMERA);
                        if (ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                            CameraUtils.takePicture(context);
                        }
                    }
                });
        myAlertDialog.show();
    }
}
