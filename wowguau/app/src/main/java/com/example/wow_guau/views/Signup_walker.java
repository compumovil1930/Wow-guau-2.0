package com.example.wow_guau.views;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import com.example.wow_guau.R;
import com.example.wow_guau.utils.CameraUtils;
import com.example.wow_guau.utils.Permisos;

import java.io.FileNotFoundException;
import java.io.InputStream;

public class Signup_walker extends AppCompatActivity {

    ImageButton ib_upload_photo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup_walker);

        ib_upload_photo = findViewById(R.id.ib_upload_photo);

        ib_upload_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startDialog();
            }
        });
    }

    private void startDialog() {
        AlertDialog.Builder myAlertDialog = new AlertDialog.Builder(Signup_walker.this);
        myAlertDialog.setTitle("Cargar foto");
        myAlertDialog.setMessage("De dónde quieres cargar la imagen?");

        myAlertDialog.setPositiveButton("Gallery",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface arg0, int arg1) {
                        Permisos.requestPermission(Signup_walker.this, Manifest.permission.READ_EXTERNAL_STORAGE, "I need to read the storage because I want to spy you", Permisos.MY_PERMISSIONS_REQUEST_READ_STORAGE);
                        if (ContextCompat.checkSelfPermission(Signup_walker.this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                            CameraUtils.pickImage(Signup_walker.this);
                        }
                    }
                });

        myAlertDialog.setNegativeButton("Camera",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface arg0, int arg1) {
                        Permisos.requestPermission(Signup_walker.this, Manifest.permission.CAMERA, "I need your camera because I want to spy you", Permisos.MY_PERMISSIONS_REQUEST_CAMERA);
                        if (ContextCompat.checkSelfPermission(Signup_walker.this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                            CameraUtils.takePicture(Signup_walker.this);
                        }
                    }
                });
        myAlertDialog.show();
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch(requestCode) {
            case Permisos.IMAGE_PICKER_REQUEST:
                if(resultCode == RESULT_OK){
                    try {
                        final Uri imageUri = data.getData();
                        final InputStream imageStream = getContentResolver().openInputStream(imageUri);
                        final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                        ib_upload_photo.setImageBitmap(selectedImage);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                }
                return;
            case Permisos.REQUEST_IMAGE_CAPTURE:
                if(resultCode == RESULT_OK){
                    Bundle extras = data.getExtras();
                    Bitmap imageBitmap = (Bitmap) extras.get("data");
                    ib_upload_photo.setImageBitmap(imageBitmap);
                }
                return;
            default:
                throw new IllegalStateException("Unexpected value: " + requestCode);
        }
    }
}
