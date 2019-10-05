package com.example.wow_guau.views;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.wow_guau.R;
import com.example.wow_guau.model.Perro;
import com.example.wow_guau.utils.CameraUtils;
import com.example.wow_guau.utils.Permisos;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


public class Signup_dog extends AppCompatActivity {
    EditText et_nombre;
    EditText et_fecha_nacimiento;
    RadioGroup rg_sexo;
    EditText et_observaciones;
    Button button_register;
    ImageButton ib_upload_photo;
    ImageButton ib_upload_card;
    EditText et_raza;
    Spinner sp_tamano;
    TextView tv_error;
    ScrollView scrollView;

    ArrayAdapter<String>spinnerAdapter;
    DatePickerDialog.OnDateSetListener date;
    Calendar calendar;
    Bitmap selectedImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup_dog);

        ib_upload_photo = findViewById(R.id.ib_upload_photo);
        ib_upload_card = findViewById(R.id.ib_upload_card);
        button_register = findViewById(R.id.button_register);
        et_nombre = findViewById(R.id.et_nombre);
        et_raza = findViewById(R.id.et_raza);
        et_fecha_nacimiento = findViewById(R.id.et_fecha_nacimiento);
        et_observaciones = findViewById(R.id.et_observaciones);
        sp_tamano = findViewById(R.id.sp_tamano);
        tv_error = findViewById(R.id.tv_error);
        scrollView = findViewById(R.id.scrollView);
        rg_sexo = findViewById(R.id.rg_sexo);

        List<String> tamanos = Arrays.asList(getResources().getStringArray(R.array.tamano_array));
        calendar = Calendar.getInstance();

        ib_upload_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CameraUtils.startDialog(Signup_dog.this);
            }
        });
        ib_upload_card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //subir archivo
            }
        });

        button_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Perro perro = registrarPerro();
                if(perro != null) {
                    // subir a firebase y archivos
                    finish();
                }
                else{
                    scrollView.fullScroll(ScrollView.FOCUS_UP);
                    tv_error.setTextSize(18);
                }
            }
        });
        et_raza.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Signup_dog.this, ListaRazasActivity.class);
                startActivityForResult(i, Permisos.RAZAS_PICKER);
            }
        });
        spinnerAdapter= new ArrayAdapter<String>(this,R.layout.spinner_item, tamanos){
            @Override
            public boolean isEnabled(int position){
                if(position == 0){
                    return false;
                }
                else{
                    return true;
                }
            }
            @Override
            public View getDropDownView(int position, View convertView, ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                TextView tv = (TextView) view;
                if(position == 0){
                    tv.setTextColor(Color.GRAY);
                }
                else {
                    tv.setTextColor(Color.BLACK);
                }
                return view;
            }
        };
        spinnerAdapter.setDropDownViewResource(R.layout.spinner_item);
        sp_tamano.setAdapter(spinnerAdapter);

        date = new DatePickerDialog.OnDateSetListener(){
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, month);
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateDate();
            }
        };
        et_fecha_nacimiento.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(Signup_dog.this, date, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch(requestCode) {
            case Permisos.IMAGE_PICKER_REQUEST:
                if(resultCode == RESULT_OK){
                    try {
                        final Uri imageUri = data.getData();
                        final InputStream imageStream = getContentResolver().openInputStream(imageUri);
                        selectedImage = BitmapFactory.decodeStream(imageStream);
                        ib_upload_photo.setImageBitmap(selectedImage);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                }
                return;
            case Permisos.REQUEST_IMAGE_CAPTURE:
                if(resultCode == RESULT_OK){
                    Bundle extras = data.getExtras();
                    selectedImage = (Bitmap) extras.get("data");
                    ib_upload_photo.setImageBitmap(selectedImage);
                }
                return;
            case Permisos.RAZAS_PICKER:
                if(resultCode == RESULT_OK){
                    et_raza.setText(data.getStringExtra("raza"));
                }
                return;
            default:
                throw new IllegalStateException("Unexpected value: " + requestCode);
        }
    }
    private void updateDate(){
        String format = "MM/dd/yy";
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        et_fecha_nacimiento.setText(sdf.format(calendar.getTime()));
    }
    Perro registrarPerro(){
        boolean completo = true;
        int radioButtonID = rg_sexo.getCheckedRadioButtonId();
        View radioButton = rg_sexo.findViewById(radioButtonID);
        int idx = rg_sexo.indexOfChild(radioButton);

        String nombre = et_nombre.getText().toString();
        String raza = et_raza.getText().toString();
        String tamano = sp_tamano.getSelectedItem().toString();
        String fecha = et_fecha_nacimiento.getText().toString();
        String observaciones = et_observaciones.getText().toString();
        String sexo;

        if(selectedImage == null)
            completo = false;
        if(nombre.isEmpty()){
            et_nombre.setError(getString(R.string.obligatorio));
            completo = false;
        }
        if(raza.isEmpty()){
            et_raza.setError(getString(R.string.obligatorio));
            completo = false;
        }
        if(tamano.isEmpty())
            completo = false;
        if(fecha.isEmpty()){
            et_fecha_nacimiento.setError(getString(R.string.obligatorio));
            completo = false;
        }
        if(radioButtonID==-1)
            completo = false;
        // if no vacunas

        if(!completo)
            return null;
        sexo = ((RadioButton)(rg_sexo.getChildAt(idx))).getText().toString();

        return new Perro(nombre, raza, tamano, calendar.getTime(), sexo, observaciones);
    }
}
