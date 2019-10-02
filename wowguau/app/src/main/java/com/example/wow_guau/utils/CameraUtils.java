package com.example.wow_guau.utils;

import android.app.Activity;
import android.content.Intent;
import android.provider.MediaStore;

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
}
