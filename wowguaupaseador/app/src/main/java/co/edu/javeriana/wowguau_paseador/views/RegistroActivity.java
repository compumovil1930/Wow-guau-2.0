package co.edu.javeriana.wowguau_paseador.views;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.location.Address;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.regex.Matcher;

import co.edu.javeriana.wowguau_paseador.R;
import co.edu.javeriana.wowguau_paseador.model.Direccion;
import co.edu.javeriana.wowguau_paseador.model.Paseador;
import co.edu.javeriana.wowguau_paseador.utils.CameraUtils;
import co.edu.javeriana.wowguau_paseador.utils.FirebaseUtils;
import co.edu.javeriana.wowguau_paseador.utils.Permisos;
import co.edu.javeriana.wowguau_paseador.utils.Utils;

public class RegistroActivity extends AppCompatActivity {
    EditText et_email;
    EditText et_password;
    EditText et_nombre;
    EditText et_cedula;
    RadioGroup rg_genero;
    EditText et_fecha_nacimiento;
    EditText et_phone;
    EditText et_direccion;
    EditText et_descripcion;
    EditText et_experiencia;
    ImageButton ib_upload_photo;
    ImageButton ib_upload_card;
    Button button_register;
    ScrollView scrollView;
    TextView tv_error;

    DatePickerDialog.OnDateSetListener date;
    Calendar calendar;
    Bitmap selectedImage;
    Address address;
    Paseador paseador;

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);
        et_email = findViewById(R.id.et_email);
        et_password = findViewById(R.id.et_password);
        et_nombre = findViewById(R.id.et_nombre);
        et_cedula = findViewById(R.id.et_cedula);
        rg_genero = findViewById(R.id.rg_genero);
        et_fecha_nacimiento = findViewById(R.id.et_date);
        et_phone = findViewById(R.id.et_phone);
        et_direccion = findViewById(R.id.et_direccion);
        et_descripcion = findViewById(R.id.et_descripcion);
        et_experiencia = findViewById(R.id.et_experiencia);
        ib_upload_card = findViewById(R.id.ib_upload_card);
        ib_upload_photo = findViewById(R.id.ib_upload_photo);
        button_register = findViewById(R.id.button_register);
        scrollView = findViewById(R.id.scrollView);
        tv_error = findViewById(R.id.tv_error2);

        calendar = Calendar.getInstance();

        mAuth = FirebaseAuth.getInstance();

        ib_upload_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CameraUtils.startDialog(RegistroActivity.this);
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
                paseador = registrarPaseador();
                if(paseador != null) {
                    createAccount(et_email.getText().toString(), et_password.getText().toString());
                }
                else{
                    scrollView.fullScroll(ScrollView.FOCUS_UP);
                    tv_error.setTextSize(18);
                }
            }
        });

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
                new DatePickerDialog(RegistroActivity.this, date, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
        et_direccion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(RegistroActivity.this, MapsActivity.class);
                startActivityForResult(i, Permisos.ADDRESS_PICKER);
            }
        });
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null && paseador!=null) {
                    updateUI(user, ((BitmapDrawable) ib_upload_photo.getDrawable()).getBitmap());
                }
            }
        };

    }
    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }
    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case Permisos.IMAGE_PICKER_REQUEST:
                if (resultCode == RESULT_OK) {
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
                if (resultCode == RESULT_OK) {
                    Bundle extras = data.getExtras();
                    selectedImage = (Bitmap) extras.get("data");
                    ib_upload_photo.setImageBitmap(selectedImage);
                }
                return;
            case Permisos.ADDRESS_PICKER:
                if (resultCode == RESULT_OK) {
                    Bundle extras = data.getExtras();
                    address = (Address) extras.get("address");
                    et_direccion.setText(extras.get("direccion").toString());
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
    private Paseador registrarPaseador() {
        boolean completo = true;
        int radioButtonID = rg_genero.getCheckedRadioButtonId();
        View radioButton = rg_genero.findViewById(radioButtonID);
        int idx = rg_genero.indexOfChild(radioButton);

        String email = et_email.getText().toString();
        String contrasena = et_password.getText().toString();
        String nombre = et_nombre.getText().toString();
        String cedula = et_cedula.getText().toString();
        String fecha = et_fecha_nacimiento.getText().toString();
        String phone = et_phone.getText().toString();
        String direccion = et_direccion.getText().toString();
        String descripcion = et_descripcion.getText().toString();
        String experiencia = et_experiencia.getText().toString();
        String genero;

        if (selectedImage == null) {
            completo = false;
            Toast.makeText(getApplicationContext(), "La foto es obligatoria", Toast.LENGTH_LONG).show();
        }
        if (email.isEmpty()) {
            et_email.setError(Utils.obligatorio);
            completo = false;
        }
        if (contrasena.isEmpty()) {
            et_password.setError(Utils.obligatorio);
            completo = false;
        }
        if (nombre.isEmpty()) {
            et_nombre.setError(Utils.obligatorio);
            completo = false;
        }
        if (cedula.isEmpty()) {
            et_cedula.setError(Utils.obligatorio);
            completo = false;
        }
        if (fecha.isEmpty()) {
            et_fecha_nacimiento.setError(Utils.obligatorio);
            completo = false;
        }
        if (phone.isEmpty()) {
            et_phone.setError(Utils.obligatorio);
            completo = false;
        }
        if (direccion.isEmpty()) {
            et_direccion.setError(Utils.obligatorio);
            completo = false;
        }
        if (experiencia.isEmpty()) {
            et_experiencia.setError(Utils.obligatorio);
            completo = false;
        }
        if(!isEmailValid(email)){
            et_email.setError("Mal escrito");
            completo = false;
        }
        if (radioButtonID == -1)
            completo = false;

        if (!completo)
            return null;

        genero = ((RadioButton) (rg_genero.getChildAt(idx))).getText().toString();

        return new Paseador(email, nombre, Integer.parseInt(cedula), calendar.getTime(), Integer.parseInt(phone), genero, new Direccion(direccion, address), descripcion, Integer.parseInt(experiencia));
    }
    private boolean isEmailValid(String correo) {
        Matcher matcher = Utils.VALID_EMAIL_ADDRESS_REGEX.matcher(correo);
        return matcher.matches();
    }
    private void createAccount(final String correo, String contrasena){
        mAuth.createUserWithEmailAndPassword(correo, contrasena)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    Log.d("BIEN", "createUserWithEmail:onComplete:" + task.isSuccessful());
                }
                if (!task.isSuccessful()) {
                    Toast.makeText(RegistroActivity.this, "Error al autenticar" + task.getException().toString(), Toast.LENGTH_LONG).show();
                    Log.e("MAL", task.getException().getMessage());
                }
            }
        });
    }
    private void updateUI(FirebaseUser currentUser, Bitmap photo) {
        FirebaseUtils.guardarUsuario(paseador, currentUser.getUid(), photo);
        Intent intent = new Intent(getBaseContext(), MenuActivity.class);
        intent.putExtra("user", currentUser.getEmail());
        startActivity(intent);
        finish();
    }
}
