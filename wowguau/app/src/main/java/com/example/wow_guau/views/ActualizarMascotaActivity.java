package com.example.wow_guau.views;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

import com.example.wow_guau.R;
import com.example.wow_guau.utils.CameraUtils;
import com.example.wow_guau.utils.Permisos;

public class ActualizarMascotaActivity extends AppCompatActivity {
    ImageButton ib_upload_photo;
    EditText et_raza;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_actualizar_mascota);
        ib_upload_photo = findViewById(R.id.ib_upload_photo);
        et_raza = findViewById(R.id.et_raza);

        ib_upload_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CameraUtils.startDialog(ActualizarMascotaActivity.this);
            }
        });
        et_raza.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ActualizarMascotaActivity.this, ListaRazasActivity.class);
                startActivityForResult(i, Permisos.RAZAS_PICKER);
            }
        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch(requestCode) {
            case Permisos.RAZAS_PICKER:
                if(resultCode == RESULT_OK){
                    et_raza.setText(data.getStringExtra("raza"));
                }
                return;
            default:
                throw new IllegalStateException("Unexpected value: " + requestCode);
        }
    }
}
